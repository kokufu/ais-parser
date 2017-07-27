package com.kokufu.nmea;

public class InvalidDataFormatException extends Exception {
    private final String mSentence;

    public InvalidDataFormatException(String sentence) {
        super();
        mSentence = sentence;
    }

    public InvalidDataFormatException(String sentence, String message) {
        super(message);
        mSentence = sentence;
    }

    public InvalidDataFormatException(String sentence, Throwable e) {
        super(e);
        mSentence = sentence;
    }

    public String getSentence() {
        return mSentence;
    }
}
