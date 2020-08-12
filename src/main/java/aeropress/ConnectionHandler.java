package aeropress;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Map;

public class ConnectionHandler implements Runnable {
	private static final int MAX_MESSAGE_BYTES = 1024;
	private final Socket socket;
	private final Map<String, RequestHandler> routeMap;
	
	public ConnectionHandler(Socket socket, Map<String, RequestHandler> routeMap) {
		System.out.println("-------- NEW CONNECTION ---------");
		this.socket = socket;
		this.routeMap = routeMap;
	}
	
	@Override
	public void run() {
		System.out.println("Connection being handled on thread \"" + Thread.currentThread().getName() + "\"");
		try {
			char[] inputBuffer = new char[MAX_MESSAGE_BYTES];
			BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			reader.read(inputBuffer);
			HttpRequest request = new HttpRequest(String.valueOf(inputBuffer)); //TODO: check length of the returned string. Is it 1024? message may be truncated.
			System.out.print(request.toString());
			routeRequest(request, socket.getOutputStream());
			reader.close();
			socket.close();
		} catch (IOException e) {
			System.out.println("Connection handler (" + Thread.currentThread().getName() + ") encountered an IO error");
			e.printStackTrace();
		}
		
	}
	
	public void routeRequest(HttpRequest request, OutputStream responseStream) {
		RequestHandler handler = null;
		for (String pathTemplate : routeMap.keySet()) {
			PathParser.ParseResult parseResult = PathParser.parse(request.getUrl(), pathTemplate);
			if (parseResult.matches()) {
				handler = routeMap.get(pathTemplate);
				HttpResponse response = handler.handle(parseResult.params());
				try {
					responseStream.write(response.getMessage().getBytes());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		if (handler == null) {
			System.out.println("422");
		}
	}

	
//	private RestController createController() {
//		RestController controller = new RestController("/");
//		controller.registerHandler("/:name", HttpMethod.GET, params -> {
//			return new HttpResponse.Builder()
//					.status(HttpStatus.OK)
//					.header("Content-Type", "text/html")
//					.body(HTML.replace("{}", params.get("name")))
//					.build();
//		});
//		
//		controller.registerHandler("/api/name/:name", HttpMethod.GET, params -> {
//			return new HttpResponse.Builder()
//					.status(HttpStatus.OK)
//					.header("Content-Type", "text/html")
//					.body(HTML.replace("{}", String.format("%s! You've reached the API", params.get("name"))))
//					.build();
//		});
//		
//		
//		return controller;
//	}

}
