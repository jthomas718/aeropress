package aeropress;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

public class RestController {
	private final String rootPath;
	private final Map<String, RequestHandler> requestMap = new HashMap<>();
	
	public RestController(String rootPath) {
		this.rootPath = rootPath;
	}
	
	public void registerHandler(String pathTemplate, HttpMethod method, RequestHandler handler) {
		requestMap.put(pathTemplate, handler);
	}
	
	public void accept(HttpRequest request, OutputStream responseStream) {
		RequestHandler handler = null;
		for (String pathTemplate : requestMap.keySet()) {
			PathParser.ParseResult parseResult = PathParser.parse(request.getUrl(), pathTemplate);
			if (parseResult.matches()) {
				handler = requestMap.get(pathTemplate);
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
	
	public String getRootPath() {
		return rootPath;
	}
}
