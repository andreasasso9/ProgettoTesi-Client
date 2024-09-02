package com.tesi.client.utils.recyclerView;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tesi.client.R;

public class NotificheHolder extends RecyclerView.ViewHolder {
	public TextView sender, descrizione, elimina;
	public ImageView foto;
	public LinearLayout container;
	public NotificheHolder(@NonNull View itemView) {
		super(itemView);
		sender=itemView.findViewById(R.id.sender);
		descrizione=itemView.findViewById(R.id.descrizione_notifica);
		foto=itemView.findViewById(R.id.foto_notifica);

		elimina=itemView.findViewById(R.id.eliminaNotifica);
		ViewGroup.LayoutParams params=new LinearLayout.LayoutParams(elimina.getLayoutParams());
		params.width=0;
		elimina.setLayoutParams(params);

		container=itemView.findViewById(R.id.containerItem);

		container.setOnClickListener(l->{
			elimina.setLayoutParams(params);
			descrizione.setMaxLines(2);
		});
	}
}
