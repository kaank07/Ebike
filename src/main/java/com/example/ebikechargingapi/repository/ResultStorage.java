package com.example.ebikechargingapi.repository;

import java.time.LocalDateTime;

public interface ResultStorage {
    void saveResult(LocalDateTime bestChargingTime);
}