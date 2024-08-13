package com.example.tesi.client.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tesi.client.R;
import com.example.tesi.client.chat.Chat;
import com.example.tesi.client.chat.Text;
import com.example.tesi.client.utils.File;
import com.example.tesi.client.utils.Session;
import com.example.tesi.client.utils.recyclerView.TextAdapter;
import com.example.tesi.entity.FotoByteArray;
import com.example.tesi.entity.User;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import ua.naiksoftware.stomp.StompClient;

public class ChatActivity extends AppCompatActivity {
	private ActivityResultLauncher<Intent> sendFotoLauncher;
	private List<FotoByteArray> foto;
	private TextAdapter textAdapter;
	private StompClient stompClient;
	private Chat chat;
	private User currentUser;
	private RelativeLayout fotoDaInviareLayout;

	@SuppressLint("CheckResult")
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chat_layout);

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
			chat=getIntent().getSerializableExtra("chat", Chat.class);
		else
			chat= (Chat) getIntent().getSerializableExtra("chat");

		foto=new LinkedList<>();

		currentUser= Session.getInstance(this).getCurrentUser();

		ImageButton indietro=findViewById(R.id.indietro);
		indietro.setOnClickListener(v-> getOnBackPressedDispatcher().onBackPressed());

		RecyclerView recyclerView=findViewById(R.id.listTexts);
		recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

		textAdapter=new TextAdapter(currentUser, chat.getTexts());
		recyclerView.setAdapter(textAdapter);

		if (textAdapter.getItemCount()>0)
			recyclerView.scrollToPosition(textAdapter.getItemCount()-1);

		recyclerView.addOnLayoutChangeListener((v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom) -> {
			if (bottom<oldBottom && textAdapter.getItemCount()>0) {
				recyclerView.postDelayed(()->{
					recyclerView.scrollToPosition(textAdapter.getItemCount()-1);
				}, 100);
			}
		});

		EditText editText=findViewById(R.id.editText);
		editText.setWidth((int) (Session.getInstance(this).getScreenWidth()*0.66));

		stompClient=Session.getInstance(this).getStompClient();
		stompClient.topic("/queue/user/"+currentUser.getUsername()).subscribe(message->{
			Text text=new Gson().fromJson(message.getPayload(), Text.class);

			chat.getTexts().add(text);

			runOnUiThread(()->{
				textAdapter.notifyItemInserted(textAdapter.getItemCount()+1);
			});
		});

		ImageButton send=findViewById(R.id.send);
		send.setOnClickListener(v->{
			String textString=editText.getText()+"";
			if (!textString.isEmpty()) {
				Text text = new Text(textString, currentUser.getUsername(), chat.getReceiver());
				chat.getTexts().add(text);
				textAdapter.notifyItemInserted(textAdapter.getItemCount());
				recyclerView.scrollToPosition(textAdapter.getItemCount()-1);

				editText.setText("");

				stompClient.send("/app/chat", new Gson().toJson(text)).subscribe();
			}
		});

		ImageButton pickFoto=findViewById(R.id.pickFoto);

		LinearLayout containerFoto=findViewById(R.id.containerFoto);
		createSendFotoLauncher(containerFoto);

		fotoDaInviareLayout=findViewById(R.id.fotoDaInviareLayout);
		pickFoto.setOnClickListener(v-> sendFotoLauncher.launch(new Intent()));

		ImageButton sendFoto=findViewById(R.id.sendFoto);
		sendFoto.setOnClickListener(v->{
			for (int i=0; i<containerFoto.getChildCount(); i++) {
				ImageView image= (ImageView) containerFoto.getChildAt(i);

			}
		});
	}

	@Override
	protected void onPause() {
		//Log.d("chat on pause", "pause");
		File.deleteFile(this, chat.getId());
		File.saveObjectToFile(this, chat.getId(), chat);
		super.onPause();
	}

	private void createSendFotoLauncher(LinearLayout containerFoto) {
		ImageView mainFoto=findViewById(R.id.mainFoto);
		containerFoto=findViewById(R.id.containerFoto);
		ActivityResultContract<Intent, ClipData> contract=new ActivityResultContract<Intent, ClipData>() {
			@NonNull
			@Override
			public Intent createIntent(@NonNull Context context, Intent intent) {
				fotoDaInviareLayout.setVisibility(View.VISIBLE);
				fotoDaInviareLayout.bringToFront();
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

		LinearLayout finalContainerFoto = containerFoto;
		ActivityResultCallback<ClipData> callback= clipData -> {
			if (clipData!=null) {
				List<FotoByteArray> list=new LinkedList<>();
				ByteArrayOutputStream stream=new ByteArrayOutputStream();
				for (int i = 0; i < clipData.getItemCount(); i++) {
					try {
						Bitmap b= BitmapFactory.decodeStream(getContentResolver().openInputStream(clipData.getItemAt(i).getUri()));

						b=Bitmap.createScaledBitmap(b, b.getWidth()/2,b.getHeight()/2,false);

						b.compress(Bitmap.CompressFormat.WEBP, 100, stream);

						list.add(new FotoByteArray(stream.toByteArray()));
					} catch (FileNotFoundException e) {
						throw new RuntimeException(e);
					}
				}
				foto.addAll(list);
				finalContainerFoto.removeAllViews();
				for (FotoByteArray b:foto) {
					ImageView image=new ImageView(this);
					Bitmap bitmap=BitmapFactory.decodeByteArray(b.getValue(), 0, b.getValue().length);
					bitmap.compress(Bitmap.CompressFormat.WEBP, 100, stream);
					image.setImageBitmap(bitmap);
					image.setLayoutParams(new LinearLayout.LayoutParams(
							500,500
					));
					finalContainerFoto.addView(image);
					image.setOnClickListener(v->{
						mainFoto.setImageBitmap(bitmap);
					});
				}
			}
		};

		sendFotoLauncher=registerForActivityResult(contract, callback);
	}
}
