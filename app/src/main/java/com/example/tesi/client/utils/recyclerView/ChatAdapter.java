package com.example.tesi.client.utils.recyclerView;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tesi.client.R;
import com.example.tesi.client.activities.ChatActivity;
import com.example.tesi.client.chat.Chat;
import com.example.tesi.client.utils.File;
import com.example.tesi.client.utils.Session;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatHolder> {
	private final List<Chat> chats;

	public ChatAdapter(List<Chat> chats) {
		this.chats = chats;
	}

	@NonNull
	@Override
	public ChatHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item_layout, parent, false);
		return new ChatHolder(v);
	}

	@Override
	public void onBindViewHolder(@NonNull ChatHolder holder, int position) {
		if (!chats.isEmpty()) {
			Chat chat = chats.get(holder.getAdapterPosition());
			holder.user.setText(chat.getReceiver());
			holder.id=chat.getId();

			int indexLastText = chat.getTexts().size() - 1;
			if (indexLastText>=0)
				holder.lastText.setText(chat.getTexts().get(indexLastText).getText());
			else
				holder.lastText.setText("");

			holder.container.setOnClickListener(v->{
				Intent i=new Intent(v.getContext(), ChatActivity.class);
				i.putExtra("chat", chat);
				v.getContext().startActivity(i);
			});

			holder.toolbar.setOnMenuItemClickListener(item->{
				if (item.getTitle().toString().equalsIgnoreCase("elimina")) {
					int pos=holder.getAdapterPosition();

					chats.remove(pos);
					notifyItemRemoved(pos);

					Session.getInstance(holder.itemView.getContext()).getFileChatsNames().remove(chat.getId());

					File.deleteFile(holder.itemView.getContext(), chat.getId());
				}

				return true;
			});
		}
	}

	@Override
	public int getItemCount() {
		return chats.size();
	}

}
