package com.example.veritaban;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class KisiAdapter extends RecyclerView.Adapter<KisiAdapter.KisiViewHolder> {

    private final Context context;
    private List<Kisi> kisiler;
    private OnKisiClickListener listener;

    public interface OnKisiClickListener {
        void onEditClick(Kisi kisi);
        void onDeleteClick(Kisi kisi);
    }

    public KisiAdapter(Context context, List<Kisi> kisiler) {
        this.context = context;
        this.kisiler = kisiler;
    }

    public void setOnKisiClickListener(OnKisiClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public KisiViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_kisi, parent, false);
        return new KisiViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull KisiViewHolder holder, int position) {
        Kisi kisi = kisiler.get(position);
        
        holder.textAdSoyad.setText(kisi.getTamAd());
        holder.textYas.setText(kisi.getYas() + " yaş");
        holder.textSehir.setText(kisi.getSehir());

        holder.btnEdit.setOnClickListener(v -> {
            if (listener != null) {
                listener.onEditClick(kisi);
            }
        });

        holder.btnDelete.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDeleteClick(kisi);
            }
        });
    }

    @Override
    public int getItemCount() {
        return kisiler.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateKisiler(List<Kisi> yeniKisiler) {
        this.kisiler = yeniKisiler;
        notifyDataSetChanged();
    }

    public static class KisiViewHolder extends RecyclerView.ViewHolder {
        TextView textAdSoyad;
        TextView textYas;
        TextView textSehir;
        ImageButton btnEdit;
        ImageButton btnDelete;

        public KisiViewHolder(@NonNull View itemView) {
            super(itemView);
            textAdSoyad = itemView.findViewById(R.id.textAdSoyad);
            textYas = itemView.findViewById(R.id.textYas);
            textSehir = itemView.findViewById(R.id.textSehir);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}