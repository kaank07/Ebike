package com.example.ebikechargingapi.repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class InMemoryResultStorage implements ResultStorage {
    private final List<LocalDateTime> results;

    public InMemoryResultStorage() {
        this.results = new ArrayList<>();
    }

    @Override
    public void saveResult(LocalDateTime bestChargingTime) {
        results.add(bestChargingTime);
    }

    public List<LocalDateTime> getResults() {
        return results;
    }
}
