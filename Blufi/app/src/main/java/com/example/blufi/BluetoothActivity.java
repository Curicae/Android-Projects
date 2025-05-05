package com.example.blufi;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
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

public class BluetoothActivity extends AppCompatActivity {

    private static final int REQUEST_BLUETOOTH_PERMISSIONS = 100;
    private static final int REQUEST_ENABLE_BT = 101;

    private BluetoothAdapter bluetoothAdapter;
    private Button btnToggleBluetooth, btnScanDevices;
    private TextView statusText;
    private RecyclerView devicesRecyclerView;
    private List<BluetoothDevice> discoveredDevices = new ArrayList<>();
    private List<Short> signalStrengths = new ArrayList<>();
    private BluetoothDeviceAdapter deviceAdapter;

    private final String[] REQUIRED_PERMISSIONS;

    // Constructor to initialize required permissions based on Android SDK version
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            // Android 12+ (API 31+)
            REQUIRED_PERMISSIONS = new String[]{
                    Manifest.permission.BLUETOOTH_SCAN,
                    Manifest.permission.BLUETOOTH_CONNECT,
                    Manifest.permission.ACCESS_FINE_LOCATION
            };
        } else {
            // Android 11 and below
            REQUIRED_PERMISSIONS = new String[]{
                    Manifest.permission.BLUETOOTH,
                    Manifest.permission.BLUETOOTH_ADMIN,
                    Manifest.permission.ACCESS_FINE_LOCATION
            };
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);

        // Initialize UI components
        btnToggleBluetooth = findViewById(R.id.btnToggleBluetooth);
        btnScanDevices = findViewById(R.id.btnScanDevices);
        statusText = findViewById(R.id.statusText);
        devicesRecyclerView = findViewById(R.id.devicesRecyclerView);

        // Set up RecyclerView
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        devicesRecyclerView.setLayoutManager(layoutManager);
        
        // Add divider between items
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(
                devicesRecyclerView.getContext(), layoutManager.getOrientation());
        devicesRecyclerView.addItemDecoration(dividerItemDecoration);
        
        // Initialize and set adapter
        deviceAdapter = new BluetoothDeviceAdapter(this, discoveredDevices, signalStrengths);
        devicesRecyclerView.setAdapter(deviceAdapter);
        
        // Initialize Bluetooth adapter
        BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        if (bluetoothManager != null) {
            bluetoothAdapter = bluetoothManager.getAdapter();
        }

        // Check if device supports Bluetooth
        if (bluetoothAdapter == null) {
            Toast.makeText(this, "Bu cihaz Bluetooth desteklemiyor", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Set up click listeners
        setupClickListeners();

        // Update the UI based on Bluetooth state
        updateBluetoothStatus();

        // Register for broadcasts when a device is discovered
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(receiver, filter);
    }

    private void setupClickListeners() {
        btnToggleBluetooth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleBluetooth();
            }
        });

        btnScanDevices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (hasRequiredPermissions()) {
                    scanForDevices();
                } else {
                    requestBluetoothPermissions();
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

    private void toggleBluetooth() {
        if (bluetoothAdapter.isEnabled()) {
            // Try forceful approach to turn OFF Bluetooth
            try {
                // First, make sure we have all the necessary permissions
                if (!hasRequiredPermissions()) {
                    requestBluetoothPermissions();
                    return;
                }
                
                // Try direct disable
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    // For Android 12+
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED) {
                        bluetoothAdapter.disable();
                        Toast.makeText(this, "Bluetooth kapatılıyor...", Toast.LENGTH_SHORT).show();
                    } else {
                        requestBluetoothPermissions();
                        return;
                    }
                } else {
                    // For Android 11 and below
                    bluetoothAdapter.disable();
                    Toast.makeText(this, "Bluetooth kapatılıyor...", Toast.LENGTH_SHORT).show();
                }
                
                // Try force through root-level command (this may not work on all devices)
                try {
                    Process process = Runtime.getRuntime().exec("su -c service call bluetooth_manager 8");
                    process.waitFor();
                } catch (Exception e) {
                    // Just ignore if we can't use root commands
                }
            } catch (Exception e) {
                Toast.makeText(this, "Bluetooth kapatılamadı: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        } else {
            // Turn ON Bluetooth - this is usually easier than turning it off
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            try {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED) {
                    startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
                } else {
                    requestBluetoothPermissions();
                }
            } catch (Exception e) {
                Toast.makeText(this, "Bluetooth açılamadı: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
        
        // Register for Bluetooth state changes to update UI automatically
        try {
            IntentFilter filter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
            registerReceiver(bluetoothStateReceiver, filter);
        } catch (Exception e) {
            // Receiver might already be registered
        }
        
        // Update UI immediately for feedback
        updateBluetoothStatus();
    }
    
    // BroadcastReceiver for Bluetooth state changes
    private final BroadcastReceiver bluetoothStateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            
            if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);
                
                switch (state) {
                    case BluetoothAdapter.STATE_OFF:
                        updateBluetoothStatus();
                        break;
                    case BluetoothAdapter.STATE_TURNING_OFF:
                        statusText.setText("Bluetooth: Kapatılıyor...");
                        break;
                    case BluetoothAdapter.STATE_ON:
                        updateBluetoothStatus();
                        break;
                    case BluetoothAdapter.STATE_TURNING_ON:
                        statusText.setText("Bluetooth: Açılıyor...");
                        break;
                }
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

    private void requestBluetoothPermissions() {
        ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_BLUETOOTH_PERMISSIONS);
    }

    private void updateBluetoothStatus() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            statusText.setText("Bluetooth: İzinler gerekli");
            return;
        }
        
        if (bluetoothAdapter.isEnabled()) {
            statusText.setText("Bluetooth: Açık");
            btnToggleBluetooth.setText("Bluetooth'u Kapat");
        } else {
            statusText.setText("Bluetooth: Kapalı");
            btnToggleBluetooth.setText("Bluetooth'u Aç");
        }
    }

    private void scanForDevices() {
        // First check if Bluetooth is enabled
        if (!bluetoothAdapter.isEnabled()) {
            Toast.makeText(this, "Lütfen önce Bluetooth'u açın", Toast.LENGTH_SHORT).show();
            return;
        }
        
        // Check for required permissions
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            // Android 12+
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Tarama için gerekli izinler eksik", Toast.LENGTH_SHORT).show();
                requestBluetoothPermissions();
                return;
            }
        } else {
            // Android 11 and below
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Konum izni gerekli", Toast.LENGTH_SHORT).show();
                ActivityCompat.requestPermissions(this, 
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_BLUETOOTH_PERMISSIONS);
                return;
            }
        }

        // Clear previous results and update UI
        discoveredDevices.clear();
        signalStrengths.clear();
        if (deviceAdapter != null) {
            deviceAdapter.clearDevices();
        }
        
        Toast.makeText(this, "Cihazlar taranıyor...", Toast.LENGTH_SHORT).show();
        
        // Cancel any ongoing discovery
        if (bluetoothAdapter.isDiscovering()) {
            bluetoothAdapter.cancelDiscovery();
        }
        
        // Make sure the broadcast receiver is registered for device discovery and discovery finished
        try {
            IntentFilter filter = new IntentFilter();
            filter.addAction(BluetoothDevice.ACTION_FOUND);
            filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
            registerReceiver(receiver, filter);
        } catch (Exception e) {
            // Receiver might already be registered
        }
        
        // Start discovery with additional error handling
        try {
            boolean discoveryStarted = bluetoothAdapter.startDiscovery();
            if (!discoveryStarted) {
                Toast.makeText(this, "Tarama başlatılamadı", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Tarama hatası: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    // Create a BroadcastReceiver for ACTION_FOUND
    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Discovery has found a device
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (device != null) {
                    try {
                        // Get signal strength if available
                        short rssi = intent.getShortExtra(BluetoothDevice.EXTRA_RSSI, Short.MIN_VALUE);
                        Short signalStrength = (rssi != Short.MIN_VALUE) ? rssi : null;
                        
                        // Add device to adapter - the adapter handles checking for duplicates
                        runOnUiThread(() -> {
                            deviceAdapter.addDevice(device, signalStrength);
                            
                            // Debugging info
                            String deviceName = "Bilinmeyen Cihaz";
                            try {
                                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED) {
                                    deviceName = device.getName() != null ? device.getName() : "İsimsiz Cihaz";
                                }
                            } catch (Exception e) {
                                // Ignore exceptions when getting device name
                            }
                            System.out.println("Bluetooth Device Found: " + deviceName);
                        });
                    } catch (Exception e) {
                        System.out.println("Error with device: " + e.getMessage());
                        e.printStackTrace();
                    }
                }
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                // Discovery is finished
                runOnUiThread(() -> {
                    if (discoveredDevices.isEmpty()) {
                        Toast.makeText(context, "Hiçbir Bluetooth cihazı bulunamadı", Toast.LENGTH_SHORT).show();
                    } else {
                        String message = discoveredDevices.size() + " Bluetooth cihazı bulundu";
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                        statusText.setText(message);
                    }
                    
                    // Cancel discovery when finished to save battery
                    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_SCAN) == PackageManager.PERMISSION_GRANTED) {
                        bluetoothAdapter.cancelDiscovery();
                    }
                });
            }
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_BLUETOOTH_PERMISSIONS) {
            boolean allPermissionsGranted = true;
            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    allPermissionsGranted = false;
                    break;
                }
            }

            if (allPermissionsGranted) {
                updateBluetoothStatus();
                Toast.makeText(this, "İzinler verildi, şimdi Bluetooth kullanabilirsiniz", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Bluetooth kullanmak için izinler gerekli", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ENABLE_BT) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(this, "Bluetooth açıldı", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Bluetooth açılamadı", Toast.LENGTH_SHORT).show();
            }
            updateBluetoothStatus();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Unregister broadcast receivers
        try {
            unregisterReceiver(receiver);
        } catch (IllegalArgumentException e) {
            // Receiver was not registered
        }
        
        try {
            unregisterReceiver(bluetoothStateReceiver);
        } catch (IllegalArgumentException e) {
            // Receiver was not registered
        }
        
        // Make sure we're not doing discovery anymore
        if (bluetoothAdapter != null && ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN) == PackageManager.PERMISSION_GRANTED) {
            bluetoothAdapter.cancelDiscovery();
        }
    }
}