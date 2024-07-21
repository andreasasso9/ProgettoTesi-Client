package com.example.tesi.control;

import com.example.tesi.entity.User;

import java.util.UUID;

public interface UserController {
	User loginUser(String username, String password);
	boolean saveUser(String email, String username, String password, String indirizzo);
	void miPiace(UUID idUser, Long idProdotto);
	boolean update(User user);
	User findById(UUID id);
}
