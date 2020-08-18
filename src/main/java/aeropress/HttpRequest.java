package aeropress;

import java.util.HashMap;
import java.util.Map;

public class HttpRequest {
	private static final String CRLF = "\r\n";
	private final Map<String, String> headers = new HashMap<>();
	private HttpMethod method;
	private String url;
	private String protocolVersion;
	private String body;

	// TODO: Validate http request and throw exception if not valid. This will allow
	// connection handler to decide on further actions (such as returning 400 status).
	public HttpRequest(String rawRequest) {
		int offset = rawRequest.indexOf(CRLF);
		int doubleCrlfIndex = rawRequest.indexOf(CRLF + CRLF);
		
		// Parse request line
		String requestLine = rawRequest.substring(0, offset);
		String[] fields = requestLine.split(" ");
		this.method = HttpMethod.valueOf(fields[0]);
		this.url = fields[1];
		this.protocolVersion = fields[2];
		
		// Parse headers
		String rawHeaders = rawRequest.substring(offset + 1, doubleCrlfIndex);
		fields = rawHeaders.split(CRLF);
		for (String headerField : fields) {
			int colonIndex = headerField.indexOf(":");
			String key = headerField.substring(0, colonIndex).trim();
			String value = headerField.substring(colonIndex + 2);
			headers.put(key, value);
		}
		
		// Parse body
		this.body = rawRequest.substring(doubleCrlfIndex);
		if (this.body.isEmpty()) {
			this.body = null;
		}
	}
	
	public String getHeader(String name) {
		return headers.get(name);
	}
	
	public HttpMethod getMethod() {
		return method;
	}
	
	public String getUrl() {
		return url;
	}
	
	public String getProtocolVersion() {
		return protocolVersion;
	}
	
	public String getBody() {
		return body;
	}
	
	@Override
	public String toString() {
		return String.format("Method: %s\nURL: %s\nProtocol version: %s\n"
				+ "Headers: %s\nBody: %s", method, url, protocolVersion, headers.toString(), body);
	}
	
}
