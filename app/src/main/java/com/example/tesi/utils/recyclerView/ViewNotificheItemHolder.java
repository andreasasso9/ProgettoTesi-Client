package com.example.tesi.utils.recyclerView;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tesi.client.R;

public class ViewNotificheItemHolder extends RecyclerView.ViewHolder {
	public TextView sender, descrizione, elimina;
	public ImageView foto;
	public LinearLayout container/*, containerElimina/*, mainContainer*/;
	public ViewNotificheItemHolder(@NonNull View itemView) {
		super(itemView);
		sender=itemView.findViewById(R.id.sender);
		descrizione=itemView.findViewById(R.id.descrizione_notifica);
		foto=itemView.findViewById(R.id.foto_notifica);
		elimina=itemView.findViewById(R.id.eliminaNotifica);
		container=itemView.findViewById(R.id.containerItem);
		//mainContainer=itemView.findViewById(R.id.mainContainer);

		container.setOnClickListener(l->{
			ViewGroup.LayoutParams params=new LinearLayout.LayoutParams(elimina.getLayoutParams());
			params.width=0;
			elimina.setLayoutParams(params);
			descrizione.setMaxLines(2);
		});
	}
}
