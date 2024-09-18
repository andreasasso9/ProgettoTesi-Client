package com.tesi.client.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.tesi.client.R;
import com.tesi.client.control.ProdottoControllerImpl;
import com.tesi.client.control.TokenControllerImpl;
import com.tesi.client.utils.Session;
import com.tesi.entity.Token;
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

		Session session=Session.getInstance(this);

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
			if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
				ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.POST_NOTIFICATIONS}, 0);
			}
		}

		new Thread(()->{
			String username, deviceToken;
			username=session.getCurrentUser().getUsername();
			deviceToken=session.getToken();

			Token token=new Token(deviceToken, username);

			TokenControllerImpl.getInstance().save(token);
			Session.getInstance(this).setLikedBy(ProdottoControllerImpl.getInstance().findByLikedBy(username));
		}).start();

		navbar=findViewById(R.id.navbar);

		homeFragment=new HomeFragment();

		fragmentManager=getSupportFragmentManager();
		fragmentManager.beginTransaction().replace(R.id.fragmentContainer, homeFragment).addToBackStack(null).commit();

		createNavBarItemListener();
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
					Intent i=new Intent(this, FormProdottoActivity.class);
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
