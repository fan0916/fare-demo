package com.example.demo.fare.store;

import com.example.demo.fare.core.FareStatistic;

import java.util.Date;

public interface FareChargeRecordStore {
    void store(long userId, int from, int to, Date time, int fare);
    FareStatistic getFareStatistic(long userId, Date time);
}
