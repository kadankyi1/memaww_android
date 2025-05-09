package com.memaww.memaww.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.widget.ContentLoadingProgressBar;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.memaww.memaww.R;
import com.memaww.memaww.Util.Config;
import com.memaww.memaww.Util.MyLifecycleHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView mCountryTextView;
    private EditText mPhoneNumberEditText, mFirstNameEditText, mLastNameEditText, mInviteCodeEditText;
    private AppCompatButton mLoginAppCompatButton;
    private ContentLoadingProgressBar mLoadingContentLoadingProgressBar;
    private NumberPicker.OnValueChangeListener mCountrySetNumberListener;
    private String country = "GHANA", loginResponse = "", phone = "", firstName = "", lastName = "", inviteCode = "";
    private int defaultCountry = 1;
    private Thread backgroundThread1 = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mCountryTextView = findViewById(R.id.activity_login_country_textview);
        mPhoneNumberEditText = findViewById(R.id.activity_login_phone_edit_text);
        mFirstNameEditText = findViewById(R.id.activity_login_firstname);
        mLastNameEditText = findViewById(R.id.activity_login_lastname);
        mInviteCodeEditText = findViewById(R.id.activity_login_invitecode_edit_text);
        mLoginAppCompatButton = findViewById(R.id.activity_login_login_button);
        mLoadingContentLoadingProgressBar = findViewById(R.id.activity_login_loader);

        // SETTING LISTENERS
        mCountryTextView.setOnClickListener(this);
        mLoginAppCompatButton.setOnClickListener(this);


        // GETTING THE COUNTRIES STRING LIST AND ARRAY SETS
        final String[] countriesStringArraySet = getResources().getStringArray(R.array.countries_array_starting_with_choose_country);
        final List<String> countriesStringArrayList = Arrays.asList(countriesStringArraySet);
        final List<String> countriesCodesStringArrayList = Arrays.asList(getResources().getStringArray(R.array.fragment_signup_personalstage2_countries_codes_array));

        mCountryTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCountrySetNumberListener = Config.openNumberPickerForCountries(LoginActivity.this, mCountrySetNumberListener, 0, countriesStringArraySet.length-1, true, getResources().getStringArray(R.array.countries_array_starting_with_choose_country), 1);
            }
        });
        mCountrySetNumberListener = new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int oldVal, int newVal) {
                defaultCountry = newVal;
                country = countriesStringArrayList.get(newVal);
                mCountryTextView.setText(countriesStringArrayList.get(newVal));
            }
        };
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == mLoginAppCompatButton.getId()){
            if(
                            !mPhoneNumberEditText.getText().toString().trim().equalsIgnoreCase("")
                            && !mFirstNameEditText.getText().toString().trim().equalsIgnoreCase("")
                            && !mLastNameEditText.getText().toString().trim().equalsIgnoreCase("")
                            && !country.trim().equalsIgnoreCase("")
                            && defaultCountry > 0
            ){

                phone = mPhoneNumberEditText.getText().toString().trim();
                firstName = mFirstNameEditText.getText().toString().trim();
                lastName = mLastNameEditText.getText().toString().trim();
                inviteCode = mInviteCodeEditText.getText().toString().trim();

                backgroundThread1 = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        loginAndGetUserCredentials(country, phone.trim(), firstName.trim(), lastName.trim(), inviteCode.trim());
                    }
                });
                backgroundThread1.start();
            }
        }

    }


    public void loginAndGetUserCredentials(final String country, final String phone, final String first_name, final String last_name, final String invite_code){

        if(!this.isFinishing() && getApplicationContext() != null) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    mCountryTextView.setVisibility(View.GONE);
                    mPhoneNumberEditText.setVisibility(View.GONE);
                    mFirstNameEditText.setVisibility(View.GONE);
                    mLastNameEditText.setVisibility(View.GONE);
                    mInviteCodeEditText.setVisibility(View.GONE);
                    mLoginAppCompatButton.setVisibility(View.GONE);
                    mLoadingContentLoadingProgressBar.setVisibility(View.VISIBLE);
                }
            });
        }

        loginResponse = "";

        Log.e("SIGN-IN-NETWORK", "country: " + country);
        Log.e("SIGN-IN-NETWORK", "phone: " + phone);
        Log.e("SIGN-IN-NETWORK", "first_name: " + first_name);
        Log.e("SIGN-IN-NETWORK", "last_name: " + last_name);
        Log.e("SIGN-IN-NETWORK", "app_version_code: " + String.valueOf(Config.getAppVersionCode(getApplicationContext())));

        AndroidNetworking.post(Config.LINK_LOGIN)
                .addBodyParameter("user_country", country)
                .addBodyParameter("user_phone", phone)
                .addBodyParameter("user_first_name", first_name)
                .addBodyParameter("user_last_name", last_name)
                .addBodyParameter("invite_code", invite_code)
                .addBodyParameter("app_type", "ANDROID")
                .addBodyParameter("app_version_code", String.valueOf(Config.getAppVersionCode(getApplicationContext())))
                .setTag("login_activity_login")
                .setPriority(Priority.MEDIUM)
                .build().getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("SIGN-IN-NETWORK", "response: " + response.toString());

                        if(!LoginActivity.this.isFinishing() && getApplicationContext() != null) {
                            try {
                                Log.e("PSignup", response);
                                JSONObject main_response = new JSONObject(response);
                                String myStatus = main_response.getString("status");
                                final String myStatusMessage = main_response.getString("message");

                                if (myStatus.equalsIgnoreCase("success")) {
                                    JSONObject user_data_response = new JSONObject(response).getJSONObject("user");

                                    //STORING THE USER DATA
                                    Config.setSharedPreferenceString(getApplicationContext(), Config.SHARED_PREF_KEY_USER_CREDENTIALS_USER_ID_SHORT, user_data_response.getString("user_id"));
                                    Config.setSharedPreferenceString(getApplicationContext(), Config.SHARED_PREF_KEY_USER_CREDENTIALS_USER_ID_LONG, user_data_response.getString("user_sys_id"));
                                    Config.setSharedPreferenceString(getApplicationContext(), Config.SHARED_PREF_KEY_USER_CREDENTIALS_USER_FIRST_NAME, user_data_response.getString("user_first_name"));
                                    Config.setSharedPreferenceString(getApplicationContext(), Config.SHARED_PREF_KEY_USER_CREDENTIALS_USER_LAST_NAME, user_data_response.getString("user_last_name"));
                                    Config.setSharedPreferenceString(getApplicationContext(), Config.SHARED_PREF_KEY_USER_CREDENTIALS_USER_PHONE, user_data_response.getString("user_phone"));
                                    Config.setSharedPreferenceString(getApplicationContext(), Config.SHARED_PREF_KEY_USER_CREDENTIALS_USER_PHONE_LOCAL, main_response.getString("user_phone_local"));
                                    Config.setSharedPreferenceString(getApplicationContext(), Config.SHARED_PREF_KEY_USER_CREDENTIALS_USER_INVITE_CODE, user_data_response.getString("user_referral_code"));
                                    Config.setSharedPreferenceString(getApplicationContext(), Config.SHARED_PREF_KEY_USER_CREDENTIALS_USER_PASSWORD_ACCESS_TOKEN, main_response.getString("access_token"));
                                    Config.setSharedPreferenceString(getApplicationContext(), Config.SHARED_PREF_KEY_USER_CREDENTIALS_ANDROID_APP_LINK, main_response.getString("android_app_link"));

                                    Config.setSharedPreferenceString(getApplicationContext(), Config.SHARED_PREF_KEY_LAST_ORDER_CONTACT_PERSON_PHONE, main_response.getString("user_phone_local"));

                                    Config.openActivity(LoginActivity.this, MainActivity.class, 1, 2, 0, "", "");
                                    return;

                                } else if (myStatus.equalsIgnoreCase("update")) {
                                    String minVersionCode = main_response.getString("minvc");
                                    String appLink = main_response.getString("app_link");
                                    Config.setSharedPreferenceString(getApplicationContext(), Config.SHARED_PREF_KEY_USER_CREDENTIALS_USER_APP_MINIMUM_VERSION_CODE, minVersionCode);
                                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                                        @Override
                                        public void run() {
                                            Config.setSharedPreferenceString(getApplicationContext(), Config.SHARED_PREF_KEY_GOOGLE_PLAYSTORE_APPID, appLink);
                                            Config.openActivity(LoginActivity.this, UpdateActivity.class, 0, 2, 0, "", "");
                                            return;
                                        }
                                    });
                                } else {
                                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                                            @Override
                                            public void run() {
                                                Config.showDialogType1(LoginActivity.this, "1", myStatusMessage, "", null, true, "Ok", "");
                                                mCountryTextView.setVisibility(View.VISIBLE);
                                                mPhoneNumberEditText.setVisibility(View.VISIBLE);
                                                mFirstNameEditText.setVisibility(View.VISIBLE);
                                                mLastNameEditText.setVisibility(View.VISIBLE);
                                                mInviteCodeEditText.setVisibility(View.VISIBLE);
                                                mLoginAppCompatButton.setVisibility(View.VISIBLE);
                                                mLoadingContentLoadingProgressBar.setVisibility(View.GONE);

                                            }
                                        });
                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                                        @Override
                                        public void run() {
                                            Config.showDialogType1(LoginActivity.this, getString(R.string.error), getString(R.string.an_unexpected_error_occured), "", null, false, "", "");
                                            mCountryTextView.setVisibility(View.VISIBLE);
                                            mPhoneNumberEditText.setVisibility(View.VISIBLE);
                                            mFirstNameEditText.setVisibility(View.VISIBLE);
                                            mLastNameEditText.setVisibility(View.VISIBLE);
                                            mInviteCodeEditText.setVisibility(View.VISIBLE);
                                            mLoginAppCompatButton.setVisibility(View.VISIBLE);
                                            mLoadingContentLoadingProgressBar.setVisibility(View.GONE);

                                        }
                                    });
                            }
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("SIGN-IN-NETWORK", "anError: " + anError.getErrorDetail());
                        Log.e("SIGN-IN-NETWORK", "anError: " + anError.getErrorBody());
                        Log.e("SIGN-IN-NETWORK", "anError: " + anError.getMessage());
                        if(!LoginActivity.this.isFinishing() && getApplicationContext() != null) {
                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                @Override
                                public void run() {
                                    Config.showToastType1(LoginActivity.this, getResources().getString(R.string.check_your_internet_connection_and_try_again));
                                    mCountryTextView.setVisibility(View.VISIBLE);
                                    mPhoneNumberEditText.setVisibility(View.VISIBLE);
                                    mFirstNameEditText.setVisibility(View.VISIBLE);
                                    mLastNameEditText.setVisibility(View.VISIBLE);
                                    mInviteCodeEditText.setVisibility(View.VISIBLE);
                                    mLoginAppCompatButton.setVisibility(View.VISIBLE);
                                    mLoadingContentLoadingProgressBar.setVisibility(View.GONE);

                                }
                            });
                        }
                    }
                });
    }
}