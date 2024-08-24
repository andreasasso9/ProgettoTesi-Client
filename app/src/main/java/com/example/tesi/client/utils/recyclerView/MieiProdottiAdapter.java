package com.example.tesi.client.utils.recyclerView;

import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.example.tesi.entity.FotoByteArray;
import com.example.tesi.entity.Prodotto;
import com.example.tesi.entity.User;

import java.util.List;

public class MieiProdottiAdapter extends ProdottoAdapter {
	public MieiProdottiAdapter(List<Prodotto> prodotti, User currentUser) {
		super(prodotti, currentUser, false);
	}

	@Override
	public void onBindViewHolder(@NonNull ProdottoHolder holder, int position) {
		Prodotto p=prodotti.get(position);

		FragmentActivity activity= (FragmentActivity) holder.itemView.getContext();
		new Thread(()->{
			FotoByteArray foto=fotoController.findFirst(p);

			activity.runOnUiThread(()->{
				if (foto!=null) {
					holder.fotoProdottoItem.setImageBitmap(BitmapFactory.decodeByteArray(foto.getValue(), 0, foto.getValue().length));
					holder.fotoProdottoItem.setScaleType(ImageView.ScaleType.FIT_CENTER);
				}
			});

		}).start();

		holder.titoloProdottoItem.setText(p.getTitolo());
		holder.prezzoProdottoItem.setText(p.getPrezzo()+"");
		LinearLayout miPiaceParent= (LinearLayout) holder.miPiaceProdottoItem.getParent();
		miPiaceParent.setVisibility(View.GONE);
		if (p.isBought())
			holder.acquistato.setVisibility(View.VISIBLE);

	}
}
