package com.example.demo.fare.util;

import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TimeUtilTest {

    @Test
    public void testBaseUsage() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ssZ");

        assertEquals("2022-10-23 00:00:00+0800", sdf.format(new Date(TimeUtil.startTsOfWeek(sdf.parse("2022-10-23 00:00:00+0800")))));
        assertEquals("2022-10-23 00:00:00+0800", sdf.format(new Date(TimeUtil.startTsOfWeek(sdf.parse("2022-10-25 10:00:00+0800")))));
        assertEquals("2022-10-23 00:00:00+0800", sdf.format(new Date(TimeUtil.startTsOfWeek(sdf.parse("2022-10-29 00:00:00+0800")))));
        assertEquals("2022-10-23 00:00:00+0800", sdf.format(new Date(TimeUtil.startTsOfWeek(sdf.parse("2022-10-29 23:59:59+0800")))));
    }

    @Test
    public void testPassYear() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ssZ");
        assertEquals("2021-12-26 00:00:00+0800", sdf.format(new Date(TimeUtil.startTsOfWeek(sdf.parse("2022-01-01 23:59:59+0800")))));
    }
}