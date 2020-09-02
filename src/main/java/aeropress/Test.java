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
	
	public static void main(String[] args) throws IOException, InterruptedException {
		Aeropress app = Aeropress.builder()
										.get("/api/:name", (req, pathParams) -> {
											return HttpResponse.builder()
											.status(HttpStatus.OK)
											.header("Content-Type", "text/html")
											.body(HTML.replace("{}", pathParams.get("name")))
											.build();
										})
										.get("/api/:id/test", (req, pathParams) -> {
											return HttpResponse.builder()
													.status(HttpStatus.OK)
													.body(String.format("You got ID #%s", pathParams.get("id")))
													.build();
										})
										.post("/api", (req, pathParams) -> {
											return HttpResponse.builder()
													.status(HttpStatus.OK)
													.header("Content-Type", "text/html")
													.body(HTML.replace("{}", req.getBody()))
													.build();
										})
										.get("/api", (req, pathParams) -> {
											return HttpResponse.builder()
													.status(HttpStatus.OK)
													.body("<h1>It's all good!</h1>")
													.build();
										})
										.build();
		
		app.start(8086);
	}
}
