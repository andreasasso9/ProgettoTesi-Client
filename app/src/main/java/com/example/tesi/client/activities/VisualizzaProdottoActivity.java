package com.example.tesi.client.activities;

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

import com.example.tesi.client.R;
import com.example.tesi.client.chat.Chat;
import com.example.tesi.client.utils.File;
import com.example.tesi.entity.FotoByteArray;
import com.example.tesi.entity.Prodotto;
import com.example.tesi.entity.User;
import com.example.tesi.client.utils.Session;
import com.example.tesi.client.utils.recyclerView.ImageAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class VisualizzaProdottoActivity extends AppCompatActivity {
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.visualizza_prodotto_layout);

		ImageButton indietro=findViewById(R.id.indietro);
		indietro.setOnClickListener(l->{
			getOnBackPressedDispatcher().onBackPressed();
		});

		indietro.bringToFront();

		Intent i=getIntent();

		int screenHeight= Session.getInstance(this).getScreenHeight();

		Prodotto prodotto;
		User proprietario;
		FotoByteArray[] foto;
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
			prodotto =i.getSerializableExtra("prodotto", Prodotto.class);
			proprietario =i.getSerializableExtra("proprietario", User.class);
			foto =i.getSerializableExtra("foto", FotoByteArray[].class);
		} else {
			prodotto = (Prodotto) i.getSerializableExtra("prodotto");
			proprietario = (User) i.getSerializableExtra("proprietario");
			foto = (FotoByteArray[]) i.getSerializableExtra("foto");
		}

		ViewPager2 containerFoto = findViewById(R.id.containerFoto);
		ViewGroup.LayoutParams params= containerFoto.getLayoutParams();
		params.height= (int) (screenHeight*.70);
		containerFoto.setLayoutParams(params);



		ImageAdapter imageAdapter=new ImageAdapter(foto);
		containerFoto.setAdapter(imageAdapter);

		TabLayout tabLayout=findViewById(R.id.tabLayout);
		new TabLayoutMediator(tabLayout, containerFoto, (tab, position)->{}).attach();

		assert proprietario!=null;
		assert prodotto!=null;

		TextView t=findViewById(R.id.proprietario);
		t.setText(proprietario.getUsername());

		t=findViewById(R.id.titolo);
		t.setText(prodotto.getTitolo());

		t=findViewById(R.id.condizioni);
		t.setText(prodotto.getCondizioni().getNome());

		t=findViewById(R.id.prezzo);
		t.setText("€"+ prodotto.getPrezzo());

		t=findViewById(R.id.descrizione);
		t.setText(prodotto.getDescrizione());

		Button acquista=findViewById(R.id.acquista);
		acquista.setOnClickListener(l->{
			Intent intent=new Intent(this, AcquistaActivity.class);
			intent.putExtra("prodotto", prodotto);
			startActivity(intent);
		});

		String usernameCurrentUser=Session.getInstance(this).getCurrentUser().getUsername();
		Button chiediInfo=findViewById(R.id.chiediInfo);
		chiediInfo.setOnClickListener(v->{
			Chat chat= (Chat) File.readObjectFromFile(v.getContext(), "chat-"+usernameCurrentUser+"-"+proprietario.getUsername());
			if (chat==null) {
				chat=new Chat(proprietario.getUsername(), new ArrayList<>(), "chat-"+usernameCurrentUser+"-"+proprietario.getUsername());
				File.saveObjectToFile(v.getContext(), chat.getId(), chat);
				Session.getInstance(v.getContext()).getFileChatsNames().add("chat-"+usernameCurrentUser+"-"+proprietario.getUsername());
				SharedPreferences.Editor editor= v.getContext().getSharedPreferences(Session.SESSION_PREFERENCES, MODE_PRIVATE).edit();
				editor.putStringSet("fileChatsNames", Session.getInstance(v.getContext()).getFileChatsNames()).apply();
			}

			Intent chatIntent=new Intent(v.getContext(), ChatActivity.class);
			chatIntent.putExtra("chat", chat);
			v.getContext().startActivity(chatIntent);

		});

	}
}
