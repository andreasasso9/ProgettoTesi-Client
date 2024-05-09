package com.example.tesi.client.activities;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import androidx.fragment.app.Fragment;

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

public class AddProdottoFragment extends Fragment {
	private ActivityResultLauncher<Intent> scegliImmaginiLauncher;
	private List<Bitmap> foto;
	private LinearLayout containerFoto;
	private LinearLayout sceltaPrezzo;
	private EditText formTitolo, formDescrizione, formPrezzo;
	private TextView contatoreTitolo, contatoreDescrizione;
	private RadioGroup opzioniCategoria, opzioniBrand, opzioniCondizioni;
	private View v;

	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, @Nullable Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		v=inflater.inflate(R.layout.add_prodotto_layout, container, false);

		foto=new LinkedList<>();
		containerFoto=v.findViewById(R.id.containerFoto);
		formTitolo=v.findViewById(R.id.formTitolo);
		formDescrizione=v.findViewById(R.id.formDescrizione);
		contatoreTitolo=v.findViewById(R.id.contatoreTitolo);
		contatoreDescrizione=v.findViewById(R.id.contatoreDescrizione);
		LinearLayout sceltaCategoria = v.findViewById(R.id.sceltaCategoria);
		LinearLayout sceltaBrand = v.findViewById(R.id.sceltaBrand);
		LinearLayout sceltaCondizioni = v.findViewById(R.id.sceltaCondizioni);
		opzioniCategoria=v.findViewById(R.id.opzioniCategoria);
		opzioniBrand=v.findViewById(R.id.opzioniBrand);
		opzioniCondizioni=v.findViewById(R.id.opzioniCondizioni);
		sceltaPrezzo=v.findViewById(R.id.sceltaPrezzo);
		formPrezzo=v.findViewById(R.id.formPrezzo);

		createLauncher();

		createContatoreTitoloListener();
		createContatoreDescrizioneListener();

		createScelte(sceltaCategoria, opzioniCategoria, Categoria.values());
		createScelte(sceltaBrand, opzioniBrand, Brand.values());
		createScelte(sceltaCondizioni, opzioniCondizioni, Condizioni.values());

		Button buttonCaricaFoto = v.findViewById(R.id.buttonCaricaFoto);
		buttonCaricaFoto.setOnClickListener(l -> scegliImmaginiLauncher.launch(new Intent()));

		createSceltaPrezzo();

		Button uploadButton=v.findViewById(R.id.uploadButton);
		upload(uploadButton);

		return v;
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
						list.add(BitmapFactory.decodeStream(getContext().getContentResolver().openInputStream(clipData.getItemAt(i).getUri())));
					} catch (FileNotFoundException e) {
						throw new RuntimeException(e);
					}
				}
				foto.addAll(list);
				containerFoto.removeAllViews();
				for (Bitmap b:foto) {
					ImageView image=new ImageView(getContext());
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
			RadioButton rb=new RadioButton(getContext());
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
		TextView t=new TextView(getContext());
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
		//TODO implementa upload prodotto sul server
		b.setOnClickListener(l->{
			//ottengo titolo e descrizione
			String titolo=formTitolo.getText()+"";
			String descrizione=formDescrizione.getText()+"";

			//ottengo categoria, brand e condizione
			RadioButton rbCategoria, rbBrand, rbCondizione;
			rbCategoria=v.findViewById(opzioniCategoria.getCheckedRadioButtonId());
			rbBrand=v.findViewById(opzioniBrand.getCheckedRadioButtonId());
			rbCondizione=v.findViewById(opzioniCondizioni.getCheckedRadioButtonId());

			String categoria=rbCategoria.getText()+"";
			String brand=rbBrand.getText()+"";
			String condizione=rbCondizione.getText()+"";

			//ottengo prezzo
			double prezzo= Double.parseDouble(formPrezzo.getText()+"");

			Prodotto p=new Prodotto(titolo, descrizione, Categoria.valueOf(categoria.toUpperCase()), Brand.valueOf(brand.toUpperCase()), Condizioni.valueOf(condizione.toUpperCase()), prezzo, foto);
		});
	}
}
