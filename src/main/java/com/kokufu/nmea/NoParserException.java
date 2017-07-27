package com.kokufu.nmea;

public class NoParserException extends Exception {
    public NoParserException() {
        super();
    }

    public NoParserException(String message) {
        super(message);
    }

    public NoParserException(Throwable e) {
        super(e);
    }
}
