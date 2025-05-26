package com.example.veritaban;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements KisiAdapter.OnKisiClickListener {

    private RecyclerView recyclerView;
    private KisiAdapter adapter;
    private FloatingActionButton fabEkle;
    private TextView textKisiSayisi;
    
    private Veritabani veritabani;
    private List<Kisi> kisiler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Veritabanı başlatma
        veritabani = new Veritabani(this);
        kisiler = new ArrayList<>();

        // View'ları başlatma
        initViews();
        setupRecyclerView();
        loadKisiler();

        // FAB click listener
        fabEkle.setOnClickListener(v -> showKisiEkleDialog());
    }

    private void initViews() {
        recyclerView = findViewById(R.id.recyclerView);
        fabEkle = findViewById(R.id.fabEkle);
        textKisiSayisi = findViewById(R.id.textKisiSayisi);
    }

    private void setupRecyclerView() {
        adapter = new KisiAdapter(this, kisiler);
        adapter.setOnKisiClickListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private void loadKisiler() {
        kisiler = veritabani.tumKisileriGetir();
        adapter.updateKisiler(kisiler);
        updateKisiSayisi();
    }

    @SuppressLint("SetTextI18n")
    private void updateKisiSayisi() {
        int sayisi = veritabani.kisiSayisiGetir();
        textKisiSayisi.setText("Toplam " + sayisi + " kişi kayıtlı");
    }

    private void showKisiEkleDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_kisi_ekle, null);
        
        EditText editAd = dialogView.findViewById(R.id.editAd);
        EditText editSoyad = dialogView.findViewById(R.id.editSoyad);
        EditText editYas = dialogView.findViewById(R.id.editYas);
        EditText editSehir = dialogView.findViewById(R.id.editSehir);

        // Klavye özelliklerini devre dışı bırak
        disableKeyboardFeatures(editAd);
        disableKeyboardFeatures(editSoyad);
        disableKeyboardFeatures(editYas);
        disableKeyboardFeatures(editSehir);

        builder.setView(dialogView)
                .setTitle("Yeni Kişi Ekle")
                .setPositiveButton("Ekle", (dialog, which) -> {
                    String ad = editAd.getText().toString().trim();
                    String soyad = editSoyad.getText().toString().trim();
                    String yasStr = editYas.getText().toString().trim();
                    String sehir = editSehir.getText().toString().trim();

                    if (validateInput(ad, soyad, yasStr, sehir)) {
                        int yas = Integer.parseInt(yasStr);
                        Kisi yeniKisi = new Kisi(ad, soyad, yas, sehir);
                        
                        long id = veritabani.kisiEkle(yeniKisi);
                        if (id != -1) {
                            Toast.makeText(this, "Kişi başarıyla eklendi!", Toast.LENGTH_SHORT).show();
                            loadKisiler();
                        } else {
                            Toast.makeText(this, "Kişi eklenirken hata oluştu!", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("İptal", null)
                .create()
                .show();
    }

    private void showKisiGuncelleDialog(Kisi kisi) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_kisi_ekle, null);
        
        EditText editAd = dialogView.findViewById(R.id.editAd);
        EditText editSoyad = dialogView.findViewById(R.id.editSoyad);
        EditText editYas = dialogView.findViewById(R.id.editYas);
        EditText editSehir = dialogView.findViewById(R.id.editSehir);

        // Klavye özelliklerini devre dışı bırak
        disableKeyboardFeatures(editAd);
        disableKeyboardFeatures(editSoyad);
        disableKeyboardFeatures(editYas);
        disableKeyboardFeatures(editSehir);

        // Mevcut verileri doldur
        editAd.setText(kisi.getAd());
        editSoyad.setText(kisi.getSoyad());
        editYas.setText(String.valueOf(kisi.getYas()));
        editSehir.setText(kisi.getSehir());

        builder.setView(dialogView)
                .setTitle("Kişi Güncelle")
                .setPositiveButton("Güncelle", (dialog, which) -> {
                    String ad = editAd.getText().toString().trim();
                    String soyad = editSoyad.getText().toString().trim();
                    String yasStr = editYas.getText().toString().trim();
                    String sehir = editSehir.getText().toString().trim();

                    if (validateInput(ad, soyad, yasStr, sehir)) {
                        int yas = Integer.parseInt(yasStr);
                        kisi.setAd(ad);
                        kisi.setSoyad(soyad);
                        kisi.setYas(yas);
                        kisi.setSehir(sehir);
                        
                        int etkilenenSatir = veritabani.kisiGuncelle(kisi);
                        if (etkilenenSatir > 0) {
                            Toast.makeText(this, "Kişi başarıyla güncellendi!", Toast.LENGTH_SHORT).show();
                            loadKisiler();
                        } else {
                            Toast.makeText(this, "Kişi güncellenirken hata oluştu!", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("İptal", null)
                .create()
                .show();
    }

    private void showKisiSilDialog(Kisi kisi) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Kişi Sil")
                .setMessage(kisi.getTamAd() + " adlı kişiyi silmek istediğinizden emin misiniz?")
                .setPositiveButton("Sil", (dialog, which) -> {
                    veritabani.kisiSil(kisi);
                    Toast.makeText(this, "Kişi başarıyla silindi!", Toast.LENGTH_SHORT).show();
                    loadKisiler();
                })
                .setNegativeButton("İptal", null)
                .create()
                .show();
    }

    private boolean validateInput(String ad, String soyad, String yasStr, String sehir) {
        if (TextUtils.isEmpty(ad)) {
            Toast.makeText(this, "Ad alanı boş olamaz!", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(soyad)) {
            Toast.makeText(this, "Soyad alanı boş olamaz!", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(yasStr)) {
            Toast.makeText(this, "Yaş alanı boş olamaz!", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(sehir)) {
            Toast.makeText(this, "Şehir alanı boş olamaz!", Toast.LENGTH_SHORT).show();
            return false;
        }
        
        try {
            int yas = Integer.parseInt(yasStr);
            if (yas < 0 || yas > 150) {
                Toast.makeText(this, "Geçerli bir yaş giriniz (0-150)!", Toast.LENGTH_SHORT).show();
                return false;
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Yaş sayısal bir değer olmalıdır!", Toast.LENGTH_SHORT).show();
            return false;
        }
        
        return true;
    }

    @Override
    public void onEditClick(Kisi kisi) {
        showKisiGuncelleDialog(kisi);
    }

    @Override
    public void onDeleteClick(Kisi kisi) {
        showKisiSilDialog(kisi);
    }

    private void disableKeyboardFeatures(EditText editText) {
        // Mikrofon, emoji ve diğer ekstra butonları kapat
        editText.setPrivateImeOptions("nm,com.google.android.inputmethod.latin.noMicrophoneKey," +
                "com.google.android.inputmethod.latin.noSettingsKey," +
                "com.google.android.inputmethod.latin.noEmojiKey");
        
        // IME seçeneklerini ayarla
        editText.setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (veritabani != null) {
            veritabani.close();
        }
    }
}