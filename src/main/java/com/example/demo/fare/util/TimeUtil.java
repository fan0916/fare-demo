package com.example.demo.fare.util;

import java.util.Calendar;
import java.util.Date;

public class TimeUtil {
    /**
     * start timestamp of week is timezone-specific, time is calculated under current timezone
     * @param time
     * @return
     */
    public static long startTsOfWeek(Date time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(time);
        calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar.getTimeInMillis();
    }
}
