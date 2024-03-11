package com.memaww.memaww.Util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;
import android.util.Log;

import com.memaww.memaww.R;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Config {


    public static final String SHARED_PREF_KEY_USER_CREDENTIALS_USER_PHONE = "USER_PHONE";
    public static final String SHARED_PREF_KEY_USER_CREDENTIALS_USER_EMAIL = "USER_EMAIL";
    public static final String SHARED_PREF_KEY_USER_CREDENTIALS_USER_ID = "USER_ID";
    public static final String SHARED_PREF_KEY_USER_CREDENTIALS_USER_PASSWORD_ACCESS_TOKEN = "USER_PASSWORD";

    // GET SHARED PREFERENCE STRING-SET
    public static Set<String> getSharedPreferenceStringSet(Context context, String key) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getStringSet(key, null);
    }

    // PUT NEW SHARED PREFERENCE STRING-SET
    public static void setSharedPreferenceSetString(Context context, String key, List<String> value) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        Set<String> set = new HashSet<String>();
        set.addAll(value);
        editor.putStringSet(key, set);
        editor.commit();
        editor.apply();
    }

    // PUT NEW SHARED PREFERENCE STRING-SET
    public static void updateSharedPreferenceSetString(Context context, String key, String newValue, Set oldset, Boolean updateIfValueExits) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        Set<String> set = preferences.getStringSet(key, null);
        if(set != null) {
            List<String> itemReferences = new ArrayList<String>(set);
            int itemIndex = itemReferences.indexOf(newValue);
            if (itemIndex < 0) {
                itemReferences.add(newValue);
                setSharedPreferenceSetString(context, key, itemReferences);
            } else {
                if(updateIfValueExits){
                    itemReferences.add(itemIndex, newValue);
                    setSharedPreferenceSetString(context, key, itemReferences);
                }
            }
        } else {
            List<String> itemReferences = new ArrayList<String>();
            itemReferences.add(newValue);
            setSharedPreferenceSetString(context, key, itemReferences);
        }
    }

    // GET SHARED PREFERENCE STRING
    public static String getSharedPreferenceString(Context context, String key) {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        context = null;
        return preferences.getString(key, "");

    }

    // EDIT SHARED PREFERENCE STRING
    public static void setSharedPreferenceString(Context context, String key, String value) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.apply();
        context = null;
    }

    // GET SHARED PREFERENCE INT
    public static int getSharedPreferenceInt(Context context, String key) {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        context = null;
        return preferences.getInt(key, 0);

    }

    // SET SHARED PREFERENCE INT
    public static void setSharedPreferenceInt(Context context, String key, int value) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(key, value);
        editor.apply();
        context = null;
    }


    // GET SHARED PREFERENCE INT
    public static int getSharedPreferenceFloat(Context context, String key) {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        context = null;
        return preferences.getInt(key, 0);

    }

    // SET SHARED PREFERENCE INT
    public static void setSharedPreferenceFloat(Context context, String key, float value) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putFloat(key, value);
        editor.apply();
        context = null;
    }


    // GET SHARED PREFERENCE BOOLEAN
    public static boolean getSharedPreferenceBoolean(Context context, String key) {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        context = null;
        return preferences.getBoolean(key, false);

    }

    // SET SHARED PREFERENCE BOOLEAN
    public static void setSharedPreferenceBoolean(Context context, String key, boolean value) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(key, value);
        context = null;
        editor.apply();
    }

    // CLEAR ALL SHARED PREFERENCE
    public static void deleteAllDataInSharedPreference(Context context){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.commit();
        context = null;
    }

    // GET APP VERSION CODE
    public static int getAppVersionCode(Context context){
        PackageInfo pinfo = null;
        int currentVersionNumber = 0;
        try {
            pinfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            currentVersionNumber = pinfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return currentVersionNumber;
    }


    // FREE MEMORY WHEN CLOSING AN ACTIVITY AND OPENING A NEW ONE
    public static void freeMemory(){
        System.runFinalization();
        Runtime.getRuntime().gc();
        System.gc();
        Log.e("memoryManage", "freeMemory DONE");
    }

    // CHECK IF USER IS LOGGED IN
    public static Boolean userIsLoggedIn(Activity thisActivity){
        Boolean userIsLoggedIn = false;
        if(!getSharedPreferenceString(thisActivity.getApplicationContext(), SHARED_PREF_KEY_USER_CREDENTIALS_USER_PHONE).trim().equalsIgnoreCase("") && !getSharedPreferenceString(thisActivity.getApplicationContext(), SHARED_PREF_KEY_USER_CREDENTIALS_USER_PASSWORD_ACCESS_TOKEN).trim().equalsIgnoreCase("")){
            userIsLoggedIn = true;
        } else {
            userIsLoggedIn = false;
        }
        return userIsLoggedIn;
    }


    public static void openActivity(Activity thisActivity, Class NewActivity, int includeAnimation, int finishActivity, int addData, String dataIndex, String dataValue) {
        Intent intent = new Intent(thisActivity, NewActivity);
        if(addData == 1){
            intent.putExtra(dataIndex, dataValue);
        }

        if(finishActivity == 1){
            thisActivity.startActivity(intent);
            thisActivity.finish();
            if(includeAnimation == 1){
                thisActivity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            } else if(includeAnimation == 2){
                thisActivity.overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_down);
            }

        } else if(finishActivity == 2){
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            thisActivity.startActivity(intent);
            thisActivity.finish();
            if(includeAnimation == 1){
                thisActivity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            } else if(includeAnimation == 2){
                thisActivity.overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_down);
            }

        } else {
            thisActivity.startActivity(intent);
            if(includeAnimation == 1){
                thisActivity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            } else if(includeAnimation == 2){
                thisActivity.overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_down);
            }

        }
        thisActivity = null;
        Config.freeMemory();
    }

}
