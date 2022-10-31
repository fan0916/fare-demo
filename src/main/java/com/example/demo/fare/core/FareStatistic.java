package com.example.demo.fare.core;


import com.google.common.base.Preconditions;

import java.time.DayOfWeek;
import java.time.ZoneId;
import java.util.Date;

public class FareStatistic {
    /**
     * can be mapped to seven fields of one database record
     */
    private final int[] dailyFares;

    public FareStatistic(int[] dailyFares) {
        Preconditions.checkArgument(dailyFares != null && dailyFares.length == DayOfWeek.values().length);
        this.dailyFares = dailyFares;
    }

    // only applicable in standalone mode, would not work in real application
    // used for demo only
    public void addDailyFare(DayOfWeek day, int fare) {
        Preconditions.checkNotNull(day);
        Preconditions.checkArgument(fare>0);
        dailyFares[day.getValue()-1] += fare;
    }

    public int getDailyFare(DayOfWeek day) {
        Preconditions.checkNotNull(day);
        return dailyFares[day.getValue()-1];
    }

    public int getDailyFare(Date time) {
        Preconditions.checkNotNull(time);
        return getDailyFare(time.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime().getDayOfWeek());
    }

    public int getWeeklyFare() {
        int sum = 0;
        for(int daily:dailyFares) {
            sum += daily;
        }
        return sum;
    }
}
