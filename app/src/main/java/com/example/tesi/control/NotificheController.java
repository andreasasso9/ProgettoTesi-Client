package com.example.tesi.control;

import com.example.tesi.entity.Notifica;

import java.util.List;
import java.util.UUID;

public interface NotificheController {
	void save(Notifica notifica);
	List<Notifica> findByReceiver(String receiver);
	void delete(String descrizione);
}
