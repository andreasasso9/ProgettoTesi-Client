package com.example.tesi.client.utils.recyclerView;

import android.graphics.BitmapFactory;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.example.tesi.client.R;
import com.example.tesi.entity.FotoByteArray;
import com.example.tesi.entity.Prodotto;
import com.example.tesi.entity.User;

import java.util.ArrayList;

public class ProdottiPreferitiAdapter extends ProdottoAdapter {
	public ProdottiPreferitiAdapter(User currentUser) {
		super(null, currentUser, true);
		prodotti=new ArrayList<>(currentUser.getProdottiPreferiti());
	}

	@Override
	public void onBindViewHolder(@NonNull ProdottoHolder holder, int position) {
		Prodotto p=prodotti.get(holder.getAdapterPosition());
		FragmentActivity activity= (FragmentActivity) holder.itemView.getContext();

		new Thread(()->{
			FotoByteArray foto=fotoController.findFirst(p);

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
