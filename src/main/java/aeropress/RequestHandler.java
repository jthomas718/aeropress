package aeropress;

import java.util.Map;

public interface RequestHandler {
	public HttpResponse handle(HttpRequest request, Map<String, String> pathParams);
}
