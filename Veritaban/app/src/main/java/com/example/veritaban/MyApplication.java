package com.example.veritaban;

import android.app.Application;
import android.content.res.Configuration;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        
        // Global klavye ayarları
        System.setProperty("debug.layout", "false");
        System.setProperty("hw.lcd.density", "160");
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }
}