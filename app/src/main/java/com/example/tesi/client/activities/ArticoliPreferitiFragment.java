package com.example.tesi.client.activities;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tesi.client.R;
import com.example.tesi.entity.Prodotto;
import com.example.tesi.entity.User;
import com.example.tesi.utils.Session;
import com.example.tesi.utils.recyclerView.RecyclerViewProdottiPreferitiAdapter;

import java.util.Set;

public class ArticoliPreferitiFragment extends Fragment {
	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View v=inflater.inflate(R.layout.articoli_preferiti_layout, null);

		RecyclerView recyclerView=v.findViewById(R.id.list_prodotti);
		recyclerView.setLayoutManager(new GridLayoutManager(requireContext(), 2));

		User currentuser=Session.getInstance(requireContext()).getCurrentUser();
		Set<Prodotto> prodottiPreferiti=currentuser.getProdottiPreferiti();

		if (prodottiPreferiti!=null && !prodottiPreferiti.isEmpty())
			recyclerView.setAdapter(new RecyclerViewProdottiPreferitiAdapter(currentuser));

		return v;
	}
}
