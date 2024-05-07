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
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tesi.client.R;
import com.example.tesi.entity.entityenum.Brand;
import com.example.tesi.entity.entityenum.Categoria;
import com.example.tesi.entity.entityenum.Condizioni;
import com.example.tesi.entity.entityenum.Option;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class AddProdottoActivity extends AppCompatActivity {
	private ActivityResultLauncher<Intent> scegliImmaginiLauncher;
	private List<Bitmap> foto;
	private LinearLayout containerFoto;
	private EditText formTitolo;
	private EditText formDescrizione;
	private TextView contatoreTitolo;
	private TextView contatoreDescrizione;
	private LinearLayout sceltaCategoria, sceltaBrand, sceltaCondizioni;
	private RadioGroup opzioniCategoria, opzioniBrand, opzioniCondizioni;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_prodotto_layout);

		foto=new LinkedList<>();
		containerFoto=findViewById(R.id.containerFoto);
		Button buttonCaricaFoto = findViewById(R.id.buttonCaricaFoto);
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

		createLauncher();

		createContatoreTitoloListener();
		createContatoreDescrizioneListener();

		createScelte(sceltaCategoria, opzioniCategoria, Categoria.values());
		createScelte(sceltaBrand, opzioniBrand, Brand.values());
		createScelte(sceltaCondizioni, opzioniCondizioni, Condizioni.values());

		buttonCaricaFoto.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				scegliImmaginiLauncher.launch(new Intent());
			}
		});

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

		ActivityResultCallback<ClipData> callback=new ActivityResultCallback<ClipData>() {
			@Override
			public void onActivityResult(ClipData clipData) {
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
	
	private void createScelte(LinearLayout layout, RadioGroup optionsGroup, Option[] opzioni) {
		TextView t=new TextView(this);
		t.setText(">");
		t.setTextSize(30);
		t.setPadding(0,0,20,0);
		t.setGravity(View.TEXT_ALIGNMENT_VIEW_END);

		layout.addView(t);

		for (Option o:opzioni) {
			RadioButton rb=new RadioButton(AddProdottoActivity.this);
			rb.setText(o.getNome());
			optionsGroup.addView(rb);
		}
		layout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//TODO completare la selezione delle opzioni
				if (t.getRotation()==0) {
					t.setRotation(90);

					optionsGroup.setVisibility(View.VISIBLE);
				} else {
					t.setRotation(0);
					optionsGroup.setVisibility(View.GONE);
				}
			}
		});


	}
}
