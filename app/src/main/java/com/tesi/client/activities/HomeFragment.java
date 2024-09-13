package com.tesi.client.activities;

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

import com.tesi.client.R;
import com.tesi.client.control.ProdottoController;
import com.tesi.client.control.ProdottoControllerImpl;
import com.tesi.client.utils.recyclerView.refresh.Refresh;
import com.tesi.client.utils.recyclerView.refresh.UpdateMethod;
import com.tesi.entity.Prodotto;
import com.tesi.entity.User;
import com.tesi.client.utils.recyclerView.ProdottoAdapter;
import com.tesi.client.utils.Session;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

public class HomeFragment extends Fragment {
	private ProdottoController prodottoController;
	private List<Prodotto> prodotti;
	private User currentUser;
	private ProdottoAdapter adapter;
	private SwipeRefreshLayout refreshLayout;
	public static Integer page=0;

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View v = inflater.inflate(R.layout.home_layout, container, false);

		v.findViewById(R.id.indietro).setVisibility(View.GONE);

		refreshLayout=v.findViewById(R.id.refreshLayout);

		RecyclerView list_prodotti = v.findViewById(R.id.list_prodotti);
		GridLayoutManager gridLayoutManager=new GridLayoutManager(requireContext(), 2);
		list_prodotti.setLayoutManager(gridLayoutManager);
		list_prodotti.addOnScrollListener(new RecyclerView.OnScrollListener() {
			@Override
			public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
				super.onScrolled(recyclerView, dx, dy);
				if (dy>0) {
					int lastPosition=gridLayoutManager.findLastVisibleItemPosition();
					int itemCount= adapter.getItemCount();
					if (itemCount <= lastPosition+1 && itemCount >= lastPosition-1) {
						refreshLayout.setRefreshing(true);
						new Thread(()->{
							List<Prodotto> l=prodottoController.getAllNotOwnedBy(currentUser.getUsername(), ++page);
							if (!l.isEmpty()) {
								prodotti.addAll(l);
								requireActivity().runOnUiThread(() -> adapter.notifyItemRangeInserted(itemCount - 1, l.size()));
							}

							refreshLayout.setRefreshing(false);
						}).start();

					}
				}
			}
		});

		currentUser=Session.getInstance(requireContext()).getCurrentUser();

		new Thread(()->{
			refreshLayout.setRefreshing(true);
			prodottoController=ProdottoControllerImpl.getInstance();
			prodotti=prodottoController.getAllNotOwnedBy(currentUser.getUsername(), 0);

			requireActivity().runOnUiThread(()->{
				adapter = new ProdottoAdapter(prodotti, currentUser, true);
				list_prodotti.setAdapter(adapter);
			});
			refreshLayout.setRefreshing(false);
		}).start();

		UpdateMethod method=new UpdateMethod(currentUser.getUsername(), UpdateMethod.GET_ALL_NOT_OWNED_BY, page);
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
