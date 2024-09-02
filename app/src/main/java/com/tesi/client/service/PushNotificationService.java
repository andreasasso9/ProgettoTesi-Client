package com.tesi.client.service;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.tesi.client.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.tesi.client.activities.MainActivity;
import com.tesi.client.utils.Session;

public class PushNotificationService extends FirebaseMessagingService {
	private static final String CHANNEL_ID = "main_channel_id";

	@Override
	public void onNewToken(@NonNull String token) {
		Session.getInstance(this).setToken(token);
		super.onNewToken(token);
	}

	@Override
	public void onMessageReceived(@NonNull RemoteMessage message) {
		RemoteMessage.Notification notification = message.getNotification();
		if (notification != null) {
			String title = notification.getTitle();
			String body = notification.getBody();

			createNotificationChannel();

			Intent intent = new Intent(this, MainActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);

			NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
					.setSmallIcon(R.drawable.ic_launcher_foreground)
					.setContentTitle(title)
					.setContentText(body)
					.setPriority(NotificationCompat.PRIORITY_DEFAULT)
					.setContentIntent(pendingIntent)
					.setAutoCancel(true);


			if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
				NotificationManagerCompat.from(this).notify(0, builder.build());
			}

		}
	}

	private void createNotificationChannel() {
		NotificationChannel channel=new NotificationChannel(CHANNEL_ID, "main_channel", NotificationManager.IMPORTANCE_DEFAULT);

		NotificationManager manager=getSystemService(NotificationManager.class);
		manager.createNotificationChannel(channel);
	}
}
