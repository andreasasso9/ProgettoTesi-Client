package com.tesi.client.utils.recyclerView;

import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tesi.client.R;

public class HistoryHolder extends RecyclerView.ViewHolder {
	public TextView ricerca;
	public ImageButton elimina;
	public HistoryHolder(@NonNull View itemView) {
		super(itemView);
		ricerca=itemView.findViewById(R.id.ricerca);
		elimina=itemView.findViewById(R.id.elimina);
	}
}
