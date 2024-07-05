package com.example.tesi.client.activities;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tesi.client.R;
import com.example.tesi.control.NotificheControllerImpl;
import com.example.tesi.entity.Notifica;
import com.example.tesi.utils.Session;
import com.example.tesi.utils.recyclerView.RecyclerViewNotificheAdapter;

import java.util.List;

public class NotificheFragment extends Fragment {
	private RecyclerView listaNotifiche;

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View v=inflater.inflate(R.layout.notifiche_layout, null);

		listaNotifiche=v.findViewById(R.id.lista_notifiche);
		listaNotifiche.setLayoutManager(new LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, true));

		List<Notifica> notifiche=new NotificheControllerImpl().findByReceiver(Session.getInstance(requireContext()).getCurrentUser().getId());

		if (notifiche!=null) {
			RecyclerViewNotificheAdapter adapter = new RecyclerViewNotificheAdapter(notifiche);
			listaNotifiche.setAdapter(adapter);
		}

		return v;
	}
}
