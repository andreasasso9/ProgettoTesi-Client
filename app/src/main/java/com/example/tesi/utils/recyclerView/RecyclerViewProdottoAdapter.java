package com.example.tesi.utils.recyclerView;

import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tesi.client.R;
import com.example.tesi.control.FotoProdottoController;
import com.example.tesi.control.FotoProdottoControllerImpl;
import com.example.tesi.control.NotificheControllerImpl;
import com.example.tesi.control.ProdottoControllerImpl;
import com.example.tesi.control.UserControllerImpl;
import com.example.tesi.entity.FotoByteArray;
import com.example.tesi.entity.Notifica;
import com.example.tesi.entity.Prodotto;
import com.example.tesi.entity.User;

import java.util.List;

public class RecyclerViewProdottoAdapter extends RecyclerView.Adapter<ViewProdottoItemHolder> {
	private final List<Prodotto> prodotti;
	private final User currentUser;

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

		for (Prodotto x:currentUser.getProdottiPreferiti())
			if (x.equals(p)) {
				holder.iconaMiPiace.setImageResource(R.drawable.icons8_loading_heart_50);
				holder.switcMiPiace.setChecked(true);
			}

		holder.switcMiPiace.setOnClickListener(createMiPiaceClickListener(holder, p, foto));
	}

	@Override
	public int getItemCount() {
		return prodotti.size();
	}

	private View.OnClickListener createMiPiaceClickListener(ViewProdottoItemHolder holder, Prodotto p, FotoByteArray foto) {
		return l->{
			SwitchCompat switchCompat=(SwitchCompat) l;
			if (switchCompat.isChecked()) {
				p.setPreferiti(p.getMiPiace()+1);
				holder.iconaMiPiace.setImageResource(R.drawable.icons8_loading_heart_50);
				currentUser.getProdottiPreferiti().add(p);

				Notifica notifica=new Notifica(currentUser.getId(), p.getIdProprietario(), currentUser.getUsername()+" ha messo mi piace", foto.getValue());
				new NotificheControllerImpl().save(notifica);
			} else {
				p.setPreferiti(p.getMiPiace()-1);
				holder.iconaMiPiace.setImageResource(R.drawable.icons8_caricamento_cuore_50);
				currentUser.getProdottiPreferiti().remove(p);
			}

			holder.miPiaceProdottoItem.setText(p.getMiPiace()+"");

			new UserControllerImpl().update(currentUser);
			new ProdottoControllerImpl().update(p);
		};
	}
}
