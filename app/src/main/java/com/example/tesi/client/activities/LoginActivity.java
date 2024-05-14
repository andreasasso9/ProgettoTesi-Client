package com.example.tesi.client.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tesi.client.R;
import com.example.tesi.control.UserController;
import com.example.tesi.control.UserControllerImpl;
import com.example.tesi.entity.User;

import retrofit2.Call;

public class LoginActivity extends AppCompatActivity {
	private Button loginButton;
	private EditText loginUsername, loginPassword;
	private TextView toSignup;
	private UserController userController;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_layout);

		userController=new UserControllerImpl();

		loginUsername = findViewById(R.id.loginUsername);
		loginPassword = findViewById(R.id.loginPassword);
		loginButton = findViewById(R.id.loginButton);
		toSignup=findViewById(R.id.toSignup);

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
			String username=loginUsername.getText()+"";
			String password=loginPassword.getText()+"";

			User user=userController.loginUser(username, password);

			if (user !=null) {
				Intent i=new Intent(this, MainActivity.class);
				i.putExtra("user", user);
				startActivity(i);
			}
		});
	}
}