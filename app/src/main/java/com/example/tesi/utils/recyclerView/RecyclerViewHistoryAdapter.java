package com.example.tesi.utils.recyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tesi.client.R;
import com.google.android.material.search.SearchBar;
import com.google.android.material.search.SearchView;

import java.util.List;

public class RecyclerViewHistoryAdapter extends RecyclerView.Adapter<HistoryHolder> {
	private final List<String> history;
	private SearchView searchView;
	private SearchBar searchBar;

	public RecyclerViewHistoryAdapter(List<String> history, SearchView searchView, SearchBar searchBar) {
		this.history = history;
		this.searchView = searchView;
		this.searchBar = searchBar;
	}

	@NonNull
	@Override
	public HistoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.history_item_layout, parent, false);
		return new HistoryHolder(v);
	}

	@Override
	public void onBindViewHolder(@NonNull HistoryHolder holder, int position) {
		if (holder.getAdapterPosition()>=0) {
				String ricerca = history.get(holder.getAdapterPosition());

			holder.ricerca.setText(ricerca);

			holder.ricerca.setOnClickListener(l->{
				searchView.setText(ricerca);
			});

			holder.elimina.setOnClickListener(l->{
				if (holder.getAdapterPosition()>=0) {
					history.remove(holder.getAdapterPosition());
					notifyItemRemoved(holder.getAdapterPosition());
				}
			});
		}
	}

	@Override
	public int getItemCount() {
		return history.size();
	}
}
