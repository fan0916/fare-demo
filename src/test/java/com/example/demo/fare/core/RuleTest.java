package com.example.demo.fare.core;

import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RuleTest {

    @Test
    public void testBaseUsage() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Rule rule = new Rule();
        rule.setPeekFare(2);
        rule.setNonPeekFare(1);
        rule.setDailyCap(8);
        rule.setWeeklyCap(55);
        PeakPeriods peakPeriods = new PeakPeriods();
        peakPeriods.addInterval(DayOfWeek.SUNDAY, LocalTime.parse("10:00").toSecondOfDay(), LocalTime.parse("10:20").toSecondOfDay());
        rule.setPeakPeriods(peakPeriods);

        assertEquals(2, rule.calculate(sdf.parse("2022-10-30 10:00:00"), 0, 0));
        assertEquals(1, rule.calculate(sdf.parse("2022-10-30 10:30:00"), 0, 0));
        assertEquals(0, rule.calculate(sdf.parse("2022-10-30 10:30:00"), 8, 8));
        assertEquals(0, rule.calculate(sdf.parse("2022-10-30 10:30:00"), 0, 55));
        assertEquals(1, rule.calculate(sdf.parse("2022-10-30 10:00:00"), 7, 10));
        assertEquals(1, rule.calculate(sdf.parse("2022-10-30 10:00:00"), 0, 54));
    }

    @Test
    public void testParameterCheck() {
        Rule rule = new Rule();
        rule.setPeekFare(2);
        rule.setNonPeekFare(1);
        rule.setDailyCap(8);
        rule.setWeeklyCap(55);
        PeakPeriods peakPeriods = new PeakPeriods();
        peakPeriods.addInterval(DayOfWeek.SUNDAY, LocalTime.parse("10:00").toSecondOfDay(), LocalTime.parse("10:20").toSecondOfDay());
        rule.setPeakPeriods(peakPeriods);

        assertThrows(Exception.class, ()-> {
            rule.calculate(null, 0, 0);
        });
    }
}