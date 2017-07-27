package com.kokufu.nmea.ais;

import org.junit.Test;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

public class AisPositionReportClassBRecordUnitTest {
    @Test
    public void parseSentenceWithPositiveLatLonCorrectly() throws Exception {
        AisParser parser = new AisParser();

        String sentence = "!AIVDM,1,1,,B,B43JRq00LhTWc5VejDI>wwWUoP06,0*29";
        // 010010000100000011011010100010111001000000000000011100110000100100100111101011000101100110101101110010010100011001001110111111111111100111100101110111100000000000000110

        AisPositionReportClassBRecord record =
                (AisPositionReportClassBRecord) parser.parse(sentence);

        assertThat(record.getChannel(), is(AisRecord.CHANNEL.B));
        assertThat(record.getMessageType(), is(18));
        assertThat(record.getRepeatIndicator(), is(0));
        assertThat(record.getMmsi(), is(272016100));
        double sog = record.getSpeedOverGround();
        assertThat(sog, is(closeTo(11.5, 0.1)));
        assertThat(record.getPositionAccuracy(), is(0));
        double longitude = record.getLongitude();
        assertThat(longitude, is(closeTo(31.99895, 0.00001)));
        double latitude = record.getLatitude();
        assertThat(latitude, is(closeTo(46.94411, 0.00001)));
        double cog = record.getCourseOverGround();
        assertThat(cog, is(closeTo(126.3, 0.1)));
        assertThat(record.getTrueHeading(), is(511));
        assertThat(record.getTimeStamp(), is(15));
    }
}
