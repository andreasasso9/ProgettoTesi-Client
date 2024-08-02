package com.example.tesi.utils;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.tesi.client.activities.NotificheFragment;
import com.example.tesi.client.activities.SearchFragment;

public class InboxPagerAdapter extends FragmentStateAdapter {
	private NotificheFragment notificheFragment;
	public InboxPagerAdapter(@NonNull Fragment fragment) {
		super(fragment);
	}

	@NonNull
	@Override
	public Fragment createFragment(int position) {
		if (position==0) {
			if (notificheFragment == null)
				notificheFragment = new NotificheFragment();
			return notificheFragment;
		} else {
			return new SearchFragment();
		}
	}

	@Override
	public int getItemCount() {
		return 2;
	}
}
