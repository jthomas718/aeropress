package aeropress;

import java.util.HashMap;
import java.util.Map;

public class HttpResponse {
	private static final String CRLF ="\r\n";
	private final HttpStatus status;
	private final Map<String, String> headers;
	private final String protocolVersion;
	private final String body;
	private final String message;
	
	private HttpResponse(
			HttpStatus status, 
			Map<String, String> headers, 
			String protocolVersion,
			String body
			) {
		
		this.status = status;
		this.headers = headers;
		this.protocolVersion = protocolVersion;
		this.body = body;
		
		StringBuilder messageBuilder = new StringBuilder()
			.append(protocolVersion).append(" ")
			.append(status.getCode().toString()).append(" ")
			.append(status.getText()).append(CRLF);
		
		for (Map.Entry<String, String> entry : headers.entrySet()) {
			messageBuilder
				.append(entry.getKey())
				.append(": ")
				.append(entry.getValue())
				.append(CRLF);
		}
		
		messageBuilder
			.append(CRLF)
			.append(body);
		
		this.message = messageBuilder.toString();
	}

	public HttpStatus getStatus() {
		return status;
	}

	public Map<String, String> getHeaders() {
		return headers;
	}

	public String getProtocolVersion() {
		return protocolVersion;
	}

	public String getBody() {
		return body;
	}

	public String getMessage() {
		return message;
	}
	
	public static Builder builder() {
		return new Builder();
	}
	
	public static class Builder {
		private HttpStatus status = null;
		private Map<String, String> headers = new HashMap<>();
		private String protocolVersion = "HTTP/1.1";
		private String body = null;
		
		public Builder status(HttpStatus status) {
			this.status = status;
			return this;
		}
		
		public Builder header(String name, String value) {
			this.headers.put(name, value);
			return this;
		}
		
		public Builder headers(Map<String, String> headers) {
			this.headers = headers;
			return this;
		}
		
		public Builder protocolVersion(String protocolVersion) {
			this.protocolVersion = protocolVersion;
			return this;
		}
		
		public Builder body(String body) {
			this.body = body;
			return this;
		}
		
		public HttpResponse build() {
			return new HttpResponse(
					this.status,
					this.headers,
					this.protocolVersion,
					this.body
					);
		}
	}
}
