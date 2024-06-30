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
import com.example.tesi.control.ProdottoController;
import com.example.tesi.control.ProdottoControllerImpl;
import com.example.tesi.entity.Prodotto;
import com.example.tesi.utils.RecyclerViewProdottoAdapter;
import com.example.tesi.utils.Session;

import java.util.List;


public class HomeFragment extends Fragment {

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View v = inflater.inflate(R.layout.home_layout, null);

		RecyclerView list_prodotti = v.findViewById(R.id.list_prodotti);
		list_prodotti.setLayoutManager(new GridLayoutManager(requireContext(), 2));

		ProdottoController prodottoController=new ProdottoControllerImpl();
		List<Prodotto> prodotti=prodottoController.getAllNotOwnedBy(Session.getInstance(requireContext()).getCurrentUser());


		if (prodotti != null) {
			RecyclerViewProdottoAdapter adapter = new RecyclerViewProdottoAdapter(prodotti);
			list_prodotti.setAdapter(adapter);
		}

		return v;
	}
}
