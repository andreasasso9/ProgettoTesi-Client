package com.example.tesi.client.activities;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tesi.client.R;

import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class AddProdottoActivity extends AppCompatActivity {
	private ActivityResultLauncher<Intent> scegliImmaginiLauncher;
	private List<Bitmap> foto;
	private LinearLayout layoutFoto;
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_prodotto_layout);

		foto=new LinkedList<>();
		layoutFoto=findViewById(R.id.layoutFoto);


		createLauncher();



	}

	private void createLauncher() {
		ActivityResultContract<Intent, ClipData> contract=new ActivityResultContract<Intent, ClipData>() {
			@NonNull
			@Override
			public Intent createIntent(@NonNull Context context, Intent intent) {
				intent.setAction(Intent.ACTION_GET_CONTENT);
				intent.setType("image/*");
				intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);

				return intent;
			}

			@Override
			public ClipData parseResult(int i, @Nullable Intent intent) {
				return intent!=null ? intent.getClipData() : null;
			}
		};

		ActivityResultCallback<ClipData> callback=new ActivityResultCallback<ClipData>() {
			@Override
			public void onActivityResult(ClipData clipData) {
				if (clipData!=null) {
					for (int i = 0; i < clipData.getItemCount(); i++) {
						System.out.println(clipData.getItemAt(i).getUri());
						try {
							foto.add(BitmapFactory.decodeStream(getContentResolver().openInputStream(clipData.getItemAt(i).getUri())));
						} catch (FileNotFoundException e) {
							throw new RuntimeException(e);
						}
					}

					for (Bitmap b:foto) {
						ImageView image=new ImageView(AddProdottoActivity.this);
						image.setImageBitmap(b);
						image.setLayoutParams(new LinearLayout.LayoutParams(
								500,500
						));
						layoutFoto.addView(image);
					}
				}

			}
		};

		scegliImmaginiLauncher=registerForActivityResult(contract, callback);
	}

	public void scegliFoto(View v) {
		scegliImmaginiLauncher.launch(new Intent());
	}
}
