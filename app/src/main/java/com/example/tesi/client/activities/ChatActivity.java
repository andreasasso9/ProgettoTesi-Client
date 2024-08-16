package com.example.tesi.client.activities;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tesi.client.R;
import com.example.tesi.client.chat.Chat;
import com.example.tesi.client.chat.Image;
import com.example.tesi.client.chat.Text;
import com.example.tesi.client.utils.File;
import com.example.tesi.client.utils.Session;
import com.example.tesi.client.utils.recyclerView.TextAdapter;
import com.example.tesi.entity.FotoByteArray;
import com.example.tesi.entity.User;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.util.Base64;
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
	private Gson gson;

	@SuppressLint("CheckResult")
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chat_layout);

		gson=new Gson();

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
				recyclerView.postDelayed(()-> recyclerView.scrollToPosition(textAdapter.getItemCount()-1), 100);
			}
		});

		EditText editText=findViewById(R.id.editText);
		editText.setWidth((int) (Session.getInstance(this).getScreenWidth()*0.66));

		stompClient=Session.getInstance(this).getStompClient();
		stompClient.topic("/queue/user/"+currentUser.getUsername()).subscribe(message->{

			Text text=gson.fromJson(message.getPayload(), Text.class);

			if (text.getText()==null) {
				Image image = gson.fromJson(message.getPayload(), Image.class);
				chat.getTexts().add(image);
			} else
				chat.getTexts().add(text);



			runOnUiThread(()-> textAdapter.notifyItemInserted(textAdapter.getItemCount()));
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

				stompClient.send("/app/chat", gson.toJson(text)).subscribe();
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
				Bitmap bitmap=((BitmapDrawable) image.getDrawable()).getBitmap();
				ByteArrayOutputStream stream=new ByteArrayOutputStream();
				bitmap.compress(Bitmap.CompressFormat.WEBP, 30, stream);

				byte[] imageByte=stream.toByteArray();
				String imageString= Base64.getEncoder().encodeToString(imageByte);

				Image imageToSend=new Image(imageString, currentUser.getUsername(), chat.getReceiver());

				chat.getTexts().add(imageToSend);

				textAdapter.notifyItemInserted(textAdapter.getItemCount());
				recyclerView.scrollToPosition(textAdapter.getItemCount()-1);

				editText.setText("");

				stompClient.send("/app/chat", gson.toJson(imageToSend)).subscribe();
				runOnUiThread(()-> Toast.makeText(this, "foto inviate", Toast.LENGTH_SHORT).show());
			}
			fotoDaInviareLayout.setVisibility(View.GONE);
		});

		findViewById(R.id.cancel).setOnClickListener(v->fotoDaInviareLayout.setVisibility(View.GONE));
	}

	@Override
	protected void onPause() {
		//Log.d("chat on pause", "pause");
		File.deleteFile(this, chat.getId());
		File.saveObjectToFile(this, chat.getId(), chat);
		super.onPause();
	}

	private void createSendFotoLauncher(LinearLayout containerFoto) {
		//todo aggiungere tasti per annullare e per eliminare le foto selezionate
		ImageView mainFoto=findViewById(R.id.mainFoto);
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
				containerFoto.removeAllViews();
				boolean first=true;
				for (FotoByteArray b:foto) {
					ImageView image=new ImageView(this);
					Bitmap bitmap=BitmapFactory.decodeByteArray(b.getValue(), 0, b.getValue().length);
					bitmap.compress(Bitmap.CompressFormat.WEBP, 100, stream);
					image.setImageBitmap(BitmapFactory.decodeByteArray(stream.toByteArray(), 0, stream.size()));
					image.setLayoutParams(new LinearLayout.LayoutParams(
							500,500
					));
					containerFoto.addView(image);
					image.setOnClickListener(v-> mainFoto.setImageBitmap(bitmap));
					if (first) {
						mainFoto.setImageBitmap(BitmapFactory.decodeByteArray(stream.toByteArray(), 0, stream.size()));
						first = false;
					}
				}
			}
			foto.clear();
			fotoDaInviareLayout.setVisibility(View.VISIBLE);
			fotoDaInviareLayout.bringToFront();
		};

		sendFotoLauncher=registerForActivityResult(contract, callback);
	}
}
