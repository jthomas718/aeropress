package aeropress;

public interface RequestHandler {
	HttpResponse handle(HttpRequest request);
}
