package com.kokufu.nmea.ais;

import org.junit.Test;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

public class AisPositionReportClassARecordUnitTest {
    @Test
    public void parseSentenceWithPositiveLatLonCorrectly() throws Exception {
        AisParser parser = new AisParser();

        String sentence = "!AIVDM,1,1,,A,13HOI:0P0000VOHLCnHQKwvL05Ip,0*23";
        // 000001000011011000011111011001001010000000100000000000000000000000000000100110011111011000011100010011110110011000100001011011111111111110011100000000000101011001111000

        AisPositionReportClassARecord record =
                (AisPositionReportClassARecord) parser.parse(sentence);

        assertThat(record.getChannel(), is(AisRecord.CHANNEL.A));
        assertThat(record.getMessageType(), is(1));
        assertThat(record.getRepeatIndicator(), is(0));
        assertThat(record.getMmsi(), is(227006760));
        assertThat(record.getNavigationStatus(), is(0));
        assertThat(record.getRateOfTurn(), is(128));
        double sog = record.getSpeedOverGround();
        assertThat(sog, is(closeTo(0.0, 0.1)));
        assertThat(record.getPositionAccuracy(), is(0));
        double longitude = record.getLongitude();
        assertThat(longitude, is(closeTo(0.13138, 0.00001)));
        double latitude = record.getLatitude();
        assertThat(latitude, is(closeTo(49.47558, 0.00001)));
        double cog = record.getCourseOverGround();
        assertThat(cog, is(closeTo(36.7, 0.1)));
        assertThat(record.getTrueHeading(), is(511));
        assertThat(record.getTimeStamp(), is(14));
    }

    @Test
    public void parseSentenceWithNegativeLatitudeCorrectly() throws Exception {
        AisParser parser = new AisParser();

        String sentence = "!AIVDM,1,1,,B,17u>=L001KR><?EfhW37iVFL05Ip,0*1D";
        // 000001000111111101001110001101011100000000000000000001011011100010001110001100001111010101101110110000100111000011000111110001100110010110011100000000000101011001111000

        AisPositionReportClassARecord record =
                (AisPositionReportClassARecord) parser.parse(sentence);

        assertThat(record.getChannel(), is(AisRecord.CHANNEL.B));
        assertThat(record.getMessageType(), is(1));
        assertThat(record.getRepeatIndicator(), is(0));
        assertThat(record.getMmsi(), is(533958000));
        assertThat(record.getNavigationStatus(), is(0));
        assertThat(record.getRateOfTurn(), is(0));
        double sog = record.getSpeedOverGround();
        assertThat(sog, is(closeTo(9.1, 0.1)));
        assertThat(record.getPositionAccuracy(), is(1));
        double longitude = record.getLongitude();
        assertThat(longitude, is(closeTo(31.06215, 0.00001)));
        double latitude = record.getLatitude();
        assertThat(latitude, is(closeTo(-30.1299, 0.00001)));
        double cog = record.getCourseOverGround();
        assertThat(cog, is(closeTo(199.0, 0.1)));
        assertThat(record.getTrueHeading(), is(203));
        assertThat(record.getTimeStamp(), is(14));
    }

    @Test
    public void parseSentenceWithNegativeLonguitudeCorrectly() throws Exception {
        AisParser parser = new AisParser();

        String sentence = "!AIVDM,1,1,,A,14`bwv000DKGV=H:S06jFRFJ0@9=,0*12";
        // 000001000100101000101010111111111110000000000000000000010100011011010111100110001101011000001010100011000000000110110010010110100010010110011010000000010000001001001101

        AisPositionReportClassARecord record =
                (AisPositionReportClassARecord) parser.parse(sentence);

        assertThat(record.getChannel(), is(AisRecord.CHANNEL.A));
        assertThat(record.getMessageType(), is(1));
        assertThat(record.getRepeatIndicator(), is(0));
        assertThat(record.getMmsi(), is(311083000));
        assertThat(record.getNavigationStatus(), is(0));
        assertThat(record.getRateOfTurn(), is(0));
        double sog = record.getSpeedOverGround();
        assertThat(sog, is(closeTo(2.0, 0.1)));
        assertThat(record.getPositionAccuracy(), is(0));
        double longitude = record.getLongitude();
        assertThat(longitude, is(closeTo(-64.75022, 0.00001)));
        double latitude = record.getLatitude();
        assertThat(latitude, is(closeTo(18.432045, 0.00001)));
        double cog = record.getCourseOverGround();
        assertThat(cog, is(closeTo(60.2, 0.1)));
        assertThat(record.getTrueHeading(), is(75));
        assertThat(record.getTimeStamp(), is(13));
    }
}
