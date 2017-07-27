package com.kokufu.nmea.ais;

import com.kokufu.nmea.ParserBase;
import com.kokufu.nmea.InvalidDataFormatException;
import com.kokufu.nmea.NoParserException;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class AisParser extends ParserBase {
    public final static String AIVDM_PROTOCOL = "!AIVDM";
    public final static String AIVDO_PROTOCOL = "!AIVDO";
    private final static int POS_PREFIX = 0;
    private final static int POS_NUMBER_OF_SENTENCES = 1;
    private final static int POS_SENTENCE_NUMBER = 2;
    private final static int POS_SEQUENTIAL_MESSAGE_ID = 3;
    private final static int POS_CHANNEL = 4;
    private final static int POS_DATA = 5;
    private final Map<String, AisRecord> mRecordStore = new HashMap<>();

    private final Map<Integer, Class<? extends AisRecord>> mCustomRecord = new HashMap<>();


    public void setCustomRecord(int messageType, Class<? extends AisRecord> recordClass) {
        mCustomRecord.put(messageType, recordClass);
    }

    public void removeCustomRecord(int messageType) {
        mCustomRecord.remove(messageType);
    }

    @Override
    protected AisRecord doParse(String sentence) throws
            InvalidDataFormatException, NoParserException {
        String[] dataElement = sentence.split(",");
        AisRecord.CHANNEL channel;
        switch (dataElement[POS_PREFIX]) {
            case AIVDM_PROTOCOL:
                try {
                    channel = AisRecord.CHANNEL.valueOf(dataElement[POS_CHANNEL]);
                } catch (IllegalArgumentException e) {
                    throw new InvalidDataFormatException(sentence,
                            "Invalid channel " + dataElement[POS_CHANNEL]);
                }
                break;
            case AIVDO_PROTOCOL:
                channel = AisRecord.CHANNEL.OWN;
                break;
            default:
                throw new InvalidDataFormatException(sentence,
                        "Unknown Prefix " + dataElement[POS_PREFIX]);
        }

        int numberOfSentences;
        try {
            numberOfSentences = Integer.parseInt(dataElement[POS_NUMBER_OF_SENTENCES]);
        } catch (NumberFormatException e) {
            throw new InvalidDataFormatException(sentence,
                    "NumberOfSentences is not number " + dataElement[POS_NUMBER_OF_SENTENCES]);
        }

        int sentenceNumber;
        try {
            sentenceNumber = Integer.parseInt(dataElement[POS_SENTENCE_NUMBER]);
        } catch (NumberFormatException e) {
            throw new InvalidDataFormatException(sentence,
                    "Sentence Number is not number " + dataElement[POS_SENTENCE_NUMBER]);
        }

        String messageId = dataElement[POS_SEQUENTIAL_MESSAGE_ID];

        String binaryString = decodeDataToBinaryString(dataElement[POS_DATA]);

        AisRecord record;
        if (sentenceNumber == 1) {
            int messageType;
            try {
                messageType = Integer.parseInt(binaryString.substring(0, 6), 2);
            } catch (NumberFormatException | IndexOutOfBoundsException e) {
                throw new InvalidDataFormatException(sentence, "Invalid Message Type");
            }

            if (mCustomRecord.containsKey(messageType)) {
                try {
                    record = mCustomRecord.get(messageType)
                            .getDeclaredConstructor(AisRecord.CHANNEL.class)
                            .newInstance(channel);
                } catch (NoSuchMethodException |
                        IllegalAccessException |
                        InstantiationException |
                        InvocationTargetException e) {
                    throw new NoParserException(e);
                }
            } else if (messageType < 1 || 27 < messageType) {
                throw new InvalidDataFormatException("Invalid Message Type: " + messageType);
            } else if (messageType == 1 || messageType == 2 || messageType == 3) {
                record = new AisPositionReportClassARecord(channel);
            } else if (messageType == 5) {
                record = new AisStaticAndVoyageRelatedDataRecord(channel);
            } else if (messageType == 18) {
                record = new AisPositionReportClassBRecord(channel);
            } else if (messageType == 24) {
                record = new AisStaticDataReportRecord(channel);
            } else {
                throw new NoParserException();
            }
        } else {
            if (messageId == null || messageId.equals("")) {
                throw new InvalidDataFormatException(sentence, "Invalid Message ID");
            }
            record = mRecordStore.remove(messageId);
            if (record == null) {
                throw new InvalidDataFormatException(sentence, "Invalid Message ID: " + messageId);
            }
        }

        boolean isFinalSentence = sentenceNumber == numberOfSentences;

        if (numberOfSentences > 1 && !isFinalSentence) {
            if (messageId == null || messageId.equals("")) {
                throw new InvalidDataFormatException(sentence, "Invalid Message ID");
            }
            mRecordStore.put(messageId, record);
        }

        try {
            record.appendBinaryData(binaryString, isFinalSentence);
        } catch (IndexOutOfBoundsException e) {
            throw new InvalidDataFormatException(sentence, "Invalid data length");
        }

        if (isFinalSentence) {
            return record;
        } else {
            return null;
        }
    }

    /**
     * {@see http://catb.org/gpsd/AIVDM.html#_aivdm_aivdo_payload_armoring}
     * @param encodedString data payload
     * @return binary string like "000100101..."
     */
    private static String decodeDataToBinaryString(String encodedString) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < encodedString.length(); i++) {
            int c = encodedString.charAt(i) - 48;
            if (c > 40) {
                c -= 8;
            }
            String bin = Integer.toBinaryString(c);
            for (int j = 0; j < 6 - bin.length(); j++) {
                sb.append("0");
            }
            sb.append(bin);
        }
        return sb.toString();
    }
}