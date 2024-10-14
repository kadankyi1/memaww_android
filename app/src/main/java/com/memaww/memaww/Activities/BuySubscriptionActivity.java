package com.memaww.memaww.Activities;

import static gh.com.payswitch.thetellerandroid.thetellerConstants.theteller_results;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.widget.ContentLoadingProgressBar;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.memaww.memaww.R;
import com.memaww.memaww.Util.Config;

import org.json.JSONException;
import org.json.JSONObject;

import gh.com.payswitch.thetellerandroid.thetellerManager;

public class BuySubscriptionActivity extends AppCompatActivity implements View.OnClickListener {

    private int numberOfPeople = 0, numberOfMonths = 0, finalPriceNoCurrency = 0;
    private String subscription1To2People1Month = "", subscriptionCurrency = "", subscription3To5People1Month = "", subscription6To10People1Month = "",subscription1To2People3Months,
            subscription3To5People3Months = "", subscription6To10People3Months = "",subscription1To2People6Months = "",subscription3To5People6Months = "",
            subscription6To10People6Months, subscription1To2People12Months = "",subscription3To5People12Months = "", subscription6To10People12Months = "",
            pickupTimeString = "", pickupTimeNiceFormatString = "", subscriptionTxnID = "", userEmail = "", txnNarration = "", txnReference = "", collectionLoc = "",
            merchantId = "", merchantApiUser = "", merchantApiKey = "", returnUrl = "", packageDescription = "", subscriptionCountryId = "";
    private Dialog.OnCancelListener cancelListenerActive1;
    private ScrollView mMainHolderViewScrollView;
    private AppCompatButton mBuyButtonAppCompatButton;
    private EditText mPickupLocationEditText;
    private ImageView mBackImageView, mReloadImageView, mGetMyLocationImageView;
    private TextView mInfoTextView, m1to2PeopleTextView, m3to5PeopleTextView, m6to10PeopleTextView,
            m1MonthSubscriptionTextView, m3MonthsSubscriptionTextView, m6MonthsSubscriptionTextView,
            m12MonthsSubscriptionTextView, mPackageInfo1TextView, mPackageInfo2TextView, mPackageInfo3TextView, mPackageInfo4TextView,
            mTermsTextView, m7amPickupTimeTextView, m12pmPickupTimeTextView, m4pmPickupTimeTextView;
    private ContentLoadingProgressBar mProgressBarContentLoadingProgressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_subscription);

        mBackImageView = findViewById(R.id.activity_buysubscription_menuholder_back_imageview);
        mMainHolderViewScrollView = findViewById(R.id.activity_buysubscription_formitemsholder_scrollview);


        m1to2PeopleTextView = findViewById(R.id.activity_buysubscription_1to2ppl_textview);
        m3to5PeopleTextView = findViewById(R.id.activity_buysubscription_3to5ppl_textview);
        m6to10PeopleTextView = findViewById(R.id.activity_buysubscription_6to10ppl_textview);

        m1MonthSubscriptionTextView = findViewById(R.id.activity_buysubscription_timeinfo_1month_textview);
        m3MonthsSubscriptionTextView = findViewById(R.id.activity_buysubscription_timeinfo_3months_textview);
        m6MonthsSubscriptionTextView = findViewById(R.id.activity_buysubscription_timeinfo_6months_textview);
        m12MonthsSubscriptionTextView = findViewById(R.id.activity_buysubscription_timeinfo_12months_textview);


        m7amPickupTimeTextView = findViewById(R.id.activity_buysubscription_timeinfo_7am_textview);
        m12pmPickupTimeTextView = findViewById(R.id.activity_buysubscription_timeinfo_12pm_textview);
        m4pmPickupTimeTextView = findViewById(R.id.activity_buysubscription_timeinfo_4pm_textview);

        mPickupLocationEditText = findViewById(R.id.activity_buysubscription_pickuplocation_edittext);
        mGetMyLocationImageView = findViewById(R.id.activity_buysubscription_pickuplocation_roundedcornerimageview);

        mPackageInfo1TextView = findViewById(R.id.activity_buysubscription_packageincludesinfo1_textview);
        mPackageInfo2TextView = findViewById(R.id.activity_buysubscription_packageincludesinfo2_textview);
        mPackageInfo3TextView = findViewById(R.id.activity_buysubscription_packageincludesinfo3_textview);
        mPackageInfo4TextView = findViewById(R.id.activity_buysubscription_packageincludesinfo4_textview);

        mTermsTextView = findViewById(R.id.activity_buysubscription_terms_textview);
        mBuyButtonAppCompatButton = findViewById(R.id.activity_buysubscription_proceedholder_button);
        mReloadImageView = findViewById(R.id.activity_buysubscription_reload_imageview);
        mInfoTextView = findViewById(R.id.activity_buysubscription_info_textview);
        mProgressBarContentLoadingProgressBar = findViewById(R.id.activity_buysubscription_loader);

        getSubscriptionOffers();

        mBuyButtonAppCompatButton.setOnClickListener(this);
        mReloadImageView.setOnClickListener(this);
        mBackImageView.setOnClickListener(this);

        m1to2PeopleTextView.setOnClickListener(this);
        m3to5PeopleTextView.setOnClickListener(this);
        m6to10PeopleTextView.setOnClickListener(this);

        m1MonthSubscriptionTextView.setOnClickListener(this);
        m3MonthsSubscriptionTextView.setOnClickListener(this);
        m6MonthsSubscriptionTextView.setOnClickListener(this);
        m12MonthsSubscriptionTextView.setOnClickListener(this);

        m7amPickupTimeTextView.setOnClickListener(this);
        m12pmPickupTimeTextView.setOnClickListener(this);
        m4pmPickupTimeTextView.setOnClickListener(this);

        cancelListenerActive1 = new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {

                Intent intent = new Intent();
                intent.putExtra("RESULTS", "success");
                setResult(9999, intent);
                finish();
                finish();
            }
        };
    }



    @Override
    public void onClick(View view) {
        if(view.getId() == mBackImageView.getId()){
            onBackPressed();
        } else if(view.getId() == mBuyButtonAppCompatButton.getId()){
            if(!mPickupLocationEditText.getText().toString().trim().equalsIgnoreCase("") && numberOfPeople > 0 && numberOfMonths > 0 & !pickupTimeString.trim().equalsIgnoreCase("")){
                collectionLoc = mPickupLocationEditText.getText().toString().trim();
                Log.e("THETELLER", subscriptionTxnID);
                new thetellerManager(this)
                        .setAmount(00.10) //finalPriceNoCurrency
                    //.setAmount(Long.parseLong("01")) //finalPriceNoCurrency
                    .setEmail(userEmail)
                    .setfName(Config.getSharedPreferenceString(getApplicationContext(), Config.SHARED_PREF_KEY_USER_CREDENTIALS_USER_FIRST_NAME))
                    .setlName(Config.getSharedPreferenceString(getApplicationContext(), Config.SHARED_PREF_KEY_USER_CREDENTIALS_USER_LAST_NAME))
                    .setMerchant_id(merchantId)
                    .setNarration(txnNarration)
                    .setApiUser(merchantApiUser)
                    .setApiKey(merchantApiKey)
                    .setTxRef(txnReference)
                    .set3dUrl(returnUrl)
                    .acceptGHMobileMoneyPayments(true)
                    .acceptCardPayments(false)
                    .onStagingEnv(false)
                    .initialize();
            } else {
                Config.showToastType1(BuySubscriptionActivity.this, "Complete the form");
            }
        } else if(view.getId() == mReloadImageView.getId()){
            getSubscriptionOffers();
        } else if(view.getId() == m1to2PeopleTextView.getId()){
            numberOfPeople = 2;
            setFinalPrice();
            m1to2PeopleTextView.setBackground(getResources().getDrawable(R.drawable.background_light_blue_pressed_lighter_blue_rounded_all_corners, getApplicationContext().getTheme()));
            m3to5PeopleTextView.setBackground(getResources().getDrawable(R.drawable.background_white_border_blue_pressed_lighter_blue_rounded_all_corners, getApplicationContext().getTheme()));
            m6to10PeopleTextView.setBackground(getResources().getDrawable(R.drawable.background_white_border_blue_pressed_lighter_blue_rounded_all_corners, getApplicationContext().getTheme()));
        } else if(view.getId() == m3to5PeopleTextView.getId()){
            numberOfPeople = 5;
            setFinalPrice();
            m3to5PeopleTextView.setBackground(getResources().getDrawable(R.drawable.background_light_blue_pressed_lighter_blue_rounded_all_corners, getApplicationContext().getTheme()));
            m1to2PeopleTextView.setBackground(getResources().getDrawable(R.drawable.background_white_border_blue_pressed_lighter_blue_rounded_all_corners, getApplicationContext().getTheme()));
            m6to10PeopleTextView.setBackground(getResources().getDrawable(R.drawable.background_white_border_blue_pressed_lighter_blue_rounded_all_corners, getApplicationContext().getTheme()));
        } else if(view.getId() == m6to10PeopleTextView.getId()){
            numberOfPeople = 10;
            setFinalPrice();
            m6to10PeopleTextView.setBackground(getResources().getDrawable(R.drawable.background_light_blue_pressed_lighter_blue_rounded_all_corners, getApplicationContext().getTheme()));
            m1to2PeopleTextView.setBackground(getResources().getDrawable(R.drawable.background_white_border_blue_pressed_lighter_blue_rounded_all_corners, getApplicationContext().getTheme()));
            m3to5PeopleTextView.setBackground(getResources().getDrawable(R.drawable.background_white_border_blue_pressed_lighter_blue_rounded_all_corners, getApplicationContext().getTheme()));
        } else if(view.getId() == m1MonthSubscriptionTextView.getId()){
            numberOfMonths = 1;
            setFinalPrice();
            m1MonthSubscriptionTextView.setBackground(getResources().getDrawable(R.drawable.background_light_blue_pressed_lighter_blue_rounded_all_corners, getApplicationContext().getTheme()));
            m3MonthsSubscriptionTextView.setBackground(getResources().getDrawable(R.drawable.background_white_border_blue_pressed_lighter_blue_rounded_all_corners, getApplicationContext().getTheme()));
            m6MonthsSubscriptionTextView.setBackground(getResources().getDrawable(R.drawable.background_white_border_blue_pressed_lighter_blue_rounded_all_corners, getApplicationContext().getTheme()));
            m12MonthsSubscriptionTextView.setBackground(getResources().getDrawable(R.drawable.background_white_border_blue_pressed_lighter_blue_rounded_all_corners, getApplicationContext().getTheme()));
        } else if(view.getId() == m3MonthsSubscriptionTextView.getId()){
            numberOfMonths = 3;
            setFinalPrice();
            m3MonthsSubscriptionTextView.setBackground(getResources().getDrawable(R.drawable.background_light_blue_pressed_lighter_blue_rounded_all_corners, getApplicationContext().getTheme()));
            m1MonthSubscriptionTextView.setBackground(getResources().getDrawable(R.drawable.background_white_border_blue_pressed_lighter_blue_rounded_all_corners, getApplicationContext().getTheme()));
            m6MonthsSubscriptionTextView.setBackground(getResources().getDrawable(R.drawable.background_white_border_blue_pressed_lighter_blue_rounded_all_corners, getApplicationContext().getTheme()));
            m12MonthsSubscriptionTextView.setBackground(getResources().getDrawable(R.drawable.background_white_border_blue_pressed_lighter_blue_rounded_all_corners, getApplicationContext().getTheme()));
        } else if(view.getId() == m6MonthsSubscriptionTextView.getId()){
            numberOfMonths = 6;
            setFinalPrice();
            m6MonthsSubscriptionTextView.setBackground(getResources().getDrawable(R.drawable.background_light_blue_pressed_lighter_blue_rounded_all_corners, getApplicationContext().getTheme()));
            m1MonthSubscriptionTextView.setBackground(getResources().getDrawable(R.drawable.background_white_border_blue_pressed_lighter_blue_rounded_all_corners, getApplicationContext().getTheme()));
            m3MonthsSubscriptionTextView.setBackground(getResources().getDrawable(R.drawable.background_white_border_blue_pressed_lighter_blue_rounded_all_corners, getApplicationContext().getTheme()));
            m12MonthsSubscriptionTextView.setBackground(getResources().getDrawable(R.drawable.background_white_border_blue_pressed_lighter_blue_rounded_all_corners, getApplicationContext().getTheme()));
        } else if(view.getId() == m12MonthsSubscriptionTextView.getId()){
            numberOfMonths = 12;
            setFinalPrice();
            m12MonthsSubscriptionTextView.setBackground(getResources().getDrawable(R.drawable.background_light_blue_pressed_lighter_blue_rounded_all_corners, getApplicationContext().getTheme()));
            m1MonthSubscriptionTextView.setBackground(getResources().getDrawable(R.drawable.background_white_border_blue_pressed_lighter_blue_rounded_all_corners, getApplicationContext().getTheme()));
            m3MonthsSubscriptionTextView.setBackground(getResources().getDrawable(R.drawable.background_white_border_blue_pressed_lighter_blue_rounded_all_corners, getApplicationContext().getTheme()));
            m6MonthsSubscriptionTextView.setBackground(getResources().getDrawable(R.drawable.background_white_border_blue_pressed_lighter_blue_rounded_all_corners, getApplicationContext().getTheme()));
        } else if(view.getId() == m7amPickupTimeTextView.getId()){
            pickupTimeString = "07:00";
            pickupTimeNiceFormatString = "7am-8am";
            m7amPickupTimeTextView.setBackground(getResources().getDrawable(R.drawable.background_light_blue_pressed_lighter_blue_rounded_all_corners, getApplicationContext().getTheme()));
            m12pmPickupTimeTextView.setBackground(getResources().getDrawable(R.drawable.background_white_border_blue_pressed_lighter_blue_rounded_all_corners, getApplicationContext().getTheme()));
            m4pmPickupTimeTextView.setBackground(getResources().getDrawable(R.drawable.background_white_border_blue_pressed_lighter_blue_rounded_all_corners, getApplicationContext().getTheme()));
        } else if(view.getId() == m12pmPickupTimeTextView.getId()){
            pickupTimeString = "12:00";
            pickupTimeNiceFormatString = "12pm-1pm";
            m7amPickupTimeTextView.setBackground(getResources().getDrawable(R.drawable.background_white_border_blue_pressed_lighter_blue_rounded_all_corners, getApplicationContext().getTheme()));
            m12pmPickupTimeTextView.setBackground(getResources().getDrawable(R.drawable.background_light_blue_pressed_lighter_blue_rounded_all_corners, getApplicationContext().getTheme()));
            m4pmPickupTimeTextView.setBackground(getResources().getDrawable(R.drawable.background_white_border_blue_pressed_lighter_blue_rounded_all_corners, getApplicationContext().getTheme()));
        } else if(view.getId() == m4pmPickupTimeTextView.getId()){
            pickupTimeString = "16:00";
            pickupTimeNiceFormatString = "4pm-5pm";
            m7amPickupTimeTextView.setBackground(getResources().getDrawable(R.drawable.background_white_border_blue_pressed_lighter_blue_rounded_all_corners, getApplicationContext().getTheme()));
            m12pmPickupTimeTextView.setBackground(getResources().getDrawable(R.drawable.background_white_border_blue_pressed_lighter_blue_rounded_all_corners, getApplicationContext().getTheme()));
            m4pmPickupTimeTextView.setBackground(getResources().getDrawable(R.drawable.background_light_blue_pressed_lighter_blue_rounded_all_corners, getApplicationContext().getTheme()));
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("PAYMENT", theteller_results);
        try {
            JSONObject payment_response = new JSONObject(theteller_results);
            if(payment_response.getString("status").trim().equalsIgnoreCase("approved")){
                updateUserSubscription(payment_response.getString("transaction_id"), String.valueOf(finalPriceNoCurrency), String.valueOf(numberOfPeople), String.valueOf(numberOfMonths), pickupTimeString, collectionLoc, packageDescription, subscriptionCountryId);
                Config.setSharedPreferenceBoolean(getApplicationContext(), Config.SHARED_PREF_KEY_USER_SUBSCRIPTION_IS_SET, true);
                cancelListenerActive1 = Config.showDialogType1(BuySubscriptionActivity.this, "Subscription Successful", "You just saved a lot on laundry. Expect us at your pickup time", "show-positive-image", cancelListenerActive1, false,  "Finish","");
            } else {
                Config.showToastType1(BuySubscriptionActivity.this, payment_response.getString("reason"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Config.showToastType1(BuySubscriptionActivity.this, "Payment error. Try again and if it persists, please call us");
        }

    }

    public void updateUserSubscription(final String subscriptionPaymentTransactionId, final String subscriptionAmountPaid, final String subscriptionMaxNumOfPeople,
                                   final String subscriptionMonths, final String subscriptionPickupTime, final String subscriptionPickupLocation,
                                       final String subscriptionPackageDescription, final String subscriptionCountryId) {

        Log.e("SERVER-REQUEST-SUB", "SUBSCRIPTION  HERE 1");
        AndroidNetworking.post(Config.LINK_SET_USER_SUBSCRIPTION)
                .addHeaders("Accept", "application/json")
                .addHeaders("Authorization", "Bearer " + Config.getSharedPreferenceString(BuySubscriptionActivity.this, Config.SHARED_PREF_KEY_USER_CREDENTIALS_USER_PASSWORD_ACCESS_TOKEN))
                .addBodyParameter("subscription_payment_transaction_id", subscriptionPaymentTransactionId)
                .addBodyParameter("subscription_amount_paid", subscriptionAmountPaid)
                .addBodyParameter("subscription_max_number_of_people_in_home", subscriptionMaxNumOfPeople)
                .addBodyParameter("subscription_number_of_months", subscriptionMonths)
                .addBodyParameter("subscription_pickup_time", subscriptionPickupTime)
                .addBodyParameter("subscription_pickup_location", subscriptionPickupLocation)
                .addBodyParameter("subscription_package_description", subscriptionPackageDescription)
                .addBodyParameter("subscription_country_id", subscriptionCountryId)
                .addBodyParameter("app_type", "ANDROID")
                .addBodyParameter("app_version_code", String.valueOf(Config.getAppVersionCode(getApplicationContext())))
                .setTag("update_order")
                .setPriority(Priority.HIGH)
                .build().getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("SERVER-REQUEST-SUB", "SUBSCRIPTION-response: " + response.toString());
                        if (getApplicationContext() != null) {
                            try {
                                JSONObject main_response = new JSONObject(response);
                                String myStatus = main_response.getString("status");
                                final String myStatusMessage = main_response.getString("message");
                                JSONObject subscription = new JSONObject(response).getJSONObject("subscription");

                                if (myStatus.trim().equalsIgnoreCase("success")) {

                                    Config.setSharedPreferenceBoolean(getApplicationContext(), Config.SHARED_PREF_KEY_USER_SUBSCRIPTION_IS_SET, true);
                                    Config.setSharedPreferenceString(getApplicationContext(), Config.SHARED_PREF_KEY_USER_SUBSCRIPTION_INFO, subscription.getString("subscription_info"));
                                    Config.setSharedPreferenceString(getApplicationContext(), Config.SHARED_PREF_KEY_USER_SUBSCRIPTION_ITEMS_WASHED, String.valueOf(subscription.getString("items_washed_info")));
                                    Config.setSharedPreferenceString(getApplicationContext(), Config.SHARED_PREF_KEY_USER_SUBSCRIPTION_PICKUPS_DONE, String.valueOf(subscription.getString("pickups_count")));
                                    Config.setSharedPreferenceString(getApplicationContext(), Config.SHARED_PREF_KEY_USER_SUBSCRIPTION_MAX_NUMBER_OF_PEOPLE, subscription.getString("subscription_max_number_of_people_in_home"));
                                    Config.setSharedPreferenceString(getApplicationContext(), Config.SHARED_PREF_KEY_USER_SUBSCRIPTION_NUMBER_OF_MONTHS, subscription.getString("subscription_number_of_months"));
                                    Config.setSharedPreferenceString(getApplicationContext(), Config.SHARED_PREF_KEY_USER_SUBSCRIPTION_PICKUP_TIME, subscription.getString("pickup_final_time"));
                                    Config.setSharedPreferenceString(getApplicationContext(), Config.SHARED_PREF_KEY_USER_SUBSCRIPTION_PICKUP_DAY, subscription.getString("subscription_pickup_day"));
                                    Config.setSharedPreferenceString(getApplicationContext(), Config.SHARED_PREF_KEY_USER_SUBSCRIPTION_PICKUP_LOCATION, subscription.getString("subscription_pickup_location"));
                                    Config.setSharedPreferenceString(getApplicationContext(), Config.SHARED_PREF_KEY_USER_SUBSCRIPTION_PACKAGE_DESCRIPTION, subscription.getString("subscription_package_description"));

                                } else {

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        if (!BuySubscriptionActivity.this.isFinishing() && getApplicationContext() != null) {
                            Log.e("SERVER-REQUEST", "anError: " + anError.getErrorDetail());
                            Log.e("SERVER-REQUEST", "anError: " + anError.getMessage());
                        }
                    }
                });

    }

    public void setFinalPrice(){

        Log.e("SERVER-REQUEST", "STARTED setFinalPrice");
        if(numberOfPeople == 2 && numberOfMonths == 1){
            finalPriceNoCurrency = Integer.valueOf(subscription1To2People1Month);
            mBuyButtonAppCompatButton.setText("Subscribe (Total : " + subscriptionCurrency + subscription1To2People1Month + ")");
            mBuyButtonAppCompatButton.setVisibility(View.VISIBLE);
            mTermsTextView.setVisibility(View.VISIBLE);
            Log.e("SERVER-REQUEST", "HERE 1 setFinalPrice");

        } else if(numberOfPeople == 2 && numberOfMonths == 3){
            finalPriceNoCurrency = Integer.valueOf(subscription1To2People3Months);
            mBuyButtonAppCompatButton.setText("Subscribe (Total : " + subscriptionCurrency + subscription1To2People3Months + ")");
            mBuyButtonAppCompatButton.setVisibility(View.VISIBLE);
            mTermsTextView.setVisibility(View.VISIBLE);
        } else if(numberOfPeople == 2 && numberOfMonths == 6){
            finalPriceNoCurrency = Integer.valueOf(subscription1To2People6Months);
            mBuyButtonAppCompatButton.setText("Subscribe (Total : " + subscriptionCurrency + subscription1To2People6Months + ")");
            mBuyButtonAppCompatButton.setVisibility(View.VISIBLE);
            mTermsTextView.setVisibility(View.VISIBLE);
        } else if(numberOfPeople == 2 && numberOfMonths == 12){
            finalPriceNoCurrency = Integer.valueOf(subscription1To2People12Months);
            mBuyButtonAppCompatButton.setText("Subscribe (Total : " + subscriptionCurrency + subscription1To2People12Months + ")");
            mBuyButtonAppCompatButton.setVisibility(View.VISIBLE);
            mTermsTextView.setVisibility(View.VISIBLE);
        }

        else if(numberOfPeople == 5 && numberOfMonths == 1){
            finalPriceNoCurrency = Integer.valueOf(subscription3To5People1Month);
            mBuyButtonAppCompatButton.setText("Subscribe (Total : " + subscriptionCurrency + subscription3To5People1Month + ")");
            mBuyButtonAppCompatButton.setVisibility(View.VISIBLE);
            mTermsTextView.setVisibility(View.VISIBLE);
        } else if(numberOfPeople == 5 && numberOfMonths == 3){
            finalPriceNoCurrency = Integer.valueOf(subscription3To5People3Months);
            mBuyButtonAppCompatButton.setText("Subscribe (Total : " + subscriptionCurrency + subscription3To5People3Months + ")");
            mBuyButtonAppCompatButton.setVisibility(View.VISIBLE);
            mTermsTextView.setVisibility(View.VISIBLE);
        } else if(numberOfPeople == 5 && numberOfMonths == 6){
            finalPriceNoCurrency = Integer.valueOf(subscription3To5People6Months);
            mBuyButtonAppCompatButton.setText("Subscribe (Total : " + subscriptionCurrency + subscription3To5People6Months + ")");
            mBuyButtonAppCompatButton.setVisibility(View.VISIBLE);
            mTermsTextView.setVisibility(View.VISIBLE);
        } else if(numberOfPeople == 5 && numberOfMonths == 12){
            finalPriceNoCurrency = Integer.valueOf(subscription3To5People12Months);
            mBuyButtonAppCompatButton.setText("Subscribe (Total : " + subscriptionCurrency + subscription3To5People12Months + ")");
            mBuyButtonAppCompatButton.setVisibility(View.VISIBLE);
            mTermsTextView.setVisibility(View.VISIBLE);
        }

        else if(numberOfPeople == 10 && numberOfMonths == 1){
            finalPriceNoCurrency = Integer.valueOf(subscription6To10People1Month);
            mBuyButtonAppCompatButton.setText("Subscribe (Total : " + subscriptionCurrency + subscription6To10People1Month + ")");
            mBuyButtonAppCompatButton.setVisibility(View.VISIBLE);
            mTermsTextView.setVisibility(View.VISIBLE);
            mTermsTextView.setVisibility(View.VISIBLE);
        } else if(numberOfPeople == 10 && numberOfMonths == 3){
            finalPriceNoCurrency = Integer.valueOf(subscription6To10People3Months);
            mBuyButtonAppCompatButton.setText("Subscribe (Total : " + subscriptionCurrency + subscription6To10People3Months + ")");
            mBuyButtonAppCompatButton.setVisibility(View.VISIBLE);
            mTermsTextView.setVisibility(View.VISIBLE);
        } else if(numberOfPeople == 10 && numberOfMonths == 6){
            finalPriceNoCurrency = Integer.valueOf(subscription6To10People6Months);
            mBuyButtonAppCompatButton.setText("Subscribe (Total : " + subscriptionCurrency + subscription6To10People6Months + ")");
            mBuyButtonAppCompatButton.setVisibility(View.VISIBLE);
            mTermsTextView.setVisibility(View.VISIBLE);
        } else if(numberOfPeople == 10 && numberOfMonths == 12){
            finalPriceNoCurrency = Integer.valueOf(subscription6To10People12Months);
            mBuyButtonAppCompatButton.setText("Subscribe (Total : " + subscriptionCurrency + subscription6To10People12Months + ")");
            mBuyButtonAppCompatButton.setVisibility(View.VISIBLE);
            mTermsTextView.setVisibility(View.VISIBLE);
        } else {
            finalPriceNoCurrency = 0;
            mBuyButtonAppCompatButton.setVisibility(View.INVISIBLE);
            mTermsTextView.setVisibility(View.INVISIBLE);
        }

        Log.e("SERVER-REQUEST", "ENDED setFinalPrice");
    }

    public void getSubscriptionOffers() {

        if (isFinishing() && getApplicationContext() != null) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    mMainHolderViewScrollView.setVisibility(View.GONE);
                    mBuyButtonAppCompatButton.setVisibility(View.GONE);
                    mTermsTextView.setVisibility(View.GONE);
                    mInfoTextView.setVisibility(View.GONE);
                    mReloadImageView.setVisibility(View.GONE);
                    mProgressBarContentLoadingProgressBar.setVisibility(View.VISIBLE);
                }
            });
        }

        AndroidNetworking.post(Config.LINK_GET_SUBSCRIPTION_OFFERS)
                .addHeaders("Accept", "application/json")
                .addHeaders("Authorization", "Bearer " + Config.getSharedPreferenceString(getApplicationContext(), Config.SHARED_PREF_KEY_USER_CREDENTIALS_USER_PASSWORD_ACCESS_TOKEN))
                .addBodyParameter("app_type", "ANDROID")
                .addBodyParameter("app_version_code", String.valueOf(Config.getAppVersionCode(getApplicationContext())))
                .setTag("get_subscription_offers")
                .setPriority(Priority.HIGH)
                .build().getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        if (!isFinishing() && getApplicationContext() != null) {
                            Log.e("SERVER-REQUEST", "response: " + response.toString());
                            try {
                                JSONObject main_response = new JSONObject(response);
                                String myStatus = main_response.getString("status");
                                final String myStatusMessage = main_response.getString("message");

                                if (myStatus.trim().equalsIgnoreCase("success")) {

                                    subscriptionCountryId = main_response.getString("subscription_country_id");
                                    subscriptionCurrency = main_response.getString("currency_symbol");
                                    subscriptionTxnID = main_response.getString("subscription_id");
                                    txnNarration = main_response.getString("txn_narration");
                                    txnReference = main_response.getString("txn_reference");
                                    merchantId = main_response.getString("merchant_id");
                                    merchantApiUser = main_response.getString("merchant_api_user");
                                    merchantApiKey = main_response.getString("merchant_api_key");
                                    returnUrl = main_response.getString("return_url");
                                    userEmail = main_response.getString("user_email");

                                    subscription1To2People1Month = main_response.getString("sub_1_to_2_ppl_1month");
                                    subscription3To5People1Month = main_response.getString("sub_3_to_5_ppl_1month");
                                    subscription6To10People1Month = main_response.getString("sub_6_to_10_ppl_1month");

                                    subscription1To2People3Months = main_response.getString("sub_1_to_2_ppl_3months");
                                    subscription3To5People3Months = main_response.getString("sub_3_to_5_ppl_3months");
                                    subscription6To10People3Months = main_response.getString("sub_6_to_10_ppl_3months");

                                    subscription1To2People6Months = main_response.getString("sub_1_to_2_ppl_6months");
                                    subscription3To5People6Months = main_response.getString("sub_3_to_5_ppl_6months");
                                    subscription6To10People6Months = main_response.getString("sub_6_to_10_ppl_6months");

                                    subscription1To2People12Months = main_response.getString("sub_1_to_2_ppl_12months");
                                    subscription3To5People12Months = main_response.getString("sub_3_to_5_ppl_12months");
                                    subscription6To10People12Months = main_response.getString("sub_6_to_10_ppl_12months");

                                    mPackageInfo1TextView.setText(main_response.getString("packageinfo1"));
                                    mPackageInfo2TextView.setText(main_response.getString("packageinfo2"));
                                    mPackageInfo3TextView.setText(main_response.getString("packageinfo3"));
                                    mPackageInfo4TextView.setText(main_response.getString("packageinfo4"));

                                    packageDescription = main_response.getString("packageinfo1") + " | " + main_response.getString("packageinfo2") + " | " + main_response.getString("packageinfo3") + " | " + main_response.getString("packageinfo4");

                                    if (!isFinishing() && getApplicationContext() != null) {
                                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                                            @Override
                                            public void run() {
                                                mMainHolderViewScrollView.setVisibility(View.VISIBLE);
                                                mProgressBarContentLoadingProgressBar.setVisibility(View.INVISIBLE);
                                                mReloadImageView.setVisibility(View.INVISIBLE);
                                                mInfoTextView.setVisibility(View.INVISIBLE);

                                            }
                                        });
                                    }
                                } else {

                                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                                        @Override
                                        public void run() {
                                            mMainHolderViewScrollView.setVisibility(View.INVISIBLE);
                                            mBuyButtonAppCompatButton.setVisibility(View.INVISIBLE);
                                            mTermsTextView.setVisibility(View.INVISIBLE);
                                            mProgressBarContentLoadingProgressBar.setVisibility(View.INVISIBLE);
                                            mReloadImageView.setVisibility(View.VISIBLE);
                                            mInfoTextView.setVisibility(View.VISIBLE);
                                            mInfoTextView.setText("No Subscription offers found");
                                        }
                                    });
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                new Handler(Looper.getMainLooper()).post(new Runnable() {
                                    @Override
                                    public void run() {
                                        Config.showToastType1(BuySubscriptionActivity.this, getResources().getString(R.string.an_unexpected_error_occured));
                                        mMainHolderViewScrollView.setVisibility(View.INVISIBLE);
                                        mBuyButtonAppCompatButton.setVisibility(View.INVISIBLE);
                                        mTermsTextView.setVisibility(View.INVISIBLE);
                                        mInfoTextView.setVisibility(View.INVISIBLE);
                                        mProgressBarContentLoadingProgressBar.setVisibility(View.INVISIBLE);
                                        mReloadImageView.setVisibility(View.VISIBLE);
                                    }
                                });
                            }
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("SERVER-REQUEST", "anError: " + anError.toString());
                        if (!isFinishing() && getApplicationContext() != null) {
                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                @Override
                                public void run() {
                                    Config.showToastType1(BuySubscriptionActivity.this, getResources().getString(R.string.check_your_internet_connection_and_try_again));
                                    mMainHolderViewScrollView.setVisibility(View.INVISIBLE);
                                    mBuyButtonAppCompatButton.setVisibility(View.INVISIBLE);
                                    mTermsTextView.setVisibility(View.INVISIBLE);
                                    mInfoTextView.setVisibility(View.INVISIBLE);
                                    mProgressBarContentLoadingProgressBar.setVisibility(View.INVISIBLE);
                                    mReloadImageView.setVisibility(View.VISIBLE);
                                }
                            });
                        }
                    }
                });

    }
}