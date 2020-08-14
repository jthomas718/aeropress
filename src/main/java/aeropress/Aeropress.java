package aeropress;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Aeropress {
	private static final int DEFAULT_PORT = 8080;
	private volatile boolean running = false;
	private final ExecutorService threadPool = Executors.newCachedThreadPool();
	private final Map<String, RequestHandler> routeMap = new HashMap<>();
	
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
			threadPool.execute(new ConnectionHandler(clientSocket, routeMap));
		}
		serverSocket.close();
	}
	
	private void registerHandler(String pathTemplate, HttpMethod method, RequestHandler handler) {
		routeMap.put(pathTemplate, handler);
	}
	
	public static Builder builder() {
		return new Builder();
	}
	
	public static class Builder {
		private final Aeropress app = new Aeropress();
		
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
