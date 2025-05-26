package com.example.veritaban;

import androidx.annotation.NonNull;

public class Kisi {
    private int id;
    private String ad;
    private String soyad;
    private int yas;
    private String sehir;

    // Boş constructor
    public Kisi() {
    }

    // Tam constructor
    public Kisi(String ad, String soyad, int yas, String sehir) {
        this.ad = ad;
        this.soyad = soyad;
        this.yas = yas;
        this.sehir = sehir;
    }

    // ID ile constructor
    public Kisi(int id, String ad, String soyad, int yas, String sehir) {
        this.id = id;
        this.ad = ad;
        this.soyad = soyad;
        this.yas = yas;
        this.sehir = sehir;
    }

    // Getter ve Setter metodları
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAd() {
        return ad;
    }

    public void setAd(String ad) {
        this.ad = ad;
    }

    public String getSoyad() {
        return soyad;
    }

    public void setSoyad(String soyad) {
        this.soyad = soyad;
    }

    public int getYas() {
        return yas;
    }

    public void setYas(int yas) {
        this.yas = yas;
    }

    public String getSehir() {
        return sehir;
    }

    public void setSehir(String sehir) {
        this.sehir = sehir;
    }

    public String getTamAd() {
        return ad + " " + soyad;
    }

    @NonNull
    @Override
    public String toString() {
        return "Kisi{" +
                "id=" + id +
                ", ad='" + ad + '\'' +
                ", soyad='" + soyad + '\'' +
                ", yas=" + yas +
                ", sehir='" + sehir + '\'' +
                '}';
    }
}