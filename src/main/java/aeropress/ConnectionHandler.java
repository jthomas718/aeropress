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
		BufferedReader reader = null;
		try {
			char[] inputBuffer = new char[MAX_MESSAGE_BYTES];
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			reader.read(inputBuffer);
			HttpRequest request = new HttpRequest(String.valueOf(inputBuffer)); //TODO: check length of the returned string. Is it 1024? message may be truncated.
			System.out.print(request.toString());
			routeRequest(request, socket.getOutputStream());
		} catch (IOException e) {
			System.out.println("Connection handler (" + Thread.currentThread().getName() + ") encountered an IO error");
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}
	
	private void routeRequest(HttpRequest request, OutputStream responseStream) throws IOException {
		RequestHandler handler = null;
		for (String pathTemplate : routeMap.keySet()) {
			PathParser.ParseResult parseResult = PathParser.parse(request.getUrl(), pathTemplate);
			if (parseResult.matches()) {
				handler = routeMap.get(pathTemplate);
				HttpResponse response = handler.handle(parseResult.params());
				sendResponse(response, responseStream);
			}
		}
		
		if (handler == null) {
			sendResponse(HttpResponse.builder()
					.status(HttpStatus.NOT_FOUND)
					.body("<h1>404 - Not Found</h1>")
					.build(),
					responseStream);
		}
	}

	private void sendResponse(HttpResponse response, OutputStream responseStream) throws IOException {
		try {
			responseStream.write(response.getMessage().getBytes());
		} catch(IOException e) {
			throw new IOException("Unable to write HTTP response to response stream");
		}
	}

}
