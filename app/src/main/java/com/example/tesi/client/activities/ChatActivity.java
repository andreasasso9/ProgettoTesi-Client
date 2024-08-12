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

import ua.naiksoftware.stomp.StompClient;

public class ChatActivity extends AppCompatActivity {
	private TextAdapter textAdapter;

	private StompClient stompClient;
	private Chat chat;
	private User currentUser;

	//todo correggere salvataggio chat

	@SuppressLint("CheckResult")
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chat_layout);

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
			chat=getIntent().getSerializableExtra("chat", Chat.class);
		else
			chat= (Chat) getIntent().getSerializableExtra("chat");

		currentUser= Session.getInstance(this).getCurrentUser();

		ImageButton indietro=findViewById(R.id.indietro);
		indietro.setOnClickListener(v->{
			getOnBackPressedDispatcher().onBackPressed();
		});

		RecyclerView recyclerView=findViewById(R.id.listTexts);
		recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));


		textAdapter=new TextAdapter(currentUser, chat.getTexts());
		recyclerView.setAdapter(textAdapter);

		recyclerView.smoothScrollToPosition(textAdapter.getItemCount()-1);

		EditText editText=findViewById(R.id.editText);


		stompClient=Session.getInstance(this).getStompClient();
		stompClient.topic("/queue/user/"+currentUser.getUsername()).subscribe(message->{
			Text text=new Gson().fromJson(message.getPayload(), Text.class);

			chat.getTexts().add(text);

			runOnUiThread(()->{
				textAdapter.notifyItemInserted(textAdapter.getItemCount()+1);
			});
		});

		Button send=findViewById(R.id.send);
		send.setOnClickListener(v->{
			String textString=editText.getText()+"";
			if (!textString.isEmpty()) {
				Text text = new Text(textString, currentUser.getUsername(), chat.getReceiver());
				chat.getTexts().add(text);
				textAdapter.notifyItemInserted(textAdapter.getItemCount());
				recyclerView.smoothScrollToPosition(textAdapter.getItemCount()-1);

				editText.setText("");

				stompClient.send("/app/chat", new Gson().toJson(text)).subscribe();
			}
		});
	}

	@Override
	protected void onPause() {
		//Log.d("chat on pause", "pause");
		File.deleteFile(this, "chat-"+currentUser.getUsername()+"-"+chat.getReceiver());
		File.saveObjectToFile(this, "chat-"+currentUser.getUsername()+"-"+chat.getReceiver(), chat);
		super.onPause();
	}
}
