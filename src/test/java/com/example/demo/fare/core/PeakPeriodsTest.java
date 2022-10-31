package com.example.demo.fare.core;

import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

class PeakPeriodsTest {

    @Test
    public void testBaseUsage() throws ParseException {
        PeakPeriods peakPeriods = new PeakPeriods();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        assertFalse(peakPeriods.isPeak(sdf.parse("2022-10-25 08:10:10")));

        peakPeriods.addInterval(DayOfWeek.SUNDAY, LocalTime.parse("10:00").toSecondOfDay(), LocalTime.parse("10:20").toSecondOfDay());
        assertTrue(peakPeriods.isPeak(sdf.parse("2022-10-30 10:00:00")));
        assertTrue(peakPeriods.isPeak(sdf.parse("2022-10-30 10:20:00")));
        assertTrue(peakPeriods.isPeak(sdf.parse("2022-10-30 10:10:10")));
        assertFalse(peakPeriods.isPeak(sdf.parse("2022-10-30 09:59:59")));
        assertFalse(peakPeriods.isPeak(sdf.parse("2022-10-30 10:20:01")));

        peakPeriods.addInterval(DayOfWeek.SUNDAY, LocalTime.parse("00:00").toSecondOfDay(), LocalTime.parse("00:10").toSecondOfDay());
        peakPeriods.addInterval(DayOfWeek.SUNDAY, LocalTime.parse("23:50").toSecondOfDay(), LocalTime.parse("23:59").toSecondOfDay());
        peakPeriods.addInterval(DayOfWeek.WEDNESDAY, LocalTime.parse("12:00").toSecondOfDay(), LocalTime.parse("12:10").toSecondOfDay());
        peakPeriods.addInterval(DayOfWeek.MONDAY, LocalTime.parse("00:00").toSecondOfDay(), LocalTime.parse("00:10").toSecondOfDay());
        peakPeriods.addInterval(DayOfWeek.MONDAY, LocalTime.parse("23:50").toSecondOfDay(), LocalTime.parse("23:59").toSecondOfDay());
        assertFalse(peakPeriods.isPeak(sdf.parse("2022-10-26 00:00:01")));
        assertFalse(peakPeriods.isPeak(sdf.parse("2022-10-26 23:55:01")));
        assertTrue(peakPeriods.isPeak(sdf.parse("2022-10-26 12:05:01")));
        assertTrue(peakPeriods.isPeak(sdf.parse("2022-10-24 00:05:00")));
        assertTrue(peakPeriods.isPeak(sdf.parse("2022-10-24 23:55:00")));
    }

    @Test
    public void testParameterCheck() {
        PeakPeriods peakPeriods = new PeakPeriods();

        assertThrows(Exception.class, () -> {
            peakPeriods.addInterval(DayOfWeek.SUNDAY, 0, 24*60*60);
        });
        assertThrows(Exception.class, () -> {
            peakPeriods.addInterval(DayOfWeek.SUNDAY, 0, -1);
        });
        assertThrows(Exception.class, () -> {
            peakPeriods.addInterval(DayOfWeek.SUNDAY, 100, 10);
        });
    }
}