package com.example.demo.fare.store.impl;

import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryFareChargeRecordStoreImplTest {

    @Test
    public void testBaseUsage() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        InMemoryFareChargeRecordStoreImpl store = new InMemoryFareChargeRecordStoreImpl();
        assertNull(store.getFareStatistic(100L, sdf.parse("2022-10-25 14:35:10")));

        store.store(100L, 1, 2, sdf.parse("2022-10-25 14:35:10"), 1);
        assertEquals(1, store.getFareStatistic(100L, sdf.parse("2022-10-25 14:35:10")).getDailyFare(sdf.parse("2022-10-25 14:35:10")));
        assertEquals(1, store.getFareStatistic(100L, sdf.parse("2022-10-25 14:35:10")).getWeeklyFare());
        assertNull( store.getFareStatistic(100L, sdf.parse("2022-10-10 14:35:10")));
        assertNull( store.getFareStatistic(101L, sdf.parse("2022-10-25 14:35:10")));
    }

    @Test
    public void testParameterCheck() {
        InMemoryFareChargeRecordStoreImpl store = new InMemoryFareChargeRecordStoreImpl();
        assertThrows(Exception.class, ()-> store.getFareStatistic(200L, null));
    }
}