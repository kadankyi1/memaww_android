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

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.memaww.memaww.Activities.MainActivity;
import com.memaww.memaww.Activities.NotificationsActivity;
import com.memaww.memaww.R;
import com.memaww.memaww.Util.Config;
import com.memaww.memaww.Util.MyLifecycleHandler;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        Log.e("FIREBASE-MSG", "FIREBASE TOKEN 2: " + token);
        Config.setSharedPreferenceString(getApplicationContext(), Config.SHARED_PREF_KEY_USER_CREDENTIALS_USER_FCM_TOKEN, token);
    }


    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        // TODO(developer): Handle FCM messages here.
            // Show the notification
            Log.e("FIREBASE-MSG", "FIREBASE NOTIFICATION RECEIVED 1: " + remoteMessage.getNotification().getTitle());
            Config.setSharedPreferenceBoolean(getApplicationContext(), Config.SHARED_PREF_KEY_USER_CREDENTIALS_USER_HAS_NEW_NOTIFICATION, true);
            String notificationBody = remoteMessage.getNotification().getBody();
            String notificationTitle = remoteMessage.getNotification().getTitle();

            Notification.getInstance().addOrder(remoteMessage.getData().get("id"));
            //&& notificationTitle.equalsIgnoreCase("New Message - MeMaww")
            if(MyLifecycleHandler.isApplicationInForeground()){
                Log.e("FIREBASE-MSG", "FORE-GROUND MESSAGE NOTIFICATION" );
            } else {
                Log.e("FIREBASE-MSG", "BACK-GROUND MESSAGE NOTIFICATION" );
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

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if(title.equalsIgnoreCase("New Message - MeMaww")){
            Intent notificationIntent = new Intent(this, MainActivity.class);
            notificationIntent.putExtra("goto", 3);
            PendingIntent conPendingIntent = PendingIntent.getActivity(this,0,notificationIntent,PendingIntent.FLAG_UPDATE_CURRENT);
            notificationBuilder.setContentIntent(conPendingIntent);
        } else {
            Intent notificationIntent = new Intent(this, NotificationsActivity.class);
            PendingIntent conPendingIntent = PendingIntent.getActivity(this,0,notificationIntent,PendingIntent.FLAG_UPDATE_CURRENT);
            notificationBuilder.setContentIntent(conPendingIntent);
        }



        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, "Channel human readable title", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }


    public static class Notification {
        private static Notification instance;
        private MutableLiveData<String> newOrder;

        private Notification() {
            newOrder = new MutableLiveData<>();
        }

        public static Notification getInstance() {
            if(instance == null){
                instance = new Notification();
            }
            return instance;
        }

        public LiveData<String> getNewOrder() {
            return newOrder;
        }

        public void addOrder(String orderID){
            newOrder.postValue(orderID);
        }
    }
}











