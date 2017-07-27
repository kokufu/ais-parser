package com.kokufu.nmea.ais;

import java.util.Calendar;

/**
 * For message 5
 * Class A Only
 * @see <a href="http://catb.org/gpsd/AIVDM.html#_type_5_static_and_voyage_related_data">Static and Voyage Related Data</a>
 */
public class AisStaticAndVoyageRelatedDataRecord extends AisRecord {
    private final Segment[] mSegments = {
            new Segment(38, 40, SegmentConverters.TO_UNSIGNED_INT), // AisVersion
            new Segment(40, 70, SegmentConverters.TO_UNSIGNED_INT), // ImoNumber
            new Segment(70, 112, SegmentConverters.TO_STRING), // CallSign
            new Segment(112, 232, SegmentConverters.TO_STRING), // Name
            new Segment(232, 240, SegmentConverters.TO_UNSIGNED_INT), // TypeOfShipAndCargo
            new Segment(240, 249, SegmentConverters.TO_UNSIGNED_INT), // DimensionToBow
            new Segment(249, 258, SegmentConverters.TO_UNSIGNED_INT), // DimensionToStern
            new Segment(258, 264, SegmentConverters.TO_UNSIGNED_INT), // DimensionToPort
            new Segment(264, 270, SegmentConverters.TO_UNSIGNED_INT), // DimensionToStarboard
            new Segment(270, 274, SegmentConverters.TO_UNSIGNED_INT), // TypeOfElectronicPositionFixingDevice
            new Segment(274, 294, SegmentConverters.TO_SHORT_CALENDAR), // ETA
            new Segment(294, 302, SegmentConverters.TO_UNSIGNED_INT), // MaxPresentStaticDraught
            new Segment(302, 422, SegmentConverters.TO_STRING), // Destination
            new Segment(422, 423, SegmentConverters.TO_UNSIGNED_INT) // DTE
    };

    @Override
    protected Segment[] getSegments(String binaryData) {
        return mSegments;
    }

    /* package */ AisStaticAndVoyageRelatedDataRecord(CHANNEL channel) {
        super(channel);
    }

    public int getAisVersion() {
        return (int) mSegments[0].getValue();
    }

    public int getImoNumber() {
        return (int) mSegments[1].getValue();
    }

    public String getCallSign() {
        return (String) mSegments[2].getValue();
    }

    public String getName() {
        return (String) mSegments[3].getValue();
    }

    public int getTypeOfShipAndCargo() {
        return (int) mSegments[4].getValue();
    }

    public int getDimensionToBow() {
        return (int) mSegments[5].getValue();
    }

    public int getDimensionToStern() {
        return (int) mSegments[6].getValue();
    }

    public int getDimensionToPort() {
        return (int) mSegments[7].getValue();
    }

    public int getDimensionToStarboard() {
        return (int) mSegments[8].getValue();
    }

    public int getTypeOfElectronicPositionFixingDevice() {
        return (int) mSegments[9].getValue();
    }

    public Calendar getEta() {
        return (Calendar) mSegments[10].getValue();
    }

    public float getMaxPresentStaticDraught() {
        return ((int) mSegments[11].getValue()) / 10f;
    }

    public String getDestination() {
        return (String) mSegments[12].getValue();
    }

    public int getDte() {
        return (int) mSegments[13].getValue();
    }
}
