package com.example.rates_program.api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Fixer.io API servisi
 */
public interface FixerApiService {
    
    /**
     * Mevcut döviz kurlarını alır
     * @param apiKey Fixer API anahtarı
     * @param base Baz para birimi (ör. EUR)
     * @param symbols İstenilen para birimleri (ör. USD,TRY,GBP,CHF)
     * @return API yanıtı
     */
    @GET("latest")
    Call<FixerResponse> getLatestRates(
            @Query("access_key") String apiKey,
            @Query("base") String base,
            @Query("symbols") String symbols);
}