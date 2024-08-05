package com.example.tesi.client.utils.recyclerView;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tesi.client.R;

public class ChatHolder extends RecyclerView.ViewHolder {
	public TextView user;
	public TextView lastText;
	public ChatHolder(@NonNull View itemView) {
		super(itemView);
		user=itemView.findViewById(R.id.user);
		lastText=itemView.findViewById(R.id.lastText);
	}
}
