package com.kokufu.nmea.ais;

/**
 * For Message 18
 * @see <a href="https://www.navcen.uscg.gov/?pageName=AISMessagesB">Class B AIS Position Report</a>
 */
public class AisPositionReportClassBRecord extends AisRecord {
    private final Segment[] mSegments = {
            new Segment( 46,  56, SegmentConverters.TO_UNSIGNED_INT), // SpeedOverGround
            new Segment( 56,  57, SegmentConverters.TO_UNSIGNED_INT), // PositionAccuracy
            new Segment( 57,  85, SegmentConverters.TO_SIGNED_INT), // Longitude
            new Segment( 85, 112, SegmentConverters.TO_SIGNED_INT), // Latitude
            new Segment(112, 124, SegmentConverters.TO_UNSIGNED_INT), // CourseOverGround
            new Segment(124, 133, SegmentConverters.TO_UNSIGNED_INT), // TrueHeading
            new Segment(133, 139, SegmentConverters.TO_UNSIGNED_INT) // TimeStamp
    };

    @Override
    protected Segment[] getSegments(String binaryData) {
        return mSegments;
    }

    /* package */ AisPositionReportClassBRecord(CHANNEL channel) {
        super(channel);
    }

    public float getSpeedOverGround() {
        return ((int) mSegments[0].getValue()) / 10f;
    }

    public int getPositionAccuracy() {
        return (int) mSegments[1].getValue();
    }

    public float getLongitude() {
        return ((int) mSegments[2].getValue()) / 600000f;
    }

    public float getLatitude() {
        return ((int) mSegments[3].getValue()) / 600000f;
    }

    public float getCourseOverGround() {
        return ((int) mSegments[4].getValue()) / 10f;
    }

    public int getTrueHeading() {
        return (int) mSegments[5].getValue();
    }

    public int getTimeStamp() {
        return (int) mSegments[6].getValue();
    }
}
