package com.example.tesi.client.activities;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.tesi.client.R;
import com.example.tesi.entity.User;
import com.example.tesi.utils.Ricerca;
import com.example.tesi.utils.Session;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.search.SearchBar;
import com.google.android.material.search.SearchView;

import java.util.List;


public class SearchFragment extends Fragment {
	private final String SEARCH_HISTORY_PREF="search_history";

	private User currentUser;
	private List<Ricerca> history;
	private LinearLayout historyLayout;


	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View v = inflater.inflate(R.layout.search_layout, container, false);

		history=Session.getInstance(requireContext()).getSearchHistory();

		SearchBar searchBar=v.findViewById(R.id.searchBar);
		SearchView searchView=v.findViewById(R.id.searchView);
		historyLayout=v.findViewById(R.id.searchHistory);

		for (Ricerca r:history)
			historyLayout.addView(createHistoryResearchtextView(r));


		currentUser=Session.getInstance(requireContext()).getCurrentUser();

		searchView.getEditText().setOnEditorActionListener((t, actionId, event) -> {
			searchBar.setText(searchView.getText());
			searchView.hide();

			if (!t.getText().toString().isEmpty()) {
				Ricerca r=new Ricerca(t.getText()+"");
				history.add(r);

				historyLayout.addView(createHistoryResearchtextView(r));
			}


			return false;
		});
		return v;
	}

	private View createHistoryResearchtextView(Ricerca r) {
		TextView textView = new TextView(requireContext());
		textView.setText(r.getText());
		textView.setTextSize(30);
		textView.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.icons8_rimuovi, 0);



		return textView;
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
		Session.getInstance(requireContext()).setSearchHistory(history);
	}
}
