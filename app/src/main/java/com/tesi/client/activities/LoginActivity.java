package com.tesi.client.activities;

import android.content.Context;
import android.content.Intent;
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

import com.tesi.client.R;
import com.tesi.client.utils.File;
import com.tesi.client.control.UserController;
import com.tesi.client.control.UserControllerImpl;
import com.tesi.client.utils.PasswordEditText;
import com.tesi.entity.User;
import com.tesi.client.utils.Session;

public class LoginActivity extends AppCompatActivity {
	private Button loginButton;
	private EditText loginUsername;
	private PasswordEditText loginPassword;
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

		User loggedUser= (User) File.readObjectFromFile(this, "loggedUser");
		userController = UserControllerImpl.getInstance();
		if (loggedUser!=null) {
			Session.getInstance(this).setCurrentUser(loggedUser, this);
			new Thread(()->{
				User user=userController.findById(loggedUser.getId());
				if (user!=null)
					Session.getInstance(this).setCurrentUser(user, this);

				Session.getInstance(this).createStompClient(this);

			}).start();

				Intent i = new Intent(this, MainActivity.class);
				i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
				startActivity(i);
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
				Session.getInstance(this).setCurrentUser(user, this);
				Session.getInstance(this).createStompClient(this);
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