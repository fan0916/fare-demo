package com.example.demo.fare.service.impl;

import com.example.demo.fare.core.PeakPeriods;
import com.example.demo.fare.core.Rule;
import com.example.demo.fare.service.RuleSelector;
import com.google.common.base.Preconditions;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class FileConfigRuleSelectorImpl implements RuleSelector, ResourceLoaderAware, InitializingBean {
    private static final String ruleConfigName = "fareRules";
    private static final String peakConfigName = "peakHours";

    private ResourceLoader resourceLoader;
    /**
     * from --> to --> rule
     */
    private final Map<Integer, Map<Integer, Rule>> ruleMap = new HashMap<>();

    @Override
    public Rule select(int from, int to) {
        return ruleMap.getOrDefault(from, Collections.emptyMap()).get(to);
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        //maybe better to use application.yaml
        try (InputStream ruleConfigStream = resourceLoader.getResource(ResourceLoader.CLASSPATH_URL_PREFIX + ruleConfigName).getInputStream();
             InputStream peakConfigStream = resourceLoader.getResource(ResourceLoader.CLASSPATH_URL_PREFIX + peakConfigName).getInputStream()
            ) {
            BufferedReader peakReader = new BufferedReader(new InputStreamReader(peakConfigStream));
            SimpleDateFormat sdf = new SimpleDateFormat("HH:ss");
            PeakPeriods peakPeriods = new PeakPeriods();
            List<String> peakLines = peakReader.lines().collect(Collectors.toList());
            for (String peakLine : peakLines) {
                String[] split = peakLine.split(",");
                Preconditions.checkState(split.length==4);

                int startDay = Integer.parseInt(split[0]);
                int endDay = Integer.parseInt(split[1]);
                Preconditions.checkState(startDay<=endDay);

                int intervalStart = LocalTime.parse(split[2]).toSecondOfDay();
                int intervalEnd = LocalTime.parse(split[3]).toSecondOfDay();
                Preconditions.checkState(intervalStart<=intervalEnd);
                for(int day=startDay;day<=endDay;day++) {
                    peakPeriods.addInterval(DayOfWeek.of(day), intervalStart, intervalEnd);
                }
            }

            BufferedReader ruleReader = new BufferedReader(new InputStreamReader(ruleConfigStream));
            List<String> rules = ruleReader.lines().collect(Collectors.toList());
            for (String rule : rules) {
                Rule ruleConfig = new Rule();
                ruleConfig.setPeakPeriods(peakPeriods);
                String[] split = rule.split(",");
                int from = Integer.parseInt(split[0]);
                int to = Integer.parseInt(split[1]);
                ruleConfig.setFrom(from);
                ruleConfig.setTo(to);
                ruleConfig.setPeekFare(Integer.parseInt(split[2]));
                ruleConfig.setNonPeekFare(Integer.parseInt(split[3]));
                ruleConfig.setDailyCap(Integer.parseInt(split[4]));
                ruleConfig.setWeeklyCap(Integer.parseInt(split[5]));
                ruleMap.computeIfAbsent(from, k -> new HashMap<>()).put(to, ruleConfig);
            }
        }
    }
}
