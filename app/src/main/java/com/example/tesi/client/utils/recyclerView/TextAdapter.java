package com.example.tesi.client.utils.recyclerView;

import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tesi.client.R;
import com.example.tesi.client.chat.Image;
import com.example.tesi.client.chat.Text;
import com.example.tesi.entity.User;

import java.util.Base64;
import java.util.List;

public class TextAdapter extends RecyclerView.Adapter<TextHolder> {
	private final List<Text> texts;
	private final User currentUser;

	public TextAdapter(User currentUser, List<Text> texts) {
		this.currentUser = currentUser;
		this.texts = texts;
	}

	@NonNull
	@Override
	public TextHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View v=LayoutInflater.from(parent.getContext()).inflate(R.layout.text_item_layout, parent, false);
		return new TextHolder(v);
	}

	@Override
	public void onBindViewHolder(@NonNull TextHolder holder, int position) {
		if (!texts.isEmpty()) {
			Text text=texts.get(holder.getAdapterPosition());

			RelativeLayout.LayoutParams textParams = (RelativeLayout.LayoutParams) holder.text.getLayoutParams();
			RelativeLayout.LayoutParams imageParams = (RelativeLayout.LayoutParams) holder.image.getLayoutParams();

			textParams.removeRule(RelativeLayout.ALIGN_PARENT_END);
			textParams.removeRule(RelativeLayout.ALIGN_PARENT_START);

			imageParams.removeRule(RelativeLayout.ALIGN_PARENT_END);
			imageParams.removeRule(RelativeLayout.ALIGN_PARENT_START);

			if (text.getSender().equals(currentUser.getUsername())) {
				textParams.addRule(RelativeLayout.ALIGN_PARENT_END);
				imageParams.addRule(RelativeLayout.ALIGN_PARENT_END);
			} else {
				textParams.addRule(RelativeLayout.ALIGN_PARENT_START);
				imageParams.addRule(RelativeLayout.ALIGN_PARENT_START);
			}

			holder.text.setLayoutParams(textParams);
			holder.image.setLayoutParams(imageParams);

			if (text.getText()==null) {
				Image image = (Image) text;
				byte[] imageByte = Base64.getDecoder().decode(image.getFoto());
				holder.image.setImageBitmap(BitmapFactory.decodeByteArray(imageByte, 0, imageByte.length));
				holder.text.setVisibility(View.GONE);
			}
			else {
				holder.text.setText(text.getText());
			}
		}
	}

	@Override
	public int getItemCount() {
		return texts.size();
	}
}
