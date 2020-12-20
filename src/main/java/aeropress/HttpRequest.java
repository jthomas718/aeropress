package aeropress;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class HttpRequest {
	private static final String CRLF = "\r\n";
	private final Map<String, String> headers = new HashMap<>();
	private final HttpMethod method;
	private final String protocolVersion;
	private final Map<String, String> queryParams = new HashMap<>();
	private Map<String, String> pathParams = new HashMap<>();
	private URI uri;
	private String body;

	public HttpRequest(String rawRequest) throws ParseException {
		int offset = rawRequest.indexOf(CRLF);
		if (offset == -1) {
			throw new ParseException("Request appears to be missing CRLF");
		}

		int doubleCrlfIndex = rawRequest.indexOf(CRLF + CRLF);
		if (doubleCrlfIndex == -1) {
			throw new ParseException("Request is missing double CRLF");
		}

		// Parse request line
		String requestLine = rawRequest.substring(0, offset);
		String[] fields = requestLine.split(" ");
		if (fields.length < 3) {
			throw new ParseException("Expected 3 fields in request line of the form 'method url protocol-version' and only found " + fields.length + " fields");
		}
		this.method = HttpMethod.valueOf(fields[0]);
		parseUri(fields[1]);
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
		this.body = rawRequest.substring(doubleCrlfIndex + (CRLF.length() * 2));
		if (this.body.isEmpty()) {
			this.body = null;
		}
	}

	private void parseUri(String rawUri) throws ParseException {
		try {
			this.uri = new URI(rawUri);
		} catch (URISyntaxException e) {
			throw new ParseException(e);
		}

		if (uri.getQuery() != null) {
			for (String param : uri.getQuery().split("&")) {
				String[] pair = param.split("=");
				if (pair.length < 2) {
					throw new ParseException("Malformed query in URI");
				}
				queryParams.put(pair[0], pair[1]);
			}
		}
	}
	
	public String header(String name) {
		return headers.get(name);
	}
	
	public HttpMethod method() {
		return method;
	}
	
	public URI uri() {
		return uri;
	}

	public Map<String, String> queryParams() {
		return queryParams;
	}
	
	public String protocolVersion() {
		return protocolVersion;
	}
	
	public String body() {
		return body;
	}

	public Map<String, String> pathParams() {
		return pathParams;
	}

	protected void setPathParams(Map<String, String> pathParams) {
		this.pathParams = pathParams;
	}

	
	@Override
	public String toString() {
		return String.format("Method: %s\nURI: %s\nProtocol version: %s\n"
				+ "Headers: %s\nBody: %s", method, uri.toString(), protocolVersion, headers.toString(), body);
	}
	
}
