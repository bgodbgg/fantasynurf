import java.io.IOException;
import java.io.OutputStream;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;


public class HandlerPage implements HttpHandler{

	public HandlerPage(){
		
	}
	
	@Override
	public void handle(HttpExchange exch) throws IOException {
		System.out.println("Log: Recieve");
		String dir = exch.getRequestURI().getPath();
		String response = "";
		int type = 404;
		int len = -1;
		if(dir.equals("/temp")){
			response = "Testing";
			type = 200;
			len = response.length();
		}
		System.out.println(type + ": " + len);
		exch.sendResponseHeaders(type, len);
		if(response.length() > 0){
			OutputStream os = exch.getResponseBody();
			os.write(response.getBytes());
			os.close();
			exch.getResponseHeaders().set("Content-Type", "Text/html");
		}
	}

}
