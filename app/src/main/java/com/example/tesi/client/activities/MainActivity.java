package com.example.tesi.client.activities;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentContainerView;

import com.example.tesi.client.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		FragmentContainerView fragmentContainer=findViewById(R.id.fragmentContainer);
		BottomNavigationView navbar=findViewById(R.id.navbar);


		navbar.setOnItemSelectedListener(l->{
			switch (l.getTitle()+"") {
				case "Home":
					return true;

				case "Cerca":
					return true;

				case ""://addProdotto
					getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, new AddProdottoFragment()).commit();
					return true;

				case "Notifiche":
					return true;

				case "Profilo":
					return true;

				default:
					return false;
			}
		});
	}

}
