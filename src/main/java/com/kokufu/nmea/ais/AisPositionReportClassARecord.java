package com.kokufu.nmea.ais;

/**
 * For Message 1, 2, and 3
 * @see <a href="https://www.navcen.uscg.gov/?pageName=AISMessagesA">Class A AIS Position Report</a>
 */
public class AisPositionReportClassARecord extends AisRecord {
    private final Segment[] mSegments = {
            new Segment( 38,  42, SegmentConverters.TO_UNSIGNED_INT), // NavigationStatus
            new Segment( 42,  50, SegmentConverters.TO_SIGNED_INT), // RateOfTurn
            new Segment( 50,  60, SegmentConverters.TO_UNSIGNED_INT), // SpeedOverGround
            new Segment( 60,  61, SegmentConverters.TO_UNSIGNED_INT), // PositionAccuracy
            new Segment( 61,  89, SegmentConverters.TO_SIGNED_INT), // Longitude
            new Segment( 89, 116, SegmentConverters.TO_SIGNED_INT), // Latitude
            new Segment(116, 128, SegmentConverters.TO_UNSIGNED_INT), // CourseOverGround
            new Segment(128, 137, SegmentConverters.TO_UNSIGNED_INT), // TrueHeading
            new Segment(137, 143, SegmentConverters.TO_UNSIGNED_INT) // TimeStamp
    };

    @Override
    protected Segment[] getSegments(String binaryData) {
        return mSegments;
    }

    /* package */ AisPositionReportClassARecord(CHANNEL channel) {
        super(channel);
    }

    /**
     * @return 0 = under way using engine
     *         1 = at anchor
     *         2 = not under command
     *         3 = restricted maneuverability
     *         4 = constrained by her draught
     *         5 = moored
     *         6 = aground
     *         7 = engaged in fishing
     *         8 = under way sailing
     *         9 = reserved for future amendment of navigational status for ships carrying DG, HS, or MP, or IMO hazard or pollutant category C, high speed craft (HSC)
     *         10 = reserved for future amendment of navigational status for ships carrying dangerous goods (DG), harmful substances (HS) or marine pollutants (MP), or IMO hazard or pollutant category A, wing in ground (WIG)
     *         11 = power-driven vessel towing astern (regional use)
     *         12 = power-driven vessel pushing ahead or towing alongside (regional use)
     *         13 = reserved for future use
     *         14 = AIS-SART (active), MOB-AIS, EPIRB-AIS
     *         15 = undefined = default (also used by AIS-SART, MOB-AIS and EPIRB-AIS under test)
     *
     * @see #getNavigationStatusString()
     */
    public int getNavigationStatus() {
        return (int) mSegments[0].getValue();
    }

    /**
     * @return 0 to +126 = turning right at up to 708 deg per min or higher
     *         0 to -126 = turning left at up to 708 deg per min or higher
     *         127 = turning right at more than 5deg/30s (No TI available)
     *         -127 = turning left at more than 5deg/30s (No TI available)
     *         128 (80 hex) = no turn information available (default)
     */
    public int getRateOfTurn() {
        return (int) mSegments[1].getValue();
    }

    /**
     * @return Speed over ground [knot] (0-102.2 knots)
     *         102.2 = 102.2 knots or higher
     *         103.3 = not available
     */
    public float getSpeedOverGround() {
        return ((int) mSegments[2].getValue() / 10f);
    }

    /**
     * The position accuracy (PA) flag
     * @return {@literal 1 = high (<= 10 m)}
     *         {@literal 0 = low (> 10 m) [default]}
     */
    public int getPositionAccuracy() {
        return (int) mSegments[3].getValue();
    }

    /**
     * Longitude
     *
     * @return -180 to 180 deg
     *         positive = East
     *         negative = West
     *         181 = not available [default]
     */
    public float getLongitude() {
        return ((int) mSegments[4].getValue() / 600000f);
    }

    /**
     * Latitude
     *
     * @return -90 to 90 deg
     *         positive = North
     *         negative = West
     *         91 = not available [default]
     */
    public float getLatitude() {
        return ((int) mSegments[5].getValue() / 600000f);
    }

    /**
     * @return 0 to 359.9 deg
     *         360 = not available [default]
     */
    public float getCourseOverGround() {
        return ((int) mSegments[6].getValue() / 10f);
    }

    /**
     * @return 0 to 359 deg
     *         511 = not available [default]
     */
    public int getTrueHeading() {
        return (int) mSegments[7].getValue();
    }

    /**
     * @return UTC second when the report was generated by the electronic position system
     *         0 to 59 second
     *         60 = not available [default]
     *         61 = positioning system is in manual input mode
     *         62 = electronic position fixing system operates in estimated (dead reckoning) mode
     *         63 = the positioning system is inoperative
     */
    public int getTimeStamp() {
        return (int) mSegments[8].getValue();
    }

    public String getNavigationStatusString() {
        switch (getNavigationStatus()) {
            case 0:
                return "Under way using engine";
            case 1:
                return "At anchor";
            case 2:
                return "Not under command";
            case 3:
                return "Restricted maneuverability";
            case 4:
                return "Constrained by her draught";
            case 5:
                return "Moored";
            case 6:
                return "Aground";
            case 7:
                return "Engaged in fishing";
            case 8:
                return "Under way sailing";
            case 9:
                return "Reserved for future amendment of navigational status for ships carrying DG, HS, or MP, or IMO hazard or pollutant category C, high speed craft (HSC)";
            case 10:
                return "Reserved for future amendment of navigational status for ships carrying dangerous goods (DG), harmful substances (HS) or marine pollutants (MP), or IMO hazard or pollutant category A, wing in ground (WIG)";
            case 11:
                return "Power-driven vessel towing astern (regional use)";
            case 12:
                return "power-driven vessel pushing ahead or towing alongside (regional use)";
            case 13:
                return "Reserved for future use";
            case 14:
                return "AIS-START (active), MOB-AIS, EPIRB-AIS";
            case 15:
            default:
                return  "Undefined";
        }
    }
}
