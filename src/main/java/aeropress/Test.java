package aeropress;

import java.io.IOException;

public class Test {
	private static final String HTML = 
			"<!DOCTYPE html>\r\n" +
			"<html>\r\n" + 
			"<body>\r\n" + 
			"<h1>Hello, {}!</h1>\r\n" + 
			"</body>\r\n" + 
			"</html>";
	
	public static void main(String[] args) throws IOException {
		Aeropress app = Aeropress.builder()
										.get("/api/:name", params -> {
											return HttpResponse.builder()
											.status(HttpStatus.OK)
											.header("Content-Type", "text/html")
											.body(HTML.replace("{}", params.get("name")))
											.build();
										})
										.get("/api/:id/test", params -> {
											return HttpResponse.builder()
													.status(HttpStatus.OK)
													.body(String.format("You got ID #%s", params.get("id")))
													.build();
										})
										.build();
		
		app.start(8086);
	}
}
