package com.tesi.client.activities;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.tesi.client.R;
import com.tesi.client.utils.InboxPagerAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class InboxFragment extends Fragment {

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View v=inflater.inflate(R.layout.inbox_layout, container, false);

		TabLayout tabLayout=v.findViewById(R.id.tabLayout);
		ViewPager2 viewPager=v.findViewById(R.id.viewPager);

		InboxPagerAdapter inboxPagerAdapter=new InboxPagerAdapter(this);
		viewPager.setAdapter(inboxPagerAdapter);
		viewPager.setUserInputEnabled(false);

		new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
			switch (position) {
				case 0:
					tab.setText("Notifiche");
					break;
				case 1:
					tab.setText("Messaggi");
					break;
			}
		}).attach();

		return v;
	}

	@Override
	public void onResume() {
		super.onResume();
		BottomNavigationView navbar=requireActivity().findViewById(R.id.navbar);
		navbar.getMenu().getItem(3).setChecked(true).setIcon(R.drawable.inbox_selected);
	}
}
