package com.memaww.memaww.Services;

import android.Manifest;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.memaww.memaww.R;
import com.memaww.memaww.Util.Config;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by zatana on 1/9/19.
 */

public class BackgroundService extends Service {

    // constant
    private static final long NOTIFY_INTERVAL = 1 * 60 * 1000; // 1 minutes
    private Timer mTimer = null;
    private Thread newsFetchThread = null;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }


    @Override
    public void onCreate() {
        // cancel if already existed
        if (mTimer != null) {
            mTimer.cancel();
        } else {
            // recreate new
            mTimer = new Timer();
        }

        newsFetchThread = new Thread(new Runnable() {
            @Override
            public void run () {
                // schedule task
                Log.e("SERVER-REQUEST", "NEWS FETCH STARTED 1");
                mTimer.scheduleAtFixedRate(new TimeDisplayTimerTask(), 0, NOTIFY_INTERVAL);
            }
        });

        newsFetchThread.start();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // CLOSE BACKGROUND THREAD START
        newsFetchThread = Config.closeBackgroundThread2(newsFetchThread);
        //CLOSE BACKGROUND THREAD END
    }

    class TimeDisplayTimerTask extends TimerTask {

        @Override
        public void run() {
            updateUserInfo(getApplicationContext(), Config.getSharedPreferenceString(getApplicationContext(), Config.SHARED_PREF_KEY_USER_CREDENTIALS_USER_FCM_TOKEN));
        }

    }


    public static void updateUserInfo(final Context context, final String fcm){
        Log.e("SERVER-REQUEST", "NEWS FETCH STARTED 2");
        AndroidNetworking.post(Config.LINK_COLLECTION_UPDATE_USER_INFO)
                .addHeaders("Accept", "application/json")
                .addHeaders("Authorization", "Bearer " + Config.getSharedPreferenceString(context, Config.SHARED_PREF_KEY_USER_CREDENTIALS_USER_PASSWORD_ACCESS_TOKEN))
                .addBodyParameter("fcm_token", fcm)
                .addBodyParameter("fcm_type", "ANDROID")
                .addBodyParameter("app_type", "ANDROID")
                .addBodyParameter("app_version_code", String.valueOf(Config.getAppVersionCode(context)))
                .setTag("update_user_info")
                .setPriority(Priority.HIGH)
                .build().getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("SERVER-REQUEST", "response: " + response.toString());
                        try {
                            JSONObject main_response = new JSONObject(response);
                            String myStatus = main_response.getString("status");
                            String myStatusMessage = main_response.getString("message");
                            if(myStatus.trim().equalsIgnoreCase("success")){
                                String minVersionCode = main_response.getString("min_vc");
                                Config.setSharedPreferenceString(context, Config.SHARED_PREF_KEY_USER_CREDENTIALS_USER_APP_MINIMUM_VERSION_CODE, minVersionCode);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("SERVER-REQUEST", "anError: " + anError.toString());
                    }
                });

    }


}