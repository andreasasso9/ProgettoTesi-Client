package com.example.tesi.control;

import com.example.tesi.entity.User;

import java.util.UUID;

public interface UserController {
	public User loginUser(String username, String password);
	public boolean saveUser(String email, String username, String password, String indirizzo);
	public boolean miPiace(UUID idUser, Long idProdotto);
	public boolean update(User user);
	public User findById(UUID id);
}
