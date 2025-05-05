package com.example.blufi;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class BluetoothDeviceAdapter extends RecyclerView.Adapter<BluetoothDeviceAdapter.ViewHolder> {

    private final List<BluetoothDevice> devices;
    private final Context context;
    private final List<Short> signalStrengths; // Optional: store signal strengths if available

    public BluetoothDeviceAdapter(Context context, List<BluetoothDevice> devices, List<Short> signalStrengths) {
        this.context = context;
        this.devices = devices;
        this.signalStrengths = signalStrengths;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_bluetooth_device, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BluetoothDevice device = devices.get(position);
        
        // Get device name and address safely
        String deviceName = "Bilinmeyen Cihaz";
        String deviceAddress = "Adres Yok";
        
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.BLUETOOTH_CONNECT) 
                        == PackageManager.PERMISSION_GRANTED) {
                    deviceName = device.getName() != null ? device.getName() : "İsimsiz Cihaz";
                    deviceAddress = device.getAddress();
                }
            } else {
                deviceName = device.getName() != null ? device.getName() : "İsimsiz Cihaz";
                deviceAddress = device.getAddress();
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        }
        
        // Set the data to views
        holder.tvDeviceName.setText(deviceName);
        holder.tvDeviceAddress.setText(deviceAddress);
        
        // Set signal strength if available
        if (signalStrengths != null && position < signalStrengths.size() && signalStrengths.get(position) != null) {
            holder.tvSignalStrength.setVisibility(View.VISIBLE);
            holder.tvSignalStrength.setText("Sinyal: " + signalStrengths.get(position) + " dBm");
        } else {
            holder.tvSignalStrength.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return devices.size();
    }

    public void addDevice(BluetoothDevice device, Short signalStrength) {
        // Check if device is already in the list
        if (!devices.contains(device)) {
            devices.add(device);
            if (signalStrength != null) {
                signalStrengths.add(signalStrength);
            } else {
                signalStrengths.add(null);
            }
            notifyItemInserted(devices.size() - 1);
        }
    }
    
    public void clearDevices() {
        devices.clear();
        signalStrengths.clear();
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvDeviceName, tvDeviceAddress, tvSignalStrength;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDeviceName = itemView.findViewById(R.id.tvDeviceName);
            tvDeviceAddress = itemView.findViewById(R.id.tvDeviceAddress);
            tvSignalStrength = itemView.findViewById(R.id.tvSignalStrength);
        }
    }
}