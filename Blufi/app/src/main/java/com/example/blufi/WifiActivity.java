package com.example.blufi;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class WifiActivity extends AppCompatActivity {

    private static final int REQUEST_WIFI_PERMISSIONS = 200;
    
    private WifiManager wifiManager;
    private Button btnToggleWifi, btnScanNetworks;
    private TextView statusText;
    private RecyclerView networksRecyclerView;
    private List<ScanResult> wifiList = new ArrayList<>();
    private WifiNetworkAdapter networkAdapter;
    
    private final String[] REQUIRED_PERMISSIONS = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_WIFI_STATE,
            Manifest.permission.CHANGE_WIFI_STATE
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi);

        // Initialize UI components
        btnToggleWifi = findViewById(R.id.btnToggleWifi);
        btnScanNetworks = findViewById(R.id.btnScanNetworks);
        statusText = findViewById(R.id.statusText);
        networksRecyclerView = findViewById(R.id.networksRecyclerView);

        // Set up RecyclerView
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        networksRecyclerView.setLayoutManager(layoutManager);
        
        // Add divider between items
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(
                networksRecyclerView.getContext(), layoutManager.getOrientation());
        networksRecyclerView.addItemDecoration(dividerItemDecoration);
        
        // Initialize adapter and set it to RecyclerView
        networkAdapter = new WifiNetworkAdapter(this, wifiList);
        networksRecyclerView.setAdapter(networkAdapter);

        // Initialize WiFi manager
        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        // Register for broadcasts when a scan has completed
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        registerReceiver(wifiScanReceiver, intentFilter);

        // Setup click listeners for buttons
        setupClickListeners();
        
        // Update WiFi status UI
        updateWifiStatus();
    }

    private void setupClickListeners() {
        btnToggleWifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleWifi();
            }
        });

        btnScanNetworks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hasRequiredPermissions()) {
                    scanWifiNetworks();
                } else {
                    requestWifiPermissions();
                }
            }
        });
        
        // Back to Main Menu button click listener
        Button btnBackToMain = findViewById(R.id.btnBackToMain);
        btnBackToMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Close this activity and return to the previous one (MainActivity)
            }
        });
    }

    private void toggleWifi() {
        if (wifiManager != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                // For Android 10 and above, direct Wi-Fi control is restricted
                // We need to take users to the Wi-Fi settings
                Intent panelIntent = new Intent(android.provider.Settings.Panel.ACTION_WIFI);
                Toast.makeText(this, "Lütfen WiFi'yi manuel olarak açın/kapatın", Toast.LENGTH_LONG).show();
                startActivityForResult(panelIntent, 1);
            } else {
                // For Android 9 and below
                if (wifiManager.isWifiEnabled()) {
                    wifiManager.setWifiEnabled(false);
                    Toast.makeText(this, "WiFi kapatılıyor", Toast.LENGTH_SHORT).show();
                } else {
                    wifiManager.setWifiEnabled(true);
                    Toast.makeText(this, "WiFi açılıyor", Toast.LENGTH_SHORT).show();
                }
                updateWifiStatus();
            }
            
            // Register for WiFi state changes to update UI
            IntentFilter filter = new IntentFilter(WifiManager.WIFI_STATE_CHANGED_ACTION);
            registerReceiver(wifiStateReceiver, filter);
        }
    }
    
    // BroadcastReceiver for WiFi state changes
    private final BroadcastReceiver wifiStateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int wifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, WifiManager.WIFI_STATE_UNKNOWN);
            
            switch (wifiState) {
                case WifiManager.WIFI_STATE_ENABLED:
                    updateWifiStatus();
                    break;
                case WifiManager.WIFI_STATE_ENABLING:
                    statusText.setText("WiFi: Açılıyor...");
                    break;
                case WifiManager.WIFI_STATE_DISABLED:
                    updateWifiStatus();
                    break;
                case WifiManager.WIFI_STATE_DISABLING:
                    statusText.setText("WiFi: Kapatılıyor...");
                    break;
            }
        }
    };

    private boolean hasRequiredPermissions() {
        for (String permission : REQUIRED_PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    private void requestWifiPermissions() {
        ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_WIFI_PERMISSIONS);
    }

    private void updateWifiStatus() {
        if (wifiManager.isWifiEnabled()) {
            statusText.setText("WiFi: Açık");
            btnToggleWifi.setText("WiFi'yi Kapat");
        } else {
            statusText.setText("WiFi: Kapalı");
            btnToggleWifi.setText("WiFi'yi Aç");
        }
    }

    private void scanWifiNetworks() {
        // First check if WiFi is enabled
        if (!wifiManager.isWifiEnabled()) {
            Toast.makeText(this, "WiFi kapalı, lütfen önce açın", Toast.LENGTH_SHORT).show();
            return;
        }

        // Ensure we have the necessary permissions
        if (!hasRequiredPermissions()) {
            Toast.makeText(this, "WiFi taraması için izinler gerekli", Toast.LENGTH_SHORT).show();
            requestWifiPermissions();
            return;
        }

        Toast.makeText(this, "WiFi ağları taranıyor...", Toast.LENGTH_SHORT).show();
        
        // Ensure the broadcast receiver is registered
        try {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
            registerReceiver(wifiScanReceiver, intentFilter);
        } catch (Exception e) {
            // Receiver might already be registered
        }
        
        // Android 9+ has restrictions on how frequently you can scan
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            // For newer devices, we need to use a workaround by opening WiFi settings
            if (!wifiManager.startScan()) {
                Toast.makeText(this, "Tarama limiti aşıldı, manuel tarama yapılıyor", Toast.LENGTH_SHORT).show();
                
                // Show the results anyway - might be from cache
                wifiList = wifiManager.getScanResults();
                if (wifiList != null && !wifiList.isEmpty()) {
                    displayWifiNetworks(wifiList);
                } else {
                    Toast.makeText(this, "Ağ bulunamadı, lütfen WiFi ayarlarından tarayın", Toast.LENGTH_LONG).show();
                }
            }
        } else {
            // Pre-Android 9 doesn't have scan throttling
            wifiManager.startScan();
        }
    }
    
    private void displayWifiNetworks(List<ScanResult> networks) {
        if (networks.size() > 0) {
            StringBuilder networksStr = new StringBuilder();
            for (ScanResult scanResult : networks) {
                if (scanResult.SSID != null && !scanResult.SSID.isEmpty()) {
                    networksStr.append(scanResult.SSID).append(" (")
                        .append(scanResult.level).append("dBm)").append("\n");
                }
            }
            Toast.makeText(this, "Bulunan ağlar:\n" + networksStr.toString(), Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Herhangi bir WiFi ağı bulunamadı", Toast.LENGTH_SHORT).show();
        }
    }

    // Broadcast receiver for WiFi scan results
    private final BroadcastReceiver wifiScanReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            boolean success = intent.getBooleanExtra(WifiManager.EXTRA_RESULTS_UPDATED, false);
            if (success) {
                scanSuccess();
            } else {
                scanFailure();
            }
        }
    };

    private void scanSuccess() {
        // Get scan results
        List<ScanResult> scanResults = wifiManager.getScanResults();
        
        // Filter out empty SSIDs if needed
        List<ScanResult> filteredResults = new ArrayList<>();
        for (ScanResult result : scanResults) {
            if (result.SSID != null && !result.SSID.isEmpty()) {
                filteredResults.add(result);
            }
        }
        
        // Update our list
        wifiList.clear();
        wifiList.addAll(filteredResults);
        
        // Update the adapter
        runOnUiThread(() -> {
            networkAdapter.updateNetworks(wifiList);
            
            // Also update status text
            if (wifiList.size() > 0) {
                statusText.setText(wifiList.size() + " WiFi ağı bulundu");
            } else {
                statusText.setText("Herhangi bir WiFi ağı bulunamadı");
                Toast.makeText(this, "Herhangi bir WiFi ağı bulunamadı", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void scanFailure() {
        runOnUiThread(() -> {
            Toast.makeText(this, "WiFi taraması başarısız oldu", Toast.LENGTH_SHORT).show();
            statusText.setText("WiFi taraması başarısız oldu");
            
            // Try to show cached results if any
            List<ScanResult> cachedResults = wifiManager.getScanResults();
            if (cachedResults != null && !cachedResults.isEmpty()) {
                wifiList.clear();
                wifiList.addAll(cachedResults);
                networkAdapter.updateNetworks(wifiList);
                Toast.makeText(this, "Önbelleğe alınmış ağlar gösteriliyor", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_WIFI_PERMISSIONS) {
            boolean allPermissionsGranted = true;
            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    allPermissionsGranted = false;
                    break;
                }
            }

            if (allPermissionsGranted) {
                Toast.makeText(this, "İzinler verildi, şimdi WiFi taraması yapabilirsiniz", Toast.LENGTH_SHORT).show();
                scanWifiNetworks();
            } else {
                Toast.makeText(this, "WiFi taraması için izinler gerekli", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Unregister broadcast receivers
        try {
            unregisterReceiver(wifiScanReceiver);
        } catch (IllegalArgumentException e) {
            // Receiver was not registered
        }
        
        try {
            unregisterReceiver(wifiStateReceiver);
        } catch (IllegalArgumentException e) {
            // Receiver was not registered
        }
    }
}