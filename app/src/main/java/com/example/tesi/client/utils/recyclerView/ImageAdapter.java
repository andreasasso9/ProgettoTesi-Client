package com.example.tesi.client.utils.recyclerView;

import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tesi.client.R;
import com.example.tesi.entity.FotoByteArray;

public class ImageAdapter extends RecyclerView.Adapter<ImageHolder> {
	private FotoByteArray[] foto;

	public ImageAdapter(FotoByteArray[] foto) {
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
		byte[] f= foto[holder.getAdapterPosition()].getValue();

		holder.foto.setImageBitmap(BitmapFactory.decodeByteArray(f, 0, f.length));
		holder.foto.setScaleType(ImageView.ScaleType.FIT_XY);
	}

	@Override
	public int getItemCount() {
		return foto.length;
	}
}
