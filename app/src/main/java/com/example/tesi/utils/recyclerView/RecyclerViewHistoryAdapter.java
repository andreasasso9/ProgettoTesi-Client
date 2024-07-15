package com.example.tesi.utils.recyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tesi.client.R;

import java.util.List;
import java.util.Stack;

public class RecyclerViewHistoryAdapter extends RecyclerView.Adapter<HistoryHolder> {
	private final List<String> history;

	public RecyclerViewHistoryAdapter(List<String> history) {
		this.history = history;
	}

	@NonNull
	@Override
	public HistoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.history_item_layout, parent, false);
		return new HistoryHolder(v);
	}

	@Override
	public void onBindViewHolder(@NonNull HistoryHolder holder, int position) {
		String ricerca=history.get(holder.getAdapterPosition());

		holder.ricerca.setText(ricerca);

		holder.elimina.setOnClickListener(l->{
			history.remove(holder.getAdapterPosition());
			notifyItemRemoved(holder.getAdapterPosition());
		});
	}

	@Override
	public int getItemCount() {
		return history.size();
	}
}
