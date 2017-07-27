package com.kokufu.nmea;

import org.junit.Test;

public class ParserBaseUnitTest {
    private static class DummyParser extends ParserBase {
        @Override
        protected IRecord doParse(String sentence) throws
                InvalidDataFormatException, NoParserException {
            return null;
        }
    }

    @Test
    public void parseSentenceWithCorrectChecksumSuccessfully() throws Exception {
        DummyParser parser = new DummyParser();
        String sentence = "$GPRMC,085120.307,A,3541.1493,N,13945.3994,E,000.0,240.3,181211,,,A*6A";
        parser.parse(sentence);
    }

    @Test(expected = InvalidDataFormatException.class)
    public void parseSentenceWithUncorrectChecksumThrowsException() throws Exception {
        DummyParser parser = new DummyParser();
        String sentence = "$GPRMC,085120.307,A,3541.1493,N,13945.3994,E,000.0,240.3,181211,,,A*5A";
        parser.parse(sentence);
    }
}
