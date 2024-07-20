package com.example.tesi.utils.recyclerView;

import android.graphics.BitmapFactory;

import androidx.annotation.NonNull;

import com.example.tesi.entity.FotoByteArray;
import com.example.tesi.entity.Prodotto;
import com.example.tesi.entity.User;

import java.util.List;

public class RecyclerViewMieiProdottiAdapter extends RecyclerViewProdottoAdapter{
	public RecyclerViewMieiProdottiAdapter(List<Prodotto> prodotti, User currentUser) {
		super(prodotti, currentUser);
	}

	@Override
	public void onBindViewHolder(@NonNull ViewProdottoItemHolder holder, int position) {
		Prodotto p=prodotti.get(position);

		FotoByteArray foto=fotoController.findFirst(p);

		if (foto!=null)
			holder.fotoProdottoItem.setImageBitmap(BitmapFactory.decodeByteArray(foto.getValue(), 0, foto.getValue().length));
		holder.titoloProdottoItem.setText(p.getTitolo());
		holder.prezzoProdottoItem.setText(p.getPrezzo()+"");
		holder.miPiaceProdottoItem.setText(p.getMiPiace()+"");
		//holder.miPiaceProdottoItem.setOnClickListener(null);
	}
}
