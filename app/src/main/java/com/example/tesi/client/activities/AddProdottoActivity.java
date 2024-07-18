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
import com.example.tesi.control.FotoProdottoController;
import com.example.tesi.control.FotoProdottoControllerImpl;
import com.example.tesi.control.ProdottoController;
import com.example.tesi.control.ProdottoControllerImpl;
import com.example.tesi.entity.FotoByteArray;
import com.example.tesi.entity.Prodotto;
import com.example.tesi.entity.entityoptions.Brand;
import com.example.tesi.entity.entityoptions.Categoria;
import com.example.tesi.entity.entityoptions.Condizioni;
import com.example.tesi.entity.entityoptions.Option;
import com.example.tesi.utils.Session;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.List;

public class AddProdottoActivity extends AppCompatActivity {
	private ActivityResultLauncher<Intent> scegliImmaginiLauncher;
	private List<FotoByteArray> foto;
	private LinearLayout containerFoto;
	private LinearLayout sceltaPrezzo;
	private EditText formTitolo, formDescrizione, formPrezzo;
	private TextView contatoreTitolo, contatoreDescrizione;
	private RadioGroup opzioniCategoria, opzioniBrand, opzioniCondizioni;
	private ProdottoController prodottoController;
	private FotoProdottoController fotoProdottoController;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_prodotto_layout);

		prodottoController=new ProdottoControllerImpl();
		fotoProdottoController=new FotoProdottoControllerImpl();

		foto=new LinkedList<>();
		containerFoto=findViewById(R.id.containerFoto);
		formTitolo=findViewById(R.id.formTitolo);
		formDescrizione=findViewById(R.id.formDescrizione);
		contatoreTitolo=findViewById(R.id.contatoreTitolo);
		contatoreDescrizione=findViewById(R.id.contatoreDescrizione);
		LinearLayout sceltaCategoria=findViewById(R.id.sceltaCategoria);
		LinearLayout sceltaBrand=findViewById(R.id.sceltaBrand);
		LinearLayout sceltaCondizioni=findViewById(R.id.sceltaCondizioni);
		opzioniCategoria=findViewById(R.id.opzioniCategoria);
		opzioniBrand=findViewById(R.id.opzioniBrand);
		opzioniCondizioni=findViewById(R.id.opzioniCondizioni);
		sceltaPrezzo=findViewById(R.id.sceltaPrezzo);
		formPrezzo=findViewById(R.id.formPrezzo);
		Button cancelButton=findViewById(R.id.cancelButton);

		createLauncher();

		formTitolo.addTextChangedListener(createContatoreEditTextListener(contatoreTitolo));
		formDescrizione.addTextChangedListener(createContatoreEditTextListener(contatoreDescrizione));


		createScelte(sceltaCategoria, opzioniCategoria, Categoria.values());
		createScelte(sceltaBrand, opzioniBrand, Brand.values());
		createScelte(sceltaCondizioni, opzioniCondizioni, Condizioni.values());

		Button buttonCaricaFoto=findViewById(R.id.buttonCaricaFoto);
		buttonCaricaFoto.setOnClickListener(l -> scegliImmaginiLauncher.launch(new Intent()));

		createSceltaPrezzo();

		Button uploadButton=findViewById(R.id.uploadButton);
		upload(uploadButton);

		cancelButton.setOnClickListener(l->{
			getOnBackPressedDispatcher().onBackPressed();
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

		ActivityResultCallback<ClipData> callback= clipData -> {
			if (clipData!=null) {
				List<FotoByteArray> list=new LinkedList<>();
				ByteArrayOutputStream stream=new ByteArrayOutputStream();
				for (int i = 0; i < clipData.getItemCount(); i++) {
					try {
						Bitmap b=BitmapFactory.decodeStream(getContentResolver().openInputStream(clipData.getItemAt(i).getUri()));

						b=Bitmap.createScaledBitmap(b, b.getWidth()/2,b.getHeight()/2,false);

						b.compress(Bitmap.CompressFormat.PNG, 100, stream);

						list.add(new FotoByteArray(stream.toByteArray()));
					} catch (FileNotFoundException e) {
						throw new RuntimeException(e);
					}
				}
				foto.addAll(list);
				containerFoto.removeAllViews();
				for (FotoByteArray b:foto) {
					ImageView image=new ImageView(AddProdottoActivity.this);
					image.setImageBitmap(BitmapFactory.decodeByteArray(b.getValue(), 0, b.getValue().length));
					image.setLayoutParams(new LinearLayout.LayoutParams(
							500,500
					));
					containerFoto.addView(image);
				}
			}

		};

		scegliImmaginiLauncher=registerForActivityResult(contract, callback);
	}

	private TextWatcher createContatoreEditTextListener(TextView contatore) {
		return new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				contatore.setText("Caratteri: "+s.length());
			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		};
	}
//	private void createContatoreDescrizioneListener() {
//		formDescrizione.addTextChangedListener(new TextWatcher() {
//			@Override
//			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//			}
//
//			@Override
//			public void onTextChanged(CharSequence s, int start, int before, int count) {
//				contatoreDescrizione.setText("Caratteri: "+s.length());
//			}
//
//			@Override
//			public void afterTextChanged(Editable s) {
//
//			}
//		});
//	}
	
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
		TextView t=new TextView(AddProdottoActivity.this);
		t.setText(text);
		t.setTextSize(size);
		t.setPadding(leftPadding, topPadding, rightPadding, bottomPadding);

		return t;
	}

	private void createSceltaPrezzo() {
		TextView t=createTextView(">", 30, 0, 0, 20, 0);
		sceltaPrezzo.addView(t);

		formPrezzo.setSelection(1);
		formPrezzo.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				if (!s.toString().startsWith("€")) {
					formPrezzo.setText("€"+s);
					formPrezzo.setSelection(1);
				}
			}
		});

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
			RadioButton rbCategoria, rbBrand, rbCondizioni;
			rbCategoria=findViewById(opzioniCategoria.getCheckedRadioButtonId());
			rbBrand=findViewById(opzioniBrand.getCheckedRadioButtonId());
			rbCondizioni=findViewById(opzioniCondizioni.getCheckedRadioButtonId());

			String categoria=rbCategoria.getText()+"";
			String brand=rbBrand.getText()+"";
			String condizioni=rbCondizioni.getText()+"";

			categoria=categoria.toUpperCase().replace(" ", "_");
			brand=brand.toUpperCase().replace(" ", "_");
			condizioni=condizioni.toUpperCase().replace(" ", "_");

			//ottengo prezzo
			String s=formPrezzo.getText().toString().replace("€", "");
			double prezzo= Double.parseDouble(s);


			Prodotto prodotto=new Prodotto(Session.getInstance(this).getCurrentUser().getId(), titolo, descrizione, Categoria.valueOf(categoria), Brand.valueOf(brand), Condizioni.valueOf(condizioni), prezzo);

			Prodotto response=prodottoController.add(prodotto);
			foto.forEach(f->f.setProdotto(response));
			fotoProdottoController.add(foto);

			goToHome();
		});
	}

	private void goToHome() {
		Intent i=new Intent(this, MainActivity.class);
		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
		startActivity(i);
	}
}
