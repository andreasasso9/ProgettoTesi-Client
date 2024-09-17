package com.tesi.client.activities;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.tesi.client.R;
import com.tesi.client.control.UserController;
import com.tesi.client.control.UserControllerImpl;
import com.tesi.client.utils.EmailRegex;
import com.tesi.client.utils.InfoTextView;
import com.tesi.client.utils.Session;
import com.tesi.entity.User;

public class InformazioniFragment extends Fragment {
	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View v=inflater.inflate(R.layout.informazioni_layout, container, false);

		//todo aggiungere metodo per caricare foto profilo
		ImageView foto=v.findViewById(R.id.foto);

		InfoTextView email, indirizzo;
		TextView username=v.findViewById(R.id.username);
		email=v.findViewById(R.id.email);
		indirizzo=v.findViewById(R.id.indirizzo);

		User currentUser= Session.getInstance(requireContext()).getCurrentUser();

		username.setText(currentUser.getUsername());
		email.setText(currentUser.getEmail());
		indirizzo.setText(currentUser.getIndirizzo());

		createUpdateInfoListener(email, v);
		createUpdateInfoListener(indirizzo, v);

		return v;
	}

	private void createUpdateInfoListener(InfoTextView textView, View view) {
		textView.setOnTouchListener((v, event) -> {
			if (event.getAction() == MotionEvent.ACTION_DOWN && event.getRawX() >= (textView.getRight()-textView.getCompoundDrawables()[2].getBounds().width())) {//controllo se il tocco sul text view è sul drawable
				int id=textView.getId();
				EditText editText;
				Button update;
				int idUpdate=0, idEditText=0;
				if (id == R.id.email) {
					idEditText=R.id.editEmail;
					idUpdate=R.id.updateEmailButton;
				} else if (id == R.id.indirizzo) {
					idEditText=R.id.editIndirizzo;
					idUpdate=R.id.updateIndirizzoButton;
				}
				update=view.findViewById(idUpdate);
				editText=view.findViewById(idEditText);

				editText.addTextChangedListener(new TextWatcher() {
					@Override
					public void beforeTextChanged(CharSequence s, int start, int count, int after) {

					}

					@Override
					public void onTextChanged(CharSequence s, int start, int before, int count) {
						update.setBackgroundColor(getResources().getColor(R.color.blue, null));
					}

					@Override
					public void afterTextChanged(Editable s) {

					}
				});

				createUpdateButtonListener(update, editText);

				ViewGroup parent = (ViewGroup) editText.getParent();
				if (parent.getVisibility() == View.GONE)
					parent.setVisibility(View.VISIBLE);
				else
					parent.setVisibility(View.GONE);
			}
			return textView.performClick();
		});
	}

	private void createUpdateButtonListener(Button button, EditText editText) {
		button.setOnClickListener(v->{
			int id=editText.getId();
			String text=editText.getText().toString();
			User currentUser=Session.getInstance(requireContext()).getCurrentUser();
			UserController userController=UserControllerImpl.getInstance();

			if (!text.isEmpty()) {
				if (id == R.id.editEmail) {
					if (EmailRegex.match(text)) {
						boolean result=userController.checkEmail(text);
						if (result)
							currentUser.setEmail(text);
						else {
							Snackbar.make(requireContext(), button, "Email già esistente", BaseTransientBottomBar.LENGTH_SHORT).show();
							return;
						}
					} else {
						Snackbar.make(requireContext(), button, "Inserisci una email valida", BaseTransientBottomBar.LENGTH_SHORT).show();
						button.setBackgroundColor(getResources().getColor(R.color.red, null));
						return;
					}
				} else if (id == R.id.editIndirizzo) {
					currentUser.setIndirizzo(text);
				}

				userController.update(currentUser);
				Snackbar.make(requireContext(), button, "Aggiornamento effettuato con successo", BaseTransientBottomBar.LENGTH_SHORT).show();
			}
		});
	}
}
