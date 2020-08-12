package aeropress;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Aeropress implements Runnable {
	private static final int DEFAULT_PORT = 8080;
	private volatile boolean running = false;
	private final ExecutorService threadPool = Executors.newCachedThreadPool();
	private final Map<String, RequestHandler> routeMap = new HashMap<>();
	
	private Aeropress() {
		
	}
	
	public void run() {
		running = true;
		System.out.println("Server is starting on port 8080...");
		try {
			ServerSocket serverSocket = new ServerSocket(DEFAULT_PORT);
			while (running) {
				Socket clientSocket = serverSocket.accept();
				threadPool.execute(new ConnectionHandler(clientSocket, routeMap));
			}
			serverSocket.close();
		} catch (IOException e) {
			
		}
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
