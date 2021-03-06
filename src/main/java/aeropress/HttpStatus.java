package aeropress;

public enum HttpStatus {
	OK("OK", 200),
	BAD_REQUEST("Bad Request", 400),
	NOT_FOUND("Not Found", 404),
	METHOD_NOT_ALLOWED("Method Not Allowed", 405);
	
	private final String text;
	private final Integer code;
	
	HttpStatus(String text, Integer code) {
		this.text = text;
		this.code = code;
	}

	public String getText() {
		return text;
	}

	public Integer getCode() {
		return code;
	}
}
