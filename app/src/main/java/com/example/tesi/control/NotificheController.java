package com.example.tesi.control;

import com.example.tesi.entity.Notifica;

import java.util.List;
import java.util.UUID;

public interface NotificheController {
	public boolean save(Notifica notifica);
	public List<Notifica> findByReceiver(UUID receiver);
	public boolean delete(String descrizione);
}
