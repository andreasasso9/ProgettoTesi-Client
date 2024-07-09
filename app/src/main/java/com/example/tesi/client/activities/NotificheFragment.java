package com.example.tesi.client.activities;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowMetrics;
import android.view.animation.AlphaAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.motion.widget.OnSwipe;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tesi.client.R;
import com.example.tesi.control.NotificheController;
import com.example.tesi.control.NotificheControllerImpl;
import com.example.tesi.entity.Notifica;
import com.example.tesi.utils.Session;
import com.example.tesi.utils.recyclerView.RecyclerViewNotificheAdapter;
import com.example.tesi.utils.recyclerView.ViewNotificheItemHolder;

import java.util.List;

public class NotificheFragment extends Fragment {
	private RecyclerView listaNotifiche;
	private NotificheController notificheController;

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View v=inflater.inflate(R.layout.notifiche_layout, null);

		notificheController=new NotificheControllerImpl();

		listaNotifiche=v.findViewById(R.id.lista_notifiche);
		listaNotifiche.setLayoutManager(new LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, true));

		List<Notifica> notifiche=notificheController.findByReceiver(Session.getInstance(requireContext()).getCurrentUser().getId());

		if (notifiche!=null) {
			RecyclerViewNotificheAdapter adapter = new RecyclerViewNotificheAdapter(notifiche);
			listaNotifiche.setAdapter(adapter);

			createEliminaNotifica(listaNotifiche);
		}

		return v;
	}

	public void createEliminaNotifica(RecyclerView recyclerView) {
		ItemTouchHelper itemTouchHelper=new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT /*| ItemTouchHelper.RIGHT*/) {
			@Override
			public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
				return false;
			}

			@Override
			public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
				ViewNotificheItemHolder holder= (ViewNotificheItemHolder) viewHolder;

				Toast.makeText(requireContext(), "on swipe", Toast.LENGTH_SHORT).show();

			}

			@Override
			public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
				super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
				ViewNotificheItemHolder holder = (ViewNotificheItemHolder) viewHolder;
				Paint paint=new Paint();
				paint.setColor(Color.rgb(183, 28, 28));

				c.drawRect(
						(float) holder.itemView.getRight() + dX,
						(float) holder.itemView.getTop(),
						(float) holder.itemView.getRight(),
						(float) holder.itemView.getBottom(), paint);

				LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);


				holder.containerElimina.setLayoutParams(params);

				holder.descrizione.setMaxLines(1);
				//Toast.makeText(requireContext(), "screenWidth="+screenWidth+" eliminaWidth="+holder.containerElimina.getWidth(), Toast.LENGTH_SHORT).show();
			}
		});

		itemTouchHelper.attachToRecyclerView(recyclerView);
	}
}
