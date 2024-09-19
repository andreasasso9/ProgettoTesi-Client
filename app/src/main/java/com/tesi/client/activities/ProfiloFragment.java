package com.tesi.client.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.fragment.app.Fragment;

import com.tesi.client.R;
import com.tesi.client.control.TokenControllerImpl;
import com.tesi.client.utils.Session;
import com.tesi.entity.Token;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ProfiloFragment extends Fragment {
	private MainActivity mainActivity;
	private ArticoliPreferitiFragment articoliPreferitiFragment;
	private IMieiAcquistiFragment iMieiAcquistiFragment;
	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View v=inflater.inflate(R.layout.profilo_layout, container, false);

		mainActivity= (MainActivity) getActivity();

		TextView logout, preferiti, iMieiAcquisti, user;
		ContentLoadingProgressBar progressBar;
		LinearLayout account;

		logout=v.findViewById(R.id.logout);
		account=v.findViewById(R.id.account);
		preferiti=v.findViewById(R.id.preferiti);
		iMieiAcquisti=v.findViewById(R.id.iMieiAcquisti);
		progressBar=v.findViewById(R.id.progressBar);
		user=v.findViewById(R.id.user);

		String username=Session.getInstance(requireContext()).getCurrentUser().getUsername();
		user.setText(username);

		createLogoutListener(logout, progressBar);
		createAccountListener(account);
		createArticoliPreferitiListener(preferiti);
		createIMieiAcquistiListener(iMieiAcquisti);

		return v;
	}

	@Override
	public void onResume() {
		super.onResume();
		BottomNavigationView navbar=requireActivity().findViewById(R.id.navbar);
		navbar.getMenu().getItem(4).setChecked(true).setIcon(R.drawable.profilo_selected);
	}

	private void createLogoutListener(TextView logout, ContentLoadingProgressBar progressBar) {
		logout.setOnClickListener(l->{
			progressBar.setVisibility(View.VISIBLE);

			new Handler(Looper.getMainLooper()).postDelayed(()->{
				Session session=Session.getInstance(getContext());

				Token deviceToken=new Token(session.getToken(), session.getCurrentUser().getUsername());
				TokenControllerImpl.getInstance().delete(deviceToken);

				session.setCurrentUser(null, requireContext());
				session.getStompClient().disconnect();
				session.setStompClient(null);


				Intent i=new Intent(getContext(), LoginActivity.class);
				i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
				startActivity(i);
			}, 100);
		});
	}

	private void createAccountListener(LinearLayout account) {
		account.setOnClickListener(l->{


			Intent i=new Intent(mainActivity, AccountActivity.class);
			startActivity(i);
		});
	}

	private void createArticoliPreferitiListener(TextView preferiti) {
		preferiti.setOnClickListener(l->{
			assert mainActivity != null;

			if (articoliPreferitiFragment==null)
				articoliPreferitiFragment=new ArticoliPreferitiFragment();
//			mainActivity.currentFragment=articoliPreferitiFragment;
			mainActivity.fragmentManager.beginTransaction().replace(R.id.fragmentContainer, articoliPreferitiFragment).addToBackStack(null).commit();
		});
	}

	private void createIMieiAcquistiListener(TextView iMieiAcquisti) {
		iMieiAcquisti.setOnClickListener(l->{
			assert mainActivity != null;

			if (iMieiAcquistiFragment==null)
				iMieiAcquistiFragment=new IMieiAcquistiFragment();
//			mainActivity.currentFragment=iMieiAcquistiFragment;
			mainActivity.fragmentManager.beginTransaction().replace(R.id.fragmentContainer, iMieiAcquistiFragment).addToBackStack(null).commit();
		});
	}

}
