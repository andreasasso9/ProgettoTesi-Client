package com.tesi.client.control;

import com.tesi.entity.chat.Chat;

import java.util.List;

public interface ChatController {
	Chat save(Chat chat);
	Chat findById(String id);
	boolean delete(String id, String username);
	List<Chat> findByUser(String user);
}
