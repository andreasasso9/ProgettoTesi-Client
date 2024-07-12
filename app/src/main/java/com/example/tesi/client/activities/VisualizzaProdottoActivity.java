package com.example.tesi.client.activities;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tesi.client.R;
import com.example.tesi.entity.FotoByteArray;
import com.example.tesi.entity.Prodotto;
import com.example.tesi.entity.User;
import com.example.tesi.utils.Session;

public class VisualizzaProdottoActivity extends AppCompatActivity {
	private Prodotto prodotto;
	private User proprietario;
	private FotoByteArray[] foto;
	private LinearLayout containerFoto;
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

		int screenHeight= Session.getScreenHeight();

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
			prodotto=i.getSerializableExtra("prodotto", Prodotto.class);
			proprietario=i.getSerializableExtra("proprietario", User.class);
			foto=i.getSerializableExtra("foto", FotoByteArray[].class);
		} else {
			prodotto= (Prodotto) i.getSerializableExtra("prodotto");
			proprietario= (User) i.getSerializableExtra("proprietario");
			foto= (FotoByteArray[]) i.getSerializableExtra("foto");
		}

		containerFoto=findViewById(R.id.containerFoto);

		LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) (screenHeight*.75));
		params.setMargins(5, 0, 5, 0);

		for (FotoByteArray f:foto) {
			ImageView imageView=new ImageView(this);
			imageView.setImageBitmap(BitmapFactory.decodeByteArray(f.getValue(), 0, f.getValue().length));
			imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
			imageView.setLayoutParams(params);
			containerFoto.addView(imageView);
		}

		TextView t=findViewById(R.id.proprietario);
		t.setText(proprietario.getUsername());

		t=findViewById(R.id.titolo);
		t.setText(prodotto.getTitolo());

		t=findViewById(R.id.condizioni);
		t.setText(prodotto.getCondizioni().getNome());

		t=findViewById(R.id.prezzo);
		t.setText("€"+prodotto.getPrezzo());

		t=findViewById(R.id.descrizione);
		t.setText(prodotto.getDescrizione());

	}
}
