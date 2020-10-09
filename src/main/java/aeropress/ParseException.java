package aeropress;

import java.net.URISyntaxException;

public class ParseException extends Exception {

    public ParseException(String message) {
        super(message);
    }

    public ParseException(Throwable t) {
        super(t);
    }

}
