package com.example.tesi.client.activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.widget.ContentLoadingProgressBar;

import com.example.tesi.client.R;
import com.example.tesi.control.FotoProdottoControllerImpl;
import com.example.tesi.control.NotificheControllerImpl;
import com.example.tesi.control.ProdottoController;
import com.example.tesi.control.ProdottoControllerImpl;
import com.example.tesi.entity.FotoByteArray;
import com.example.tesi.entity.Notifica;
import com.example.tesi.entity.Prodotto;
import com.example.tesi.entity.User;
import com.example.tesi.client.utils.CheckNotEmptyStrings;
import com.example.tesi.client.utils.Session;

import java.util.Calendar;

public class AcquistaActivity extends AppCompatActivity {
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pagamento_layout);

		EditText numero, titolare, scadenza, cvv;
		Button paga;

		numero=findViewById(R.id.numeroCarta);
		titolare=findViewById(R.id.titolare);
		scadenza=findViewById(R.id.scadenza);
		cvv=findViewById(R.id.cvv);
		paga=findViewById(R.id.paga);

		createNumeroTextWatcher(numero);
		createScadenzaTextWatcher(scadenza);
		createTitolareTextWatcher(titolare);

		paga(paga, numero, titolare, scadenza, cvv);
	}

	@Override
	protected void onPause() {
		super.onPause();
		GradientDrawable drawable = (GradientDrawable) ContextCompat.getDrawable(this, R.drawable.shape_edittext);
		assert drawable != null;
		drawable.setStroke(8, Color.BLACK);
	}

	private void paga(Button paga, EditText numero, EditText titolare, EditText scadenza, EditText cvv) {
		paga.setOnClickListener(l->{
			ContentLoadingProgressBar progressBar=findViewById(R.id.progressBar);
			progressBar.setVisibility(View.VISIBLE);

			String numeroString=numero.getText()+"";
			String titolareString=titolare.getText()+"";
			String scadenzaString=scadenza.getText()+"";
			String cvvString=cvv.getText()+"";

			boolean valid=true;

			GradientDrawable drawable = (GradientDrawable) ContextCompat.getDrawable(this, R.drawable.shape_edittext);
			assert drawable != null;
			drawable.setStroke(8, Color.RED);


			if (!CheckNotEmptyStrings.check(numeroString, titolareString, scadenzaString, cvvString)) {
				valid=false;
			}

			if (!titolareString.matches("^[A-Za-zÀ-ÖØ-öø-ÿ]+(([' -][A-Za-zÀ-ÖØ-öø-ÿ])?[A-Za-zÀ-ÖØ-öø-ÿ]*)*$")) {
				titolare.setTextColor(Color.RED);
				titolare.setBackground(drawable);

				valid = false;
			}

			if (scadenzaString.length()==5) {
				int year, month, currentMonth, currentYear;
				month = Integer.parseInt(scadenzaString.substring(0, 2));
				year = Integer.parseInt(scadenzaString.substring(3));

				currentMonth = Calendar.getInstance().get(Calendar.MONTH) + 1;
				currentYear = Calendar.getInstance().get(Calendar.YEAR) % 100;

				if (month<=0 || month>12 || year<currentYear || (year==currentYear && month<currentMonth)) {
					scadenza.setTextColor(Color.RED);
					scadenza.setBackground(drawable);
					scadenza.setHintTextColor(Color.RED);

					valid = false;
				}
			} else {
				scadenza.setTextColor(Color.RED);
				scadenza.setBackground(drawable);
				scadenza.setHintTextColor(Color.RED);

				valid = false;
			}

			if (valid) {
				new Handler(Looper.getMainLooper()).postDelayed(()->{
					Prodotto p;
					Intent i=getIntent();
					User currentUser= Session.getInstance(this).getCurrentUser();
					if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
						p=i.getSerializableExtra("prodotto", Prodotto.class);
					else
						p= (Prodotto) i.getSerializableExtra("prodotto");

					assert p != null;
					p.setCompratore(currentUser.getUsername());

					ProdottoController prodottoController=new ProdottoControllerImpl();
					prodottoController.update(p);
					FotoByteArray fotoNotifica=new FotoProdottoControllerImpl().findFirst(p);
					Notifica notifica=new Notifica(currentUser.getUsername(), p.getProprietario(), String.format("%s ha acquistato il tuo articolo %s", currentUser.getUsername(), p.getTitolo()), fotoNotifica.getValue());
					new NotificheControllerImpl().save(notifica);
					Toast.makeText(this, "Il pagamento è andato a buon fine", Toast.LENGTH_SHORT).show();

					Intent intent=new Intent(this, MainActivity.class);
					startActivity(intent);
				}, 100);

			} else
				progressBar.setVisibility(View.GONE);
		});
	}

	private void createNumeroTextWatcher(EditText numero) {
		numero.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				StringBuilder string= new StringBuilder();
				String original=s.toString().replaceAll("\\s", "");

				for (int i=0; i<original.length(); i++) {
					if (i>0 && i%4 == 0)
						string.append(" ");
					string.append(original.charAt(i));
				}

				numero.removeTextChangedListener(this);
				numero.setText(string);
				numero.setSelection(string.length());
				numero.addTextChangedListener(this);
			}
		});
	}

	private void createScadenzaTextWatcher(EditText scadenza) {
		scadenza.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				GradientDrawable drawable = (GradientDrawable) ContextCompat.getDrawable(scadenza.getContext(), R.drawable.shape_edittext);
				assert drawable != null;
				drawable.setStroke(8, Color.BLACK);

				scadenza.setBackground(drawable);
				scadenza.setTextColor(Color.BLACK);
				scadenza.setHintTextColor(Color.LTGRAY);
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				StringBuilder string=new StringBuilder();
				String original=s.toString().replaceAll("/", "");

				for (int i=0; i<original.length(); i++) {
					if (i==2)
						string.append("/");
					string.append(original.charAt(i));
				}


				scadenza.removeTextChangedListener(this);
				scadenza.setText(string);
				scadenza.setSelection(string.length());
				scadenza.addTextChangedListener(this);
			}
		});
	}

	private void createTitolareTextWatcher(EditText titolare) {
		titolare.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				GradientDrawable drawable = (GradientDrawable) ContextCompat.getDrawable(titolare.getContext(), R.drawable.shape_edittext);
				assert drawable != null;
				drawable.setStroke(8, Color.BLACK);

				titolare.setBackground(drawable);
				titolare.setTextColor(Color.BLACK);
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {

			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});
	}
}
