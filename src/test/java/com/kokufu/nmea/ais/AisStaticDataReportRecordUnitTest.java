package com.kokufu.nmea.ais;

import org.junit.Test;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

public class AisStaticDataReportRecordUnitTest {
    @Test
    public void parseSentenceOfTypeACorrectly() throws Exception {
        AisParser parser = new AisParser();

        String sentence = "!AIVDM,1,1,,B,H6KPfL@pvs7R1@t@u8tdV0l59D0,2*52";
        // 011000000110011011100000101110011100010000111000111110111011000111100010000001010000111100010000111101001000111100101100100110000000110100000101001001010100000000

        AisStaticDataReportRecord record =
                (AisStaticDataReportRecord) parser.parse(sentence);

        assertThat(record.getChannel(), is(AisRecord.CHANNEL.B));
        assertThat(record.getMessageType(), is(24));
        assertThat(record.getRepeatIndicator(), is(0));
        assertThat(record.getMmsi(), is(431500913));
        assertThat(record.getPartType(), is(AisStaticDataReportRecord.PartType.A));
        assertThat(record.getName(), is("NO.18 TODOROKI MARU@"));
        assertThat(record.getTypeOfShipAndCargo(), is(0));
        assertThat(record.getVendorId(), is(nullValue()));
        assertThat(record.getCallSign(), is(nullValue()));
        assertThat(record.getDimensionToBow(), is(0));
        assertThat(record.getDimensionToStern(), is(0));
        assertThat(record.getDimensionToPort(), is(0));
        assertThat(record.getDimensionToStarboard(), is(0));
    }

    @Test
    public void parseSentenceOfTypeBCorrectly() throws Exception {
        AisParser parser = new AisParser();

        String sentence = "!AIVDM,1,1,,A,H6K>GLE@653hhhi:7mjkqP4`<720,0*0F";
        // 011000000110011011001110010111011100010101010000000110000101000011110000110000110000110001001010000111110101110010110011111001100000000100101000001100000111000010000000

        AisStaticDataReportRecord record =
                (AisStaticDataReportRecord) parser.parse(sentence);

        assertThat(record.getChannel(), is(AisRecord.CHANNEL.A));
        assertThat(record.getMessageType(), is(24));
        assertThat(record.getRepeatIndicator(), is(0));
        assertThat(record.getMmsi(), is(431200113));
        assertThat(record.getPartType(), is(AisStaticDataReportRecord.PartType.B));
        assertThat(record.getName(), is(nullValue()));
        assertThat(record.getTypeOfShipAndCargo(), is(80));
        assertThat(record.getVendorId(), is("FEC0001"));
        assertThat(record.getCallSign(), is("JG5239 "));
        assertThat(record.getDimensionToBow(), is(37));
        assertThat(record.getDimensionToStern(), is(12));
        assertThat(record.getDimensionToPort(), is(7));
        assertThat(record.getDimensionToStarboard(), is(2));
    }
}
