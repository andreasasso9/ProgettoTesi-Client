package com.tesi.client.utils.recyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tesi.client.R;
import com.tesi.client.activities.ChatActivity;
import com.tesi.client.control.ChatControllerImpl;
import com.tesi.entity.chat.Chat;
import com.tesi.entity.chat.Image;
import com.tesi.entity.chat.Text;
import com.tesi.client.utils.File;
import com.tesi.client.utils.Session;
import com.tesi.entity.User;
import com.google.gson.Gson;

import java.util.Arrays;
import java.util.List;

import ua.naiksoftware.stomp.StompClient;

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

	@SuppressLint("CheckResult")
	@Override
	public void onBindViewHolder(@NonNull ChatHolder holder, int position) {
		if (!chats.isEmpty()) {
			Chat chat = chats.get(holder.getAdapterPosition());
			String username=Session.getInstance(holder.itemView.getContext()).getCurrentUser().getUsername();
			String receiver=Arrays.stream(chat.getId().split("-")).filter(s->!s.equalsIgnoreCase(username) && !s.equalsIgnoreCase("chat")).findAny().orElse("");
			holder.user.setText(receiver);
			holder.id=chat.getId();

			int indexLastText = chat.getTexts().size() - 1;
			if (indexLastText>=0) {
				String lastText=chat.getTexts().get(indexLastText).getText();
				if (lastText==null)
					holder.lastText.setText("📷");
				else
					holder.lastText.setText(lastText);
			} else
				holder.lastText.setText("");

			holder.container.setOnClickListener(v->{
				Intent i=new Intent(v.getContext(), ChatActivity.class);
				i.putExtra("chat", chat);
				v.getContext().startActivity(i);
			});

			holder.toolbar.setOnMenuItemClickListener(item->{
				if (item.getTitle().toString().equalsIgnoreCase("elimina")) {
					int pos=holder.getAdapterPosition();

					String id=chats.remove(pos).getId();
					notifyItemRemoved(pos);

					ChatControllerImpl.getInstance().delete(id, username);

					//Session.getInstance(holder.itemView.getContext()).getFileChatsNames().remove(chat.getId());

//					File.deleteFile(holder.itemView.getContext(), chat.getId());
				}

				return true;
			});

			Session session=Session.getInstance(holder.itemView.getContext());
			StompClient stompClient=session.getStompClient();
			User currentUser=session.getCurrentUser();
			stompClient.topic("/queue/user/"+currentUser.getUsername()).retry(5).subscribe(message->{
				Gson gson=new Gson();
				Text text=gson.fromJson(message.getPayload(), Text.class);
				Image image;
				if (text.getText()==null) {
					image = gson.fromJson(message.getPayload(), Image.class);
					chat.getTexts().add(image);
					holder.lastText.setText("📷");
				} else {
					chat.getTexts().add(text);
					holder.lastText.setText(text.getText());
				}
			});
		}
	}

	@Override
	public int getItemCount() {
		return chats.size();
	}

}
