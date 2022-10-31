package com.example.demo.fare.service;

import com.example.demo.fare.core.Rule;

public interface RuleSelector {
    Rule select(int from, int to);
}
