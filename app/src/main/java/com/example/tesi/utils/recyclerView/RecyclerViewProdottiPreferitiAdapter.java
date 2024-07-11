package com.example.tesi.utils.recyclerView;

import android.graphics.BitmapFactory;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;

import com.example.tesi.client.R;
import com.example.tesi.entity.FotoByteArray;
import com.example.tesi.entity.Prodotto;
import com.example.tesi.entity.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class RecyclerViewProdottiPreferitiAdapter extends RecyclerViewProdottoAdapter {
	public RecyclerViewProdottiPreferitiAdapter(User currentUser) {
		super(null, currentUser);
		prodotti=new ArrayList<>(currentUser.getProdottiPreferiti());
	}

	@NonNull
	@Override
	public ViewProdottoItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		return super.onCreateViewHolder(parent, viewType);
	}

	@Override
	public void onBindViewHolder(@NonNull ViewProdottoItemHolder holder, int position) {
		Prodotto p=prodotti.get(holder.getAdapterPosition());

		FotoByteArray foto=fotoController.findFirst(p);

		if (foto!=null)
			holder.fotoProdottoItem.setImageBitmap(BitmapFactory.decodeByteArray(foto.getValue(), 0, foto.getValue().length));
		holder.brandProdottoItem.setText(p.getBrand().getNome());
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
	}
}
