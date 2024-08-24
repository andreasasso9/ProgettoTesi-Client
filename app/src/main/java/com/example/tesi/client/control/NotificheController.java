package com.example.tesi.client.control;

import com.example.tesi.entity.Notifica;

import java.util.List;

public interface NotificheController {
	void save(Notifica notifica);
	List<Notifica> findByReceiver(String receiver);
	void delete(String descrizione);
}
