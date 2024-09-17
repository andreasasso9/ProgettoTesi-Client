package com.tesi.client.control;

import com.tesi.entity.User;

import java.util.UUID;

public interface UserController {
	User loginUser(String username, String password);
	boolean saveUser(String email, String username, String password, String indirizzo);
	boolean update(User user);
	User findById(UUID id);
	boolean checkEmail(String email);
}
