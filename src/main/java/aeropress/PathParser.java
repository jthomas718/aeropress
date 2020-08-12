package aeropress;

import java.util.HashMap;
import java.util.Map;

public class PathParser {
	public static final ParseResult NO_MATCH = new ParseResult(false, null);
	
	public static PathParser.ParseResult parse(String path, String pathTemplate) {
		Map<String, String> params = new HashMap<>();
		String[] pathVals = path.endsWith("/") ? path.substring(0, path.length() - 1).split("/") : path.split("/");
		String[] pathTemplateVals = pathTemplate.endsWith("/") ? pathTemplate.substring(0, pathTemplate.length() - 1).split("/") : pathTemplate.split("/");
		
		if (pathVals.length != pathTemplateVals.length) {
			return NO_MATCH;
		}
		
		for (int i = 0; i < pathVals.length; i++) {
			if (pathTemplateVals[i].startsWith(":")) {
				params.put(pathTemplateVals[i].substring(1), pathVals[i]);
			} else if (!pathVals[i].equals(pathTemplateVals[i])) {
				return NO_MATCH;
			}
		}
		
		return new ParseResult(true, params);
	}
	
	public static class ParseResult {
		private final boolean matches;
		private final Map<String, String> parsedParams;
		
		public ParseResult(boolean matches, Map<String, String> parsedParams) {
			this.matches = matches;
			this.parsedParams = parsedParams;
		}
		
		public boolean matches() {
			return matches;
		}
		
		public Map<String, String> params() {
			return parsedParams;
		}
	}
}
