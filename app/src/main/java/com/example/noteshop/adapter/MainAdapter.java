package com.example.noteshop.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.example.noteshop.R;
import com.example.noteshop.holder.MainViewHolder;
import com.example.noteshop.model.ModelBarang;

import java.util.ArrayList;

@SuppressLint("NewApi")
public class MainAdapter extends RecyclerView.Adapter<MainViewHolder> implements View.OnCapturedPointerListener {

    private Context context;
    private ArrayList<ModelBarang> daftarBarang;
    private FirebaseDataListener listener;

    public MainAdapter(Context context, ArrayList<ModelBarang> daftarBarang) {
        this.context = context;
        this.daftarBarang = daftarBarang;
        this.listener = (FirebaseDataListener) context;
    }

    @Override
    public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_barang, parent, false);
        return new MainViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MainViewHolder holder, int position) {
        holder.namaBarang.setText("Nama: " + daftarBarang.get(position).getNama());
        holder.merkBarang.setText("Merk: " + daftarBarang.get(position).getMerk());
        holder.hargaBarang.setText("Harga: " + daftarBarang.get(position).getHarga());

        holder.view.setOnClickListener(v -> listener.onDataClick(daftarBarang.get(position), position));
    }

    @Override
    public int getItemCount() {
        return daftarBarang != null ? daftarBarang.size() : 0;
    }

    @Override
    public boolean onCapturedPointer(View view, MotionEvent event) {
        return false;
    }

    public interface FirebaseDataListener {
        void onDataClick(ModelBarang barang, int position);
    }
}
