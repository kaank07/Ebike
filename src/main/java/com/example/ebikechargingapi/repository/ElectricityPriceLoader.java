package com.example.ebikechargingapi.repository;

import java.io.IOException;
import java.util.List;

import com.example.ebikechargingapi.model.ElectricityPrice;

public interface ElectricityPriceLoader {
    List<ElectricityPrice> loadElectricityPrices() throws IOException;
}