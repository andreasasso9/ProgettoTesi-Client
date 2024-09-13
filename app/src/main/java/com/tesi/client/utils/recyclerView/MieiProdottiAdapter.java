package com.tesi.client.utils.recyclerView;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.tesi.client.activities.FormProdottoActivity;
import com.tesi.entity.FotoByteArray;
import com.tesi.entity.Prodotto;
import com.tesi.entity.User;

import java.util.List;
import java.util.Locale;

public class MieiProdottiAdapter extends ProdottoAdapter {
	public MieiProdottiAdapter(List<Prodotto> prodotti, User currentUser) {
		super(prodotti, currentUser, false);
	}

	@Override
	public void onBindViewHolder(@NonNull ProdottoHolder holder, int position) {
		Prodotto p=prodotti.get(holder.getAdapterPosition());

		holder.acquistato.setVisibility(View.GONE);

		if (!p.isBought())
			holder.menu.setVisibility(View.VISIBLE);

		FragmentActivity activity= (FragmentActivity) holder.itemView.getContext();
		new Thread(()->{
			FotoByteArray foto=fotoController.findFirst(p.getId());

			activity.runOnUiThread(()->{
				if (foto!=null) {
					holder.fotoProdottoItem.setImageBitmap(BitmapFactory.decodeByteArray(foto.getValue(), 0, foto.getValue().length));
					holder.fotoProdottoItem.setScaleType(ImageView.ScaleType.FIT_CENTER);
				}
			});

		}).start();

		holder.userProdottoItem.setText(p.getProprietario());
		holder.titoloProdottoItem.setText(p.getTitolo());
		holder.prezzoProdottoItem.setText(String.format(Locale.ITALIAN, "€%.2f", p.getPrezzo()));
		holder.miPiaceProdottoItem.setOnClickListener(null);
		if (p.isBought())
			holder.acquistato.setVisibility(View.VISIBLE);

		holder.menu.setOnMenuItemClickListener(item -> {
			String title=item.getTitle().toString();

			if (title.equalsIgnoreCase("modifica")) {
				Intent i=new Intent(holder.itemView.getContext(), FormProdottoActivity.class);
				i.putExtra("prodotto", p);
				holder.itemView.getContext().startActivity(i);
				return true;
			} else if (title.equalsIgnoreCase("elimina")) {
				prodottoController.deleteById(p.getId());
				notifyItemRemoved(holder.getAdapterPosition());
				prodotti.remove(p);
				return true;
			}

			return false;
		});
	}
}
