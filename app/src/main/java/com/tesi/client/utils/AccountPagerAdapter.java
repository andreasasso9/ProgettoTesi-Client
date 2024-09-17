package com.tesi.client.utils;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.tesi.client.activities.ArmadioFragment;
import com.tesi.client.activities.InformazioniFragment;

public class AccountPagerAdapter extends FragmentStateAdapter {
	private ArmadioFragment armadioFragment;
	private InformazioniFragment informazioniFragment;
	public AccountPagerAdapter(@NonNull Fragment fragment) {
		super(fragment);
	}

	@NonNull
	@Override
	public Fragment createFragment(int position) {
		if (position == 0) {
			if (armadioFragment == null)
				armadioFragment = new ArmadioFragment();
			return armadioFragment;
		} else {
			if (informazioniFragment == null)
				informazioniFragment=new InformazioniFragment();
			return informazioniFragment;
		}
	}

	@Override
	public int getItemCount() {
		return 2;
	}
}
