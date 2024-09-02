package com.tesi.client.control;

import com.tesi.entity.Token;

public interface TokenController {
	boolean save(Token token);
	boolean delete(Token token);
	Token findByToken(String token);
}
