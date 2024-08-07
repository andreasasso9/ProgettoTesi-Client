package com.example.tesi.client.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowMetrics;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.ContentLoadingProgressBar;

import com.example.tesi.client.R;
import com.example.tesi.control.UserController;
import com.example.tesi.control.UserControllerImpl;
import com.example.tesi.entity.User;
import com.example.tesi.client.utils.Session;

public class LoginActivity extends AppCompatActivity {
	private Button loginButton;
	private EditText loginUsername, loginPassword;
	private TextView toSignup, errorMessage;
	private UserController userController;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_layout);

		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
			WindowMetrics windowMetrics=getWindowManager().getCurrentWindowMetrics();
			Session.getInstance(this).setScreenHeight(windowMetrics.getBounds().height());
			Session.getInstance(this).setScreenWidth(windowMetrics.getBounds().width());
		} else {
			DisplayMetrics displayMetrics=new DisplayMetrics();
			getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
			Session.getInstance(this).setScreenWidth(displayMetrics.widthPixels);
			Session.getInstance(this).setScreenHeight(displayMetrics.heightPixels);
		}

		SharedPreferences preferences=getSharedPreferences(Session.SESSION_PREFERENCES, MODE_PRIVATE);
		String username=preferences.getString("username","");
		String password=preferences.getString("password", "");

		userController = new UserControllerImpl();

		if (!username.isEmpty() && !password.isEmpty()) {
			User user = userController.loginUser(username, password);

			if (user != null) {
				Intent i = new Intent(this, MainActivity.class);
				Session.getInstance(this).setCurrentUser(user, password);
				startActivity(i);
			}
		}

		loginUsername = findViewById(R.id.loginUsername);
		loginPassword = findViewById(R.id.loginPassword);
		loginButton = findViewById(R.id.loginButton);
		toSignup = findViewById(R.id.toSignup);
		errorMessage = findViewById(R.id.loginErrorMessage);

		createToSignupListener();

		createLoginListener();
	}

	private void createToSignupListener() {
		toSignup.setOnClickListener(l -> {
			Intent i=new Intent(LoginActivity.this, SignupActivity.class);
			i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
			startActivity(i);
		});
	}

	private void createLoginListener() {
		loginButton.setOnClickListener(l->{
			ContentLoadingProgressBar progressBar=findViewById(R.id.progressBar);

			progressBar.setVisibility(View.VISIBLE);

			InputMethodManager imm=(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			View currentFocusedView=getCurrentFocus();
			if (currentFocusedView!=null)
				imm.hideSoftInputFromWindow(currentFocusedView.getWindowToken(), 0);

			String username=loginUsername.getText()+"";
			String password=loginPassword.getText()+"";


			new Handler(Looper.getMainLooper()).postDelayed(()->{
				User user=userController.loginUser(username, password);


				if (user==null) {
					progressBar.setVisibility(View.GONE);

					errorMessage.setVisibility(View.VISIBLE);
					errorMessage.setText("Credenziali errate");
					return;
				}

				Intent i=new Intent(this, MainActivity.class);
				Session.getInstance(this).setCurrentUser(user, password);
				i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
				startActivity(i);
			}, 100);

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

		loginUsername.addTextChangedListener(textWatcher);
		loginPassword.addTextChangedListener(textWatcher);
	}

}