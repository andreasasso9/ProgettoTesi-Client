package com.tesi.client.utils.recyclerView;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.tesi.client.R;

public class ProdottoHolder extends RecyclerView.ViewHolder {
	public ImageView fotoProdottoItem;
	public TextView miPiaceProdottoItem, titoloProdottoItem, prezzoProdottoItem, userProdottoItem;
	public ImageView iconaMiPiace;
	public SwitchCompat switcMiPiace;
	public LinearLayout acquistato;
	public Toolbar menu;
	public ProdottoHolder(@NonNull View itemView) {
		super(itemView);
		fotoProdottoItem=itemView.findViewById(R.id.foto_prodotto_item);
		//fotoProdottoItem.setImageResource(R.drawable.icons8_nessuna_immagine_50);

		miPiaceProdottoItem =itemView.findViewById(R.id.miPiace_prodotto_item);
		titoloProdottoItem=itemView.findViewById(R.id.titolo_prodotto_item);
		prezzoProdottoItem=itemView.findViewById(R.id.prezzo_prodotto_item);
		switcMiPiace=itemView.findViewById(R.id.switchMiPiace);
		iconaMiPiace=itemView.findViewById(R.id.iconaMiPiace);
		acquistato=itemView.findViewById(R.id.acquistato);
		userProdottoItem=itemView.findViewById(R.id.user);
		menu=itemView.findViewById(R.id.menu);
	}
}
