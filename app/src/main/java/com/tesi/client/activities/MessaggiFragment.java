package com.tesi.client.activities;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tesi.client.R;
import com.tesi.client.chat.Chat;
import com.tesi.client.utils.File;
import com.tesi.client.utils.Session;
import com.tesi.client.utils.recyclerView.ChatAdapter;
import com.tesi.entity.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class MessaggiFragment extends Fragment {
	private RecyclerView recyclerView;
	private User currentUser;

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View v=inflater.inflate(R.layout.messaggi_layout, container, false);

		recyclerView=v.findViewById(R.id.lista_chat);
		recyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));

		Session session=Session.getInstance(requireContext());

		List<Chat> chats=new ArrayList<>();
		Set<String> fileChatsNames=session.getFileChatsNames();

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
