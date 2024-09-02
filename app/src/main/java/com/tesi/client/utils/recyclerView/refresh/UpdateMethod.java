package com.tesi.client.utils.recyclerView.refresh;

import com.tesi.client.control.ProdottoController;
import com.tesi.client.control.ProdottoControllerImpl;
import com.tesi.entity.Prodotto;

import java.util.ArrayList;
import java.util.List;

public class UpdateMethod implements Runnable {
	public final static String GET_ALL_NOT_OWNED_BY="get alla not owned by";
	public final static String FIND_BY_PROPRIETARIO="find by proprietario";
	public final static String FIND_BY_COMPRATORE="find by compratore";

	private List<Prodotto> list;
	private final String username;
	private final String method;

	public UpdateMethod(String username, String method) {
		this.list = new ArrayList<>();
		this.username = username;
		this.method = method;
	}

	@Override
	public void run() {
		ProdottoController prodottoController= ProdottoControllerImpl.getInstance();
		switch (method) {
			case GET_ALL_NOT_OWNED_BY:
				list=prodottoController.getAllNotOwnedBy(username);
				return;

			case FIND_BY_PROPRIETARIO:
				list=prodottoController.findByProprietario(username);
				return;

			case FIND_BY_COMPRATORE:
				list=prodottoController.findByCompratore(username);
				return;

			default:
		}
	}

	public List<Prodotto> getList() {
		return list;
	}

	public void setList(List<Prodotto> list) {
		this.list = list;
	}
}
