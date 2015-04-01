import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.security.KeyStore;
import java.util.Scanner;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLParameters;
import javax.net.ssl.TrustManagerFactory;

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
	}
}
