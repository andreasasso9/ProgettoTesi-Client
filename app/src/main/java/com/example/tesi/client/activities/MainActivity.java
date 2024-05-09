package com.example.tesi.client.activities;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.tesi.client.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
	private FragmentManager fragmentManager;
	private Fragment currentFragment;
	private BottomNavigationView navbar;
	private SearchFragment searchFragment;
	private AddProdottoFragment addProdottoFragment;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		navbar=findViewById(R.id.navbar);

		fragmentManager=getSupportFragmentManager();

		createNavBarItemListener();

		getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
			@Override
			public void handleOnBackPressed() {

			}
		});
	}

	private void createNavBarItemListener() {
		navbar.setOnItemSelectedListener(item->{
			MenuItem iconHome=navbar.getMenu().getItem(0);
			MenuItem iconSearch=navbar.getMenu().getItem(1);
			MenuItem iconAdd=navbar.getMenu().getItem(2);
			MenuItem iconNotifiche=navbar.getMenu().getItem(3);
			MenuItem iconProfilo=navbar.getMenu().getItem(4);
			switch (item.getTitle()+"") {
				case "Home":
					iconHome.setIcon(R.drawable.casa_selected);
					return true;

				case "Cerca":
					iconSearch.setIcon(R.drawable.ricerca_selected);
					if (searchFragment == null)
						searchFragment=new SearchFragment();
					currentFragment=searchFragment;
					fragmentManager.beginTransaction().replace(R.id.fragmentContainer, currentFragment).addToBackStack(null).commit();
					return true;

				case ""://addProdotto
					iconAdd.setIcon(R.drawable.piu_selected);
					if (addProdottoFragment == null)
						addProdottoFragment=new AddProdottoFragment();
					currentFragment=addProdottoFragment;
					fragmentManager.beginTransaction().replace(R.id.fragmentContainer, currentFragment).addToBackStack(null).commit();
					return true;

				case "Notifiche":
					iconNotifiche.setIcon(R.drawable.notifica_selected);
					return true;

				case "Profilo":
					iconProfilo.setIcon(R.drawable.profilo_selected);
					return true;

				default:
					return false;
			}
		});
	}

}
