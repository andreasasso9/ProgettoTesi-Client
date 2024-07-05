package com.example.tesi.client.activities;

import android.content.Intent;
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
	private HomeFragment homeFragment;
	private ProfiloFragment profiloFragment;
	private NotificheFragment notificheFragment;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		navbar=findViewById(R.id.navbar);

		homeFragment=new HomeFragment();

		fragmentManager=getSupportFragmentManager();
		fragmentManager.beginTransaction().replace(R.id.fragmentContainer, homeFragment).addToBackStack(null).commit();

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
			MenuItem iconNotifiche=navbar.getMenu().getItem(3);
			MenuItem iconProfilo=navbar.getMenu().getItem(4);
			switch (item.getTitle()+"") {
				case "Home":
					iconHome.setIcon(R.drawable.casa_selected);
					iconSearch.setIcon(R.drawable.ricerca_unselected);
					iconNotifiche.setIcon(R.drawable.notifica_unselected);
					iconProfilo.setIcon(R.drawable.profilo_unselected);

					currentFragment=homeFragment;
					fragmentManager.beginTransaction().replace(R.id.fragmentContainer, currentFragment).addToBackStack(null).commit();
					return true;

				case "Cerca":
					iconHome.setIcon(R.drawable.casa_unselected);
					iconSearch.setIcon(R.drawable.ricerca_selected);
					iconNotifiche.setIcon(R.drawable.notifica_unselected);
					iconProfilo.setIcon(R.drawable.profilo_unselected);
					if (searchFragment == null)
						searchFragment=new SearchFragment();
					currentFragment=searchFragment;
					fragmentManager.beginTransaction().replace(R.id.fragmentContainer, currentFragment).addToBackStack(null).commit();
					return true;

				case ""://addProdotto
					Intent i=new Intent(this, AddProdottoActivity.class);
					startActivity(i);
					return true;

				case "Notifiche":
					iconHome.setIcon(R.drawable.casa_unselected);
					iconSearch.setIcon(R.drawable.ricerca_unselected);
					iconNotifiche.setIcon(R.drawable.notifica_selected);
					iconProfilo.setIcon(R.drawable.profilo_unselected);
					if (notificheFragment==null)
						notificheFragment=new NotificheFragment();
					currentFragment=notificheFragment;
					fragmentManager.beginTransaction().replace(R.id.fragmentContainer, currentFragment).addToBackStack(null).commit();
					return true;

				case "Profilo":
					iconHome.setIcon(R.drawable.casa_unselected);
					iconSearch.setIcon(R.drawable.ricerca_unselected);
					iconNotifiche.setIcon(R.drawable.notifica_unselected);
					iconProfilo.setIcon(R.drawable.profilo_selected);

					if (profiloFragment==null)
						profiloFragment=new ProfiloFragment();
					currentFragment=profiloFragment;
					fragmentManager.beginTransaction().replace(R.id.fragmentContainer, currentFragment).addToBackStack(null).commit();
					return true;

				default:
					return false;
			}
		});
	}
}
