package com.kokufu.nmea.ais;

import com.kokufu.nmea.IRecord;
import com.kokufu.nmea.InvalidDataFormatException;

public abstract class AisRecord implements IRecord {
    public enum CHANNEL {
        A, // 161.975Mhz (87B)
        B, // 162.025Mhz (88B)
        OWN
    }

    protected static class Segment {
        interface ISegmentConverter {
            Object convert(String value);
        }

        private final int mFrom;
        private final int mTo;
        private final ISegmentConverter mConverter;
        private Object mValue;

        protected Segment(int from, int to, ISegmentConverter converter) {
            this.mFrom = from;
            this.mTo = to;
            this.mConverter = converter;
        }

        private void setValue(String value) throws InvalidDataFormatException {
            try {
                mValue = mConverter.convert(value);
            } catch (NumberFormatException e) {
                throw new InvalidDataFormatException(value, e);
            }
        }

        protected Object getValue() {
            return mValue;
        }
    }

    private final Segment mCommonSegments[] = {
            new Segment(  0,   6, SegmentConverters.TO_UNSIGNED_INT), // MessageType
            new Segment(  6,   8, SegmentConverters.TO_UNSIGNED_INT), // RepeatIndicator
            new Segment(  8,  38, SegmentConverters.TO_UNSIGNED_INT), // Mmsi
    };

    private final CHANNEL mChannel;
    private StringBuilder mDataCache = null;

    protected AisRecord(CHANNEL channel) {
        mChannel = channel;
    }

    /* package */ void appendBinaryData(String binaryData, boolean isFinalSentence)
            throws StringIndexOutOfBoundsException, InvalidDataFormatException {
        if (!isFinalSentence) {
            if (mDataCache == null) {
                mDataCache = new StringBuilder();
            }
            mDataCache.append(binaryData);
        } else {
            if (mDataCache == null) {
                parseToAisRecord(binaryData);
            } else {
                mDataCache.append(binaryData);
                parseToAisRecord(mDataCache.toString());
                mDataCache = null;
            }
        }
    }

    public CHANNEL getChannel() {
        return mChannel;
    }

    protected abstract Segment[] getSegments(String binaryData) throws InvalidDataFormatException;

    private void parseToAisRecord(String binaryData)
            throws StringIndexOutOfBoundsException,
            InvalidDataFormatException {
        for (Segment segment : mCommonSegments) {
            String value = binaryData.substring(segment.mFrom, segment.mTo);
            segment.setValue(value);
        }
        for (Segment segment : getSegments(binaryData)) {
            String value = binaryData.substring(segment.mFrom, segment.mTo);
            segment.setValue(value);
        }
    }

    /**
     * @return message type which identify this message.
     */
    public int getMessageType() {
        return (int) mCommonSegments[0].getValue();
    }

    /**
     * Used by the repeater to indicate how many times a message has been repeated.
     * @return 0: default
     *         3: do not repeat any more
     */
    public int getRepeatIndicator() {
        return (int) mCommonSegments[1].getValue();
    }

    public int getMmsi() {
        return (int) mCommonSegments[2].getValue();
    }
}
