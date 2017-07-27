package com.kokufu.nmea.ais;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class IsShortCalendar extends BaseMatcher<Calendar> {
    private final int mMonth;
    private final int mDay;
    private final int mHour;
    private final int mMinute;
    private Calendar mActual = null;

    /* package */ static Matcher<Calendar> dateOf(int month, int day, int hour, int minute) {
        return new IsShortCalendar(month, day, hour, minute);
    }

    private IsShortCalendar(int month, int day, int hour, int minute) {
        mMonth = month;
        mDay = day;
        mHour = hour;
        mMinute = minute;
    }

    @Override
    public boolean matches(Object item) {
        if (!(item instanceof Calendar)) {
            return false;
        }
        mActual = (Calendar) item;
        return mActual.get(Calendar.MONTH) == mMonth - 1 &&
               mActual.get(Calendar.DAY_OF_MONTH) == mDay &&
               mActual.get(Calendar.HOUR_OF_DAY) == mHour &&
               mActual.get(Calendar.MINUTE) == mMinute;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText(
                String.format(Locale.US, "%02d/%02d %02d:%02d",
                        mMonth,
                        mDay,
                        mHour,
                        mMinute));
        if (mActual != null) {
            description.appendText(" but actual is ");
            description.appendText(
                    new SimpleDateFormat("MM/dd HH:mm", Locale.US).format(mActual.getTime()));
        }
    }
}
