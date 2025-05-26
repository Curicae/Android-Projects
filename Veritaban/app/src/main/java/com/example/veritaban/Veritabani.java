package com.example.veritaban;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;

public class Veritabani extends SQLiteOpenHelper {
    
    // Veritabanı bilgileri
    private static final String VERITABANI_ADI = "KisilerDB";
    private static final int VERITABANI_VERSIYONU = 1;
    
    // Tablo ve sütun adları
    private static final String TABLO_ADI = "kisiler";
    private static final String SUTUN_ID = "id";
    private static final String SUTUN_AD = "ad";
    private static final String SUTUN_SOYAD = "soyad";
    private static final String SUTUN_YAS = "yas";
    private static final String SUTUN_SEHIR = "sehir";
    
    // Tablo oluşturma SQL komutu
    private static final String TABLO_OLUSTUR = 
        "CREATE TABLE " + TABLO_ADI + " (" +
        SUTUN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
        SUTUN_AD + " TEXT NOT NULL, " +
        SUTUN_SOYAD + " TEXT NOT NULL, " +
        SUTUN_YAS + " INTEGER NOT NULL, " +
        SUTUN_SEHIR + " TEXT NOT NULL)";

    public Veritabani(Context context) {
        super(context, VERITABANI_ADI, null, VERITABANI_VERSIYONU);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLO_OLUSTUR);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLO_ADI);
        onCreate(db);
    }

    // Kişi ekleme
    public long kisiEkle(Kisi kisi) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        
        values.put(SUTUN_AD, kisi.getAd());
        values.put(SUTUN_SOYAD, kisi.getSoyad());
        values.put(SUTUN_YAS, kisi.getYas());
        values.put(SUTUN_SEHIR, kisi.getSehir());
        
        long id = db.insert(TABLO_ADI, null, values);
        db.close();
        return id;
    }

    // Tüm kişileri getirme
    public List<Kisi> tumKisileriGetir() {
        List<Kisi> kisiler = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLO_ADI + " ORDER BY " + SUTUN_AD;
        
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        
        if (cursor.moveToFirst()) {
            do {
                Kisi kisi = new Kisi();
                kisi.setId(cursor.getInt(cursor.getColumnIndexOrThrow(SUTUN_ID)));
                kisi.setAd(cursor.getString(cursor.getColumnIndexOrThrow(SUTUN_AD)));
                kisi.setSoyad(cursor.getString(cursor.getColumnIndexOrThrow(SUTUN_SOYAD)));
                kisi.setYas(cursor.getInt(cursor.getColumnIndexOrThrow(SUTUN_YAS)));
                kisi.setSehir(cursor.getString(cursor.getColumnIndexOrThrow(SUTUN_SEHIR)));
                
                kisiler.add(kisi);
            } while (cursor.moveToNext());
        }
        
        cursor.close();
        db.close();
        return kisiler;
    }

    // Kişi güncelleme
    public int kisiGuncelle(Kisi kisi) {
        SQLiteDatabase db = this.getWritableDatabase();
        
        ContentValues values = new ContentValues();
        values.put(SUTUN_AD, kisi.getAd());
        values.put(SUTUN_SOYAD, kisi.getSoyad());
        values.put(SUTUN_YAS, kisi.getYas());
        values.put(SUTUN_SEHIR, kisi.getSehir());
        
        int etkilenenSatir = db.update(TABLO_ADI, values, 
                SUTUN_ID + " = ?", 
                new String[]{String.valueOf(kisi.getId())});
        
        db.close();
        return etkilenenSatir;
    }

    // Kişi silme
    public void kisiSil(Kisi kisi) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLO_ADI, SUTUN_ID + " = ?", 
                new String[]{String.valueOf(kisi.getId())});
        db.close();
    }

    // Toplam kişi sayısı
    public int kisiSayisiGetir() {
        String countQuery = "SELECT COUNT(*) FROM " + TABLO_ADI;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        
        int sayisi = 0;
        if (cursor.moveToFirst()) {
            sayisi = cursor.getInt(0);
        }
        cursor.close();
        db.close();
        
        return sayisi;
    }
}