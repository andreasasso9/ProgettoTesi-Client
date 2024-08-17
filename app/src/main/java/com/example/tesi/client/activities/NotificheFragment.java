package com.example.tesi.client.activities;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tesi.client.R;
import com.example.tesi.control.NotificheController;
import com.example.tesi.control.NotificheControllerImpl;
import com.example.tesi.entity.Notifica;
import com.example.tesi.client.utils.Session;
import com.example.tesi.client.utils.recyclerView.NotificheAdapter;
import com.example.tesi.client.utils.recyclerView.NotificheHolder;

import java.util.Collections;
import java.util.List;

public class NotificheFragment extends Fragment {
	private NotificheController notificheController;

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View v=inflater.inflate(R.layout.notifiche_layout, container, false);

		RecyclerView listaNotifiche = v.findViewById(R.id.lista_notifiche);
		listaNotifiche.setLayoutManager(new LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false));

		new Thread(()->{
			notificheController=new NotificheControllerImpl();
			List<Notifica> notifiche=notificheController.findByReceiver(Session.getInstance(requireContext()).getCurrentUser().getUsername());

			if (notifiche != null && !notifiche.isEmpty()) {
				Collections.reverse(notifiche);
				getActivity().runOnUiThread(()->{
					NotificheAdapter adapter = new NotificheAdapter(notifiche);
					listaNotifiche.setAdapter(adapter);
					createEliminaNotifica(listaNotifiche, inflater, container, savedInstanceState);
				});

			}
		}).start();

		return v;
	}

	public void createEliminaNotifica(RecyclerView recyclerView, @NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		ItemTouchHelper itemTouchHelper=new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
			@Override
			public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
				return false;
			}

			@Override
			public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
				NotificheHolder holder = (NotificheHolder) viewHolder;

				if (direction==ItemTouchHelper.LEFT) {
					int position = holder.getAdapterPosition();

					notificheController.delete(holder.descrizione.getText() + "");

					NotificheAdapter adapter = (NotificheAdapter) recyclerView.getAdapter();

					assert adapter != null;
					adapter.getNotifiche().remove(position);
					adapter.notifyItemRemoved(position);
				}
			}

			@Override
			public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
				super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
				NotificheHolder holder = (NotificheHolder) viewHolder;
				Paint paint=new Paint();
				paint.setColor(Color.rgb(183, 28, 28));

				c.drawRect(
						(float) holder.itemView.getRight() + dX,
						(float) holder.itemView.getTop(),
						(float) holder.itemView.getRight(),
						(float) holder.itemView.getBottom(), paint);

				LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(holder.elimina.getLayoutParams());
				params.width=ViewGroup.LayoutParams.WRAP_CONTENT;

				holder.elimina.setLayoutParams(params);

				holder.descrizione.setMaxLines(1);
			}
		});

		itemTouchHelper.attachToRecyclerView(recyclerView);
	}
}
