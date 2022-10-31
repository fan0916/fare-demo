package com.example.demo.fare.core;

import com.google.common.base.Preconditions;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoField;
import java.util.*;

public class PeakPeriods {
    private final Map<DayOfWeek, List<int[]>> map = new HashMap<>();

    public synchronized void addInterval(DayOfWeek dayOfWeek, int start, int end) {
        Preconditions.checkNotNull(dayOfWeek);
        ChronoField.SECOND_OF_DAY.checkValidValue(start);
        ChronoField.SECOND_OF_DAY.checkValidValue(end);
        Preconditions.checkArgument(start<=end);

        List<int[]> intervals = map.computeIfAbsent(dayOfWeek, k -> new ArrayList<>());
        intervals.add(new int[]{start, end});
    }

    public boolean isPeak(Date time) {
        //can be optimized by sorting interval list
        LocalDateTime localDateTime = time.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        DayOfWeek dayOfWeek = localDateTime.getDayOfWeek();
        int secondsOfDay = localDateTime.get(ChronoField.SECOND_OF_DAY);
        for (int[] interval : map.getOrDefault(dayOfWeek, Collections.emptyList())) {
            if (secondsOfDay>=interval[0] && secondsOfDay<=interval[1]) {
                return true;
            }
        }
        return false;
    }
}
