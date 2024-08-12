package com.example.tesi.client.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tesi.client.R;
import com.example.tesi.client.chat.Chat;
import com.example.tesi.client.chat.Text;
import com.example.tesi.client.utils.File;
import com.example.tesi.client.utils.Session;
import com.example.tesi.client.utils.recyclerView.ChatAdapter;
import com.example.tesi.entity.User;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import ua.naiksoftware.stomp.StompClient;

public class MessaggiFragment extends Fragment {
	private RecyclerView recyclerView;
	private User currentUser;
	@SuppressLint("CheckResult")
	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View v=inflater.inflate(R.layout.messaggi_layout, container, false);

		recyclerView=v.findViewById(R.id.lista_chat);
		recyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));

		List<Chat> chats=new ArrayList<>();
		Set<String> fileChatsNames=Session.getInstance(requireContext()).getFileChatsNames();

		Session session=Session.getInstance(requireContext());

		currentUser=session.getCurrentUser();
		for (String s:fileChatsNames) {
			Chat chat= (Chat) File.readObjectFromFile(requireContext(), s);
			if (chat!=null)
				if (chat.getId().contains(currentUser.getUsername()) && !chat.getId().endsWith(currentUser.getUsername()))
					chats.add(chat);
		}

		ChatAdapter chatAdapter=new ChatAdapter(chats);
		recyclerView.setAdapter(chatAdapter);

		return v;
	}

	@Override
	public void onResume() {
		super.onResume();
		List<Chat> chats=new ArrayList<>();
		Set<String> fileChatsNames=Session.getInstance(requireContext()).getFileChatsNames();

		for (String s:fileChatsNames) {
			Chat chat= (Chat) File.readObjectFromFile(requireContext(), s);
			if (chat!=null)
				if (chat.getId().contains(currentUser.getUsername()) && !chat.getId().endsWith(currentUser.getUsername()))
					chats.add(chat);
		}

		ChatAdapter chatAdapter=new ChatAdapter(chats);
		recyclerView.setAdapter(chatAdapter);
	}
}
