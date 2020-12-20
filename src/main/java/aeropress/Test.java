package aeropress;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Test {
	private static final String HTML = 
			"<!DOCTYPE html>\r\n" +
			"<html>\r\n" + 
			"<body>\r\n" + 
			"<h1>Hello, {}!</h1>\r\n" + 
			"</body>\r\n" + 
			"</html>";
	
	public static void main(String[] args) throws IOException, InterruptedException, ParseException {
		Aeropress app = Aeropress.builder()
										.executor(Executors.newFixedThreadPool(5))
										.get("/api/:name", req -> {
											return HttpResponse.builder()
											.status(HttpStatus.OK)
											.header("Content-Type", "text/html")
											.body(HTML.replace("{}", req.pathParams().get("name")))
											.build();
										})
										.get("/api/:id/test", req -> {
											return HttpResponse.builder()
													.status(HttpStatus.OK)
													.body(String.format("You got ID #%s", req.pathParams().get("id")))
													.build();
										})
										.post("/api", req -> {
											return HttpResponse.builder()
													.status(HttpStatus.OK)
													.header("Content-Type", "text/html")
													.body(HTML.replace("{}", req.body()))
													.build();
										})
										.post("/postOnly", req -> {
											return HttpResponse.builder().status(HttpStatus.OK).build();
										})
										.get("/api", req -> {
											return HttpResponse.builder()
													.status(HttpStatus.OK)
													.body(HTML.replace("{}", req.queryParams().get("param")))
													.build();
										})
										.build();

		app.start(8086);
		//parseUri("https://www.geeksforgeeks.org/url-getprotocol-method-in-java-with-examples?title=protocol%E2%82%AC&second=secondParam");
	}
}
