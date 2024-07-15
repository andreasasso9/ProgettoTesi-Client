package com.example.tesi.client.activities;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tesi.client.R;
import com.example.tesi.control.ProdottoController;
import com.example.tesi.control.ProdottoControllerImpl;
import com.example.tesi.entity.Prodotto;
import com.example.tesi.entity.User;
import com.example.tesi.utils.Session;
import com.example.tesi.utils.recyclerView.RecyclerViewHistoryAdapter;
import com.example.tesi.utils.recyclerView.RecyclerViewProdottoAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.search.SearchBar;
import com.google.android.material.search.SearchView;

import java.util.ArrayList;
import java.util.List;


public class SearchFragment extends Fragment {
	private final String SEARCH_HISTORY_PREF="search_history";

	private List<String> listHistory;
	private ProdottoController prodottoController;
	private List<Prodotto> prodottiCercati;

	private User currentUser;


	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View v = inflater.inflate(R.layout.search_layout, container, false);

		currentUser=Session.getInstance(requireContext()).getCurrentUser();

		prodottoController=new ProdottoControllerImpl();

		listHistory=Session.getInstance(requireContext()).getSearchHistory();

		SearchBar searchBar=v.findViewById(R.id.searchBar);
		SearchView searchView=v.findViewById(R.id.searchView);

		RecyclerView historySearch = v.findViewById(R.id.historySearch);

		historySearch.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));


		RecyclerViewHistoryAdapter adapter=new RecyclerViewHistoryAdapter(listHistory);
		historySearch.setAdapter(adapter);


		RecyclerView prodotti=v.findViewById(R.id.list_prodotti);
		prodotti.setLayoutManager(new GridLayoutManager(requireContext(), 2));

		prodottiCercati=new ArrayList<>();
		RecyclerViewProdottoAdapter prodottoAdapter=new RecyclerViewProdottoAdapter(prodottiCercati, currentUser);
		prodotti.setAdapter(prodottoAdapter);

		searchView.getEditText().setOnEditorActionListener((t, actionId, event) -> {
			searchBar.setText(t.getText());
			searchView.hide();

			if (!t.getText().toString().isEmpty()) {
				listHistory.add(0, t.getText()+"");
				adapter.notifyItemInserted(0);

				prodottiCercati.clear();
				prodottiCercati.addAll(prodottoController.findByTitoloODescrizione(currentUser.getId(), t.getText()+""));
				adapter.notifyItemRangeChanged(0, prodottiCercati.size()-1);
			}

			return true;
		});
		return v;
	}

	@Override
	public void onResume() {
		super.onResume();
		BottomNavigationView navbar=requireActivity().findViewById(R.id.navbar);
		navbar.getMenu().getItem(1).setChecked(true).setIcon(R.drawable.ricerca_selected);
	}

	@Override
	public void onPause() {
		super.onPause();
		Session.getInstance(requireContext()).setSearchHistory(listHistory);
	}
}
