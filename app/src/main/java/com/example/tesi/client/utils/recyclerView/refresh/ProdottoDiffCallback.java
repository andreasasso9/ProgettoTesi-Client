package com.example.tesi.client.utils.recyclerView.refresh;

import androidx.recyclerview.widget.DiffUtil;

import com.example.tesi.entity.Prodotto;

import java.util.List;
import java.util.Objects;

public class ProdottoDiffCallback extends DiffUtil.Callback {
	private final List<Prodotto> oldList, newList;

	public ProdottoDiffCallback(List<Prodotto> oldList, List<Prodotto> newList) {
		this.oldList = oldList;
		this.newList = newList;
	}
	@Override
	public int getOldListSize() {
		return oldList.size();
	}

	@Override
	public int getNewListSize() {
		return newList.size();
	}

	@Override
	public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
		return Objects.equals(oldList.get(oldItemPosition).getId(), newList.get(newItemPosition).getId());
	}

	@Override
	public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
		return oldList.get(oldItemPosition).equals(newList.get(newItemPosition));
	}
}
