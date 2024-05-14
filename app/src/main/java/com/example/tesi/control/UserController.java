package com.example.tesi.control;

import com.example.tesi.entity.User;

public interface UserController {
	public boolean saveUser(User user);
	public User loginUser(String username, String password);
}
