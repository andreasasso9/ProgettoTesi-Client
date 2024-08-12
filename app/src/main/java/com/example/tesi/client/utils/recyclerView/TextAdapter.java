package com.example.tesi.client.utils.recyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tesi.client.R;
import com.example.tesi.client.chat.Text;
import com.example.tesi.entity.User;

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
			holder.text.setText(text.getText());

			RelativeLayout.LayoutParams params= (RelativeLayout.LayoutParams) holder.text.getLayoutParams();

			params.removeRule(RelativeLayout.ALIGN_PARENT_END);
			params.removeRule(RelativeLayout.ALIGN_PARENT_START);

			if (text.getSender().equals(currentUser.getUsername()))
				params.addRule(RelativeLayout.ALIGN_PARENT_END);
			else
				params.addRule(RelativeLayout.ALIGN_PARENT_START);

			holder.text.setLayoutParams(params);
		}
	}

	@Override
	public int getItemCount() {
		return texts.size();
	}

	public List<Text> getTexts() {
		return texts;
	}
}
