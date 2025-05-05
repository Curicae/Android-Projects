package com.example.blufi;

import android.Manifest;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.common.util.concurrent.ListenableFuture;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class CameraActivity extends AppCompatActivity {
    private static final int REQUEST_CAMERA_PERMISSION = 300;
    private static final String[] CAMERA_PERMISSIONS = {
            Manifest.permission.CAMERA
    };

    private PreviewView viewFinder;
    private Button btnCameraStart, btnCapture, btnCameraStop;
    private TextView tvCameraStatus;

    private Camera camera;
    private ImageCapture imageCapture;
    private ProcessCameraProvider cameraProvider;
    private final Executor executor = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        // Initialize UI components
        viewFinder = findViewById(R.id.viewFinder);
        btnCameraStart = findViewById(R.id.btnCameraStart);
        btnCapture = findViewById(R.id.btnCapture);
        btnCameraStop = findViewById(R.id.btnCameraStop);
        tvCameraStatus = findViewById(R.id.tvCameraStatus);

        // Setup click listeners
        setupClickListeners();
        
        // Initially disable capture and stop buttons
        btnCapture.setEnabled(false);
        btnCameraStop.setEnabled(false);
    }

    private void setupClickListeners() {
        btnCameraStart.setOnClickListener(v -> {
            if (hasCameraPermission()) {
                startCamera();
            } else {
                requestCameraPermission();
            }
        });

        btnCapture.setOnClickListener(v -> {
            if (imageCapture != null) {
                capturePhoto();
            }
        });

        btnCameraStop.setOnClickListener(v -> {
            stopCamera();
        });
        
        // Back to Main Menu button click listener
        Button btnBackToMain = findViewById(R.id.btnBackToMain);
        btnBackToMain.setOnClickListener(v -> {
            // Make sure to stop camera before returning to main menu
            stopCamera();
            finish(); // Close this activity and return to the previous one (MainActivity)
        });
    }

    private boolean hasCameraPermission() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == 
                PackageManager.PERMISSION_GRANTED;
    }

    private void requestCameraPermission() {
        ActivityCompat.requestPermissions(this, CAMERA_PERMISSIONS, REQUEST_CAMERA_PERMISSION);
    }

    private void startCamera() {
        ListenableFuture<ProcessCameraProvider> cameraProviderFuture = 
                ProcessCameraProvider.getInstance(this);

        cameraProviderFuture.addListener(() -> {
            try {
                // Used to bind the lifecycle of cameras to the lifecycle owner
                cameraProvider = cameraProviderFuture.get();
                bindCameraUseCases();
            } catch (ExecutionException | InterruptedException e) {
                Toast.makeText(this, 
                        "Kamera başlatılamadı: " + e.getMessage(), 
                        Toast.LENGTH_SHORT).show();
            }
        }, ContextCompat.getMainExecutor(this));
    }

    private void bindCameraUseCases() {
        // Get screen metrics used to setup camera
        Preview preview = new Preview.Builder().build();
        preview.setSurfaceProvider(viewFinder.getSurfaceProvider());

        // Select back camera
        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build();

        // Set up the image capture use case
        imageCapture = new ImageCapture.Builder()
                .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                .build();

        // Must unbind before binding
        if (cameraProvider != null) {
            cameraProvider.unbindAll();
            
            try {
                // Bind use cases to camera
                camera = cameraProvider.bindToLifecycle(
                        this, cameraSelector, preview, imageCapture);
                
                // Update UI state
                updateCameraUI(true);
                
            } catch (Exception e) {
                Toast.makeText(this, 
                        "Kamera kullanım durumu bağlanamadı: " + e.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void capturePhoto() {
        if (imageCapture == null) {
            return;
        }

        // Create timestamped file name for the image
        String timestamp = new SimpleDateFormat("yyyyMMdd-HHmmss", Locale.getDefault()).format(System.currentTimeMillis());
        String fileName = "IMG_" + timestamp + ".jpg";

        // Create output options object which contains file + metadata
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, fileName);
        contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, "Pictures/Blufi");
        }

        // Create the output options object which contains file + metadata
        ImageCapture.OutputFileOptions outputOptions = new ImageCapture.OutputFileOptions.Builder(
                getContentResolver(),
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                contentValues).build();

        // Set up image capture listener which is triggered after the photo has been taken
        imageCapture.takePicture(outputOptions, executor, new ImageCapture.OnImageSavedCallback() {
            @Override
            public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {
                runOnUiThread(() -> {
                    Toast.makeText(CameraActivity.this, 
                            "Fotoğraf kaydedildi: " + outputFileResults.getSavedUri(),
                            Toast.LENGTH_SHORT).show();
                });
            }

            @Override
            public void onError(@NonNull ImageCaptureException exception) {
                runOnUiThread(() -> {
                    Toast.makeText(CameraActivity.this, 
                            "Fotoğraf çekimi başarısız: " + exception.getMessage(),
                            Toast.LENGTH_SHORT).show();
                });
            }
        });
    }

    private void stopCamera() {
        if (cameraProvider != null) {
            cameraProvider.unbindAll();
            updateCameraUI(false);
            Toast.makeText(this, "Kamera durduruldu", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateCameraUI(boolean cameraActive) {
        btnCameraStart.setEnabled(!cameraActive);
        btnCapture.setEnabled(cameraActive);
        btnCameraStop.setEnabled(cameraActive);
        
        if (cameraActive) {
            tvCameraStatus.setText("Kamera Açık");
        } else {
            tvCameraStatus.setText("Kamera Kapalı");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startCamera();
            } else {
                Toast.makeText(this, "Kamera kullanımı için izin gerekli", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (cameraProvider != null) {
            cameraProvider.unbindAll();
        }
    }
}