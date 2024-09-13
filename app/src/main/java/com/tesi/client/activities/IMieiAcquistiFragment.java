package com.tesi.client.activities;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

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
import com.tesi.client.utils.recyclerView.ProdottoAdapter;

import java.util.List;

public class IMieiAcquistiFragment extends Fragment {
	private List<Prodotto> acquisti;
	private ProdottoAdapter adapter;
	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View v=inflater.inflate(R.layout.lista_di_prodotti_layout, container, false);

		ImageButton indietro=v.findViewById(R.id.indietro);
		createIndietroListener(indietro);

		RecyclerView recyclerView=v.findViewById(R.id.list_prodotti);
		recyclerView.setLayoutManager(new GridLayoutManager(requireContext(), 2));

		User currentUser= Session.getInstance(requireContext()).getCurrentUser();

		SwipeRefreshLayout swipeRefreshLayout=v.findViewById(R.id.refreshLayout);
		new Thread(()->{
			swipeRefreshLayout.setRefreshing(true);
			ProdottoController prodottoController= ProdottoControllerImpl.getInstance();
			acquisti=prodottoController.findByCompratore(currentUser.getUsername());

			requireActivity().runOnUiThread(()->{
				adapter=new ProdottoAdapter(acquisti, currentUser, false);
				recyclerView.setAdapter(adapter);
			});
			swipeRefreshLayout.setRefreshing(false);
		}).start();

		UpdateMethod method=new UpdateMethod(currentUser.getUsername(), UpdateMethod.FIND_BY_COMPRATORE, null);
		swipeRefreshLayout.setOnRefreshListener(()-> Refresh.run(requireActivity(), acquisti, adapter, swipeRefreshLayout, method));

		return v;
	}

	private void createIndietroListener(ImageButton indietro) {
		indietro.setOnClickListener(l->{
			MainActivity mainActivity= (MainActivity) getActivity();
			assert mainActivity != null;
			if (mainActivity.profiloFragment==null)
				mainActivity.profiloFragment=new ProfiloFragment();
			mainActivity.currentFragment=mainActivity.profiloFragment;
			mainActivity.fragmentManager.beginTransaction().replace(R.id.fragmentContainer, mainActivity.currentFragment).addToBackStack(null).commit();
		});
	}
}
