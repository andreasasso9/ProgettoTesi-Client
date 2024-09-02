package com.tesi.client.utils.recyclerView;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.tesi.client.R;

public class ChatHolder extends RecyclerView.ViewHolder {
	public String id=null;
	public TextView user;
	public TextView lastText;
	public Toolbar toolbar;
	public LinearLayout container;
	public ChatHolder(@NonNull View itemView) {
		super(itemView);
		user=itemView.findViewById(R.id.user);
		lastText=itemView.findViewById(R.id.lastText);
		toolbar=itemView.findViewById(R.id.toolbar);
		container=itemView.findViewById(R.id.container);
	}
}
