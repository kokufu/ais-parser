package com.kokufu.nmea;

public abstract class ParserBase {
    public IRecord parse(String sentence) throws
            InvalidDataFormatException, NoParserException {
        if (!validateCheckSum(sentence)) {
            throw new InvalidDataFormatException(sentence, "Checksum Error");
        }

        return doParse(sentence);
    }

    protected abstract IRecord doParse(String sentence) throws
            InvalidDataFormatException, NoParserException;


    private static boolean validateCheckSum(String sentence) {
        sentence = sentence.trim();

        int starIndex = sentence.indexOf("*");
        if (starIndex < 0) {
            return false;
        }
        try {
            String csKey = sentence.substring(starIndex + 1);
            int csk = Integer.parseInt(csKey, 16);
            String str2validate = sentence.substring(1, sentence.indexOf("*"));
            int calcCheckSum = calculateCheckSum(str2validate);

            return (calcCheckSum == csk);
        } catch (IndexOutOfBoundsException | NumberFormatException e) {
            return false;
        }
    }

    private static int calculateCheckSum(String str) {
        char[] ca = str.toCharArray();
        int cs = ca[0];
        for (int i = 1; i < ca.length; i++) {
            cs = cs ^ ca[i];
        }
        return cs;
    }
}
