package com.memaww.memaww.Util;

import android.app.Application;
import android.content.Context;
import android.os.StrictMode;
/**
 * Created by zatana on 10/27/18.
 */


public class Home extends Application {


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base, "en"));
    }


    @Override
    public void onCreate() {
        super.onCreate();

        registerActivityLifecycleCallbacks(new MyLifecycleHandler());
    }
}
