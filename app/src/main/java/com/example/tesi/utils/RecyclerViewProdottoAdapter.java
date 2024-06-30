package com.example.tesi.utils;

import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tesi.client.R;
import com.example.tesi.control.FotoProdottoController;
import com.example.tesi.control.FotoProdottoControllerImpl;
import com.example.tesi.control.ProdottoController;
import com.example.tesi.control.ProdottoControllerImpl;
import com.example.tesi.entity.FotoByteArray;
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

		FotoProdottoController fotoController=new FotoProdottoControllerImpl();

		FotoByteArray foto=fotoController.findFirst(p);

		if (foto!=null)
			holder.fotoProdottoItem.setImageBitmap(BitmapFactory.decodeByteArray(foto.getValue(), 0, foto.getValue().length));
		holder.brandProdottoItem.setText(p.getBrand().getNome());
		holder.prezzoProdottoItem.setText(p.getPrezzo()+"");
		if (p.getMiPiace()>0)
			holder.nPreferitiProdottoItem.setText(p.getMiPiace()+"");

		createPreferitiClickListener(holder, p);

	}

	@Override
	public int getItemCount() {
		return prodotti.size();
	}

	//todo aggiungere il riferimento all'utente che ha messo mi piace e la rimozione del mi piace
	private void createPreferitiClickListener(ViewProdottoItemHolder holder, Prodotto p) {
		holder.containerPreferiti.setOnClickListener(l->{
			p.setPreferiti(p.getMiPiace()+1);
			holder.nPreferitiProdottoItem.setText(p.getMiPiace()+"");
			holder.iconaPreferiti.setBackgroundResource(R.drawable.icons8_loading_heart_50);

			p.setTitolo("gioco");
			ProdottoController prodottoController=new ProdottoControllerImpl();
			prodottoController.miPiace(p);
		});
	}
}
