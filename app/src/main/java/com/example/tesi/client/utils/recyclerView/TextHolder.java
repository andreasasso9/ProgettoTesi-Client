package com.example.tesi.client.utils.recyclerView;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tesi.client.R;

public class TextHolder extends RecyclerView.ViewHolder {
	public TextView text;
	public TextHolder(@NonNull View itemView) {
		super(itemView);
		text=itemView.findViewById(R.id.text);
	}
}
