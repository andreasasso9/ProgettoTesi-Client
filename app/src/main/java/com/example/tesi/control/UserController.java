package com.example.tesi.control;

import com.example.tesi.entity.User;

public interface UserController {
	public User loginUser(String username, String password);

	public boolean saveUser(String email, String username, String password, String indirizzo);
}
