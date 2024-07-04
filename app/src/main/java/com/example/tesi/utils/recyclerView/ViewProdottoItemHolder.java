package com.example.tesi.utils.recyclerView;

import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tesi.client.R;

public class ViewProdottoItemHolder extends RecyclerView.ViewHolder {
	public ImageView fotoProdottoItem;
	public TextView miPiaceProdottoItem, brandProdottoItem, prezzoProdottoItem;
	public ImageView iconaMiPiace;
	public SwitchCompat switcMiPiace;
	public ViewProdottoItemHolder(@NonNull View itemView) {
		super(itemView);
		fotoProdottoItem=itemView.findViewById(R.id.foto_prodotto_item);
		miPiaceProdottoItem =itemView.findViewById(R.id.miPiace_prodotto_item);
		brandProdottoItem=itemView.findViewById(R.id.brand_prodotto_item);
		prezzoProdottoItem=itemView.findViewById(R.id.prezzo_prodotto_item);
		switcMiPiace=itemView.findViewById(R.id.switchMiPiace);
		iconaMiPiace=itemView.findViewById(R.id.iconaMiPiace);
	}
}
