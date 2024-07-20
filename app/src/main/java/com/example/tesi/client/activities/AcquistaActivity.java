package com.example.tesi.client.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tesi.client.R;

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
	}
}
