package com.tesi.client.activities;

import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tesi.client.R;
import com.tesi.client.control.ProdottoController;
import com.tesi.client.control.ProdottoControllerImpl;
import com.tesi.entity.Prodotto;
import com.tesi.entity.User;
import com.tesi.client.utils.Session;
import com.tesi.client.utils.recyclerView.HistoryAdapter;
import com.tesi.client.utils.recyclerView.ProdottoAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.search.SearchBar;
import com.google.android.material.search.SearchView;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


public class SearchFragment extends Fragment {
	private List<String> listHistory;
	private ProdottoController prodottoController;
	private List<Prodotto> prodottiCercati;
	private User currentUser;
	private ProdottoAdapter prodottoAdapter;
	private RecyclerView prodotti;

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View v = inflater.inflate(R.layout.search_layout, container, false);

		currentUser= Session.getInstance(requireContext()).getCurrentUser();

		prodottoController= ProdottoControllerImpl.getInstance();

		listHistory=Session.getInstance(requireContext()).getSearchHistory();

		SearchBar searchBar=v.findViewById(R.id.searchBar);
		SearchView searchView=v.findViewById(R.id.searchView);

		RecyclerView historySearch = v.findViewById(R.id.historySearch);

		historySearch.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));

		prodotti=v.findViewById(R.id.list_prodotti);
		prodotti.setLayoutManager(new GridLayoutManager(requireContext(), 2));

		prodottiCercati=new ArrayList<>();
		prodottoAdapter=new ProdottoAdapter(prodottiCercati, currentUser, true);
		prodotti.setAdapter(prodottoAdapter);

		HistoryAdapter historyAdapter; historyAdapter=new HistoryAdapter(listHistory, searchView);
		historySearch.setAdapter(historyAdapter);

		searchView.getEditText().setOnEditorActionListener((t, actionId, event) -> {
			if (!prodottoAdapter.getProdotti().equals(prodottiCercati)) {
				prodottoAdapter.setProdotti(prodottiCercati);
			}
			prodotti.setVisibility(View.VISIBLE);

			String ricerca=t.getText()+"";

			searchBar.setText(ricerca);
			searchView.hide();

			if (!ricerca.isEmpty()) {
				listHistory.remove(ricerca);
				listHistory.add(0, ricerca);
				historyAdapter.notifyItemRangeChanged(0, listHistory.size());

				prodottoAdapter.notifyItemRangeRemoved(0, prodottiCercati.size());
				prodottiCercati.clear();

				prodottiCercati.addAll(prodottoController.findByRicerca(currentUser.getUsername(), ricerca));
				prodottoAdapter.notifyItemRangeInserted(0, prodottiCercati.size());

				if (prodottiCercati.isEmpty())
					prodotti.setVisibility(View.GONE);
			} else {
				prodottoAdapter.notifyItemRangeRemoved(0, prodottiCercati.size());
				prodottiCercati.clear();
			}

			searchBar.getMenu().getItem(0/*Prezzo*/).setOnMenuItemClickListener(item -> {
				EditText maxEditText=new EditText(requireContext());
				maxEditText.setHint("Prezzo massimo");
				maxEditText.setHintTextColor(getResources().getColor(R.color.gray, null));
				maxEditText.setInputType(InputType.TYPE_CLASS_NUMBER);

				AlertDialog.Builder builder=new AlertDialog.Builder(requireContext())
						.setView(maxEditText)
						.setPositiveButton("Applica", (dialog, which) -> {
							double maxPrezzo=Double.parseDouble(maxEditText.getText().toString());
							List<Prodotto> prodottiFiltered=new LinkedList<>();
							for (Prodotto p:prodottiCercati) {
								if (p.getPrezzo() <= maxPrezzo)
									prodottiFiltered.add(p);
							}
							prodottoAdapter.setProdotti(prodottiFiltered);
							prodottoAdapter.notifyDataSetChanged();
						}).setNegativeButton("Annulla", (dialog, which) -> {
							prodottoAdapter.setProdotti(prodottiCercati);
							prodottoAdapter.notifyDataSetChanged();
						});

				builder.show();
				return true;
			});

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
