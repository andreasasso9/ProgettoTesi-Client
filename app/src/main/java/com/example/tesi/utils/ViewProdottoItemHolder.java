package com.example.tesi.utils;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tesi.client.R;

public class ViewProdottoItemHolder extends RecyclerView.ViewHolder {
	public ImageView fotoProdottoItem;
	public TextView nPreferitiProdottoItem, brandProdottoItem, prezzoProdottoItem;
	public ViewProdottoItemHolder(@NonNull View itemView) {
		super(itemView);
		fotoProdottoItem=itemView.findViewById(R.id.foto_prodotto_item);
		nPreferitiProdottoItem=itemView.findViewById(R.id.nPreferiti_prodotto_item);
		brandProdottoItem=itemView.findViewById(R.id.brand_prodotto_item);
		prezzoProdottoItem=itemView.findViewById(R.id.prezzo_prodotto_item);
	}
}
