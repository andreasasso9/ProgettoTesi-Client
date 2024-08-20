package com.example.tesi.client.activities;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.ContentLoadingProgressBar;

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
import com.example.tesi.client.utils.Session;

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
		TextView contatoreTitolo = findViewById(R.id.contatoreTitolo);
		TextView contatoreDescrizione = findViewById(R.id.contatoreDescrizione);
		LinearLayout sceltaCategoria=findViewById(R.id.sceltaCategoria);
		LinearLayout sceltaBrand=findViewById(R.id.sceltaBrand);
		LinearLayout sceltaCondizioni=findViewById(R.id.sceltaCondizioni);
		opzioniCategoria=findViewById(R.id.opzioniCategoria);
		opzioniBrand=findViewById(R.id.opzioniBrand);
		opzioniCondizioni=findViewById(R.id.opzioniCondizioni);
		sceltaPrezzo=findViewById(R.id.sceltaPrezzo);
		formPrezzo=findViewById(R.id.formPrezzo);
		ImageButton cancelButton=findViewById(R.id.cancelButton);

		createAddFotoLauncher();

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

	private void createAddFotoLauncher() {
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

						b.compress(Bitmap.CompressFormat.WEBP, 50, stream);

						list.add(new FotoByteArray(stream.toByteArray()));
					} catch (FileNotFoundException e) {
						throw new RuntimeException(e);
					}
				}
				foto.addAll(list);
				containerFoto.removeAllViews();
				for (FotoByteArray b:foto) {
					RelativeLayout container=new RelativeLayout(this);
					LinearLayout.LayoutParams containerParams=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
					container.setLayoutParams(containerParams);

					ImageButton cancel = getImageButton(containerFoto, container/*, mainFoto*/);


					ImageView image=new ImageView(this);
					Bitmap bitmap=BitmapFactory.decodeByteArray(b.getValue(), 0, b.getValue().length);
					bitmap.compress(Bitmap.CompressFormat.WEBP, 50, stream);
					image.setImageBitmap(BitmapFactory.decodeByteArray(stream.toByteArray(), 0, stream.size()));

					container.addView(image);
					container.addView(cancel);

					RelativeLayout.LayoutParams imageParams=new RelativeLayout.LayoutParams(500,500);
					imageParams.addRule(RelativeLayout.CENTER_IN_PARENT);

					RelativeLayout.LayoutParams cancelParams=new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
					cancelParams.addRule(RelativeLayout.CENTER_IN_PARENT);

					cancel.setLayoutParams(cancelParams);
					image.setLayoutParams(imageParams);

					containerFoto.addView(container);
					image.setOnClickListener(v->{
						for (int i=0; i<containerFoto.getChildCount(); i++) {
							RelativeLayout x=(RelativeLayout) containerFoto.getChildAt(i);
							x.getChildAt(1).setVisibility(View.GONE);
						}
						cancel.setVisibility(View.VISIBLE);
					});

					cancel.setOnClickListener(v->{
						containerFoto.removeView(container);
						foto.remove(b);
						if (containerFoto.getChildCount()>0) {
							RelativeLayout x= (RelativeLayout) containerFoto.getChildAt(0);
							x.getChildAt(1).setVisibility(View.VISIBLE);
						}
					});
				}
			}

		};

		scegliImmaginiLauncher=registerForActivityResult(contract, callback);
	}

	@NonNull
	private ImageButton getImageButton(LinearLayout containerFoto, RelativeLayout container/*, ImageView mainFoto*/) {
		ImageButton cancel=new ImageButton(this);
		cancel.setImageResource(R.drawable.icons8_cestino_64__1_);
		cancel.setBackgroundColor(Color.TRANSPARENT);
		cancel.setVisibility(View.GONE);

		cancel.setScaleType(ImageView.ScaleType.FIT_CENTER);
		return cancel;
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
	
	private void createScelte(LinearLayout layout, RadioGroup optionsGroup, Option... opzioni) {
		TextView t=createTextView();

		layout.addView(t);

		for (Option o:opzioni) {
			RadioButton rb=new RadioButton(AddProdottoActivity.this);
			rb.setText(o.getNome());
			rb.setTextColor(getResources().getColor(R.color.black, null));
			optionsGroup.addView(rb);
		}
		layout.setOnClickListener(v -> {
			if (t.getRotation()==0) {
				t.setRotation(90);
				optionsGroup.setVisibility(View.VISIBLE);
			} else {
				t.setRotation(0);
				optionsGroup.setVisibility(View.GONE);
			}
		});
	}

	private TextView createTextView() {
		TextView t=new TextView(AddProdottoActivity.this);
		t.setText(">");
		t.setTextSize(30);
		t.setPadding(0, 0, 20, 0);
		t.setTextColor(getResources().getColor(R.color.black, null));

		return t;
	}

	private void createSceltaPrezzo() {
		TextView t=createTextView();
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
		b.setOnClickListener(l->{
			ContentLoadingProgressBar progressBar=findViewById(R.id.progressBar);
			progressBar.setVisibility(View.VISIBLE);

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

			Prodotto prodotto=new Prodotto(Session.getInstance(this).getCurrentUser().getUsername(), titolo, descrizione, Categoria.valueOf(categoria), Brand.valueOf(brand), Condizioni.valueOf(condizioni), prezzo);

			new Handler(Looper.getMainLooper()).postDelayed(()->{
				Prodotto response=prodottoController.add(prodotto);
				foto.forEach(f->f.setProdotto(response));
				fotoProdottoController.add(foto);
				goToHome();
			}, 100);



		});
	}

	private void goToHome() {
		Intent i=new Intent(this, MainActivity.class);
		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
		startActivity(i);
	}
}
