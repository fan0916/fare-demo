package com.example.demo.fare.core;

import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FareStatisticTest {

    @Test
    public void testBaseUsage() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        FareStatistic statistic = new FareStatistic(new int[7]);
        assertEquals(0, statistic.getWeeklyFare());
        assertEquals(0, statistic.getDailyFare(sdf.parse("2022-10-10 00:00:05")));
        assertEquals(0, statistic.getDailyFare(sdf.parse("2022-10-16 00:00:05")));

        statistic.addDailyFare(DayOfWeek.MONDAY, 5);
        assertEquals(5, statistic.getWeeklyFare());
        assertEquals(5, statistic.getDailyFare(sdf.parse("2022-10-10 00:00:05")));
        assertEquals(0, statistic.getDailyFare(sdf.parse("2022-10-16 00:00:05")));

        statistic.addDailyFare(DayOfWeek.TUESDAY, 6);
        assertEquals(11, statistic.getWeeklyFare());
        assertEquals(5, statistic.getDailyFare(sdf.parse("2022-10-10 00:00:05")));
        assertEquals(6, statistic.getDailyFare(sdf.parse("2022-10-11 00:00:05")));
        assertEquals(0, statistic.getDailyFare(sdf.parse("2022-10-16 00:00:05")));

        statistic.addDailyFare(DayOfWeek.SUNDAY, 7);
        assertEquals(18, statistic.getWeeklyFare());
        assertEquals(5, statistic.getDailyFare(sdf.parse("2022-10-10 00:00:05")));
        assertEquals(6, statistic.getDailyFare(sdf.parse("2022-10-11 00:00:05")));
        assertEquals(7, statistic.getDailyFare(sdf.parse("2022-10-16 00:00:05")));

        statistic.addDailyFare(DayOfWeek.MONDAY, 8);
        statistic.addDailyFare(DayOfWeek.MONDAY, 9);
        assertEquals(35, statistic.getWeeklyFare());
        assertEquals(22, statistic.getDailyFare(sdf.parse("2022-10-10 00:00:05")));
    }

    @Test
    public void testParameterCheck() {
        FareStatistic statistic = new FareStatistic(new int[7]);
        assertThrows(Exception.class, ()-> statistic.addDailyFare(null, 5));
        assertThrows(Exception.class, ()-> statistic.addDailyFare(DayOfWeek.MONDAY, 0));
        assertThrows(Exception.class, ()-> statistic.getDailyFare((DayOfWeek) null));
    }
}