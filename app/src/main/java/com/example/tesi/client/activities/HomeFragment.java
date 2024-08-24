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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.tesi.client.R;
import com.example.tesi.client.control.ProdottoController;
import com.example.tesi.client.control.ProdottoControllerImpl;
import com.example.tesi.client.utils.recyclerView.refresh.Refresh;
import com.example.tesi.client.utils.recyclerView.refresh.UpdateMethod;
import com.example.tesi.entity.Prodotto;
import com.example.tesi.entity.User;
import com.example.tesi.client.utils.recyclerView.ProdottoAdapter;
import com.example.tesi.client.utils.Session;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class HomeFragment extends Fragment {
	private ProdottoController prodottoController;
	private List<Prodotto> prodotti;
	private User currentUser;
	private ProdottoAdapter adapter;
	private SwipeRefreshLayout refreshLayout;

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View v = inflater.inflate(R.layout.home_layout, container, false);

		v.findViewById(R.id.indietro).setVisibility(View.GONE);

		refreshLayout=v.findViewById(R.id.refreshLayout);

		RecyclerView list_prodotti = v.findViewById(R.id.list_prodotti);
		list_prodotti.setLayoutManager(new GridLayoutManager(requireContext(), 2));

		currentUser=Session.getInstance(requireContext()).getCurrentUser();

		new Thread(()->{
			refreshLayout.setRefreshing(true);
			prodottoController=new ProdottoControllerImpl();
			prodotti=prodottoController.getAllNotOwnedBy(currentUser.getUsername());

			requireActivity().runOnUiThread(()->{
				adapter = new ProdottoAdapter(prodotti, currentUser, true);
				list_prodotti.setAdapter(adapter);
			});
			refreshLayout.setRefreshing(false);
		}).start();

		UpdateMethod method=new UpdateMethod(currentUser.getUsername(), UpdateMethod.GET_ALL_NOT_OWNED_BY);
		refreshLayout.setOnRefreshListener(() -> Refresh.run(requireActivity(), prodotti, adapter, refreshLayout, method));

		return v;
	}

	@Override
	public void onResume() {
		super.onResume();
		BottomNavigationView navbar=requireActivity().findViewById(R.id.navbar);
		navbar.getMenu().getItem(0).setChecked(true).setIcon(R.drawable.casa_selected);
	}
}
