package com.kokufu.nmea.ais;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class SegmentConverters {
    private SegmentConverters() {
        // static class
    }

    public static final AisRecord.Segment.ISegmentConverter TO_UNSIGNED_INT =
            new AisRecord.Segment.ISegmentConverter() {
                @Override
                public Object convert(String value) {
                    return Integer.parseInt(value, 2);
                }
            };

    public static final AisRecord.Segment.ISegmentConverter TO_SIGNED_INT =
            new AisRecord.Segment.ISegmentConverter() {
                @Override
                public Object convert(String value) {
                    int mask = 1 << (value.length() - 1);
                    int intValue = Integer.parseInt(value, 2);
                    if ((intValue & mask) != 0) {
                        intValue = (~(intValue - 1)) & ((mask - 1) + mask);
                        // Below judgment is not a standard twos-complement integers,
                        // only for AIS
                        if ((intValue & mask) == 0) {
                            intValue *= -1;
                        }
                    }
                    return intValue;
                }
            };

    public static final AisRecord.Segment.ISegmentConverter TO_STRING =
            new AisRecord.Segment.ISegmentConverter() {
                /**
                 * {@see http://catb.org/gpsd/AIVDM.html#_ais_payload_data_types}
                 * @param value like "000100101..."
                 * @return ascii string
                 */
                @Override
                public Object convert(String value) {
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < value.length(); i += 6) {
                        int c = Integer.parseInt(value.substring(i, i + 6), 2);
                        if (c < 32) {
                            c += 64;
                        }
                        sb.append(Character.toString((char) c));
                    }
                    return sb.toString();
                }
            };

    public static final AisRecord.Segment.ISegmentConverter TO_SHORT_CALENDAR =
            new AisRecord.Segment.ISegmentConverter() {
                @Override
                public Object convert(String value) {
                    Calendar calendar = new GregorianCalendar();
                    calendar.setTimeZone(TimeZone.getTimeZone("UTC"));
                    int month = Integer.parseInt(value.substring(0, 4), 2);
                    if (month == 0) {
                        return null;
                    }
                    int day = Integer.parseInt(value.substring(4, 9), 2);
                    if (day == 0) {
                        return null;
                    }
                    calendar.set(Calendar.MONTH, month - 1);
                    calendar.set(Calendar.DAY_OF_MONTH, day);
                    int hour = Integer.parseInt(value.substring(9, 14), 2);
                    if (hour == 24) {
                        return calendar;
                    }
                    int minute = Integer.parseInt(value.substring(14, 20), 2);
                    if (minute == 60) {
                        return calendar;
                    }
                    calendar.set(Calendar.HOUR_OF_DAY, hour);
                    calendar.set(Calendar.MINUTE, minute);
                    return calendar;
                }
            };

    public static final AisRecord.Segment.ISegmentConverter TO_FULL_CALENDAR =
            new AisRecord.Segment.ISegmentConverter() {
                @Override
                public Object convert(String value) {
                    Calendar calendar = new GregorianCalendar();
                    calendar.setTimeZone(TimeZone.getTimeZone("UTC"));
                    int year = Integer.parseInt(value.substring(0, 14), 2);
                    if (year == 0) {
                        return null;
                    }
                    int month = Integer.parseInt(value.substring(14, 18), 2);
                    if (month == 0) {
                        return null;
                    }
                    int day = Integer.parseInt(value.substring(18, 23), 2);
                    if (day == 0) {
                        return null;
                    }
                    calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.MONTH, month - 1);
                    calendar.set(Calendar.DAY_OF_MONTH, day);
                    int hour = Integer.parseInt(value.substring(23, 28), 2);
                    if (hour == 24) {
                        return calendar;
                    }
                    int minute = Integer.parseInt(value.substring(28, 34), 2);
                    if (minute == 60) {
                        return calendar;
                    }
                    int second = Integer.parseInt(value.substring(34, 40), 2);
                    calendar.set(Calendar.HOUR_OF_DAY, hour);
                    calendar.set(Calendar.MINUTE, minute);
                    calendar.set(Calendar.SECOND, second);
                    return calendar;
                }
            };
}
