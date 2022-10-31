package com.example.demo.fare.core;

import com.google.common.base.Preconditions;
import lombok.Setter;

import java.util.Date;

@Setter
public class Rule {
    private int from;
    private int to;
    private int peekFare;
    private int nonPeekFare;
    private PeakPeriods peakPeriods;
    private Integer dailyCap;
    private Integer weeklyCap;

    public int calculate(Date time, int dailyCharged, int weeklyCharged) {
        Preconditions.checkNotNull(time);

        if ((dailyCap != null && dailyCharged>=dailyCap)
                || (weeklyCap != null && weeklyCharged>=weeklyCap)) {
            return 0;
        }
        boolean isPeak = peakPeriods != null && peakPeriods.isPeak(time);
        int fare = isPeak ? peekFare : nonPeekFare;
        if (dailyCap != null && dailyCharged+fare>dailyCap) {
            fare = dailyCap - dailyCharged;
        }
        if (weeklyCap != null && weeklyCharged+fare>weeklyCap) {
            fare = weeklyCap - weeklyCharged;
        }
        return fare;
    }
}
