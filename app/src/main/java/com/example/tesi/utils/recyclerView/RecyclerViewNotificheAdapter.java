package com.example.tesi.utils.recyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tesi.client.R;
import com.example.tesi.entity.Notifica;

import java.util.List;

public class RecyclerViewNotificheAdapter extends RecyclerView.Adapter<ViewNotificheItemHolder> {
	private List<Notifica> notifiche;

	public RecyclerViewNotificheAdapter(List<Notifica> notifiche) {
		this.notifiche = notifiche;
	}

	@NonNull
	@Override
	public ViewNotificheItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.notifica_item_layout, parent, false);
		return new ViewNotificheItemHolder(v);
	}

	@Override
	public void onBindViewHolder(@NonNull ViewNotificheItemHolder holder, int position) {
		Notifica n=notifiche.get(position);


	}

	@Override
	public int getItemCount() {
		return notifiche.size();
	}
}
