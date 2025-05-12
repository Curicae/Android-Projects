package com.example.rates_program;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.example.rates_program.api.RetrofitClient;
import com.example.rates_program.api.FixerApiService;
import com.example.rates_program.api.FixerResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    // API anahtarı
    private static final String API_KEY = "<Your_API_Key";
    // Para birimleri
    private static final String SYMBOLS = "TRY,USD,EUR,GBP,CHF";
    // Baz para birimi
    private static final String BASE_CURRENCY = "EUR";

    private TextView tvLastUpdate;
    private TextView tvTRY, tvUSD, tvEUR, tvGBP, tvCHF;
    private Button btnRefresh;
    private ProgressBar progressBar;

    // Para Birimi Dönüştürücü UI Elemanları
    private Spinner spinnerFromCurrency, spinnerToCurrency;
    private EditText etAmount;
    private Button btnConvert;
    private TextView tvConvertedAmount;

    private FixerApiService apiService;
    private Map<String, Double> currentRates = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // UI bileşenlerini başlat
        initViews();
        
        // API servisini başlat
        apiService = RetrofitClient.getFixerApiService();
        
        // Para birimi dönüştürücüyü ayarla
        setupCurrencyConverter();
        
        // Uygulama başladığında kurları yükle
        fetchCurrencyRates();
        
        // Yenile butonuna tıklama olayını ekle
        btnRefresh.setOnClickListener(v -> fetchCurrencyRates());
    }
    
    private void initViews() {
        tvLastUpdate = findViewById(R.id.tvLastUpdate);
        tvTRY = findViewById(R.id.tvTRY);
        tvUSD = findViewById(R.id.tvUSD);
        tvEUR = findViewById(R.id.tvEUR);
        tvGBP = findViewById(R.id.tvGBP);
        tvCHF = findViewById(R.id.tvCHF);
        btnRefresh = findViewById(R.id.btnRefresh);
        progressBar = findViewById(R.id.progressBar);

        // Dönüştürücü elemanları
        spinnerFromCurrency = findViewById(R.id.spinnerFromCurrency);
        spinnerToCurrency = findViewById(R.id.spinnerToCurrency);
        etAmount = findViewById(R.id.etAmount);
        btnConvert = findViewById(R.id.btnConvert);
        tvConvertedAmount = findViewById(R.id.tvConvertedAmount);
    }
    
    private void setupCurrencyConverter() {
        List<String> currencyList = Arrays.asList(SYMBOLS.split(","));
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, currencyList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerFromCurrency.setAdapter(adapter);
        spinnerToCurrency.setAdapter(adapter);

        btnConvert.setOnClickListener(v -> performConversion());
    }
    
    private void performConversion() {
        String fromCurrency = spinnerFromCurrency.getSelectedItem().toString();
        String toCurrency = spinnerToCurrency.getSelectedItem().toString();
        String amountStr = etAmount.getText().toString();

        if (TextUtils.isEmpty(amountStr)) {
            showError("Lütfen bir miktar girin.");
            return;
        }

        if (currentRates == null || currentRates.isEmpty()) {
            showError("Döviz kurları henüz yüklenmedi. Lütfen bekleyin veya güncelleyin.");
            return;
        }

        try {
            double amount = Double.parseDouble(amountStr);
            double result = convertCurrency(amount, fromCurrency, toCurrency);
            tvConvertedAmount.setText(String.format(Locale.getDefault(), "Sonuç: %.2f %s", result, toCurrency));
        } catch (NumberFormatException e) {
            showError("Geçersiz miktar formatı.");
        } catch (IllegalArgumentException e) {
            showError(e.getMessage()); // Hata mesajını doğrudan göster (ör: kur bulunamadı)
        }
    }
    
    private double convertCurrency(double amount, String fromCurrency, String toCurrency) {
        if (fromCurrency.equals(toCurrency)) {
            return amount;
        }

        // API baz para birimi EUR olduğu için tüm hesaplamalar EUR üzerinden yapılır.
        Double fromRate = currentRates.get(fromCurrency);
        Double toRate = currentRates.get(toCurrency);

        if (fromRate == null || toRate == null) {
            throw new IllegalArgumentException("Seçilen para birimleri için kur bulunamadı.");
        }

        // Önce miktarı baz para birimine (EUR) çevir
        double amountInBase = amount / fromRate;

        // Sonra baz para biriminden hedef para birimine çevir
        return amountInBase * toRate;
    }
    
    private void fetchCurrencyRates() {
        showLoading(true);
        tvConvertedAmount.setText("Sonuç: --"); // Kurlar güncellenirken eski sonucu temizle
        
        apiService.getLatestRates(API_KEY, BASE_CURRENCY, SYMBOLS)
                .enqueue(new Callback<FixerResponse>() {
                    @Override
                    public void onResponse(Call<FixerResponse> call, Response<FixerResponse> response) {
                        showLoading(false);
                        
                        if (response.isSuccessful() && response.body() != null) {
                            FixerResponse data = response.body();
                            
                            if (data.isSuccess()) {
                                currentRates = data.getRates(); // Kurları sakla
                                updateUiWithRates(data);
                            } else {
                                showError("API yanıtı başarılı değil veya beklenmeyen bir hata oluştu.");
                            }
                        } else {
                            showError("API yanıtı alınamadı: " + response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<FixerResponse> call, Throwable t) {
                        showLoading(false);
                        showError("Ağ hatası: " + t.getMessage());
                    }
                });
    }
    
    private void updateUiWithRates(FixerResponse data) {
        // Son güncelleme zamanını ayarla
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss", Locale.getDefault());
        String currentTime = sdf.format(new Date(data.getTimestamp() * 1000L)); // API'den gelen timestamp'ı kullan
        tvLastUpdate.setText("Son Güncelleme: " + currentTime);
        
        // Kurları güncelle (currentRates zaten sınıf seviyesinde saklandı)
        if (currentRates != null) {
            tvTRY.setText(String.format(Locale.getDefault(), "%.4f", currentRates.getOrDefault("TRY", 0.0)));
            tvUSD.setText(String.format(Locale.getDefault(), "%.4f", currentRates.getOrDefault("USD", 0.0)));
            tvEUR.setText(String.format(Locale.getDefault(), "%.4f", currentRates.getOrDefault("EUR", 0.0))); // EUR her zaman 1.0 olmalı (eğer base ise)
            tvGBP.setText(String.format(Locale.getDefault(), "%.4f", currentRates.getOrDefault("GBP", 0.0)));
            tvCHF.setText(String.format(Locale.getDefault(), "%.4f", currentRates.getOrDefault("CHF", 0.0)));
        }
    }
    
    private void showLoading(boolean isLoading) {
        progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        btnRefresh.setEnabled(!isLoading);
        btnConvert.setEnabled(!isLoading); // Yükleme sırasında dönüştürme butonu da pasif olsun
    }
    
    private void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show(); // Hata mesajları daha uzun süre görünsün
    }
}
