package com.memaww.memaww.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.memaww.memaww.R;
import com.memaww.memaww.Util.Config;

public class NotificationsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        Config.setSharedPreferenceBoolean(getApplicationContext(), Config.SHARED_PREF_KEY_USER_CREDENTIALS_USER_HAS_NEW_NOTIFICATION, false);
    }
}