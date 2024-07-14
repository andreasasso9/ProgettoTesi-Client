package com.example.tesi.client.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tesi.client.R;
import com.example.tesi.utils.Session;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ProfiloFragment extends Fragment {
	private TextView logout, articoli, preferiti;
	private IMieiArticoliFragment iMieiArticoliFragment;
	private MainActivity mainActivity;
	private ArticoliPreferitiFragment articoliPreferitiFragment;
	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View v=inflater.inflate(R.layout.profilo_layout, null);

		mainActivity= (MainActivity) getActivity();

		logout=v.findViewById(R.id.logout);
		articoli=v.findViewById(R.id.articoli);
		preferiti=v.findViewById(R.id.preferiti);

		createLogoutListener();
		createIMieiArticoliListener();
		createArticoliPreferitiListener();

		return v;
	}

	@Override
	public void onResume() {
		super.onResume();
		BottomNavigationView navbar=requireActivity().findViewById(R.id.navbar);
		navbar.getMenu().getItem(4).setChecked(true).setIcon(R.drawable.profilo_selected);
	}

	private void createLogoutListener() {
		logout.setOnClickListener(l->{
			Session session=Session.getInstance(getContext());
			session.setCurrentUser(null, null);

			Intent i=new Intent(getContext(), LoginActivity.class);
			i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
			startActivity(i);
		});
	}

	private void createIMieiArticoliListener() {
		articoli.setOnClickListener(l->{
			assert mainActivity != null;

			if (iMieiArticoliFragment==null)
				iMieiArticoliFragment=new IMieiArticoliFragment();

			mainActivity.currentFragment=iMieiArticoliFragment;
			mainActivity.fragmentManager.beginTransaction().replace(R.id.fragmentContainer, iMieiArticoliFragment).addToBackStack(null).commit();
		});
	}

	private void createArticoliPreferitiListener() {
		preferiti.setOnClickListener(l->{
			assert mainActivity != null;

			if (articoliPreferitiFragment==null)
				articoliPreferitiFragment=new ArticoliPreferitiFragment();
			mainActivity.currentFragment=articoliPreferitiFragment;
			mainActivity.fragmentManager.beginTransaction().replace(R.id.fragmentContainer, mainActivity.currentFragment).addToBackStack(null).commit();
		});
	}

}
