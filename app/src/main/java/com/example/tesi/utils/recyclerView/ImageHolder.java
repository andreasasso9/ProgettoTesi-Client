package com.example.tesi.utils.recyclerView;

import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tesi.client.R;

public class ImageHolder extends RecyclerView.ViewHolder {
	public ImageView foto;
	public ImageHolder(@NonNull View itemView) {
		super(itemView);
		foto=itemView.findViewById(R.id.foto);
	}
}
