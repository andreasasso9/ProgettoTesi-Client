package com.tesi.client.utils.recyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tesi.client.R;
import com.google.android.material.search.SearchBar;
import com.google.android.material.search.SearchView;
import com.tesi.client.utils.Session;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryHolder> {
	private final List<String> history;
	private final SearchView searchView;

	public HistoryAdapter(List<String> history, SearchView searchView) {
		this.history = history;
		this.searchView = searchView;
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

			holder.ricerca.setOnClickListener(l->searchView.setText(ricerca));

			Session session=Session.getInstance(holder.itemView.getContext());
			int textWidth = (int) (session.getScreenWidth() * 0.75);
			int eliminaWidth = (int) (session.getScreenWidth() * 0.25);

			holder.ricerca.getLayoutParams().width=textWidth;
			holder.elimina.getLayoutParams().width=eliminaWidth;

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
