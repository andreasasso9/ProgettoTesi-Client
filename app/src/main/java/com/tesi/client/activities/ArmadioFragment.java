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
import com.tesi.client.utils.Session;
import com.tesi.client.utils.recyclerView.MieiProdottiAdapter;

import java.util.List;

public class ArmadioFragment extends Fragment {
	private List<Prodotto> prodotti;
	private MieiProdottiAdapter adapter;

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View v=inflater.inflate(R.layout.lista_di_prodotti_layout, container, false);

		v.findViewById(R.id.indietro).setVisibility(View.GONE);

		RecyclerView recyclerView = v.findViewById(R.id.list_prodotti);
		recyclerView.setLayoutManager(new GridLayoutManager(requireContext(), 2));

		User currentUser= Session.getInstance(requireContext()).getCurrentUser();

		SwipeRefreshLayout refreshLayout=v.findViewById(R.id.refreshLayout);
		new Thread(()->{
			refreshLayout.setRefreshing(true);
			ProdottoController prodottoController = ProdottoControllerImpl.getInstance();
			prodotti= prodottoController.findByProprietario(currentUser.getUsername());

			requireActivity().runOnUiThread(()->{
				adapter=new MieiProdottiAdapter(prodotti, currentUser);
				recyclerView.setAdapter(adapter);
			});
			refreshLayout.setRefreshing(false);
		}).start();

		UpdateMethod method=new UpdateMethod(currentUser.getUsername(), UpdateMethod.FIND_BY_PROPRIETARIO, null);
		refreshLayout.setOnRefreshListener(()-> Refresh.run(requireActivity(), prodotti, adapter, refreshLayout, method));

		return v;
	}
}
