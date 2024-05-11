package com.memaww.memaww.Services;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.memaww.memaww.Activities.MainActivity;
import com.memaww.memaww.Activities.NotificationsActivity;
import com.memaww.memaww.R;
import com.memaww.memaww.Util.Config;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // TODO(developer): Handle FCM messages here.

        if (remoteMessage.getNotification()!=null){
            // Show the notification
            Log.e("FIREBASE-MSG", "FIREBASE NOTIFICATION RECEIVED 1: " + remoteMessage.getNotification().getTitle());
            Config.setSharedPreferenceBoolean(getApplicationContext(), Config.SHARED_PREF_KEY_USER_CREDENTIALS_USER_HAS_NEW_NOTIFICATION, true);
            String notificationBody = remoteMessage.getNotification().getBody();
            String notificationTitle = remoteMessage.getNotification().getTitle();

            sendNotification (notificationTitle, notificationBody);

        }
    }

    public void handleIntent(Intent intent) {
        try {
            if (intent.getExtras() != null) {
                Config.setSharedPreferenceBoolean(getApplicationContext(), Config.SHARED_PREF_KEY_USER_CREDENTIALS_USER_HAS_NEW_NOTIFICATION, true);
                Log.e("FIREBASE-MSG", "FIREBASE NOTIFICATION RECEIVED 2: ");
                RemoteMessage.Builder builder = new RemoteMessage.Builder("MyFirebaseMessagingService");
                for (String key : intent.getExtras().keySet()) {
                    builder.addData(key, intent.getExtras().get(key).toString());
                }
                onMessageReceived(builder.build());
            } else {
                Log.e("FIREBASE-MSG", "FIREBASE NOTIFICATION RECEIVED 3: ");
                super.handleIntent(intent);
            }
        } catch (Exception e) {
            Log.e("FIREBASE-MSG", "FIREBASE NOTIFICATION RECEIVED 4: ");
            super.handleIntent(intent);
        }
    }

    private void sendNotification(String title, String body) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_IMMUTABLE);

        String channelId = "fcm_default_channel";
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.drawable.memaww_logo_svg)
                        .setContentTitle(title)
                        .setContentText(body)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);

        Intent notificationIntent = new Intent(this, NotificationsActivity.class);
        PendingIntent conPendingIntent = PendingIntent.getActivity(this,0,notificationIntent,PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationBuilder.setContentIntent(conPendingIntent);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, "Channel human readable title", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }


}











