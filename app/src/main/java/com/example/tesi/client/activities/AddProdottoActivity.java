package com.example.tesi.client.activities;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tesi.client.R;
import com.example.tesi.entity.Prodotto;
import com.example.tesi.entity.entityenum.Brand;
import com.example.tesi.entity.entityenum.Categoria;
import com.example.tesi.entity.entityenum.Condizioni;
import com.example.tesi.entity.entityenum.Option;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class AddProdottoActivity extends AppCompatActivity {
	private ActivityResultLauncher<Intent> scegliImmaginiLauncher;
	private List<Bitmap> foto;
	private LinearLayout containerFoto, sceltaCategoria, sceltaBrand, sceltaCondizioni, sceltaPrezzo;
	private EditText formTitolo, formDescrizione, formPrezzo;
	private TextView contatoreTitolo, contatoreDescrizione;
	private RadioGroup opzioniCategoria, opzioniBrand, opzioniCondizioni;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_prodotto_layout);

		foto=new LinkedList<>();
		containerFoto=findViewById(R.id.containerFoto);
		formTitolo=findViewById(R.id.formTitolo);
		formDescrizione=findViewById(R.id.formDescrizione);
		contatoreTitolo=findViewById(R.id.contatoreTitolo);
		contatoreDescrizione=findViewById(R.id.contatoreDescrizione);
		sceltaCategoria=findViewById(R.id.sceltaCategoria);
		sceltaBrand=findViewById(R.id.sceltaBrand);
		sceltaCondizioni=findViewById(R.id.sceltaCondizioni);
		opzioniCategoria=findViewById(R.id.opzioniCategoria);
		opzioniBrand=findViewById(R.id.opzioniBrand);
		opzioniCondizioni=findViewById(R.id.opzioniCondizioni);
		sceltaPrezzo=findViewById(R.id.sceltaPrezzo);
		formPrezzo=findViewById(R.id.formPrezzo);

		createLauncher();

		createContatoreTitoloListener();
		createContatoreDescrizioneListener();

		createScelte(sceltaCategoria, opzioniCategoria, Categoria.values());
		createScelte(sceltaBrand, opzioniBrand, Brand.values());
		createScelte(sceltaCondizioni, opzioniCondizioni, Condizioni.values());

		Button buttonCaricaFoto = findViewById(R.id.buttonCaricaFoto);
		buttonCaricaFoto.setOnClickListener(v -> scegliImmaginiLauncher.launch(new Intent()));

		createSceltaPrezzo();

		Button uploadButton=findViewById(R.id.uploadButton);
		upload(uploadButton);

	}

	private void createLauncher() {
		ActivityResultContract<Intent, ClipData> contract=new ActivityResultContract<Intent, ClipData>() {
			@NonNull
			@Override
			public Intent createIntent(@NonNull Context context, Intent intent) {
				intent.setAction(Intent.ACTION_GET_CONTENT);
				intent.setType("image/*");
				intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);

				return intent;
			}

			@Override
			public ClipData parseResult(int i, @Nullable Intent intent) {
				return intent!=null ? intent.getClipData() : null;
			}
		};

		ActivityResultCallback<ClipData> callback= clipData -> {
			if (clipData!=null) {
				List<Bitmap> list=new ArrayList<>();
				for (int i = 0; i < clipData.getItemCount(); i++) {
					try {
						list.add(BitmapFactory.decodeStream(getContentResolver().openInputStream(clipData.getItemAt(i).getUri())));
					} catch (FileNotFoundException e) {
						throw new RuntimeException(e);
					}
				}
				foto.addAll(list);
				containerFoto.removeAllViews();
				for (Bitmap b:foto) {
					ImageView image=new ImageView(AddProdottoActivity.this);
					image.setImageBitmap(b);
					image.setLayoutParams(new LinearLayout.LayoutParams(
							500,500
					));
					containerFoto.addView(image);
				}
			}

		};

		scegliImmaginiLauncher=registerForActivityResult(contract, callback);
	}

	private void createContatoreTitoloListener() {
		formTitolo.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				contatoreTitolo.setText("Caratteri: "+s.length());
			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});
	}
	private void createContatoreDescrizioneListener() {
		formDescrizione.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				contatoreDescrizione.setText("Caratteri: "+s.length());
			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});
	}
	
	private void createScelte(LinearLayout layout, RadioGroup optionsGroup, Option... opzioni) {
		TextView t=createTextView(">", 30, 0, 0, 20, 0);

		layout.addView(t);

		for (Option o:opzioni) {
			RadioButton rb=new RadioButton(AddProdottoActivity.this);
			rb.setText(o.getNome());
			optionsGroup.addView(rb);
		}
		layout.setOnClickListener(v -> {
			//TODO completare la selezione delle opzioni
			if (t.getRotation()==0) {
				t.setRotation(90);
				optionsGroup.setVisibility(View.VISIBLE);
			} else {
				t.setRotation(0);
				optionsGroup.setVisibility(View.GONE);
			}
		});
	}

	private TextView createTextView(String text, int size, int leftPadding, int topPadding, int rightPadding, int bottomPadding) {
		TextView t=new TextView(this);
		t.setText(text);
		t.setTextSize(size);
		t.setPadding(leftPadding, topPadding, rightPadding, bottomPadding);

		return t;
	}

	private void createSceltaPrezzo() {
		TextView t=createTextView(">", 30, 0, 0, 20, 0);
		sceltaPrezzo.addView(t);

		sceltaPrezzo.setOnClickListener(l->{
			//TODO implementa inserimento prezzo
			if (t.getRotation()==0) {
				t.setRotation(90);
				formPrezzo.setVisibility(View.VISIBLE);
			} else {
				t.setRotation(0);
				formPrezzo.setVisibility(View.GONE);
			}
		});
	}

	private void upload(Button b) {
		//TODO implementa upload prodotto
		b.setOnClickListener(l->{
			//ottengo titolo e descrizione
			String titolo=formTitolo.getText()+"";
			String descrizione=formDescrizione.getText()+"";

			//ottengo categoria, brand e condizione
			RadioButton rbCategoria, rbBrand, rbCondizione;
			rbCategoria=findViewById(opzioniCategoria.getCheckedRadioButtonId());
			rbBrand=findViewById(opzioniBrand.getCheckedRadioButtonId());
			rbCondizione=findViewById(opzioniCondizioni.getCheckedRadioButtonId());

			String categoria=rbCategoria.getText()+"";
			String brand=rbBrand.getText()+"";
			String condizione=rbCondizione.getText()+"";

			//ottengo prezzo
			double prezzo= Double.parseDouble(formPrezzo.getText()+"");

			Prodotto p=new Prodotto(titolo, descrizione, Categoria.valueOf(categoria), Brand.valueOf(brand), Condizioni.valueOf(condizione), prezzo, foto);
		});
	}
}
