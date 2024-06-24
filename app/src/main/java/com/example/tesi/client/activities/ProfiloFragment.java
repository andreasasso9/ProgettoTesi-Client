package com.example.tesi.client.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.tesi.client.R;
import com.example.tesi.utils.Session;

public class ProfiloFragment extends Fragment {
	private TextView logout;
	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View v=inflater.inflate(R.layout.profilo_layout, null);

		logout=v.findViewById(R.id.logout);


		createLogoutListener();

		return v;
	}

	private void createLogoutListener() {
		logout.setOnClickListener(l->{
			Session session=Session.getInstance(getContext());
			session.setCurrentUser(null, null, false);
			Intent i=new Intent(getContext(), LoginActivity.class);
			i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
			startActivity(i);
		});
	}

}
