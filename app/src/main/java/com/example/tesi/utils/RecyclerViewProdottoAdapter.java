package com.example.tesi.utils;

import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tesi.client.R;
import com.example.tesi.entity.Prodotto;

import java.util.List;

public class RecyclerViewProdottoAdapter extends RecyclerView.Adapter<ViewProdottoItemHolder> {
	private List<Prodotto> prodotti;

	public RecyclerViewProdottoAdapter(List<Prodotto> prodotti) {
		this.prodotti=prodotti;
	}

	@NonNull
	@Override
	public ViewProdottoItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View v=LayoutInflater.from(parent.getContext()).inflate(R.layout.prodotto_item_layout, parent, false);
		return new ViewProdottoItemHolder(v);
	}

	@Override
	public void onBindViewHolder(@NonNull ViewProdottoItemHolder holder, int position) {
		Prodotto p=prodotti.get(position);
		byte[] foto=p.getFoto().get(0).getValue();

		holder.fotoProdottoItem.setImageBitmap(BitmapFactory.decodeByteArray(foto, 0, foto.length));
		holder.nPreferitiProdottoItem.setText(p.getPreferiti()+"");
		holder.brandProdottoItem.setText(p.getBrand().getNome());
		holder.prezzoProdottoItem.setText(p.getPrezzo()+"");

	}

	@Override
	public int getItemCount() {
		return prodotti.size();
	}
}