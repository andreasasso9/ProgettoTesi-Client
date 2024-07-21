package com.example.tesi.client.activities;

import android.content.Intent;
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
import com.example.tesi.entity.FotoByteArray;
import com.example.tesi.entity.Prodotto;
import com.example.tesi.entity.User;
import com.example.tesi.utils.Session;
import com.example.tesi.utils.recyclerView.RecyclerViewImageAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

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
		//int screenWidth= Session.getInstance(this).getScreenWidth();

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



		RecyclerViewImageAdapter imageAdapter=new RecyclerViewImageAdapter(foto);
		containerFoto.setAdapter(imageAdapter);

		TabLayout tabLayout=findViewById(R.id.tabLayout);
		new TabLayoutMediator(tabLayout, containerFoto, (tab, position)->{}).attach();


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

	}
}
