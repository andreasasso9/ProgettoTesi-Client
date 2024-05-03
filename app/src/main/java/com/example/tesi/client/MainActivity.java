package com.example.tesi.client;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
	private boolean loggedIn;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		loggedIn=false;
		super.onCreate(savedInstanceState);
		if (loggedIn)
			setContentView(R.layout.activity_main);
		else
			setContentView(R.layout.login_layout);



	}
}