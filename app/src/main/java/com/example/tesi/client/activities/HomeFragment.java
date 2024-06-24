package com.example.tesi.client.activities;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.tesi.client.R;
import com.example.tesi.entity.User;
import com.example.tesi.utils.Session;

public class HomeFragment extends Fragment {
	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View v = inflater.inflate(R.layout.home_layout, null);

		TextView t= v.findViewById(R.id.currentUser);

		User u= Session.getInstance(getContext()).getCurrentUser();
		t.setText(u.getUsername());

		return v;
	}
}
