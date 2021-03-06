# aeropress
A minimalist Java web framework inspired by Express.js for Javascript (https://expressjs.com/).

Currently a work in progress.

## Usage
A simple 'hello world' Aeropress app can be built using the Aeropress builder:

```Java
String html =
    "<!DOCTYPE html>\r\n" +
    "<html>\r\n" +
    "<body>\r\n" +
    "<h1>Hello, {}!</h1>\r\n" +
    "</body>\r\n" +
    "</html>";

Aeropress app = Aeropress.builder()
                .get("/api/:name", req -> {
                  return HttpResponse.builder()
                  .status(HttpStatus.OK)
                  .header("Content-Type", "text/html")
                  .body(html.replace("{}", req.pathParams().get("name")))
                  .build();
                })
                .build();
```
This builds the Aeropress app and maps the URL to the specified handler. The path parameters (and other values) can be accessed via the request that's injected into the handler. The HttpResponse class also has a builder for creating immutable HTTP responses. Any responses returned by the handler will be written to the response stream.

After building, you can start the Aeropress server on any port:

```Java
app.start(8086);
```

The Aeropress app will listen on the specified port, in this case port 8086. If no port is specified, the default is port 8080.

If the Aeropress app is running locally, then navigating to the URL in a browser will yield:

![image](https://github.com/jthomas718/aeropress/blob/master/assets/example.png)
