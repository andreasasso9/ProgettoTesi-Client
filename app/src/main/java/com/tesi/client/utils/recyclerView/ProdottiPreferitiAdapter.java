package com.tesi.client.utils.recyclerView;

import android.graphics.BitmapFactory;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.tesi.client.R;
import com.tesi.entity.FotoByteArray;
import com.tesi.entity.Prodotto;
import com.tesi.entity.User;

import java.util.ArrayList;

public class ProdottiPreferitiAdapter extends ProdottoAdapter {
	public ProdottiPreferitiAdapter(User currentUser) {
		super(null, currentUser, true);
		prodotti=new ArrayList<>(prodottoController.findByLikedBy(currentUser.getUsername()));
	}

	@Override
	public void onBindViewHolder(@NonNull ProdottoHolder holder, int position) {
		Prodotto p=prodotti.get(holder.getAdapterPosition());
		FragmentActivity activity= (FragmentActivity) holder.itemView.getContext();

		new Thread(()->{
			FotoByteArray foto=fotoController.findFirst(p.getId());

			activity.runOnUiThread(()->{
				//holder.fotoProdottoItem.setImageResource(R.drawable.icons8_nessuna_immagine_50);
				if (foto!=null) {
					holder.fotoProdottoItem.setImageBitmap(BitmapFactory.decodeByteArray(foto.getValue(), 0, foto.getValue().length));
					holder.fotoProdottoItem.setScaleType(ImageView.ScaleType.FIT_CENTER);
				}
			});

		}).start();

		holder.titoloProdottoItem.setText(p.getTitolo());
		holder.prezzoProdottoItem.setText(p.getPrezzo()+"");
		holder.miPiaceProdottoItem.setText(p.getMiPiace()+"");

		holder.iconaMiPiace.setImageResource(R.drawable.icons8_loading_heart_50);
		holder.switcMiPiace.setChecked(true);

		holder.switcMiPiace.setOnCheckedChangeListener((button, isChecked) -> {
			if (!isChecked) {
				prodotti.remove(holder.getAdapterPosition());
				//currentUser.getProdottiPreferiti().remove(p);
				p.getLikedBy().remove(currentUser);
				notifyItemRemoved(holder.getAdapterPosition());

				String descrizione=String.format("%s ha messo mi piace al tuo articolo %s", currentUser.getUsername(), p.getTitolo());
				notificheController.delete(descrizione);

				prodottoController.update(p);
			}
		});
		holder.itemView.setOnClickListener(createVisualizzaProdottoListener(p));
	}


}
