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
import com.example.tesi.entity.User;
import com.example.tesi.client.utils.Session;
import com.example.tesi.client.utils.recyclerView.ProdottiPreferitiAdapter;

public class ArticoliPreferitiFragment extends Fragment {
	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View v=inflater.inflate(R.layout.lista_di_prodotti_layout, container, false);

		ImageButton indietro=v.findViewById(R.id.indietro);
		createIndietroListener(indietro);

		RecyclerView recyclerView=v.findViewById(R.id.list_prodotti);
		recyclerView.setLayoutManager(new GridLayoutManager(requireContext(), 2));

		User currentuser=Session.getInstance(requireContext()).getCurrentUser();

		ProdottiPreferitiAdapter adapter=new ProdottiPreferitiAdapter(currentuser);
		recyclerView.setAdapter(adapter);

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
