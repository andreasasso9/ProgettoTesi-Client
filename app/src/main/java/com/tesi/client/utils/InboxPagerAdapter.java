package com.tesi.client.utils;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.tesi.client.activities.MessaggiFragment;
import com.tesi.client.activities.NotificheFragment;

public class InboxPagerAdapter extends FragmentStateAdapter {
	public InboxPagerAdapter(@NonNull Fragment fragment) {
		super(fragment);
	}

	@NonNull
	@Override
	public Fragment createFragment(int position) {
		if (position==0) {
			return new NotificheFragment();
		} else {

			return new MessaggiFragment();
		}
	}

	@Override
	public int getItemCount() {
		return 2;
	}
}
