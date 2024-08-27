package com.example.tesi.client.control;

import com.example.tesi.entity.Token;

public interface TokenController {
	boolean save(Token token);
	boolean delete(Token token);
	Token findByToken(String token);
}
