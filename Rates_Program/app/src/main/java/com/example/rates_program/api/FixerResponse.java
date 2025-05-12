package com.example.rates_program.api;

import com.google.gson.annotations.SerializedName;

import java.util.Map;

/**
 * Fixer API'den gelen yanıtı temsil eden model sınıfı
 */
public class FixerResponse {
    @SerializedName("success")
    private boolean success;
    
    @SerializedName("timestamp")
    private long timestamp;
    
    @SerializedName("base")
    private String base;
    
    @SerializedName("date")
    private String date;
    
    @SerializedName("rates")
    private Map<String, Double> rates;
    
    // Getters
    public boolean isSuccess() {
        return success;
    }
    
    public long getTimestamp() {
        return timestamp;
    }
    
    public String getBase() {
        return base;
    }
    
    public String getDate() {
        return date;
    }
    
    public Map<String, Double> getRates() {
        return rates;
    }
}