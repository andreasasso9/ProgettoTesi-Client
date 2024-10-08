package com.tesi.client.activities;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
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

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.tesi.client.R;
import com.tesi.client.control.FotoProdottoController;
import com.tesi.client.control.FotoProdottoControllerImpl;
import com.tesi.client.control.ProdottoController;
import com.tesi.client.control.ProdottoControllerImpl;
import com.tesi.entity.FotoByteArray;
import com.tesi.entity.Prodotto;
import com.tesi.client.utils.Session;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class FormProdottoActivity extends AppCompatActivity {
	private ActivityResultLauncher<Intent> scegliImmaginiLauncher;
	private List<FotoByteArray> foto;
	private LinearLayout containerFoto;
	private LinearLayout sceltaPrezzo;
	private EditText formTitolo, formDescrizione, formPrezzo, altriBrand, altreCategorie;
	private RadioGroup opzioniCategoria, opzioniBrand, opzioniCondizioni;
	private ProdottoController prodottoController;
	private FotoProdottoController fotoProdottoController;
	private Button buttonCaricaFoto;
	private Map<String, TextView> textViews;
	private ViewGroup mainContainer;

	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.form_prodotto_layout);

		Intent i=getIntent();
		Prodotto prodotto;
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU)
			prodotto=i.getSerializableExtra("prodotto", Prodotto.class);
		else
			prodotto= (Prodotto) i.getSerializableExtra("prodotto");

		ViewGroup mainContainer= findViewById(R.id.mainContainer);
		textViews=new HashMap<>();
		for (int x=1; x<7; x++) {
			TextView t=mainContainer.findViewWithTag("nameForm"+x);
			textViews.put(t.getText().toString().toLowerCase(), t);
		}

		prodottoController = ProdottoControllerImpl.getInstance();
		fotoProdottoController = FotoProdottoControllerImpl.getInstance();

		foto = new LinkedList<>();
		containerFoto = findViewById(R.id.containerFoto);
		formTitolo = findViewById(R.id.formTitolo);
		formDescrizione = findViewById(R.id.formDescrizione);
		TextView contatoreTitolo = findViewById(R.id.contatoreTitolo);
		TextView contatoreDescrizione = findViewById(R.id.contatoreDescrizione);
		LinearLayout sceltaCategoria = findViewById(R.id.sceltaCategoria);
		LinearLayout sceltaBrand = findViewById(R.id.sceltaBrand);
		LinearLayout sceltaCondizioni = findViewById(R.id.sceltaCondizioni);
		opzioniCategoria = findViewById(R.id.opzioniCategoria);
		opzioniBrand = findViewById(R.id.opzioniBrand);
		opzioniCondizioni = findViewById(R.id.opzioniCondizioni);
		sceltaPrezzo = findViewById(R.id.sceltaPrezzo);
		formPrezzo = findViewById(R.id.formPrezzo);
		ImageButton cancelButton = findViewById(R.id.cancelButton);
		TextView content = findViewById(R.id.content);
		altriBrand = findViewById(R.id.altriBrand);
		altreCategorie = findViewById(R.id.altreCategorie);

		createAddFotoLauncher();

		formTitolo.addTextChangedListener(createContatoreEditTextListener(contatoreTitolo, textViews.get("titolo")));
		formDescrizione.addTextChangedListener(createContatoreEditTextListener(contatoreDescrizione, textViews.get("descrivi il tuo articolo")));

		createScelte(sceltaCategoria, opzioniCategoria, prodotto, altreCategorie, textViews.get("categoria"), Prodotto.categorie);
		createScelte(sceltaBrand, opzioniBrand, prodotto, altriBrand, textViews.get("brand"), Prodotto.brands);
		createScelte(sceltaCondizioni, opzioniCondizioni, prodotto, null, textViews.get("condizioni"), Prodotto.condizioni);

		buttonCaricaFoto = findViewById(R.id.buttonCaricaFoto);
		buttonCaricaFoto.setOnClickListener(l -> {
			buttonCaricaFoto.setBackgroundColor(getResources().getColor(R.color.blue, null));
			scegliImmaginiLauncher.launch(new Intent());
		});

		createSceltaPrezzo();

		Button uploadButton = findViewById(R.id.uploadButton);

		cancelButton.setOnClickListener(l -> getOnBackPressedDispatcher().onBackPressed());
		if (prodotto==null) {
			upload(uploadButton);
			content.setText("Aggiungi un articolo");
			uploadButton.setText("Carica");
		} else {
			content.setText("Aggiorna un prodotto");
			uploadButton.setText("Aggiorna");

			formTitolo.setText(prodotto.getTitolo());
			formDescrizione.setText(prodotto.getDescrizione());
			formPrezzo.setText(String.valueOf(prodotto.getPrezzo()));

			setScelte(opzioniBrand, altriBrand, prodotto.getBrand(), Prodotto.brands);
			setScelte(opzioniCategoria, altreCategorie, prodotto.getCategoria(), Prodotto.categorie);

			new Thread(()->{
				foto=fotoProdottoController.findByProdotto(prodotto.getId());

				runOnUiThread(this::createImageViewer);

			}).start();

			update(uploadButton, prodotto);
		}
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
				for (int i = 0; i < clipData.getItemCount(); i++) {
					try (ByteArrayOutputStream stream=new ByteArrayOutputStream()){
						Bitmap b=BitmapFactory.decodeStream(getContentResolver().openInputStream(clipData.getItemAt(i).getUri()));

						b=Bitmap.createScaledBitmap(b, b.getWidth()/2,b.getHeight()/2,false);

						b.compress(Bitmap.CompressFormat.WEBP, 50, stream);

						foto.add(new FotoByteArray(stream.toByteArray()));
					} catch (IOException e) {
						throw new RuntimeException(e);
					}
				};
				createImageViewer();
			}

		};

		scegliImmaginiLauncher=registerForActivityResult(contract, callback);
	}

	@NonNull
	private ImageButton getImageButton() {
		ImageButton cancel=new ImageButton(this);
		cancel.setImageResource(R.drawable.icons8_cestino_64__1_);
		cancel.setBackgroundColor(Color.TRANSPARENT);
		cancel.setVisibility(View.GONE);

		cancel.setScaleType(ImageView.ScaleType.FIT_CENTER);
		return cancel;
	}

	private TextWatcher createContatoreEditTextListener(TextView contatore, TextView t) {
		return new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				t.setTextColor(getResources().getColor(R.color.black, null));
				contatore.setText("Caratteri: "+s.length());
			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		};
	}
	
	private void createScelte(LinearLayout layout, RadioGroup optionsGroup, Prodotto p, EditText altro, TextView textView, String... opzioni) {
		TextView t=createTextView();

		layout.addView(t);

		optionsGroup.setOnCheckedChangeListener((group, checkedId) -> textView.setTextColor(getResources().getColor(R.color.black, null)));

		for (String o:opzioni) {
			RadioButton rb=new RadioButton(FormProdottoActivity.this);
			rb.setText(o);
			rb.setTextColor(getResources().getColor(R.color.black, null));
			optionsGroup.addView(rb);
			if (o.equalsIgnoreCase("altro"))
				rb.setOnCheckedChangeListener((buttonView, isChecked) -> {
					if (isChecked)
						altro.setVisibility(View.VISIBLE);
					else
						altro.setVisibility(View.GONE);

				});
			if (p!=null) {
				String condizioni, brand, categoria;
				condizioni=p.getCondizione();
				brand=p.getBrand();
				categoria=p.getCategoria();

				if (o.equalsIgnoreCase(condizioni) || o.equalsIgnoreCase(brand) || o.equalsIgnoreCase(categoria))
					rb.setChecked(true);
			}
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
		TextView t=new TextView(FormProdottoActivity.this);
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
				textViews.get("prezzo").setTextColor(getResources().getColor(R.color.black, null));
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
			progressBar.show();

			Map<String, String> values=getValues();

			if (isValid(values)) {
				double prezzo=Double.parseDouble(values.get("prezzo"));
				Prodotto prodotto = new Prodotto(Session.getInstance(this).getCurrentUser().getUsername(), values.get("titolo"), values.get("descrizione"), values.get("categoria"), values.get("brand"), values.get("condizioni"), prezzo);

				new Thread(() -> {
					Prodotto response = prodottoController.add(prodotto);
					foto.forEach(f -> f.setIdProdotto(response.getId()));
					fotoProdottoController.add(foto);

					goToHome();
				}).start();
			} else {
				progressBar.hide();
				Snackbar.make(this, containerFoto, "Inserisci tutti i campi", BaseTransientBottomBar.LENGTH_SHORT).show();
			}
		});
	}

	private void update(Button button, Prodotto prodotto) {
		button.setOnClickListener(v->{
			ContentLoadingProgressBar progressBar=findViewById(R.id.progressBar);
			progressBar.show();

			Map<String,String> values=getValues();

			if (isValid(values)) {
				prodotto.setTitolo(values.get("titolo"));
				prodotto.setDescrizione(values.get("descrizione"));
				prodotto.setPrezzo(Double.parseDouble(values.get("prezzo")));
				prodotto.setBrand(values.get("brand"));
				prodotto.setCategoria(values.get("categoria"));
				prodotto.setCondizione(values.get("condizioni"));

				new Thread(() -> {
					prodottoController.update(prodotto.getId());
					foto.forEach(f -> f.setIdProdotto(prodotto.getId()));
					fotoProdottoController.add(foto);

					goToHome();
				}).start();
			} else {
				progressBar.hide();
				Snackbar.make(this, containerFoto, "Inserisci tutti i campi", BaseTransientBottomBar.LENGTH_SHORT).show();
			}
		});
	}

	private boolean isValid(Map<String, String> values) {
		boolean valid=true;

		String s;

		s=values.get("titolo");
		if (s==null || s.isEmpty()) {
			textViews.get("titolo").setTextColor(getResources().getColor(R.color.red, null));
			valid=false;
		}

		s=values.get("descrizione");
		if (s==null || s.isEmpty()) {
			textViews.get("descrivi il tuo articolo").setTextColor(getResources().getColor(R.color.red, null));
			valid=false;
		}

		s=values.get("categoria");
		if (s==null || s.isEmpty()) {
			textViews.get("categoria").setTextColor(getResources().getColor(R.color.red, null));
			valid=false;
		}

		s=values.get("brand");
		if (s==null || s.isEmpty()) {
			textViews.get("brand").setTextColor(getResources().getColor(R.color.red, null));
			valid=false;
		}

		s=values.get("condizioni");
		if (s==null || s.isEmpty()){
			textViews.get("condizioni").setTextColor(getResources().getColor(R.color.red, null));
			valid=false;
		}

		if (foto==null || foto.isEmpty()) {
			buttonCaricaFoto.setBackgroundColor(getResources().getColor(R.color.red, null));
			valid=false;
		}

		double prezzo=0;
		s=values.get("prezzo");
		if (s == null || s.isEmpty()) {
			textViews.get("prezzo").setTextColor(getResources().getColor(R.color.red, null));
			valid=false;
		}

		return valid;
	}

	private Map<String, String> getValues() {
		//ottengo titolo e descrizione
		String titolo=String.valueOf(formTitolo.getText()).trim();
		String descrizione=String.valueOf(formDescrizione.getText()).trim();

		//ottengo categoria, brand e condizione
		RadioButton rbCategoria, rbBrand, rbCondizioni;

		rbCategoria=findViewById(opzioniCategoria.getCheckedRadioButtonId());
		rbBrand=findViewById(opzioniBrand.getCheckedRadioButtonId());
		rbCondizioni=findViewById(opzioniCondizioni.getCheckedRadioButtonId());

		String categoria = null;
		String brand = null;
		String condizioni = null;

		if (rbCategoria != null)
			if (String.valueOf(rbCategoria.getText()).equalsIgnoreCase("altro"))
				categoria=String.valueOf(altreCategorie.getText()).trim();
			else
				categoria=String.valueOf(rbCategoria.getText()).trim();

		if (rbBrand != null)
			if (String.valueOf(rbBrand.getText()).equalsIgnoreCase("altro"))
				brand= String.valueOf(altriBrand.getText()).trim();
			else
				brand=String.valueOf(rbBrand.getText()).trim();

		if (rbCondizioni != null)
			condizioni=String.valueOf(rbCondizioni.getText()).trim();

		//ottengo prezzo
		String prezzo=String.valueOf(formPrezzo.getText()).replace("€", "");

		Map<String, String> values=new HashMap<>();
		values.put("titolo", titolo);
		values.put("descrizione", descrizione);
		values.put("categoria", categoria);
		values.put("brand", brand);
		values.put("condizioni", condizioni);
		values.put("prezzo", prezzo);

		return values;
	}

	private void createImageViewer() {
		containerFoto.removeAllViews();
		for (FotoByteArray b:foto) {
			try (ByteArrayOutputStream stream=new ByteArrayOutputStream()) {
				RelativeLayout container = new RelativeLayout(this);
				LinearLayout.LayoutParams containerParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
				container.setLayoutParams(containerParams);

				ImageButton cancel = getImageButton();

				ImageView image = new ImageView(this);
				Bitmap bitmap = BitmapFactory.decodeByteArray(b.getValue(), 0, b.getValue().length);
				bitmap.compress(Bitmap.CompressFormat.WEBP, 50, stream);
				image.setImageBitmap(BitmapFactory.decodeByteArray(stream.toByteArray(), 0, stream.size()));

				container.addView(image);
				container.addView(cancel);

				RelativeLayout.LayoutParams imageParams = new RelativeLayout.LayoutParams(500, 500);
				imageParams.addRule(RelativeLayout.CENTER_IN_PARENT);

				RelativeLayout.LayoutParams cancelParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
				cancelParams.addRule(RelativeLayout.CENTER_IN_PARENT);

				cancel.setLayoutParams(cancelParams);
				image.setLayoutParams(imageParams);

				containerFoto.addView(container);
				image.setOnClickListener(v -> {
					for (int y = 0; y < containerFoto.getChildCount(); y++) {
						RelativeLayout x = (RelativeLayout) containerFoto.getChildAt(y);
						x.getChildAt(1).setVisibility(View.GONE);
					}
					cancel.setVisibility(View.VISIBLE);
				});

				cancel.setOnClickListener(v -> {
					containerFoto.removeView(container);
					foto.remove(b);
				});
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	}

	private void setScelte(RadioGroup group, EditText editText, String option, String... options) {
		RadioButton rb;
		if (Arrays.stream(options).anyMatch(s->s.equalsIgnoreCase(option))) {
			for (int i = 0; i < group.getChildCount(); i++) {
				rb = (RadioButton) group.getChildAt(i);
				if (rb.getText().toString().equalsIgnoreCase(option))
					rb.setChecked(true);

			}
		} else {
			rb = (RadioButton) group.getChildAt(group.getChildCount() - 1);
			rb.setChecked(true);
			editText.setVisibility(View.VISIBLE);
			editText.setText(option);
		}
	}

	private void goToHome() {
		Intent i=new Intent(this, MainActivity.class);
		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
		startActivity(i);
	}
}
