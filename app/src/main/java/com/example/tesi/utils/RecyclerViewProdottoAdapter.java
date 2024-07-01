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
import com.example.tesi.control.UserController;
import com.example.tesi.control.UserControllerImpl;
import com.example.tesi.entity.FotoByteArray;
import com.example.tesi.entity.Prodotto;
import com.example.tesi.entity.User;

import java.util.List;
import java.util.Objects;

public class RecyclerViewProdottoAdapter extends RecyclerView.Adapter<ViewProdottoItemHolder> {
	private List<Prodotto> prodotti;
	private User currentUser;

	public RecyclerViewProdottoAdapter(List<Prodotto> prodotti, User currentUser) {
		this.prodotti=prodotti;
		this.currentUser=currentUser;
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
			holder.miPiaceProdottoItem.setText(p.getMiPiace()+"");

		boolean addMiPiace=true;
		for (Prodotto x:currentUser.getProdottiPreferiti())
			if (x.getId().equals(p.getId())) {
				addMiPiace=false;
				holder.iconaPreferiti.setBackgroundResource(R.drawable.icons8_loading_heart_50);
			}

		createMiPiaceClickListener(holder, p, addMiPiace);

	}

	@Override
	public int getItemCount() {
		return prodotti.size();
	}

	//todo aggiungere il riferimento all'utente che ha messo mi piace e la rimozione del mi piace
	private void createMiPiaceClickListener(ViewProdottoItemHolder holder, Prodotto p, boolean addMiPiace) {
		holder.containerPreferiti.setOnClickListener(l->{
			if (addMiPiace) {
				p.setPreferiti(p.getMiPiace()+1);
				holder.iconaPreferiti.setBackgroundResource(R.drawable.icons8_loading_heart_50);
				currentUser.getProdottiPreferiti().add(p);
			}


			holder.miPiaceProdottoItem.setText(p.getMiPiace()+"");


			//todo combiare metodo mi piace con update
			UserController userController=new UserControllerImpl();
			userController.miPiace(currentUser.getId(), p.getId());

			ProdottoController prodottoController=new ProdottoControllerImpl();
			prodottoController.update(p);
		});
	}
}
