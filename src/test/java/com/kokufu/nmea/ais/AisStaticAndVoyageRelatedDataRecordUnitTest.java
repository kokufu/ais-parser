package com.kokufu.nmea.ais;

import org.junit.Test;

import static com.kokufu.nmea.ais.IsShortCalendar.dateOf;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

public class AisStaticAndVoyageRelatedDataRecordUnitTest {
    @Test
    public void parseNormalSentencesCorrectly() throws Exception {
        AisParser parser = new AisParser();

        String sentence = "!AIVDM,2,1,9,B,53nFBv01SJ<thHp6220H4heHTf2222222222221?50:454o<`9QSlUDp,0*09";
        // 000101000011110110010110010010111110000000000001100011011010001100111100110000011000111000000110000010000010000000011000000100110000101101011000100100101110000010000010000010000010000010000010000010000010000010000010000010000010000001001111000101000000001010000100000101000100110111001100101000001001100001100011110100100101010100111000

        AisStaticAndVoyageRelatedDataRecord record =
                (AisStaticAndVoyageRelatedDataRecord) parser.parse(sentence);

        assertNull(record);

        String sentence2 = "!AIVDM,2,2,9,B,888888888888880,2*2E";
        // 001000001000001000001000001000001000001000001000001000001000001000001000001000001000000000

        record = (AisStaticAndVoyageRelatedDataRecord) parser.parse(sentence2);

        assertThat(record.getChannel(), is(AisRecord.CHANNEL.B));
        assertThat(record.getMessageType(), is(5));
        assertThat(record.getRepeatIndicator(), is(0));
        assertThat(record.getMmsi(), is(258315000));
        assertThat(record.getAisVersion(), is(0));
        assertThat(record.getImoNumber(), is(6514895));
        assertThat(record.getCallSign(), is("LFNA   "));
        assertThat(record.getName(), is("FALKVIK             "));
        assertThat(record.getTypeOfShipAndCargo(), is(79));
        assertThat(record.getDimensionToBow(), is(40));
        assertThat(record.getDimensionToStern(), is(10));
        assertThat(record.getDimensionToPort(), is(4));
        assertThat(record.getDimensionToStarboard(), is(5));
        assertThat(record.getTypeOfElectronicPositionFixingDevice(), is(1));
        assertThat(record.getEta(), is(dateOf(3, 14, 12, 40)));
        double maxPresentStaticDraught = record.getMaxPresentStaticDraught();
        assertThat(maxPresentStaticDraught, is(closeTo(3.8, 0.1)));
        assertThat(record.getDestination(), is("FORUS               "));
        assertThat(record.getDte(), is(0));
    }
}
