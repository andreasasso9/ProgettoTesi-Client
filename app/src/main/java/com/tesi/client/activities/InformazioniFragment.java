package com.tesi.client.activities;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
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

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

public class InformazioniFragment extends Fragment {

	private ActivityResultLauncher<Intent> scegliFotoLauncher;
	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View v=inflater.inflate(R.layout.informazioni_layout, container, false);

		ImageView foto=v.findViewById(R.id.foto);
		User currentUser=Session.getInstance(requireContext()).getCurrentUser();
		byte[] fotoUser=currentUser.getFoto();

		SwitchCompat buttonFoto=v.findViewById(R.id.buttonFoto);

		if (fotoUser != null) {
			buttonFoto.setChecked(true);
			buttonFoto.setText("Elimina");
			buttonFoto.setBackgroundColor(getResources().getColor(R.color.red, null));
			byte[] fotoDecoded=Base64.getDecoder().decode(fotoUser);
			Bitmap b=BitmapFactory.decodeByteArray(fotoDecoded, 0, fotoDecoded.length);
			foto.setImageBitmap(b);
			foto.setBackgroundColor(Color.TRANSPARENT);
		} else {
			buttonFoto.setChecked(false);
			buttonFoto.setText("Carica foto profilo");
			buttonFoto.setBackgroundColor(getResources().getColor(R.color.blue, null));
		}

		buttonFoto.setOnCheckedChangeListener((buttonView, isChecked) -> {
			if (isChecked) {
				scegliFotoLauncher.launch(new Intent());
				foto.setBackgroundColor(Color.TRANSPARENT);

				buttonView.setText("Elimina");
				buttonView.setBackgroundColor(getResources().getColor(R.color.red, null));

			} else {
				foto.setImageBitmap(null);
				foto.setBackgroundColor(getResources().getColor(R.color.gray, null));
				foto.setImageResource(R.drawable.profilo_unselected);
				currentUser.setFoto(null);
				UserControllerImpl.getInstance().update(currentUser);

				buttonView.setText("Carica foto profilo");
				buttonView.setBackgroundColor(getResources().getColor(R.color.blue, null));
			}
		});

		createAddFoto(foto);

		InfoTextView email, indirizzo;
		TextView username=v.findViewById(R.id.username);
		email=v.findViewById(R.id.email);
		indirizzo=v.findViewById(R.id.indirizzo);

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

	private void createAddFoto(ImageView imageView) {
		ActivityResultContract<Intent, ClipData> contract=new ActivityResultContract<Intent, ClipData>() {
			@NonNull
			@Override
			public Intent createIntent(@NonNull Context context, Intent intent) {
				intent.setAction(Intent.ACTION_GET_CONTENT);
				intent.setType("image/*");
				intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false);

				return intent;
			}

			@Override
			public ClipData parseResult(int i, @Nullable Intent intent) {
				return intent!=null ? intent.getClipData() : null;
			}
		};

		ActivityResultCallback<ClipData> callback= clipData -> {
			if (clipData!=null) {
				try (ByteArrayOutputStream stream=new ByteArrayOutputStream()){
					Bitmap b= BitmapFactory.decodeStream(requireActivity().getContentResolver().openInputStream(clipData.getItemAt(0).getUri()));

					b=Bitmap.createScaledBitmap(b, b.getWidth()/2,b.getHeight()/2,false);

					b.compress(Bitmap.CompressFormat.WEBP, 50, stream);

					imageView.setImageBitmap(b);
					User currentUser=Session.getInstance(requireContext()).getCurrentUser();
					currentUser.setFoto(Base64.getEncoder().encode(stream.toByteArray()));
					UserControllerImpl.getInstance().update(currentUser);
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
		};

		scegliFotoLauncher=registerForActivityResult(contract, callback);
	}
}
