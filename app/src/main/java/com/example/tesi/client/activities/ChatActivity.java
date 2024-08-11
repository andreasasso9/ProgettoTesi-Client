package com.example.tesi.client.activities;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tesi.client.R;
import com.example.tesi.client.chat.Chat;
import com.example.tesi.client.chat.Text;
import com.example.tesi.client.utils.File;
import com.example.tesi.client.utils.Session;
import com.example.tesi.client.utils.recyclerView.TextAdapter;
import com.example.tesi.entity.User;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import ua.naiksoftware.stomp.StompClient;

public class ChatActivity extends AppCompatActivity {

	private StompClient stompClient;
	@SuppressLint("CheckResult")
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chat_layout);

		Chat chat;
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
			chat=getIntent().getSerializableExtra("chat", Chat.class);
		else
			chat= (Chat) getIntent().getSerializableExtra("chat");

		User currentUser= Session.getInstance(this).getCurrentUser();

		assert chat != null;

		ImageButton indietro=findViewById(R.id.indietro);
		indietro.setOnClickListener(v->{
			File.deleteFile(v.getContext(), "chat-"+currentUser.getId()+"-"+chat.getReceiver());
			File.saveObjectToFile(v.getContext(), "chat-"+currentUser.getId()+"-"+chat.getReceiver(), chat);
			getOnBackPressedDispatcher().onBackPressed();
		});

		RecyclerView recyclerView=findViewById(R.id.listTexts);
		recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));


		TextAdapter textAdapter=new TextAdapter(currentUser, chat.getTexts());
		recyclerView.setAdapter(textAdapter);

		EditText editText=findViewById(R.id.editText);

		stompClient=Session.getInstance(this).getStompClient();
		stompClient.topic("/queue/user/"+currentUser.getUsername()).subscribe(message->{
			Text text=new Gson().fromJson(message.getPayload(), Text.class);

			chat.getTexts().add(text);
			textAdapter.notifyItemInserted(textAdapter.getItemCount());
		});

		Button send=findViewById(R.id.send);
		send.setOnClickListener(v->{
			String textString=editText.getText()+"";
			if (!textString.isEmpty()) {
				Text text = new Text(textString, currentUser.getUsername(), chat.getReceiver());
				chat.getTexts().add(text);
				textAdapter.notifyItemInserted(textAdapter.getItemCount());

				editText.setText("");

				stompClient.send("/app/chat", new Gson().toJson(text)).subscribe();
			}
		});
	}

	@SuppressLint("CheckResult")
	@Override
	protected void onPause() {
		Session session=Session.getInstance(this);
		User currentUser=session.getCurrentUser();
		Set<String> fileChatsNames=session.getFileChatsNames();

		stompClient.topic("/queue/user/"+currentUser.getUsername()).subscribe(message->{
			Text text=new Gson().fromJson(message.getPayload(), Text.class);
			String nameChat="chat-"+text.getSender()+"-"+text.getReceiver();
			Chat chat;
			if (fileChatsNames.contains(nameChat)) {
				chat = (Chat) File.readObjectFromFile(this, nameChat);
				assert chat != null;
				chat.getTexts().add(text);
				File.deleteFile(this, nameChat);
			} else {
				fileChatsNames.add(nameChat);
				List<Text> texts=new ArrayList<>();
				texts.add(text);
				chat=new Chat(text.getReceiver(), texts, nameChat);
			}

			File.saveObjectToFile(this, nameChat, chat);
		});
		super.onPause();
	}
}
