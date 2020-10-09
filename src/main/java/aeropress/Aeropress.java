package aeropress;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Aeropress {
	private static final int DEFAULT_PORT = 8080;
	private volatile boolean running = false;
	private ExecutorService esvc = Executors.newCachedThreadPool();
	private final Map<String, Map<HttpMethod, RequestHandler>> routes = new HashMap<>();
	
	private Aeropress() { }

	/**
	 * Start an Aeropress server on default port 8080.
	 */
	public void start() throws IOException {
		start(DEFAULT_PORT);
	}

	/**
	 * Start an Aeropress server on the specified port.
	 *
	 * @param port The port that the Aeropress server will listen on.
	 */
	public void start(int port) throws IOException {
		running = true;
		System.out.println(String.format("Server is starting on port %d...", port));
		ServerSocket serverSocket = new ServerSocket(port);
			while (running) {
				Socket clientSocket = serverSocket.accept();
				esvc.execute(new ConnectionHandler(clientSocket, routes));
			}
	}
	
	private void registerHandler(String pathTemplate, HttpMethod method, RequestHandler handler) {
		Map<HttpMethod, RequestHandler> methods = routes.computeIfAbsent(pathTemplate, k -> new HashMap<>());
		methods.put(method, handler);
	}
	
	public static Builder builder() {
		return new Builder();
	}
	
	public static class Builder {
		private final Aeropress app = new Aeropress();

		public Builder executor(ExecutorService executorService) {
			app.esvc = executorService;
			return this;
		}
		
		public Builder get(String pathTemplate, RequestHandler handler) {
			app.registerHandler(pathTemplate, HttpMethod.GET, handler);
			return this;
		}
		
		public Builder post(String pathTemplate, RequestHandler handler) {
			app.registerHandler(pathTemplate, HttpMethod.POST, handler);
			return this;
		}
		
		public Aeropress build() {
			return app;
		}
	}
}
