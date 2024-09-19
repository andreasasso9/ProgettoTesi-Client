package com.tesi.client.activities;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.tesi.client.R;
import com.tesi.client.utils.AccountPagerAdapter;

public class AccountActivity extends AppCompatActivity {

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.multitab_layout);

		TabLayout tabLayout=findViewById(R.id.tabLayout);
		ViewPager2 viewPager=findViewById(R.id.viewPager);

		AccountPagerAdapter accountPagerAdapter=new AccountPagerAdapter(getSupportFragmentManager(), getLifecycle());
		viewPager.setAdapter(accountPagerAdapter);
		viewPager.setUserInputEnabled(true);

		new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
			switch (position) {
				case 0:
					tab.setText("Armadio");
					break;
				case 1:
					tab.setText("Informazioni");
					break;
			}
		}).attach();
	}

}
