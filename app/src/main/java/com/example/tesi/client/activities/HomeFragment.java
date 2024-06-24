package com.example.tesi.client.activities;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tesi.client.R;
import com.example.tesi.utils.RecyclerViewProdottoAdapter;

import java.util.ArrayList;

public class HomeFragment extends Fragment {
	private RecyclerView list_prodotti;
	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View v = inflater.inflate(R.layout.home_layout, null);

		list_prodotti=v.findViewById(R.id.list_prodotti);
		list_prodotti.setLayoutManager(new GridLayoutManager(requireContext(), 2));


		RecyclerViewProdottoAdapter adapter=new RecyclerViewProdottoAdapter(new ArrayList<>());
		list_prodotti.setAdapter(adapter);

		return v;
	}
}
