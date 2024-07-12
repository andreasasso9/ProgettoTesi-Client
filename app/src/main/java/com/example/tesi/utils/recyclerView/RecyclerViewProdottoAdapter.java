package com.example.tesi.utils.recyclerView;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tesi.client.R;
import com.example.tesi.client.activities.VisualizzaProdottoActivity;
import com.example.tesi.control.FotoProdottoController;
import com.example.tesi.control.FotoProdottoControllerImpl;
import com.example.tesi.control.NotificheController;
import com.example.tesi.control.NotificheControllerImpl;
import com.example.tesi.control.ProdottoController;
import com.example.tesi.control.ProdottoControllerImpl;
import com.example.tesi.control.UserController;
import com.example.tesi.control.UserControllerImpl;
import com.example.tesi.entity.FotoByteArray;
import com.example.tesi.entity.Prodotto;
import com.example.tesi.entity.User;


import java.util.List;

public class RecyclerViewProdottoAdapter extends RecyclerView.Adapter<ViewProdottoItemHolder> {
	protected List<Prodotto> prodotti;
	protected final User currentUser;
	protected final NotificheController notificheController;
	protected final ProdottoController prodottoController;
	protected final FotoProdottoController fotoController;
	protected final UserController userController;

	public RecyclerViewProdottoAdapter(List<Prodotto> prodotti, User currentUser) {
		this.prodotti=prodotti;
		this.currentUser=currentUser;
		notificheController=new NotificheControllerImpl();
		prodottoController=new ProdottoControllerImpl();
		fotoController=new FotoProdottoControllerImpl();
		userController=new UserControllerImpl();
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

		holder.switcMiPiace.setOnCheckedChangeListener(createMiPiaceClickListener(holder, p));
		holder.itemView.setOnClickListener(createVisualizzaProdottoListener(p));
	}

	@Override
	public int getItemCount() {
		return prodotti.size();
	}

	private CompoundButton.OnCheckedChangeListener createMiPiaceClickListener(ViewProdottoItemHolder holder, Prodotto p) {
		return (button, isChecked)->{
			if (isChecked) {
				p.setMiPiace(p.getMiPiace()+1);
				holder.iconaMiPiace.setImageResource(R.drawable.icons8_loading_heart_50);
				currentUser.getProdottiPreferiti().add(p);

				userController.miPiace(currentUser.getId(), p.getId());
			} else {
				p.setMiPiace(p.getMiPiace()-1);
				holder.iconaMiPiace.setImageResource(R.drawable.icons8_caricamento_cuore_50);
				currentUser.getProdottiPreferiti().remove(p);

				String descrizione=String.format("%s ha messo mi piace al tuo articolo %s", currentUser.getUsername(), p.getTitolo());
				notificheController.delete(descrizione);
			}

			holder.miPiaceProdottoItem.setText(p.getMiPiace()+"");

			userController.update(currentUser);
			prodottoController.update(p);
		};
	}

	protected View.OnClickListener createVisualizzaProdottoListener(Prodotto p) {
		return l->{
			Intent i=new Intent(l.getContext(), VisualizzaProdottoActivity.class);

			i.putExtra("prodotto", p);

			User proprietario=userController.findById(p.getIdProprietario());
			i.putExtra("proprietario", proprietario);

			FotoByteArray[] foto= fotoController.findByProdotto(p).toArray(new FotoByteArray[0]);
			i.putExtra("foto", foto);

			l.getContext().startActivity(i);
		};
	}
}
