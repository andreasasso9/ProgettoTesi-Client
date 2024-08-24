package com.example.tesi.client.control;

import com.example.tesi.entity.User;

import java.util.UUID;

public interface UserController {
	User loginUser(String username, String password);
	boolean saveUser(String email, String username, String password, String indirizzo);
	void miPiace(String user, Long idProdotto);
	boolean update(User user);
	User findById(UUID id);
}
