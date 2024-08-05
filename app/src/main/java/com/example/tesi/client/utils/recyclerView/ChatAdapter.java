package com.example.tesi.client.utils.recyclerView;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tesi.client.R;
import com.example.tesi.client.activities.ChatActivity;
import com.example.tesi.client.chat.Chat;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatHolder> {
	private final List<Chat> list;

	public ChatAdapter(List<Chat> list) {
		this.list = list;
	}

	@NonNull
	@Override
	public ChatHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item_layout, parent, false);
		return new ChatHolder(v);
	}

	@Override
	public void onBindViewHolder(@NonNull ChatHolder holder, int position) {
		if (!list.isEmpty()) {
			Chat chat = list.get(holder.getAdapterPosition());
			holder.user.setText(chat.getReceiver().getUsername());

			int indexLastText = chat.getTexts().size() - 1;
			if (indexLastText>=0)
				holder.lastText.setText(chat.getTexts().get(indexLastText).getText());
			else
				holder.lastText.setText("");

			holder.itemView.setOnClickListener(v->{
				Intent i=new Intent(v.getContext(), ChatActivity.class);
				v.getContext().startActivity(i);
			});
		}
	}

	@Override
	public int getItemCount() {
		return list.size();
	}
}
