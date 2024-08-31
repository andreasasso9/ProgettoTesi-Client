package com.example.tesi.client.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.ContentLoadingProgressBar;

import com.example.tesi.client.control.UserController;
import com.example.tesi.client.control.UserControllerImpl;
import com.example.tesi.client.utils.CheckNotEmptyStrings;
import com.example.tesi.client.R;
import com.example.tesi.client.utils.EmailRegex;
import com.example.tesi.client.utils.PasswordEditText;
import com.example.tesi.client.utils.Session;

public class SignupActivity extends AppCompatActivity {
	private EditText signupEmail, signupUsername, signupIndirizzo;
	private PasswordEditText signupPassword, signupConfPassword;
	private Button signupButton;
	private TextView toLogin, errorMessage;
	private UserController userController;
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.signup_layout);

		userController=UserControllerImpl.getInstance();

		signupEmail=findViewById(R.id.signupEmail);
		signupUsername=findViewById(R.id.signupUsername);
		signupIndirizzo=findViewById(R.id.signupIndirizzo);
		signupPassword=findViewById(R.id.signupPassword);
		signupConfPassword=findViewById(R.id.signupConfPassword);
		signupButton=findViewById(R.id.signupButton);
		toLogin=findViewById(R.id.toLogin);
		errorMessage =findViewById(R.id.signupErrorMessage);


		createToLoginListener();
		createSignupListener();


	}

	private void createSignupListener() {
		signupButton.setOnClickListener(v -> {
			ContentLoadingProgressBar progressBar=findViewById(R.id.progressBar);
			progressBar.setVisibility(View.VISIBLE);

			boolean valid=true;

			View currentFocusedView=getCurrentFocus();
			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			if (currentFocusedView!=null)
				imm.hideSoftInputFromWindow(currentFocusedView.getWindowToken(), 0);

			String email=signupEmail.getText()+"";
			String username=signupUsername.getText()+"";
			String indirizzo=signupIndirizzo.getText()+"";
			String password=signupPassword.getText()+"";
			String confPassword=signupConfPassword.getText()+"";

			boolean areNotEmpty=CheckNotEmptyStrings.check(email, username, indirizzo, password, confPassword);

			if (!areNotEmpty) {
				progressBar.setVisibility(View.GONE);

				errorMessage.setVisibility(View.VISIBLE);
				errorMessage.setText("Riempi tutti i campi");
				valid=false;
			}

			if (!EmailRegex.match(email)) {
				progressBar.setVisibility(View.GONE);

				errorMessage.setVisibility(View.VISIBLE);
				errorMessage.setText("Email non valida");
				valid=false;
			}

			if (!password.equals(confPassword)) {
				progressBar.setVisibility(View.GONE);

				errorMessage.setVisibility(View.VISIBLE);
				errorMessage.setText("Inserisci la stessa password");
				valid=false;
			}

			if (valid) {
				new Handler(Looper.getMainLooper()).postDelayed(()->{
					boolean result = userController.saveUser(email, username, password, indirizzo);
					if (!result) {
						progressBar.setVisibility(View.GONE);

						errorMessage.setVisibility(View.VISIBLE);
						errorMessage.setText("E-mail o username già esistenti");
					}

					Session.getInstance(this).setCurrentUser(null, this);
					goToLogin();
				},100);

			}
		});

		TextWatcher textWatcher=new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				errorMessage.setVisibility(View.GONE);
			}

			@Override
			public void afterTextChanged(Editable s) {}
		};
		signupEmail.addTextChangedListener(textWatcher);
		signupUsername.addTextChangedListener(textWatcher);
		signupIndirizzo.addTextChangedListener(textWatcher);
		signupPassword.addTextChangedListener(textWatcher);
		signupConfPassword.addTextChangedListener(textWatcher);

	}



	private void createToLoginListener() {
		toLogin.setOnClickListener(v -> goToLogin());
	}

	private void goToLogin() {
		Intent i=new Intent(SignupActivity.this, LoginActivity.class);
		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
		startActivity(i);
	}
}
