package com.memaww.memaww.Util;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.memaww.memaww.R;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Config {

    // CURRENT HTTP
    public static final String CURRENT_HTTP_IN_USE = "http://";
    //public static final String CURRENT_HTTP_IN_USE = "https://";

    // LIVE OR TEST ENVIRONMENT
    public static final String CURRENT_ENVIRONMENT_DOMAIN_IN_USE = "10.0.2.2/memaww/public"; // TEST
    //public static final String CURRENT_ENVIRONMENT_DOMAIN_IN_USE = "app.memaww.com"; // LIVE

    // SERVER-SIDE API FOR LOGIN
    public static final String LINK_LOGIN = CURRENT_HTTP_IN_USE + CURRENT_ENVIRONMENT_DOMAIN_IN_USE + "/api/v1/user/sign-in";

    // SERVER-SIDE API FOR PLACING COLLECTION ORDER
    public static final String LINK_COLLECTION_ORDER = CURRENT_HTTP_IN_USE + CURRENT_ENVIRONMENT_DOMAIN_IN_USE + "/api/v1/user/request-collection";

    // SERVER-SIDE API FOR PLACING CALLBACK REQUEST FOR COLLECTION ORDER
    public static final String LINK_COLLECTION_REQUEST_ORDER = CURRENT_HTTP_IN_USE + CURRENT_ENVIRONMENT_DOMAIN_IN_USE + "/api/v1/user/request-collection-callback";

    // SERVER-SIDE API FOR PLACING CALLBACK REQUEST FOR COLLECTION ORDER
    public static final String LINK_GET_MY_ORDERS = CURRENT_HTTP_IN_USE + CURRENT_ENVIRONMENT_DOMAIN_IN_USE + "/api/v1/user/get-my-orders";

    public static final String SHARED_PREF_KEY_USER_CREDENTIALS_USER_ID_SHORT = "USER_ID";
    public static final String SHARED_PREF_KEY_USER_CREDENTIALS_USER_ID_LONG = "USER_ID_LONG";
    public static final String SHARED_PREF_KEY_USER_CREDENTIALS_USER_FIRST_NAME = "USER_FIRST_NAME";
    public static final String SHARED_PREF_KEY_USER_CREDENTIALS_USER_LAST_NAME = "USER_LAST_NAME";
    public static final String SHARED_PREF_KEY_USER_CREDENTIALS_USER_PHONE = "USER_PHONE";
    public static final String SHARED_PREF_KEY_USER_CREDENTIALS_USER_EMAIL = "USER_EMAIL";
    public static final String SHARED_PREF_KEY_USER_CREDENTIALS_USER_PASSWORD_ACCESS_TOKEN = "USER_PASSWORD";


    // OTHERS
    public static final String WEBVIEW_KEY_URL = "URL";

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
        if(
                !getSharedPreferenceString(thisActivity.getApplicationContext(), SHARED_PREF_KEY_USER_CREDENTIALS_USER_ID_SHORT).trim().equalsIgnoreCase("")
                        && !getSharedPreferenceString(thisActivity.getApplicationContext(), SHARED_PREF_KEY_USER_CREDENTIALS_USER_ID_LONG).trim().equalsIgnoreCase("")
                        && !getSharedPreferenceString(thisActivity.getApplicationContext(), SHARED_PREF_KEY_USER_CREDENTIALS_USER_FIRST_NAME).trim().equalsIgnoreCase("")
                        && !getSharedPreferenceString(thisActivity.getApplicationContext(), SHARED_PREF_KEY_USER_CREDENTIALS_USER_LAST_NAME).trim().equalsIgnoreCase("")
                        && !getSharedPreferenceString(thisActivity.getApplicationContext(), SHARED_PREF_KEY_USER_CREDENTIALS_USER_PHONE).trim().equalsIgnoreCase("")
                        && !getSharedPreferenceString(thisActivity.getApplicationContext(), SHARED_PREF_KEY_USER_CREDENTIALS_USER_PASSWORD_ACCESS_TOKEN).trim().equalsIgnoreCase("")
        ){
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
            } else if(includeAnimation == 3){
                thisActivity.overridePendingTransition(R.anim.slide_out_down, R.anim.slide_in_up);
            }

        } else if(finishActivity == 2){
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            thisActivity.startActivity(intent);
            thisActivity.finish();
            if(includeAnimation == 1){
                thisActivity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            } else if(includeAnimation == 2){
                thisActivity.overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_down);
            } else if(includeAnimation == 3){
                thisActivity.overridePendingTransition(R.anim.slide_out_down, R.anim.slide_in_up);
            }

        } else {
            thisActivity.startActivity(intent);
            if(includeAnimation == 1){
                thisActivity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            } else if(includeAnimation == 2){
                thisActivity.overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_down);
            } else if(includeAnimation == 3){
                thisActivity.overridePendingTransition(R.anim.slide_out_down, R.anim.slide_in_up);
            }

        }
        thisActivity = null;
        Config.freeMemory();
    }



    // OPENING A FRAGMENT
    public static void openFragment(FragmentManager fragmentManager, int fragmentContainerId, Fragment newFragment, String fragmentName, int includeAnimation){
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if(includeAnimation == 1){
            transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_right, R.anim.slide_in_right, R.anim.slide_out_right);
        } else if (includeAnimation == 2){
            transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_right, R.anim.slide_out_left);
        } else if (includeAnimation == 3){
            transaction.setCustomAnimations(R.anim.slide_in_up, R.anim.slide_out_down,R.anim.slide_in_down, R.anim.slide_out_up);
        }

        transaction.addToBackStack(fragmentName);
        transaction.add(fragmentContainerId, newFragment, fragmentName).commit();
        fragmentManager = null;
    }



    // FUNCTION FOR SETTING A NUMBER PICKER AND GETTING THE NUMBER VALUE CHANGE LISTENER
    public static NumberPicker.OnValueChangeListener openNumberPickerForCountries(Activity thisActivity, NumberPicker.OnValueChangeListener  mNumberSetListener, int minNumber, int maxNumber, Boolean disNumbersOnUiToUser, String[] displayStringsValues, int defaultCountry) {

        final Dialog d = new Dialog(thisActivity);
        d.setContentView(R.layout.country_picker_dialog);
        Button b1 = (Button) d.findViewById(R.id.fragment_signup_stage2_dialog_button);
        final NumberPicker np = (NumberPicker) d.findViewById(R.id.fragment_signup_stage2_numberpicker);
        np.setMaxValue(maxNumber);
        np.setMinValue(minNumber);
        np.setWrapSelectorWheel(false);
        np.setValue(defaultCountry);
        if(disNumbersOnUiToUser){
            np.setDisplayedValues(displayStringsValues);
        }
        np.setOnValueChangedListener(mNumberSetListener);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                d.dismiss();
            }
        });
        d.show();

        return mNumberSetListener;
    }

    // DIALOG TYPE 1 SHOWS AN OKAY BUTTON THAT DOES NOTHING
    public static Dialog.OnCancelListener showDialogType1(final Activity thisActivity, String subTitle, String subBody, String subBody2, Dialog.OnCancelListener cancelListener, Boolean canNotBeClosedFromOutSideClick, String positiveButtonText, String negativeButtonText){

        if(thisActivity != null) {
            final Dialog dialog = new Dialog(thisActivity);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            if (canNotBeClosedFromOutSideClick) {
                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);
            }
            if (subBody2.trim().equalsIgnoreCase("show-positive-image")) {
                dialog.setContentView(R.layout.positive_icon_dialog);
            } else {
                dialog.setContentView(R.layout.login_activity_dialog);
            }

            TextView dialogTextView = dialog.findViewById(R.id.login_activity_dialog_text);
            TextView dialogTextView2 = dialog.findViewById(R.id.login_activity_dialog_text2);
            ImageView dialogTextImageView = dialog.findViewById(R.id.login_activity_dialog_imageview);
            dialogTextView.setText(subBody);

            if (subBody.trim().equalsIgnoreCase("")) {
                dialogTextView.setVisibility(View.INVISIBLE);
            } else {
                dialogTextView.setText(subBody);
            }

            if (subBody2.trim().equalsIgnoreCase("") || subBody2.trim().equalsIgnoreCase("show-positive-image")) {
                dialogTextView2.setVisibility(View.INVISIBLE);
            } else {
                dialogTextView2.setText(subBody2);
            }

            if (subTitle.trim().equalsIgnoreCase("1")) {
                dialogTextImageView.setVisibility(View.GONE);
            }

            dialogTextView2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String url = CURRENT_HTTP_IN_USE + "www.fishpott.com/service_agreements.html";
                    //openActivity(thisActivity, WebViewActivity.class, 1, 0, 1, WEBVIEW_KEY_URL, url);
                }
            });

            Button positiveDialogButton = dialog.findViewById(R.id.login_activity_dialog_button);
            Button negativeDialogButton = dialog.findViewById(R.id.login_activity_dialog_button_cancel);
            if (!positiveButtonText.trim().equalsIgnoreCase("")) {
                positiveDialogButton.setText(positiveButtonText);
            }
            if (!negativeButtonText.trim().equalsIgnoreCase("")) {
                negativeDialogButton.setText(negativeButtonText);
            } else {
                negativeDialogButton.setVisibility(View.GONE);
            }
            positiveDialogButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.cancel();
                }
            });
            negativeDialogButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            dialog.show();
            dialog.setOnCancelListener(cancelListener);
            return cancelListener;
        } else {
            return null;
        }
    }


    //TOAST TYPE 1 SHOWS JUST A TEXT
    public static void showToastType1(Activity thisActivity, String toastInfo){
        View toastView = thisActivity.getLayoutInflater().inflate(R.layout.toast, (ViewGroup) thisActivity.findViewById(R.id.activity_login_toast_root));
        TextView toastTextView = toastView.findViewById(R.id.activity_login_toast_text);
        toastTextView.setText(toastInfo);
        Toast toast = new Toast(thisActivity.getApplicationContext());
        toast.setGravity(Gravity.BOTTOM | Gravity.FILL_HORIZONTAL, 0, 0);
        //toast.setGravity(Gravity.FILL_HORIZONTAL, 0, 0);
        //toast.setGravity(Gravity.BOTTOM, 0, 0);
        //toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(toastView);
        toast.show();
    }

}
