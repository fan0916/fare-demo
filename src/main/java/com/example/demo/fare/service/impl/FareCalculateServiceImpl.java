package com.example.demo.fare.service.impl;

import com.example.demo.fare.core.FareStatistic;
import com.example.demo.fare.core.Rule;
import com.example.demo.fare.service.FareCalculateService;
import com.example.demo.fare.service.RuleSelector;
import com.example.demo.fare.store.FareChargeRecordStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class FareCalculateServiceImpl implements FareCalculateService {
    private final RuleSelector ruleSelector;
    private final FareChargeRecordStore fareChargeRecordStore;

    public FareCalculateServiceImpl(@Autowired RuleSelector ruleSelector,
                                    @Autowired FareChargeRecordStore fareChargeRecordStore) {
        this.ruleSelector = ruleSelector;
        this.fareChargeRecordStore = fareChargeRecordStore;
    }

    @Override
    public int calculate(long userId, int from, int to, Date time) {
        FareStatistic fareStatistic = fareChargeRecordStore.getFareStatistic(userId, time);
        int weeklyFare = fareStatistic == null ? 0 : fareStatistic.getWeeklyFare();
        int dailyFare = fareStatistic == null ? 0 : fareStatistic.getDailyFare(time);
        Rule rule = ruleSelector.select(from, to);
        return rule.calculate(time, dailyFare, weeklyFare);
    }
}
