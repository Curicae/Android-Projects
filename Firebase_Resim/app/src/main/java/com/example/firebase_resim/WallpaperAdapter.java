package com.example.firebase_resim;

import android.app.WallpaperManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.IOException;
import java.util.List;

public class WallpaperAdapter extends RecyclerView.Adapter<WallpaperAdapter.WallpaperViewHolder> {

    private List<Wallpaper> wallpaperList;
    private Context context;

    public WallpaperAdapter(Context context, List<Wallpaper> wallpaperList) {
        this.context = context;
        this.wallpaperList = wallpaperList;
    }

    @NonNull
    @Override
    public WallpaperViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.wallpaper_item, parent, false);
        return new WallpaperViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WallpaperViewHolder holder, int position) {
        Wallpaper wallpaper = wallpaperList.get(position);
        
        holder.wallpaperName.setText(wallpaper.getName());
        
        // Load image with Picasso
        Picasso.get()
                .load(wallpaper.getImageUrl())
                .placeholder(R.drawable.ic_launcher_foreground)
                .error(R.drawable.ic_launcher_foreground)
                .into(holder.wallpaperImage);

        // Set click listener to set wallpaper
        holder.itemView.setOnClickListener(v -> {
            setWallpaper(wallpaper.getImageUrl());
        });
    }

    @Override
    public int getItemCount() {
        return wallpaperList.size();
    }

    private void setWallpaper(String imageUrl) {
        WallpaperManager wallpaperManager = WallpaperManager.getInstance(context);
        
        Picasso.get().load(imageUrl).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                try {
                    wallpaperManager.setBitmap(bitmap);
                    Toast.makeText(context, "Duvar kağıdı başarıyla değiştirildi!", Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(context, "Duvar kağıdı değiştirilemedi!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                Toast.makeText(context, "Resim yüklenemedi!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
                Toast.makeText(context, "Duvar kağıdı ayarlanıyor...", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static class WallpaperViewHolder extends RecyclerView.ViewHolder {
        ImageView wallpaperImage;
        TextView wallpaperName;

        public WallpaperViewHolder(@NonNull View itemView) {
            super(itemView);
            wallpaperImage = itemView.findViewById(R.id.wallpaperImage);
            wallpaperName = itemView.findViewById(R.id.wallpaperName);
        }
    }
}