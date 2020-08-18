# aeropress
A minimalist Java web framework inspired by Express.js for Javascript (https://expressjs.com/).

This is primarily educational in order to further my understanding of web servers. Currently a work in progress.

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
                .get("/api/:name", params -> {
                  return HttpResponse.builder()
                  .status(HttpStatus.OK)
                  .header("Content-Type", "text/html")
                  .body(html.replace("{}", params.get("name")))
                  .build();
                })
```
This builds the Aeropress app and maps the URL (along with path parameters) to the specified handler. The path parameters can be accessed via the 'params' map that's injected into the handler. The HttpResponse class also has a builder for creating immutable HTTP responses. Any responses returned by the handler will be written to the response stream.

After building, you can start the Aeropress server on any port:

```Java
app.start(8086);
```

The Aeropress app will listen on the specified port, in this case port 8086. If no port is specified, the default is port 8080.

Navigating to the URL in a browser will yield:

![image](https://github.com/jthomas718/aeropress/blob/master/assets/example.png)
