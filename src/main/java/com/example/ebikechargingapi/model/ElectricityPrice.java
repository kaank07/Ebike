package com.example.ebikechargingapi.model;

import java.time.LocalDateTime;

public class ElectricityPrice {
    private LocalDateTime time;
    private double price;

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}