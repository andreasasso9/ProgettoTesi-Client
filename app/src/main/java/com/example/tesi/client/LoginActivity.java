package com.example.tesi.client;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
	private EditText loginUsername;
	private EditText loginPassword;
	private Button loginButton;
	private TextView toSignup;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_layout);

		loginUsername=findViewById(R.id.loginUsername);
		loginPassword=findViewById(R.id.loginPassword);
		loginButton=findViewById(R.id.loginButton);
		toSignup=findViewById(R.id.toSignup);

		createToSignupListener();
	}

	private void createToSignupListener() {
		toSignup.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i=new Intent(LoginActivity.this, SignupActivity.class);
				i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
				startActivity(i);
			}
		});
	}
}