package com.example.tesi.client.activities;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tesi.client.R;
import com.example.tesi.client.chat.Chat;
import com.example.tesi.client.utils.File;
import com.example.tesi.client.utils.Session;
import com.example.tesi.client.utils.recyclerView.ChatAdapter;
import com.example.tesi.entity.User;

import java.util.ArrayList;
import java.util.List;

public class MessaggiFragment extends Fragment {
	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View v=inflater.inflate(R.layout.messaggi_layout, container, false);

		RecyclerView recyclerView=v.findViewById(R.id.lista_chat);
		recyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));

		User currentUser= Session.getInstance(requireContext()).getCurrentUser();
		List<?> chatsObjects= (List<?>) File.readObjectFromFile(requireContext(), "chats-"+currentUser.getId());
		List<Chat> chats=new ArrayList<>();
		if (chatsObjects!=null)
			for (Object o:chatsObjects) {
				Chat c= (Chat) o;
				chats.add(c);
			}

		ChatAdapter chatAdapter=new ChatAdapter(chats);
		recyclerView.setAdapter(chatAdapter);

		return v;
	}
}
