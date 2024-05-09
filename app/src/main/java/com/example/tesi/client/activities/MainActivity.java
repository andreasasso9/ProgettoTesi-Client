package com.example.tesi.client.activities;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;

import com.example.tesi.client.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
	private FragmentManager fragmentManager;
	private Fragment currentFragment;
	private boolean isUserAction=true;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		SearchFragment searchFragment=new SearchFragment();
		BottomNavigationView navbar=findViewById(R.id.navbar);

		fragmentManager=getSupportFragmentManager();

		navbar.setOnItemSelectedListener(item->{
			switch (item.getTitle()+"") {
				case "Home":
					return true;

				case "Cerca":
					isUserAction=true;
					currentFragment=searchFragment;
					fragmentManager.beginTransaction().replace(R.id.fragmentContainer, currentFragment).addToBackStack(null).commit();
					return true;

				case ""://addProdotto
					isUserAction=true;
					currentFragment=new AddProdottoFragment();
					fragmentManager.beginTransaction().replace(R.id.fragmentContainer, currentFragment).addToBackStack(null).commit();
					return true;

				case "Notifiche":
					return true;

				case "Profilo":
					return true;

				default:
					return false;
			}
		});
		//TODO completa funzionamento backpressd
		fragmentManager.addOnBackStackChangedListener(() -> {
			if (isUserAction) {
				currentFragment = fragmentManager.findFragmentById(R.id.fragmentContainer);
				if (currentFragment instanceof SearchFragment) {
					navbar.setSelectedItemId(R.id.nav_cerca);
					System.out.println("search fragment");
				} else if (currentFragment instanceof AddProdottoFragment) {
					navbar.setSelectedItemId(R.id.nav_add);
					System.out.println("add prodotto fragment");
				}
				isUserAction=false;
			}
		});
	}


}
