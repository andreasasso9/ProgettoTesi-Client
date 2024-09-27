package com.tesi.client.activities;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tesi.client.R;
import com.tesi.client.control.ChatControllerImpl;
import com.tesi.entity.chat.Chat;
import com.tesi.entity.chat.Image;
import com.tesi.entity.chat.Text;
import com.tesi.client.control.UserControllerImpl;
import com.tesi.client.utils.File;
import com.tesi.client.utils.Session;
import com.tesi.client.utils.recyclerView.TextAdapter;
import com.tesi.entity.FotoByteArray;
import com.tesi.entity.User;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.util.Arrays;
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

		currentUser= Session.getInstance(this).getCurrentUser();

		gson=new Gson();

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
			chat=getIntent().getSerializableExtra("chat", Chat.class);
		else
			chat= (Chat) getIntent().getSerializableExtra("chat");

		String receiver=Arrays.stream(chat.getId().split("-")).filter(s->!s.equalsIgnoreCase(currentUser.getUsername()) && !s.equalsIgnoreCase("chat")).findAny().orElse("");

		foto=new LinkedList<>();

		TextView username=findViewById(R.id.username);
		username.setText(receiver);

		byte[] fotoUser=UserControllerImpl.getInstance().findFoto(receiver);
		if (fotoUser!=null) {
			fotoUser=Base64.getDecoder().decode(fotoUser);
			ImageView fotoProfiloImageView=findViewById(R.id.fotoUser);
			Bitmap b=BitmapFactory.decodeByteArray(fotoUser, 0, fotoUser.length);
			fotoProfiloImageView.setImageBitmap(b);
		}

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
		stompClient.topic("/queue/user/"+currentUser.getUsername()).retry(5).subscribe(message->{

			Text text=gson.fromJson(message.getPayload(), Text.class);
			if (text.getSender().equalsIgnoreCase(chat.getUser1()) || text.getSender().equalsIgnoreCase(chat.getUser2())) {

				if (text.getText() == null) {
					Image image = gson.fromJson(message.getPayload(), Image.class);
					chat.getTexts().add(image);
				} else
					chat.getTexts().add(text);

				runOnUiThread(() -> textAdapter.notifyItemInserted(textAdapter.getItemCount()));
			}
		});

		ImageButton send=findViewById(R.id.send);
		send.setOnClickListener(v->{
			String textString=editText.getText()+"";
			if (!textString.isEmpty()) {
				Text text = new Text(textString, currentUser.getUsername(), receiver, chat.getId());
				chat.getTexts().add(text);
				textAdapter.notifyItemInserted(textAdapter.getItemCount());
				recyclerView.scrollToPosition(textAdapter.getItemCount()-1);

				editText.setText("");

				stompClient.send("/app/send", gson.toJson(text)).retry(0).subscribe();
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
				RelativeLayout container= (RelativeLayout) containerFoto.getChildAt(i);
				ImageView image= (ImageView) container.getChildAt(0);
				Bitmap bitmap=((BitmapDrawable) image.getDrawable()).getBitmap();
				ByteArrayOutputStream stream=new ByteArrayOutputStream();
				bitmap.compress(Bitmap.CompressFormat.WEBP, 30, stream);

				byte[] imageByte=stream.toByteArray();
				String imageString= Base64.getEncoder().encodeToString(imageByte);

				Image imageToSend=new Image(imageString, currentUser.getUsername(), receiver, chat.getId());

				chat.getTexts().add(imageToSend);

				textAdapter.notifyItemInserted(textAdapter.getItemCount());
				recyclerView.scrollToPosition(textAdapter.getItemCount());

				editText.setText("");

				stompClient.send("/app/chat", gson.toJson(imageToSend)).subscribe();

				File.deleteFile(this, chat.getId());
				File.saveObjectToFile(this, chat.getId(), chat);
			}
			fotoDaInviareLayout.setVisibility(View.GONE);
		});

		findViewById(R.id.cancel).setOnClickListener(v->fotoDaInviareLayout.setVisibility(View.GONE));
	}

	@Override
	protected void onPause() {
		//Log.d("chat on pause", "pause");
		ChatControllerImpl.getInstance().save(chat);
		super.onPause();
	}

	private void createSendFotoLauncher(LinearLayout containerFoto) {
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
					RelativeLayout container=new RelativeLayout(this);
					LinearLayout.LayoutParams containerParams=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
					container.setLayoutParams(containerParams);

					ImageButton cancel = getImageButton(containerFoto, container, mainFoto);

					ImageView image=new ImageView(this);
					Bitmap bitmap=BitmapFactory.decodeByteArray(b.getValue(), 0, b.getValue().length);
					bitmap.compress(Bitmap.CompressFormat.WEBP, 50, stream);
					image.setImageBitmap(BitmapFactory.decodeByteArray(stream.toByteArray(), 0, stream.size()));

					container.addView(image);
					container.addView(cancel);

					RelativeLayout.LayoutParams imageParams=new RelativeLayout.LayoutParams(500,500);
					imageParams.addRule(RelativeLayout.CENTER_IN_PARENT);

					RelativeLayout.LayoutParams cancelParams=new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
					cancelParams.addRule(RelativeLayout.CENTER_IN_PARENT);

					cancel.setLayoutParams(cancelParams);
					image.setLayoutParams(imageParams);

					containerFoto.addView(container);
					image.setOnClickListener(v->{
						mainFoto.setImageBitmap(bitmap);
						for (int i=0; i<containerFoto.getChildCount(); i++) {
							RelativeLayout x=(RelativeLayout) containerFoto.getChildAt(i);
							x.getChildAt(1).setVisibility(View.GONE);
						}
						cancel.setVisibility(View.VISIBLE);
					});
					if (first) {
						cancel.setVisibility(View.VISIBLE);
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

	@NonNull
	private ImageButton getImageButton(LinearLayout containerFoto, RelativeLayout container, ImageView mainFoto) {
		ImageButton cancel=new ImageButton(this);
		cancel.setImageResource(R.drawable.icons8_cestino_64__1_);
		cancel.setBackgroundColor(Color.TRANSPARENT);
		cancel.setVisibility(View.GONE);

		cancel.setScaleType(ImageView.ScaleType.FIT_CENTER);
		cancel.setOnClickListener(v->{
			containerFoto.removeView(container);
			if (containerFoto.getChildCount()>0) {
				RelativeLayout x= (RelativeLayout) containerFoto.getChildAt(0);
				ImageView image= (ImageView) x.getChildAt(0);
				mainFoto.setImageDrawable(image.getDrawable());
				x.getChildAt(1).setVisibility(View.VISIBLE);
			} else
				mainFoto.setImageResource(R.drawable.icons8_non_abbiamo_trovato_nulla_100);
		});
		return cancel;
	}
}
