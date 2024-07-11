package com.example.tesi.client.activities;

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

import com.example.tesi.client.R;
import com.example.tesi.control.ProdottoController;
import com.example.tesi.control.ProdottoControllerImpl;
import com.example.tesi.entity.Prodotto;
import com.example.tesi.entity.User;
import com.example.tesi.utils.Session;
import com.example.tesi.utils.recyclerView.RecyclerViewMieiProdottiAdapter;

import java.util.List;

public class IMieiArticoliFragment extends Fragment {
	private ProdottoController prodottoController;
	private RecyclerView recyclerView;
	private ImageButton indietro;

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View v=inflater.inflate(R.layout.i_miei_articoli_layout, null);

		indietro=v.findViewById(R.id.indietro);

		createIndietroListener();

		prodottoController=new ProdottoControllerImpl();
		User currentUser=Session.getInstance(requireContext()).getCurrentUser();

		List<Prodotto> prodotti=prodottoController.findByIdProprietario(currentUser.getId());

		recyclerView=v.findViewById(R.id.list_prodotti);
		recyclerView.setLayoutManager(new GridLayoutManager(requireContext(), 2));

		if (prodotti != null && !prodotti.isEmpty()) {
			RecyclerViewMieiProdottiAdapter adapter=new RecyclerViewMieiProdottiAdapter(prodotti, currentUser);
			recyclerView.setAdapter(adapter);
		}

		return v;
	}

	private void createIndietroListener() {
		indietro.setOnClickListener(l->{
			MainActivity mainActivity= (MainActivity) getActivity();
			assert mainActivity != null;
			if (mainActivity.profiloFragment==null)
				mainActivity.profiloFragment=new ProfiloFragment();
			mainActivity.currentFragment=mainActivity.profiloFragment;
			mainActivity.fragmentManager.beginTransaction().replace(R.id.fragmentContainer, mainActivity.profiloFragment).addToBackStack(null).commit();
		});
	}
}
