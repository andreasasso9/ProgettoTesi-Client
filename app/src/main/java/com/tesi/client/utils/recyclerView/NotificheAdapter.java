package com.tesi.client.utils.recyclerView;

import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tesi.client.R;
import com.tesi.client.control.NotificheControllerImpl;
import com.tesi.entity.Notifica;

import java.util.List;

public class NotificheAdapter extends RecyclerView.Adapter<NotificheHolder> {
	private final List<Notifica> notifiche;

	public NotificheAdapter(List<Notifica> notifiche) {
		this.notifiche = notifiche;
	}

	@NonNull
	@Override
	public NotificheHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.notifica_item_layout, parent, false);
		return new NotificheHolder(v);
	}

	@Override
	public void onBindViewHolder(@NonNull NotificheHolder holder, int position) {
		Notifica n=notifiche.get(holder.getAdapterPosition());

		holder.sender.setText(n.getSender());
		holder.descrizione.setText(n.getDescrizione());
		holder.foto.setImageBitmap(BitmapFactory.decodeByteArray(n.getFoto(), 0, n.getFoto().length));

		holder.elimina.setOnClickListener(l->{
			notifiche.remove(holder.getAdapterPosition());
			NotificheControllerImpl.getInstance().delete(holder.descrizione.getText()+"", null);

			TranslateAnimation animation=new TranslateAnimation(0,-holder.itemView.getWidth(),0,0);
			animation.setDuration(1000);
			animation.setFillAfter(true);

			animation.setAnimationListener(new Animation.AnimationListener() {
				@Override
				public void onAnimationStart(Animation animation) {}

				@Override
				public void onAnimationEnd(Animation animation) {
					notifyItemRemoved(holder.getAdapterPosition());
				}

				@Override
				public void onAnimationRepeat(Animation animation) {}
			});
			holder.container.startAnimation(animation);
			holder.elimina.startAnimation(animation);
		});
	}

	@Override
	public int getItemCount() {
		return notifiche.size();
	}

	public List<Notifica> getNotifiche() {
		return notifiche;
	}
}
