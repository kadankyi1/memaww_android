package com.memaww.memaww.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;
import com.memaww.memaww.R;
import com.memaww.memaww.Util.Config;

public class StartActivity extends AppCompatActivity {

    private int SPLASH_TIME_OUT = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.cancelAll();

        FirebaseMessaging.getInstance().getToken().addOnSuccessListener(new OnSuccessListener<String>() {
            @Override
            public void onSuccess(String token) {
                Log.e("FIREBASE-MSG", "FIREBASE TOKEN 1: " + token);
                Config.setSharedPreferenceString(getApplicationContext(), Config.SHARED_PREF_KEY_USER_CREDENTIALS_USER_FCM_TOKEN, token);
            }
        });

        FirebaseMessaging.getInstance().subscribeToTopic("ANDROID_USERS").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Log.e("FIREBASE", "SUCCESS : subscribeToTopic ANDROID_USERS");
                } else {
                    Log.e("FIREBASE", "FAILED: subscribeToTopic ANDROID_USERS");
                }
            }
        });
        FirebaseMessaging.getInstance().subscribeToTopic("ALL_USERS").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Log.e("FIREBASE", "SUCCESS: subscribeToTopic ALL_USERS");
                } else {
                    Log.e("FIREBASE", "FAILED: subscribeToTopic ALL_USERS");
                }
            }
        });

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                if(Config.userIsLoggedIn(StartActivity.this)){
                    Config.openActivity(StartActivity.this, MainActivity.class, 1, 2, 0, "", "");
                    return;
                } else {
                    //Config.openActivity(StartActivity.this, MainActivity.class, 1, 2, 0, "", "");
                    Config.openActivity(StartActivity.this, SliderActivity.class, 1, 2, 0, "", "");
                    return;
                }

            }
        }, SPLASH_TIME_OUT);
    }
}