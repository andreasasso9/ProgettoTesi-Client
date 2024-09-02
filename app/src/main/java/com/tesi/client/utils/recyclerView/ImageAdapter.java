package com.tesi.client.utils.recyclerView;

import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tesi.client.R;
import com.tesi.entity.FotoByteArray;

import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageHolder> {
	private final List<FotoByteArray> foto;

	public ImageAdapter(List<FotoByteArray> foto) {
		this.foto = foto;
	}

	@NonNull
	@Override
	public ImageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.image_layout, parent, false);
		return new ImageHolder(v);
	}

	@Override
	public void onBindViewHolder(@NonNull ImageHolder holder, int position) {
		if (!foto.isEmpty()) {
			FotoByteArray fotoByteArray=foto.get(holder.getAdapterPosition());

			if (fotoByteArray!=null) {
				byte[] f = fotoByteArray.getValue();

				holder.foto.setImageBitmap(BitmapFactory.decodeByteArray(f, 0, f.length));
				holder.foto.setScaleType(ImageView.ScaleType.FIT_XY);
			} else {
				holder.foto.setImageResource(R.drawable.icons8_nessuna_immagine_50);
				holder.foto.setScaleType(ImageView.ScaleType.CENTER);
			}
		}
	}

	@Override
	public int getItemCount() {
		if (foto!=null)
			return foto.size();
		return 0;
	}
}
