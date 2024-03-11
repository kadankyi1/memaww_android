package com.memaww.memaww.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;

import com.memaww.memaww.R;
import com.memaww.memaww.Util.Config;

public class StartActivity extends AppCompatActivity {

    private int SPLASH_TIME_OUT = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);


        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                if(Config.userIsLoggedIn(StartActivity.this)){
                    Config.openActivity(StartActivity.this, MainActivity.class, 1, 2, 0, "", "");
                    return;
                } else {
                    Config.openActivity(StartActivity.this, SliderActivity.class, 1, 2, 0, "", "");
                    return;
                }

            }
        }, SPLASH_TIME_OUT);
    }
}