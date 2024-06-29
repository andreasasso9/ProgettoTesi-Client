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
		int nPreferiti=p.getPreferiti();
		if (nPreferiti>0)
			holder.nPreferitiProdottoItem.setText(nPreferiti+"");

		createPreferitiClickListener(holder, nPreferiti);

	}

	@Override
	public int getItemCount() {
		return prodotti.size();
	}

	//todo completare il funzionamento del tasto 'Mi Piace' e aggiungere la chiamata al server
	private void createPreferitiClickListener(ViewProdottoItemHolder holder, int currentPreferiti) {
		holder.containerPreferiti.setOnClickListener(l->{
			int newPreferiti=currentPreferiti+1;
			holder.nPreferitiProdottoItem.setText(newPreferiti+"");
			holder.iconaPreferiti.setBackgroundResource(R.drawable.icons8_loading_heart_50);
		});
	}
}
