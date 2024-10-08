package com.tesi.client.utils.recyclerView;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tesi.client.R;

public class TextHolder extends RecyclerView.ViewHolder {
	public TextView text;
	public ImageView image;
	public TextHolder(@NonNull View itemView) {
		super(itemView);
		text=itemView.findViewById(R.id.text);
		image=itemView.findViewById(R.id.image);
	}
}
