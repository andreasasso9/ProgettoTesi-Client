package com.example.tesi.client.service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.example.tesi.client.R;
import com.example.tesi.client.activities.MainActivity;
import com.example.tesi.client.control.TokenControllerImpl;
import com.example.tesi.client.utils.Session;
import com.example.tesi.entity.Token;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Optional;

public class PushNotificationService extends FirebaseMessagingService {

	@Override
	public void onNewToken(@NonNull String token) {
		super.onNewToken(token);
		Session.getInstance(this).setToken(token);
	}

	@Override
	public void onMessageReceived(@NonNull RemoteMessage message) {
		String title, body;

		RemoteMessage.Notification notification= message.getNotification();
		if (notification!=null) {
			title = notification.getTitle();
			body = notification.getBody();
			sendNotification(title, body);
		}

	}

	private void sendNotification(String title, String body) {
		Intent intent=new Intent(this, MainActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);

		String channelId = "default_channel_id";
		Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
		Optional<String> optionalBody= Optional.ofNullable(body);
		NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, channelId)
				.setSmallIcon(R.drawable.ic_launcher_foreground)
				.setContentTitle(title)
				.setContentText(optionalBody.orElse("📷"))
				.setAutoCancel(true)
				.setSound(defaultSoundUri)
				.setContentIntent(pendingIntent);

		NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

		NotificationChannel channel = new NotificationChannel(channelId, "channel", NotificationManager.IMPORTANCE_DEFAULT);
		notificationManager.createNotificationChannel(channel);

		notificationManager.notify(0, notificationBuilder.build());
	}
}
