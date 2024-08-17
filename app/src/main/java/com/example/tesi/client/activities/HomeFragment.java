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
import com.example.tesi.entity.User;
import com.example.tesi.client.utils.recyclerView.ProdottoAdapter;
import com.example.tesi.client.utils.Session;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;


public class HomeFragment extends Fragment {

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View v = inflater.inflate(R.layout.home_layout, container, false);

		v.findViewById(R.id.indietro).setVisibility(View.GONE);

		RecyclerView list_prodotti = v.findViewById(R.id.list_prodotti);
		list_prodotti.setLayoutManager(new GridLayoutManager(requireContext(), 2));

		User currentUser=Session.getInstance(requireContext()).getCurrentUser();

		new Thread(()->{
			ProdottoController prodottoController=new ProdottoControllerImpl();
			List<Prodotto> prodotti=prodottoController.getAllNotOwnedBy(currentUser.getUsername());

			if (prodotti != null) {
				getActivity().runOnUiThread(()->{
					ProdottoAdapter adapter = new ProdottoAdapter(prodotti, currentUser, true);
					list_prodotti.setAdapter(adapter);
				});
			}
		}).start();

		return v;
	}

	@Override
	public void onResume() {
		super.onResume();
		BottomNavigationView navbar=requireActivity().findViewById(R.id.navbar);
		navbar.getMenu().getItem(0).setChecked(true).setIcon(R.drawable.casa_selected);
	}
}
