package com.example.ebikechargingapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.ebikechargingapi.model.ElectricityPrice;
import com.example.ebikechargingapi.repository.ElectricityPriceLoader;
import com.example.ebikechargingapi.repository.InMemoryResultStorage;
import com.example.ebikechargingapi.repository.JsonElectricityPriceLoader;
import com.example.ebikechargingapi.repository.ResultStorage;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class ChargingController {

    private final ElectricityPriceLoader electricityPriceLoader;
    private final ResultStorage resultStorage;

    public ChargingController() {
        this.electricityPriceLoader = new JsonElectricityPriceLoader();
        this.resultStorage = new InMemoryResultStorage();
    }

    @GetMapping("/")
    public String home() {
        return "Welcome to the E-Bike Charging API. Use the /best-charging-time endpoint with a readyTime parameter to find the best charging time.";
    }

    @GetMapping("/best-charging-time")
    public ResponseEntity<?> getBestChargingTime(@RequestParam String readyTime) throws IOException {
        List<ElectricityPrice> electricityPrices = electricityPriceLoader.loadElectricityPrices();
        LocalDateTime desiredReadyTime = LocalDateTime.parse(readyTime, DateTimeFormatter.ISO_DATE_TIME);
        LocalDateTime bestChargingTime = calculateBestChargingTime(electricityPrices, desiredReadyTime);
        resultStorage.saveResult(bestChargingTime);
        return ResponseEntity.ok().body(Map.of("bestChargingTime", bestChargingTime.format(DateTimeFormatter.ISO_DATE_TIME)));
    }

    private LocalDateTime calculateBestChargingTime(List<ElectricityPrice> electricityPrices, LocalDateTime desiredReadyTime) {
        double minCost = Double.MAX_VALUE;
        LocalDateTime bestChargingTime = null;
        LocalDateTime currentTime = LocalDateTime.now();

        List<Double> prices = new ArrayList<>();
        for (int i = 0; i < electricityPrices.size() - 2; i++) {
            double currentCost = electricityPrices.get(i).getPrice() + electricityPrices.get(i + 1).getPrice() + electricityPrices.get(i + 2).getPrice();
            prices.add(currentCost);
        }

        List<LocalDateTime> chargingTimes = new ArrayList<>();
        for (int i = 0; i < electricityPrices.size() - 2; i++) {
            LocalDateTime priceTime = electricityPrices.get(i).getTime();
            LocalDateTime endTime = priceTime.plusHours(3);

            if ((priceTime.isAfter(currentTime) || priceTime.isEqual(currentTime)) && (endTime.isBefore(desiredReadyTime) || endTime.isEqual(desiredReadyTime))) {
                chargingTimes.add(priceTime);
            }
        }

        if (chargingTimes.isEmpty()) {
            throw new IllegalStateException("No suitable charging time found.");
        }

        for (int i = 0; i < chargingTimes.size(); i++) {
            if (prices.get(i) < minCost) {
                minCost = prices.get(i);
                bestChargingTime = chargingTimes.get(i);
            }
        }

        return bestChargingTime;
    }
}
