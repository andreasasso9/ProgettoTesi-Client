package com.example.tesi.utils.recyclerView;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tesi.client.R;

public class ViewNotificheItemHolder extends RecyclerView.ViewHolder {
	public TextView sender, descrizione;
	public ImageView foto;
	public ViewNotificheItemHolder(@NonNull View itemView) {
		super(itemView);
		sender=itemView.findViewById(R.id.sender);
		descrizione=itemView.findViewById(R.id.descrizione_notifica);
		foto=itemView.findViewById(R.id.foto_notifica);
	}
}
