package com.example.tesi.client.activities;

import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.activity.OnBackPressedDispatcher;
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

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {
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
			File.deleteFile(v.getContext(), "chat-"+currentUser.getId()+chat.getReceiver());
			File.saveObjectToFile(v.getContext(), "chat-"+currentUser.getId()+chat.getReceiver(), chat);
			getOnBackPressedDispatcher().onBackPressed();
		});



		RecyclerView recyclerView=findViewById(R.id.listTexts);
		recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));


		TextAdapter textAdapter=new TextAdapter(currentUser, chat.getTexts());
		recyclerView.setAdapter(textAdapter);

		EditText editText=findViewById(R.id.editText);

		Button send=findViewById(R.id.send);
		send.setOnClickListener(v->{
			String textString=editText.getText()+"";
			if (!textString.isEmpty()) {
				Text text = new Text(textString, currentUser.getId());
				chat.getTexts().add(text);
				textAdapter.notifyItemInserted(chat.getTexts().size());

				editText.setText("");
			}
		});
	}
}
