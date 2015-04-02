import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.URL;
import java.security.KeyStore;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLParameters;
import javax.net.ssl.TrustManagerFactory;

import com.robrua.orianna.api.core.ChallengeAPI;
import com.robrua.orianna.api.dto.BaseRiotAPI;
import com.robrua.orianna.type.core.common.Region;
import com.robrua.orianna.type.core.match.Match;
import com.sun.net.httpserver.HttpsConfigurator;
import com.sun.net.httpserver.HttpsParameters;
import com.sun.net.httpserver.HttpsServer;

public class Main {
	public static void main(String[] args) throws IOException {
		HttpsServer server = HttpsServer.create(new InetSocketAddress(9175), 0);
		File config = new File("C:\\FantasyUrf\\config");
		if(!config.exists()){
			System.err.println("Missing config file");
			return;
		}
		Scanner configScan = new Scanner(config);
		String base = "";
		while(configScan.hasNext()){
			String line = configScan.nextLine();
			String type = line.substring(0, line.indexOf("="));
			if(type.equals("base"))
				base = line.substring(line.indexOf("\"") + 1, line.lastIndexOf("\""));
		}
		configScan.close();
		File jks = new File(base + "\\fantasyurf.jks");
		if(!jks.exists()){
			System.err.println("Unable to find JKS file");
			return;
		}
		FileInputStream fis = new FileInputStream (jks);
		SSLContext context = null;
		try{
			context = SSLContext.getInstance("TLS");
			char[] password = "fantasyurf".toCharArray ();
			KeyStore ks = KeyStore.getInstance("JKS");
			
			
			ks.load(fis, password);
			KeyManagerFactory kmf = KeyManagerFactory.getInstance ( "SunX509" );
			kmf.init(ks, "fantasyurf".toCharArray());
			TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
			tmf.init(ks);
			context.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
		}catch(Exception e){
			System.err.println(e.getMessage());
			return;
		}
		server.setHttpsConfigurator(new HttpsConfigurator(context){
			public void configure(HttpsParameters params){
				try{
					// initialise the SSL context
					SSLContext c = SSLContext.getDefault();
					SSLEngine engine = c.createSSLEngine();
					params.setNeedClientAuth(false);
					params.setCipherSuites(engine.getEnabledCipherSuites());
					params.setProtocols(engine.getEnabledProtocols());

					// get the default parameters
					SSLParameters defaultSSLParameters = c.getDefaultSSLParameters();
					params.setSSLParameters(defaultSSLParameters);
				}
				catch(Exception ex){
					System.out.println(ex);
					System.out.println("Failed to create HTTPS port");
				}
			}
		});
		HandlerPage page = new HandlerPage();
		server.createContext("/temp", page);
		server.start();
		System.out.println("Log: begin");
		getMatchIds();
	}
	
	private static void getMatchIds() {
		BaseRiotAPI.setAPIKey("81940c8a-50aa-484d-a8ca-ca2f87714a9f");
		BaseRiotAPI.setRegion(Region.NA);
		BaseRiotAPI.setMirror(Region.NA);
		long startTime = (long) 1427949300;
		long oriStartTime = (long) 1427949300 * 1000;
		//long startTime = (long) 1427943300;
		try {
			while (true) {
				File file = new File("C:/Users/Brian/Downloads/matchIds.txt");
				// if file doesnt exists, then create it
				if (!file.exists()) {
					file.createNewFile();
				}
				FileWriter fw = new FileWriter(file.getAbsoluteFile(), true);
				BufferedWriter bw = new BufferedWriter(fw);
				String targetUrl = "https://na.api.pvp.net/api/lol/na/v4.1/game/ids?beginDate=" + startTime + "&api_key=81940c8a-50aa-484d-a8ca-ca2f87714a9f";
				URL url;
			    HttpURLConnection connection = null;  
			    try {
			      //Create connection
			      url = new URL(targetUrl);
			      connection = (HttpURLConnection)url.openConnection();
			      connection.setRequestMethod("POST");
			      connection.setRequestProperty("Content-Type", 
			           "application/x-www-form-urlencoded");
			      connection.setRequestProperty("Content-Language", "en-US");  
		
			      connection.setUseCaches (false);
			      connection.setDoInput(true);
			      connection.setDoOutput(true);
		
			      //Send request
			      DataOutputStream wr = new DataOutputStream (
			                  connection.getOutputStream ());
			      wr.flush ();
			      wr.close ();
		
			      //Get Response    
			      InputStream is = connection.getInputStream();
			      BufferedReader rd = new BufferedReader(new InputStreamReader(is));
			      String line;
			      StringBuffer response = new StringBuffer(); 
			      while((line = rd.readLine()) != null) {
			        response.append(line);
			        response.append('\r');
			      }
			      rd.close();
			      System.out.println(response.toString());
			      bw.append(response.toString() + "\n");
			      bw.close();
			      
			      /*List<Match> matches = ChallengeAPI.getURFMatches(oriStartTime);
			      for (int i = 0; i < matches.size(); i++) {
			    	  System.out.print(matches.get(i));
			    	  if (i != matches.size() - 1) {
			    		  System.out.print(",");
			    	  }
			      }
			      System.out.println("");*/
			    } catch (Exception e) {
			      e.printStackTrace();
			    } finally {
			      if(connection != null) {
			        connection.disconnect(); 
			      }
			    }
			    startTime += 300;
		        try {
					Thread.sleep(300 * 1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			//writer.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
}
