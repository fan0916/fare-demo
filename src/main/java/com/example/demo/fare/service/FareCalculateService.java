package com.example.demo.fare.service;

import java.util.Date;

public interface FareCalculateService {
    int calculate(long userId, int from, int to, Date time);
}
