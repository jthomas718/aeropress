package aeropress;

import java.util.HashMap;
import java.util.Map;

public class PathParser {
	public static final ParseResult NO_MATCH = new ParseResult(false, null);

	public static PathParser.ParseResult parse(String path, String pathTemplate) {
		Map<String, String> pathParams = new HashMap<>();
		String[] pathVals = path.endsWith("/") ? path.substring(0, path.length() - 1).split("/") : path.split("/");
		String[] pathTemplateVals = pathTemplate.endsWith("/") ? pathTemplate.substring(0, pathTemplate.length() - 1).split("/") : pathTemplate.split("/");
		
		if (pathVals.length != pathTemplateVals.length) {
			return NO_MATCH;
		}
		
		for (int i = 0; i < pathVals.length; i++) {
			if (pathTemplateVals[i].startsWith(":")) {
				pathParams.put(pathTemplateVals[i].substring(1), pathVals[i]);
			} else if (!pathVals[i].equals(pathTemplateVals[i])) {
				return NO_MATCH;
			}
		}
		
		return new ParseResult(true, pathParams);
	}
	
	public static class ParseResult {
		private final boolean matches;
		private final Map<String, String> pathParams;
		
		public ParseResult(boolean matches, Map<String, String> pathParams) {
			this.matches = matches;
			this.pathParams = pathParams;
		}
		
		public boolean matches() {
			return matches;
		}
		
		public Map<String, String> pathParams() {
			return pathParams;
		}
	}
}
