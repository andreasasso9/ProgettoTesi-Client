package com.tesi.client.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.tesi.client.R;
import com.tesi.client.control.ChatController;
import com.tesi.client.control.ChatControllerImpl;
import com.tesi.entity.chat.Chat;
import com.tesi.client.utils.File;
import com.tesi.client.control.FotoProdottoControllerImpl;
import com.tesi.entity.FotoByteArray;
import com.tesi.entity.Prodotto;
import com.tesi.client.utils.Session;
import com.tesi.client.utils.recyclerView.ImageAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;


import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class VisualizzaProdottoActivity extends AppCompatActivity {
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.visualizza_prodotto_layout);

		ImageButton indietro = findViewById(R.id.indietro);
		indietro.setOnClickListener(l -> getOnBackPressedDispatcher().onBackPressed());

		indietro.bringToFront();

		Intent i = getIntent();

		int screenHeight = Session.getInstance(this).getScreenHeight();

		Prodotto prodotto;
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
			prodotto = i.getSerializableExtra("prodotto", Prodotto.class);
		else
			prodotto = (Prodotto) i.getSerializableExtra("prodotto");

		if (prodotto != null) {
			new Thread(() -> {
				List<FotoByteArray> list = FotoProdottoControllerImpl.getInstance().findByProdotto(prodotto.getId());

				List<FotoByteArray> foto = new LinkedList<>(list);

				ViewPager2 containerFoto = findViewById(R.id.containerFoto);
				ViewGroup.LayoutParams params = containerFoto.getLayoutParams();
				params.height = (int) (screenHeight * .70);


				runOnUiThread(() -> {
					containerFoto.setLayoutParams(params);

					if (foto.isEmpty())
						foto.add(null);
					ImageAdapter imageAdapter = new ImageAdapter(foto);
					containerFoto.setAdapter(imageAdapter);

					TabLayout tabLayout = findViewById(R.id.tabLayout);
					new TabLayoutMediator(tabLayout, containerFoto, (tab, position) -> {
					}).attach();
				});

			}).start();
			String proprietario=prodotto.getProprietario();

			TextView t = findViewById(R.id.proprietario);
			t.setText(proprietario);

			t = findViewById(R.id.titolo);
			t.setText(prodotto.getTitolo());

			t = findViewById(R.id.condizioni);
			t.setText(prodotto.getCondizione());

			t = findViewById(R.id.prezzo);
			t.setText("€" + prodotto.getPrezzo());

			t = findViewById(R.id.descrizione);
			t.setText(prodotto.getDescrizione());

			Button acquista = findViewById(R.id.acquista);
			acquista.setOnClickListener(l -> {
				Intent intent = new Intent(this, AcquistaActivity.class);
				intent.putExtra("prodotto", prodotto);
				startActivity(intent);
			});

			String usernameCurrentUser = Session.getInstance(this).getCurrentUser().getUsername();
			Button chiediInfo = findViewById(R.id.chiediInfo);
			chiediInfo.setOnClickListener(v -> {
				ChatController chatController= ChatControllerImpl.getInstance();
				String id1="chat-"+usernameCurrentUser+"-"+proprietario;
				String id2="chat-"+proprietario+"-"+usernameCurrentUser;

				Chat chat=chatController.findById(id1);
				if (chat==null)
					chat=chatController.findById(id2);



//				Chat chat = (Chat) File.readObjectFromFile(v.getContext(), "chat-" + usernameCurrentUser + "-" + proprietario);
				if (chat == null) {
					chat = new Chat(new ArrayList<>(), "chat-" + usernameCurrentUser + "-" + proprietario, usernameCurrentUser, proprietario);
					chatController.save(chat);
				}

				Intent chatIntent = new Intent(v.getContext(), ChatActivity.class);
				chatIntent.putExtra("chat", chat);
				v.getContext().startActivity(chatIntent);
			});

		}
	}
}
