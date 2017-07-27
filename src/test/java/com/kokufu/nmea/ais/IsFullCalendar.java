package com.kokufu.nmea.ais;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/* package */ class IsFullCalendar extends BaseMatcher<Calendar> {
    private final int mYear;
    private final int mMonth;
    private final int mDay;
    private final int mHour;
    private final int mMinute;
    private final int mSecond;
    private Calendar mActual = null;

    /* package */ static Matcher<Calendar> dateOf(int year, int month, int day, int hour, int minute, int second) {
        return new IsFullCalendar(year, month, day, hour, minute, second);
    }

    private IsFullCalendar(int year, int month, int day, int hour, int minute, int second) {
        mYear = year;
        mMonth = month;
        mDay = day;
        mHour = hour;
        mMinute = minute;
        mSecond = second;
    }

    @Override
    public boolean matches(Object item) {
        if (!(item instanceof Calendar)) {
            return false;
        }
        mActual = (Calendar) item;
        return mActual.get(Calendar.YEAR) == mYear &&
                mActual.get(Calendar.MONTH) == mMonth - 1 &&
                mActual.get(Calendar.DAY_OF_MONTH) == mDay &&
                mActual.get(Calendar.HOUR_OF_DAY) == mHour &&
                mActual.get(Calendar.MINUTE) == mMinute &&
                mActual.get(Calendar.SECOND) == mSecond;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText(
                String.format(Locale.US, "%d/%02d/%02d %02d:%02d:%02d",
                        mYear,
                        mMonth,
                        mDay,
                        mHour,
                        mMinute,
                        mSecond));
        if (mActual != null) {
            description.appendText(" but actual is ");
            description.appendText(
                    new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.US).format(mActual.getTime()));
        }
    }
}
