package com.tesi.client.activities;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

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

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class HomeFragment extends Fragment {
	private ProdottoController prodottoController;
	private List<Prodotto> prodotti;
	private User currentUser;
	private ProdottoAdapter adapter;
	private SwipeRefreshLayout refreshLayout;
	private Integer page=0;

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

		currentUser=Session.getInstance(requireContext()).getCurrentUser();

		new Thread(()->{
			refreshLayout.setRefreshing(true);
			prodottoController=ProdottoControllerImpl.getInstance();
			prodotti=prodottoController.getAllNotOwnedBy(currentUser.getUsername(), page++);

			requireActivity().runOnUiThread(()->{
				adapter = new ProdottoAdapter(prodotti, currentUser, true);
				list_prodotti.setAdapter(adapter);
			});
			refreshLayout.setRefreshing(false);
		}).start();

		UpdateMethod method=new UpdateMethod(currentUser.getUsername(), UpdateMethod.GET_ALL_NOT_OWNED_BY, page);
		refreshLayout.setOnRefreshListener(() -> Refresh.run(requireActivity(), prodotti, adapter, refreshLayout, method));

		Button vediAltro=v.findViewById(R.id.vediAltro);
		vediAltro.setOnClickListener(view->{
			new Thread(()->{
				List<Prodotto> list=prodottoController.getAllNotOwnedBy(currentUser.getUsername(), page++);
				if (!list.isEmpty()) {
					int oldSize=prodotti.size();
					prodotti.addAll(list);
					requireActivity().runOnUiThread(()->adapter.notifyItemRangeInserted(oldSize, list.size()));
//				else {
//					page=1;
//					List<Prodotto> l=prodottoController.getAllNotOwnedBy(currentUser.getUsername(), page++);
//					if (!l.isEmpty())
//						prodotti.addAll(l);
//				}
//					Set<Prodotto> noDuplicates = new HashSet<>(prodotti);
//					int oldSize = prodotti.size();
//					prodotti.clear();
//					requireActivity().runOnUiThread(() -> adapter.notifyItemRangeRemoved(0, oldSize));
//					prodotti.addAll(noDuplicates);
//					requireActivity().runOnUiThread(() -> adapter.notifyItemRangeInserted(0, prodotti.size()));
				} else {
					page=0;
//					List<Prodotto> l=prodottoController.getAllNotOwnedBy(currentUser.getUsername(), page++);
//					if (!l.isEmpty()) {
//						int oldSize=prodotti.size();
//						prodotti.addAll(l);
//						requireActivity().runOnUiThread(()->adapter.notifyItemRangeInserted(oldSize, l.size()));
//					}
				}

			}).start();
		});

		list_prodotti.addOnScrollListener(new RecyclerView.OnScrollListener() {
			@Override
			public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
				super.onScrolled(recyclerView, dx, dy);
				int lastVisibleItemPosition=gridLayoutManager.findLastVisibleItemPosition();
				if (lastVisibleItemPosition <= prodotti.size() && lastVisibleItemPosition >= prodotti.size()-1)
					vediAltro.setVisibility(View.VISIBLE);
				else
					vediAltro.setVisibility(View.GONE);
			}
		});

		return v;
	}

	@Override
	public void onResume() {
		super.onResume();
		BottomNavigationView navbar=requireActivity().findViewById(R.id.navbar);
		navbar.getMenu().getItem(0).setChecked(true).setIcon(R.drawable.casa_selected);
	}
}
