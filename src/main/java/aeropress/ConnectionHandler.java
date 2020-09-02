package aeropress;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Map;
import java.util.Set;

public class ConnectionHandler implements Runnable {
	private static final int MAX_MESSAGE_BYTES = 1024;
	private final Socket socket;
	private final Map<String, Map<HttpMethod, RequestHandler>> routes;
	
	public ConnectionHandler(Socket socket, Map<String, Map<HttpMethod, RequestHandler>> routes) {
		System.out.println("-------- NEW CONNECTION ---------");
		this.socket = socket;
		this.routes = routes;
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
		HttpResponse res = null;
		boolean matched = false;
		for (String pathTemplate : routes.keySet()) {
			PathParser.ParseResult parseResult = PathParser.parse(request.getUrl(), pathTemplate);
			if (parseResult.matches()) {
				matched = true;
				Map<HttpMethod, RequestHandler> methods = routes.get(pathTemplate);
				RequestHandler handler = methods.get(request.getMethod());
				if (handler != null) {
					res = handler.handle(request, parseResult.params());
				} else {
					res = HttpResponse.builder()
							.status(HttpStatus.METHOD_NOT_ALLOWED)
							.header("Allow", methodsToString(methods.keySet())) // HTTP RFC section 10.4.6 (https://tools.ietf.org/html/rfc2616#section-10.4.6)
							.body("<h1>405 - Method Not Allowed</h1>")
							.build();
				}
			}
		}
		
		if (!matched) {
			res = HttpResponse.builder()
					.status(HttpStatus.NOT_FOUND)
					.body("<h1>404 - Not Found</h1>")
					.build();
		}

		sendResponse(res, responseStream);
	}

	private void sendResponse(HttpResponse response, OutputStream responseStream) throws IOException {
		try {
			responseStream.write(response.getMessage().getBytes());
		} catch (IOException e) {
			throw new IOException("Unable to write HTTP response to response stream", e);
		}
	}

	private static String methodsToString(Set<HttpMethod> methods) {
		StringBuilder b = new StringBuilder();
		for (HttpMethod method : methods) {
			b.append(method.toString()).append(", ");
		}

		return b.substring(0, b.length() - 2);
	}

}
