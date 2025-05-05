package com.example.blufi;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class WifiNetworkAdapter extends RecyclerView.Adapter<WifiNetworkAdapter.ViewHolder> {

    private final List<ScanResult> networks;
    private final Context context;

    public WifiNetworkAdapter(Context context, List<ScanResult> networks) {
        this.context = context;
        this.networks = networks;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_wifi_network, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ScanResult network = networks.get(position);
        
        // Set network name (SSID)
        String networkName = network.SSID;
        if (networkName == null || networkName.isEmpty()) {
            networkName = "Gizli Ağ";
        }
        holder.tvNetworkName.setText(networkName);
        
        // Set signal strength
        int level = WifiManager.calculateSignalLevel(network.level, 5); // Convert to 0-4 scale
        String signalStrength = "Sinyal: " + network.level + " dBm (" + getSignalStrengthDescription(level) + ")";
        holder.tvSignalStrength.setText(signalStrength);
        
        // Set security type
        String securityType = getSecurityType(network);
        holder.tvSecurityType.setText("Güvenlik: " + securityType);
    }

    @Override
    public int getItemCount() {
        return networks.size();
    }
    
    public void updateNetworks(List<ScanResult> newNetworks) {
        networks.clear();
        networks.addAll(newNetworks);
        notifyDataSetChanged();
    }

    // Helper method to determine WiFi security type
    private String getSecurityType(ScanResult scanResult) {
        String capabilities = scanResult.capabilities;
        if (capabilities.contains("WEP")) {
            return "WEP";
        } else if (capabilities.contains("WPA3")) {
            return "WPA3";
        } else if (capabilities.contains("WPA2")) {
            return "WPA2";
        } else if (capabilities.contains("WPA")) {
            return "WPA";
        } else if (capabilities.contains("ESS")) {
            return "Açık";
        } else {
            return "Bilinmeyen";
        }
    }
    
    // Helper method to convert signal level to text description
    private String getSignalStrengthDescription(int level) {
        switch (level) {
            case 4:
                return "Mükemmel";
            case 3:
                return "İyi";
            case 2:
                return "Orta";
            case 1:
                return "Zayıf";
            case 0:
                return "Çok Zayıf";
            default:
                return "Bilinmiyor";
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNetworkName, tvSignalStrength, tvSecurityType;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNetworkName = itemView.findViewById(R.id.tvNetworkName);
            tvSignalStrength = itemView.findViewById(R.id.tvSignalStrength);
            tvSecurityType = itemView.findViewById(R.id.tvSecurityType);
        }
    }
}