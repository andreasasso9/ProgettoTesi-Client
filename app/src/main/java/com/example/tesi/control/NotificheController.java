package com.example.tesi.control;

import com.example.tesi.entity.Notifica;

import java.util.List;
import java.util.UUID;

public interface NotificheController {
	boolean save(Notifica notifica);
	List<Notifica> findByReceiver(UUID receiver);
	void delete(String descrizione);
}
