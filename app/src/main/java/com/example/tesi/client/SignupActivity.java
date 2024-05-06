package com.example.tesi.client;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SignupActivity extends AppCompatActivity {
	private EditText signupEmail;
	private EditText signupUsername;
	private EditText signupIndirizzo;
	private EditText signupPassword;
	private EditText signupConfPassword;
	private Button signupButton;
	private TextView toLogin;
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.signup_layout);

		signupEmail=findViewById(R.id.signupEmail);
		signupUsername=findViewById(R.id.signupUsername);
		signupIndirizzo=findViewById(R.id.signupIndirizzo);
		signupPassword=findViewById(R.id.signupPassword);
		signupConfPassword=findViewById(R.id.signupConfPassword);
		signupButton=findViewById(R.id.signupButton);
		toLogin=findViewById(R.id.toLogin);

		createToLoginListener();
	}

	private void createToLoginListener() {
		toLogin.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i=new Intent(SignupActivity.this, LoginActivity.class);
				startActivity(i);
			}
		});
	}
}
