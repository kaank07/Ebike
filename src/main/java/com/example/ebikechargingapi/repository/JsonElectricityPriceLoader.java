package com.example.ebikechargingapi.repository;

import com.example.ebikechargingapi.model.ElectricityPrice;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class JsonElectricityPriceLoader implements ElectricityPriceLoader {

    private final ObjectMapper objectMapper;

    public JsonElectricityPriceLoader() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Override
    public List<ElectricityPrice> loadElectricityPrices() throws IOException {
        FileInputStream inputStream = new FileInputStream("C:/Users/Kaan Küçük/Desktop/ebike-charging-api/src/main/resources/electricityPricePerHour.json");
        return objectMapper.readValue(inputStream, objectMapper.getTypeFactory().constructCollectionType(List.class, ElectricityPrice.class));
    }
}
