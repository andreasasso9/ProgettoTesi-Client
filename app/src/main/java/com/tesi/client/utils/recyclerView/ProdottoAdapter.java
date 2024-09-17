package com.tesi.client.utils.recyclerView;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.tesi.client.R;
import com.tesi.client.activities.VisualizzaProdottoActivity;
import com.tesi.client.control.FotoProdottoController;
import com.tesi.client.control.FotoProdottoControllerImpl;
import com.tesi.client.control.NotificheController;
import com.tesi.client.control.NotificheControllerImpl;
import com.tesi.client.control.ProdottoController;
import com.tesi.client.control.ProdottoControllerImpl;
import com.tesi.client.control.UserController;
import com.tesi.client.control.UserControllerImpl;
import com.tesi.client.utils.Session;
import com.tesi.entity.FotoByteArray;
import com.tesi.entity.likes.Id;
import com.tesi.entity.Prodotto;
import com.tesi.entity.User;

import java.util.List;
import java.util.Locale;

public class ProdottoAdapter extends RecyclerView.Adapter<ProdottoHolder> {
	protected List<Prodotto> prodotti;
	protected final User currentUser;
	protected final NotificheController notificheController;
	protected final ProdottoController prodottoController;
	protected final FotoProdottoController fotoController;
	protected final UserController userController;
	protected boolean miPiaceEnabled;
	private int page;

	public ProdottoAdapter(List<Prodotto> prodotti, User currentUser, boolean miPiaceEnabled) {
		this.prodotti=prodotti;
		this.currentUser=currentUser;
		notificheController= NotificheControllerImpl.getInstance();
		prodottoController= ProdottoControllerImpl.getInstance();
		fotoController= FotoProdottoControllerImpl.getInstance();
		userController= UserControllerImpl.getInstance();
		this.miPiaceEnabled=miPiaceEnabled;
		this.page=page;
	}

	@NonNull
	@Override
	public ProdottoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View v=LayoutInflater.from(parent.getContext()).inflate(R.layout.prodotto_item_layout, parent, false);
		return new ProdottoHolder(v);
	}

	@Override
	public void onBindViewHolder(@NonNull ProdottoHolder holder, int position) {
		Prodotto p=prodotti.get(holder.getAdapterPosition());

		holder.miPiaceProdottoItem.setText("");
		holder.userProdottoItem.setText("");
		holder.fotoProdottoItem.setImageBitmap(null);
		holder.prezzoProdottoItem.setText("");
		holder.titoloProdottoItem.setText("");

		holder.iconaMiPiace.setImageResource(R.drawable.icons8_caricamento_cuore_50);
		holder.switcMiPiace.setOnCheckedChangeListener(null);
		holder.switcMiPiace.setChecked(false);

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
		if (p.getLikes()>0)
			holder.miPiaceProdottoItem.setText(String.valueOf(p.getLikes()));

		if (Session.getInstance(holder.itemView.getContext()).getLikedBy().contains(p)) {
			holder.iconaMiPiace.setImageResource(R.drawable.icons8_loading_heart_50);
			holder.switcMiPiace.setChecked(true);
		}

		if (miPiaceEnabled)
			holder.switcMiPiace.setOnCheckedChangeListener(createMiPiaceClickListener(holder, p));
		holder.itemView.setOnClickListener(createVisualizzaProdottoListener(p));
	}

	@Override
	public int getItemCount() {
		if (prodotti!=null)
			return prodotti.size();
		return 0;
	}

	private CompoundButton.OnCheckedChangeListener createMiPiaceClickListener(ProdottoHolder holder, Prodotto p) {
		return (button, isChecked)->{
			if (isChecked) {
				holder.iconaMiPiace.setImageResource(R.drawable.icons8_loading_heart_50);
				p.setLikes(p.getLikes()+1);

				notificheController.miPiace(currentUser.getUsername(), p.getId());
			} else {
				holder.iconaMiPiace.setImageResource(R.drawable.icons8_caricamento_cuore_50);
				p.setLikes(p.getLikes()-1);

				Id likeId=new Id(p.getId(), currentUser.getUsername());
				String descrizione=String.format("%s ha messo mi piace al tuo articolo %s", currentUser.getUsername(), p.getTitolo());
				notificheController.delete(descrizione, likeId);
			}


			if (p.getLikes()>0)
				holder.miPiaceProdottoItem.setText(String.valueOf(p.getLikes()));
			else
				holder.miPiaceProdottoItem.setText("");
		};
	}

	protected View.OnClickListener createVisualizzaProdottoListener(Prodotto p) {
		return l->{
			Intent i=new Intent(l.getContext(), VisualizzaProdottoActivity.class);

			i.putExtra("prodotto", p);

			l.getContext().startActivity(i);
		};
	}

	public List<Prodotto> getProdotti() {
		return prodotti;
	}

	public void setProdotti(List<Prodotto> prodotti) {
		this.prodotti = prodotti;
	}
}
