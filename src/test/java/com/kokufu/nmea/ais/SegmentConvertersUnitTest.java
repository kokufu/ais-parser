package com.kokufu.nmea.ais;

import org.junit.Test;

import java.util.Calendar;

import static com.kokufu.nmea.ais.IsFullCalendar.dateOf;
import static com.kokufu.nmea.ais.IsShortCalendar.dateOf;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

public class SegmentConvertersUnitTest {
    @Test
    public void convertUnsignedIntCorrectly() {
        int value = (int) SegmentConverters.TO_UNSIGNED_INT.convert("1011");

        assertThat(value, is(11));
    }

    @Test
    public void convertSignedIntCorrectly() {
        int value = (int) SegmentConverters.TO_SIGNED_INT.convert("1011");

        assertThat(value, is(-5));
    }

    @Test
    public void converting128ResultsPositive128() {
        int value = (int) SegmentConverters.TO_SIGNED_INT.convert("10000000");

        assertThat(value, is(128));
    }

    @Test
    public void convertShortCalendarCorrectly() {
        Calendar cal = (Calendar) SegmentConverters.TO_SHORT_CALENDAR.convert("01111101110111101001");

        assertThat(cal, is(notNullValue()));

        assertThat(cal, is(dateOf(7, 27, 23, 41)));
    }

    @Test
    public void convertShortCalendarWithDefaultValueCorrectly() {
        Calendar cal = (Calendar) SegmentConverters.TO_SHORT_CALENDAR.convert("00000000000000000000");

        assertThat(cal, is(nullValue()));
    }

    @Test
    public void convertFullCalendarCorrectly() throws Exception {
        Calendar cal = (Calendar) SegmentConverters.TO_FULL_CALENDAR.convert("0001111110000100010100100010110111000110");

        assertThat(cal, is(notNullValue()));

        assertThat(cal, is(dateOf(2017, 1, 9, 2, 55, 6)));
    }

    @Test
    public void convertFullCalendarWithDefaultValueCorrectly() throws Exception {
        Calendar cal = (Calendar) SegmentConverters.TO_FULL_CALENDAR.convert("0000000000000000000000000000000000000000");

        assertThat(cal, is(nullValue()));
    }
}
