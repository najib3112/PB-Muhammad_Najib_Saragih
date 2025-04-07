package com.example.noteshop.holder;

import android.view.View;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.noteshop.R;

public class MainViewHolder extends RecyclerView.ViewHolder {

    public TextView namaBarang;
    public TextView merkBarang;
    public TextView hargaBarang;
    public CardView view;

    public MainViewHolder(View itemView) {
        super(itemView);

        // Initialize views by finding them using itemView
        namaBarang = itemView.findViewById(R.id.nama_barang);
        merkBarang = itemView.findViewById(R.id.merk_barang);
        hargaBarang = itemView.findViewById(R.id.harga_barang);
        view = itemView.findViewById(R.id.cvMain);
    }
}
