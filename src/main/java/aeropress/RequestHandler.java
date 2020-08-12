package aeropress;

import java.util.Map;

public interface RequestHandler {
	public HttpResponse handle(Map<String, String> params);
}
