package com.kokufu.nmea.ais;

import com.kokufu.nmea.InvalidDataFormatException;

/**
 * For Message 24
 * Class B Only
 * @see <a href="https://www.navcen.uscg.gov/?pageName=AISMessagesB">AIS Standard class B Equipment Position Report</a>
 */
public class AisStaticDataReportRecord extends AisRecord {
    public enum PartType {
        A,
        B,
        INVALID
    }

    private final Segment[] mSegmentsA = {
            new Segment(40, 160, SegmentConverters.TO_STRING)
    };

    private final Segment[] mSegmentsB = {
            new Segment(40, 48, SegmentConverters.TO_UNSIGNED_INT), // TypeOfShipAndCargo
            new Segment(48, 90, SegmentConverters.TO_STRING), //  VendorId
            new Segment(90, 132, SegmentConverters.TO_STRING), // CallSign
            new Segment(132, 141, SegmentConverters.TO_UNSIGNED_INT), // DimensionToBow
            new Segment(141, 150, SegmentConverters.TO_UNSIGNED_INT), // DimensionToStern
            new Segment(150, 156, SegmentConverters.TO_UNSIGNED_INT), // DimensionToPort
            new Segment(156, 162, SegmentConverters.TO_UNSIGNED_INT), // DimensionToStarboard
    };

    private PartType mPartType = PartType.INVALID;

    @Override
    protected Segment[] getSegments(String binaryData) throws InvalidDataFormatException {
        String sentence = binaryData.substring(38, 40);
        int partNumber = Integer.parseInt(sentence, 2);
        if (partNumber == 0) {
            mPartType = PartType.A;
            return mSegmentsA;
        } else if (partNumber == 1) {
            mPartType = PartType.B;
            return mSegmentsB;
        } else {
            throw new InvalidDataFormatException(sentence, "Invalid part number: " + partNumber);
        }
    }

    /* package */ AisStaticDataReportRecord(CHANNEL channel) {
        super(channel);
    }

    /**
     *
     * @return partType
     */
    public PartType getPartType() {
        return mPartType;
    }

    public String getName() {
        if (mPartType == PartType.A) {
            return (String) mSegmentsA[0].getValue();
        }
        return null;
    }

    public int getTypeOfShipAndCargo() {
        if (mPartType == PartType.B) {
            return (int) mSegmentsB[0].getValue();
        }
        return 0;
    }

    public String getVendorId() {
        if (mPartType == PartType.B) {
            return (String) mSegmentsB[1].getValue();
        }
        return null;
    }

    public String getCallSign() {
        if (mPartType == PartType.B) {
            return (String) mSegmentsB[2].getValue();
        }
        return null;
    }

    public int getDimensionToBow() {
        if (mPartType == PartType.B) {
            return (int) mSegmentsB[3].getValue();
        }
        return 0;
    }

    public int getDimensionToStern() {
        if (mPartType == PartType.B) {
            return (int) mSegmentsB[4].getValue();
        }
        return 0;
    }

    public int getDimensionToPort() {
        if (mPartType == PartType.B) {
            return (int) mSegmentsB[5].getValue();
        }
        return 0;
    }

    public int getDimensionToStarboard() {
        if (mPartType == PartType.B) {
            return (int) mSegmentsB[6].getValue();
        }
        return 0;
    }
}
