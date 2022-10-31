package com.example.demo.fare.store.impl;

import com.example.demo.fare.core.FareStatistic;
import com.example.demo.fare.store.FareChargeRecordStore;
import com.example.demo.fare.util.TimeUtil;
import com.google.common.base.Preconditions;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class InMemoryFareChargeRecordStoreImpl implements FareChargeRecordStore {
    /**
     * userId to context mapping
     */
    private final ConcurrentHashMap<Long, ChargeRecordContext> map = new ConcurrentHashMap<>();

    // skip maintain charge record for demo
    // in real application chargeRecord and statistic should be maintained in one database transaction
    @Override
    public void store(long userId, int from, int to, Date time, int fare) {
        Preconditions.checkNotNull(time);
        Preconditions.checkArgument(fare > 0);

        long weekStartTs = TimeUtil.startTsOfWeek(time);
        DayOfWeek dayOFWeek = time.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime().getDayOfWeek();
        ChargeRecordContext context = map.computeIfAbsent(userId, k -> new ChargeRecordContext());
        context.addFare(weekStartTs, dayOFWeek, fare);
    }

    @Override
    public FareStatistic getFareStatistic(long userId, Date time) {
        Preconditions.checkNotNull(time);

        ChargeRecordContext context = map.get(userId);
        if (context == null) {
            return null;
        }
        return context.getFareStatistic(TimeUtil.startTsOfWeek(time));
    }

    static class ChargeRecordContext {
        /**
         * week start timestamp to FareStatistic mapping
         */
        private final Map<Long,FareStatistic> statisticMap = new HashMap<>();

        public synchronized void addFare(long weekStartTs, DayOfWeek dayOFWeek, int fare) {
            FareStatistic statistic = statisticMap.computeIfAbsent(weekStartTs,
                    k -> new FareStatistic(new int[DayOfWeek.values().length]));
            statistic.addDailyFare(dayOFWeek, fare);
        }

        public FareStatistic getFareStatistic(long weekStartTs) {
            return statisticMap.get(weekStartTs);
        }
    }
}
