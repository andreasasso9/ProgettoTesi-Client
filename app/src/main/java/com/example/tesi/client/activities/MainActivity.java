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
import com.example.tesi.client.utils.File;
import com.example.tesi.client.utils.Session;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
	public FragmentManager fragmentManager;
	public Fragment currentFragment;
	private BottomNavigationView navbar;
	private SearchFragment searchFragment;
	private HomeFragment homeFragment;
	public ProfiloFragment profiloFragment;
	private InboxFragment inboxFragment;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		navbar=findViewById(R.id.navbar);

		homeFragment=new HomeFragment();

		fragmentManager=getSupportFragmentManager();
		fragmentManager.beginTransaction().replace(R.id.fragmentContainer, homeFragment).addToBackStack(null).commit();

		createNavBarItemListener();

//		getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
//			@Override
//			public void handleOnBackPressed() {}
//		});

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
					iconNotifiche.setIcon(R.drawable.inbox_unselected);
					iconProfilo.setIcon(R.drawable.profilo_unselected);

					currentFragment=homeFragment;
					break;

				case "Cerca":
					iconHome.setIcon(R.drawable.casa_unselected);
					iconSearch.setIcon(R.drawable.ricerca_selected);
					iconNotifiche.setIcon(R.drawable.inbox_unselected);
					iconProfilo.setIcon(R.drawable.profilo_unselected);
					if (searchFragment == null)
						searchFragment=new SearchFragment();
					currentFragment=searchFragment;
					break;

				case ""://addProdotto
					Intent i=new Intent(this, AddProdottoActivity.class);
					startActivity(i);
					return true;

				case "Inbox":
					iconHome.setIcon(R.drawable.casa_unselected);
					iconSearch.setIcon(R.drawable.ricerca_unselected);
					iconNotifiche.setIcon(R.drawable.inbox_selected);
					iconProfilo.setIcon(R.drawable.profilo_unselected);
					if (inboxFragment==null)
						inboxFragment=new InboxFragment();
					currentFragment=inboxFragment;
					break;

				case "Profilo":
					iconHome.setIcon(R.drawable.casa_unselected);
					iconSearch.setIcon(R.drawable.ricerca_unselected);
					iconNotifiche.setIcon(R.drawable.inbox_unselected);
					iconProfilo.setIcon(R.drawable.profilo_selected);

					if (profiloFragment==null)
						profiloFragment=new ProfiloFragment();
					currentFragment=profiloFragment;
					break;

				default:
					return false;
			}
			fragmentManager.beginTransaction().replace(R.id.fragmentContainer, currentFragment).addToBackStack(null).commit();
			return true;
		});
	}
}
