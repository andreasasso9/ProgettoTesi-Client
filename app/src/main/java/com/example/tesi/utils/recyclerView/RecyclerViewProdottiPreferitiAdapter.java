package com.example.tesi.utils.recyclerView;

import android.graphics.BitmapFactory;

import androidx.annotation.NonNull;

import com.example.tesi.client.R;
import com.example.tesi.entity.FotoByteArray;
import com.example.tesi.entity.Prodotto;
import com.example.tesi.entity.User;

import java.util.ArrayList;

public class RecyclerViewProdottiPreferitiAdapter extends RecyclerViewProdottoAdapter {
	public RecyclerViewProdottiPreferitiAdapter(User currentUser) {
		super(null, currentUser);
		prodotti=new ArrayList<>(currentUser.getProdottiPreferiti());
	}

	@Override
	public void onBindViewHolder(@NonNull ViewProdottoItemHolder holder, int position) {
		Prodotto p=prodotti.get(holder.getAdapterPosition());

		FotoByteArray foto=fotoController.findFirst(p);

		if (foto!=null)
			holder.fotoProdottoItem.setImageBitmap(BitmapFactory.decodeByteArray(foto.getValue(), 0, foto.getValue().length));
		holder.titoloProdottoItem.setText(p.getTitolo());
		holder.prezzoProdottoItem.setText(p.getPrezzo()+"");
		holder.miPiaceProdottoItem.setText(p.getMiPiace()+"");

		holder.iconaMiPiace.setImageResource(R.drawable.icons8_loading_heart_50);
		holder.switcMiPiace.setChecked(true);

		holder.switcMiPiace.setOnCheckedChangeListener((button, isChecked) -> {
			if (!isChecked) {
				prodotti.remove(holder.getAdapterPosition());
				currentUser.getProdottiPreferiti().remove(p);
				p.setMiPiace(p.getMiPiace()-1);
				notifyItemRemoved(holder.getAdapterPosition());

				String descrizione=String.format("%s ha messo mi piace al tuo articolo %s", currentUser.getUsername(), p.getTitolo());
				notificheController.delete(descrizione);

				userController.update(currentUser);
				prodottoController.update(p);
			}
		});
		holder.itemView.setOnClickListener(createVisualizzaProdottoListener(p));
	}
}
