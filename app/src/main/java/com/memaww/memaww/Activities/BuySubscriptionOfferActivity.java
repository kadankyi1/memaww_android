package com.memaww.memaww.Activities;

import static gh.com.payswitch.thetellerandroid.thetellerConstants.theteller_results;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.core.widget.ContentLoadingProgressBar;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.ScrollView;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.memaww.memaww.Fragments.ConfirmOrderFragment;
import com.memaww.memaww.Fragments.ConfirmSubscriptionOrderFragment;
import com.memaww.memaww.R;
import com.memaww.memaww.Util.Config;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class BuySubscriptionOfferActivity extends AppCompatActivity implements View.OnClickListener, ConfirmSubscriptionOrderFragment.onConfirmSubscriptionOrderDoneButtonClickedEventListener {

    private int numberOfPeople = 0, numberOfMonths = 0, defaultDay = 0;
    private NumberPicker.OnValueChangeListener mPickupDaySetNumberListener;
    private String subscriptionCurrency = "", finalPriceNoCurrency = "0", pickupTimeString = "", pickupTimeNiceFormatString = "", subscriptionTxnID = "", userEmail = "", txnNarration = "",
            txnReference = "", collectionLoc = "", merchantId = "", merchantApiUser = "", merchantApiKey = "", returnUrl = "",
            packageDescription = "", subscriptionCountryId = "", collectionLocGPS = "", oldTxnReference = "", pickupDay = "Saturday";
    private ScrollView mMainHolderViewScrollView;
    private AppCompatButton mGetPriceAppCompatButton, mPaidButtonAppCompatButton;
    private EditText mNumberOfPeopleEditText, mPickupLocationEditText, mPickupDayTextView;
    private TextView mInfoTextView, m1MonthSubscriptionTextView, m3MonthsSubscriptionTextView, m6MonthsSubscriptionTextView,
            m12MonthsSubscriptionTextView, m7amPickupTimeTextView, m12pmPickupTimeTextView, m4pmPickupTimeTextView;
    private Boolean useCurrentLocation = false, paidForNewSubscription = false, useCurrentLocationIconClicked = false;
    private Dialog.OnCancelListener cancelListenerActive1;
    private ImageView mBackImageView, mReloadImageView, mGetMyLocationImageView;
    private ContentLoadingProgressBar mProgressBarContentLoadingProgressBar;
    FusedLocationProviderClient mFusedLocationClient;
    int PERMISSION_ID = 44;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_subscription_offer);


        mBackImageView = findViewById(R.id.activity_buysubscriptionoffer_menuholder_back_imageview);
        mMainHolderViewScrollView = findViewById(R.id.activity_buysubscriptionoffer_formitemsholder_scrollview);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(BuySubscriptionOfferActivity.this);
        mNumberOfPeopleEditText = findViewById(R.id.activity_buysubscriptionoffer_numberofpeople_edittext);

        m1MonthSubscriptionTextView = findViewById(R.id.activity_buysubscriptionoffer_timeinfo_1month_textview);
        m3MonthsSubscriptionTextView = findViewById(R.id.activity_buysubscriptionoffer_timeinfo_3months_textview);
        m6MonthsSubscriptionTextView = findViewById(R.id.activity_buysubscriptionoffer_timeinfo_6months_textview);
        m12MonthsSubscriptionTextView = findViewById(R.id.activity_buysubscriptionoffer_timeinfo_12months_textview);

        mPickupDayTextView = findViewById(R.id.activity_buysubscriptionoffer_pickuptime_edittext);
        m7amPickupTimeTextView = findViewById(R.id.activity_buysubscriptionoffer_timeinfo_7am_textview);
        m12pmPickupTimeTextView = findViewById(R.id.activity_buysubscriptionoffer_timeinfo_12pm_textview);
        m4pmPickupTimeTextView = findViewById(R.id.activity_buysubscriptionoffer_timeinfo_4pm_textview);

        mPickupLocationEditText = findViewById(R.id.activity_buysubscriptionoffer_pickuplocation_edittext);
        mGetMyLocationImageView = findViewById(R.id.activity_buysubscriptionoffer_pickuplocation_roundedcornerimageview);

        mGetPriceAppCompatButton = findViewById(R.id.activity_buysubscriptionoffer_proceedholder_button);
        mPaidButtonAppCompatButton = findViewById(R.id.activity_checkpaymentsubscription_proceedholder_button);
        mReloadImageView = findViewById(R.id.activity_buysubscriptionoffer_reload_imageview);
        mInfoTextView = findViewById(R.id.activity_buysubscriptionoffer_info_textview);
        mProgressBarContentLoadingProgressBar = findViewById(R.id.activity_buysubscriptionoffer_loader);

        mPaidButtonAppCompatButton.setOnClickListener(this);
        mGetPriceAppCompatButton.setOnClickListener(this);
        mReloadImageView.setOnClickListener(this);
        mBackImageView.setOnClickListener(this);

        m1MonthSubscriptionTextView.setOnClickListener(this);
        m3MonthsSubscriptionTextView.setOnClickListener(this);
        m6MonthsSubscriptionTextView.setOnClickListener(this);
        m12MonthsSubscriptionTextView.setOnClickListener(this);


        // GETTING THE COUNTRIES STRING LIST AND ARRAY SETS
        final String[] daysOfTheWeekStringArraySet = getResources().getStringArray(R.array.days_of_the_week_array);
        final List<String> daysOfTheWeekStringArrayList = Arrays.asList(daysOfTheWeekStringArraySet);

        /*
        mPickupDayTextView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                mPickupDaySetNumberListener = Config.openNumberPickerForCountries(BuySubscriptionOfferActivity.this, mPickupDaySetNumberListener, 0, daysOfTheWeekStringArraySet.length-1, true, getResources().getStringArray(R.array.days_of_the_week_array), 0);
                return true;
            }
        });
        */
        mPickupDayTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPickupDaySetNumberListener = Config.openNumberPickerForCountries(BuySubscriptionOfferActivity.this, mPickupDaySetNumberListener, 0, daysOfTheWeekStringArraySet.length-1, true, getResources().getStringArray(R.array.days_of_the_week_array), 0);
            }
        });
        mPickupDaySetNumberListener = new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int oldVal, int newVal) {
                defaultDay = newVal;
                pickupDay = daysOfTheWeekStringArrayList.get(newVal);

                mPickupDayTextView.setText(daysOfTheWeekStringArrayList.get(newVal));
            }
        };

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

        mGetMyLocationImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // method to get the location
                view.startAnimation(AnimationUtils.loadAnimation(BuySubscriptionOfferActivity.this, R.anim.main_activity_onclick_icon_anim));
                useCurrentLocationIconClicked = true;
                getLastLocation();
            }
        });
    }

    public void popFragment(){
        getSupportFragmentManager().popBackStack();
        mMainHolderViewScrollView.setVisibility(View.VISIBLE);
        mPaidButtonAppCompatButton.setVisibility(View.VISIBLE);
        mGetPriceAppCompatButton.setVisibility(View.VISIBLE);
        mProgressBarContentLoadingProgressBar.setVisibility(View.GONE);
        mReloadImageView.setVisibility(View.GONE);
        mInfoTextView.setVisibility(View.GONE);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(!paidForNewSubscription){
            updateUserSubscription(true, txnReference, String.valueOf(finalPriceNoCurrency), String.valueOf(numberOfPeople), String.valueOf(numberOfMonths), pickupTimeString, (useCurrentLocation && !collectionLocGPS.equalsIgnoreCase("")) ? collectionLocGPS:collectionLoc, packageDescription, subscriptionCountryId, "1");
        }

    }

    @Override
    public void onClick(View view) {
        if(view.getId() == mBackImageView.getId()){
            onBackPressed();
        } else if(view.getId() == mPaidButtonAppCompatButton.getId()){
            updateUserSubscription(true, txnReference, String.valueOf(finalPriceNoCurrency), String.valueOf(numberOfPeople), String.valueOf(numberOfMonths), pickupTimeString, (useCurrentLocation && !collectionLocGPS.equalsIgnoreCase("")) ? collectionLocGPS:collectionLoc, packageDescription, subscriptionCountryId, "0");
        } else if(view.getId() == mGetPriceAppCompatButton.getId()){
            if(mNumberOfPeopleEditText.getText().toString().trim().equalsIgnoreCase("")){
                Config.showToastType1(BuySubscriptionOfferActivity.this, "You need to enter the number of people");
                return;
            }

            numberOfPeople = Integer.valueOf(mNumberOfPeopleEditText.getText().toString().trim());
            if(numberOfPeople < 1 || numberOfPeople > 10){
                Config.showToastType1(BuySubscriptionOfferActivity.this, "Number of people has to be from 1 to 10");
                return;
            }

            if(!useCurrentLocation && mPickupLocationEditText.getText().toString().trim().equalsIgnoreCase("")){
                Config.showToastType1(BuySubscriptionOfferActivity.this, "Set the pickup location");
                return;
            } else if(!useCurrentLocation && !mPickupLocationEditText.getText().toString().trim().equalsIgnoreCase("")){
                collectionLoc =  mPickupLocationEditText.getText().toString().trim();
            }

            if(pickupTimeString.trim().equalsIgnoreCase("")){
                Config.showToastType1(BuySubscriptionOfferActivity.this, "The pickup time has not been set");
                return;
            }
            getSubscriptionPrice(mNumberOfPeopleEditText.getText().toString().trim(), String.valueOf(numberOfMonths),  pickupDay, pickupTimeString, (useCurrentLocation) ? collectionLocGPS:collectionLoc);
        } else if(view.getId() == mReloadImageView.getId()){
            getSubscriptionPrice(mNumberOfPeopleEditText.getText().toString().trim(), String.valueOf(numberOfMonths),  pickupDay, pickupTimeString, (useCurrentLocation) ? collectionLocGPS:collectionLoc);
        } else if(view.getId() == m1MonthSubscriptionTextView.getId()){
            numberOfMonths = 1;
            m1MonthSubscriptionTextView.setBackground(getResources().getDrawable(R.drawable.background_light_blue_pressed_lighter_blue_rounded_all_corners, getApplicationContext().getTheme()));
            m3MonthsSubscriptionTextView.setBackground(getResources().getDrawable(R.drawable.background_white_border_blue_pressed_lighter_blue_rounded_all_corners, getApplicationContext().getTheme()));
            m6MonthsSubscriptionTextView.setBackground(getResources().getDrawable(R.drawable.background_white_border_blue_pressed_lighter_blue_rounded_all_corners, getApplicationContext().getTheme()));
            m12MonthsSubscriptionTextView.setBackground(getResources().getDrawable(R.drawable.background_white_border_blue_pressed_lighter_blue_rounded_all_corners, getApplicationContext().getTheme()));
        } else if(view.getId() == m3MonthsSubscriptionTextView.getId()){
            numberOfMonths = 3;
            m3MonthsSubscriptionTextView.setBackground(getResources().getDrawable(R.drawable.background_light_blue_pressed_lighter_blue_rounded_all_corners, getApplicationContext().getTheme()));
            m1MonthSubscriptionTextView.setBackground(getResources().getDrawable(R.drawable.background_white_border_blue_pressed_lighter_blue_rounded_all_corners, getApplicationContext().getTheme()));
            m6MonthsSubscriptionTextView.setBackground(getResources().getDrawable(R.drawable.background_white_border_blue_pressed_lighter_blue_rounded_all_corners, getApplicationContext().getTheme()));
            m12MonthsSubscriptionTextView.setBackground(getResources().getDrawable(R.drawable.background_white_border_blue_pressed_lighter_blue_rounded_all_corners, getApplicationContext().getTheme()));
        } else if(view.getId() == m6MonthsSubscriptionTextView.getId()){
            numberOfMonths = 6;
            m6MonthsSubscriptionTextView.setBackground(getResources().getDrawable(R.drawable.background_light_blue_pressed_lighter_blue_rounded_all_corners, getApplicationContext().getTheme()));
            m1MonthSubscriptionTextView.setBackground(getResources().getDrawable(R.drawable.background_white_border_blue_pressed_lighter_blue_rounded_all_corners, getApplicationContext().getTheme()));
            m3MonthsSubscriptionTextView.setBackground(getResources().getDrawable(R.drawable.background_white_border_blue_pressed_lighter_blue_rounded_all_corners, getApplicationContext().getTheme()));
            m12MonthsSubscriptionTextView.setBackground(getResources().getDrawable(R.drawable.background_white_border_blue_pressed_lighter_blue_rounded_all_corners, getApplicationContext().getTheme()));
        } else if(view.getId() == m12MonthsSubscriptionTextView.getId()){
            numberOfMonths = 12;
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


    @SuppressLint("MissingPermission")
    private void getLastLocation() {
        // check if permissions are given
        if (checkPermissions()) {

            // check if location is enabled
            if (isLocationEnabled()) {

                // getting last
                // location from
                // FusedLocationClient
                // object
                mFusedLocationClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        Location location = task.getResult();
                        if (location == null) {
                            requestNewLocationData();
                        } else {
                            if(useCurrentLocationIconClicked){

                                useCurrentLocation = true;
                                collectionLocGPS = location.getLatitude() + "," + location.getLongitude() + "";
                                mPickupLocationEditText.setText("My Current Location");
                                mPickupLocationEditText.setEnabled(false);
                                mPickupLocationEditText.setFocusable(false);
                            }
                            //Config.showToastType1(getActivity(), location.getLatitude() + " " + location.getLongitude() + "");
                        }
                    }
                });
            } else {
                Config.showToastType1(BuySubscriptionOfferActivity.this, "Please turn on your location...");
                //Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                //startActivity(intent);
            }
        } else {
            // if permissions aren't available,
            // request for permissions
            requestPermissions();
        }
    }

    @SuppressLint("MissingPermission")
    private void requestNewLocationData() {

        // Initializing LocationRequest
        // object with appropriate methods
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(5);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);

        // setting LocationRequest
        // on FusedLocationClient
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(BuySubscriptionOfferActivity.this);
        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
    }

    private LocationCallback mLocationCallback = new LocationCallback() {

        @Override
        public void onLocationResult(LocationResult locationResult) {
            if(useCurrentLocation) {
                Location mLastLocation = locationResult.getLastLocation();
                collectionLocGPS = mLastLocation.getLatitude() + "," + mLastLocation.getLongitude();
                mPickupLocationEditText.setText("Set To Your Current Location");
            }
            //Config.showToastType1(getActivity(), mLastLocation.getLatitude() + " " + mLastLocation.getLongitude() + "");
        }
    };

    // method to check for permissions
    private boolean checkPermissions() {
        return ActivityCompat.checkSelfPermission(BuySubscriptionOfferActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(BuySubscriptionOfferActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;

        // If we want background location
        // on Android 10.0 and higher,
        // use:
        // ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    // method to request for permissions
    private void requestPermissions() {
        ActivityCompat.requestPermissions(BuySubscriptionOfferActivity.this, new String[]{
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_ID);
    }

    // method to check
    // if location is enabled
    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    // If everything is alright then
    @Override
    public void
    onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_ID) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (checkPermissions()) {
            getLastLocation();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("PAYMENT", theteller_results);

        oldTxnReference = txnReference;
        //txnReference = subscriptionTxnID + getDateTime();
        mPaidButtonAppCompatButton.setVisibility(View.VISIBLE);
        mPaidButtonAppCompatButton.setVisibility(View.VISIBLE);
        try {
            JSONObject payment_response = new JSONObject(theteller_results);
            if(payment_response.getString("status").trim().equalsIgnoreCase("approved")){
                updateUserSubscription(false, payment_response.getString("transaction_id"), String.valueOf(finalPriceNoCurrency), String.valueOf(numberOfPeople), String.valueOf(numberOfMonths), pickupTimeString, (useCurrentLocation) ? collectionLocGPS:collectionLoc, packageDescription, subscriptionCountryId, "0");
                Config.setSharedPreferenceBoolean(getApplicationContext(), Config.SHARED_PREF_KEY_USER_SUBSCRIPTION_IS_SET, true);
                cancelListenerActive1 = Config.showDialogType1(BuySubscriptionOfferActivity.this, "Subscription Successful", "You just saved a lot on laundry. Expect us at your pickup time", "show-positive-image", cancelListenerActive1, false,  "Finish","");
            } else {
                Config.showToastType1(BuySubscriptionOfferActivity.this, payment_response.getString("reason"));
                updateUserSubscription(false, payment_response.getString("transaction_id"), String.valueOf(finalPriceNoCurrency), String.valueOf(numberOfPeople), String.valueOf(numberOfMonths), pickupTimeString, (useCurrentLocation) ? collectionLocGPS:collectionLoc, packageDescription, subscriptionCountryId, "0");
                popFragment();
            }
        } catch (JSONException e) {
            e.printStackTrace();
            //Config.showToastType1(BuySubscriptionOfferActivity.this, "Payment error. Try again and if it persists, please call us");
            updateUserSubscription(false, txnReference, String.valueOf(finalPriceNoCurrency), String.valueOf(numberOfPeople), String.valueOf(numberOfMonths), pickupTimeString, (useCurrentLocation) ? collectionLocGPS:collectionLoc, packageDescription, subscriptionCountryId, "0");
            popFragment();
        }

    }


    public String getDateTime(){
        return new SimpleDateFormat("yyyyMMddHHmmss").format(Calendar.getInstance().getTime());
    }

    public void updateUserSubscription(Boolean reCheckingPayment, final String subscriptionPaymentTransactionId, final String subscriptionAmountPaid, final String subscriptionMaxNumOfPeople,
                                       final String subscriptionMonths, final String subscriptionPickupTime, final String subscriptionPickupLocation,
                                       final String subscriptionPackageDescription, final String subscriptionCountryId, String purge) {
        if (reCheckingPayment && !isFinishing() && getApplicationContext() != null) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    mMainHolderViewScrollView.setVisibility(View.GONE);
                    mPaidButtonAppCompatButton.setVisibility(View.GONE);
                    mGetPriceAppCompatButton.setVisibility(View.GONE);
                    mInfoTextView.setVisibility(View.GONE);
                    mReloadImageView.setVisibility(View.GONE);
                    mProgressBarContentLoadingProgressBar.setVisibility(View.VISIBLE);
                }
            });
        }
        Log.e("SERVER-REQUEST-SUB", "SUBSCRIPTION  HERE 1");
        Log.e("SERVER-REQUEST-SUB", "SUBSCRIPTION PAYMENT ID - " + subscriptionPaymentTransactionId);
        AndroidNetworking.post(Config.LINK_SET_USER_SUBSCRIPTION)
                .addHeaders("Accept", "application/json")
                .addHeaders("Authorization", "Bearer " + Config.getSharedPreferenceString(BuySubscriptionOfferActivity.this, Config.SHARED_PREF_KEY_USER_CREDENTIALS_USER_PASSWORD_ACCESS_TOKEN))
                .addBodyParameter("subscription_payment_transaction_id", subscriptionPaymentTransactionId)
                .addBodyParameter("subscription_amount_paid", subscriptionAmountPaid)
                .addBodyParameter("subscription_max_number_of_people_in_home", subscriptionMaxNumOfPeople)
                .addBodyParameter("subscription_number_of_months", subscriptionMonths)
                .addBodyParameter("subscription_pickup_time", subscriptionPickupTime)
                .addBodyParameter("subscription_pickup_location", subscriptionPickupLocation)
                .addBodyParameter("subscription_package_description", subscriptionPackageDescription)
                .addBodyParameter("subscription_country_id", subscriptionCountryId)
                .addBodyParameter("subscription_purge", purge)
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


                                if (myStatus.trim().equalsIgnoreCase("success")) {
                                    JSONObject subscription = new JSONObject(response).getJSONObject("subscription");
                                    paidForNewSubscription = true;
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

                                    if (reCheckingPayment && !isFinishing() && getApplicationContext() != null) {
                                        cancelListenerActive1 = Config.showDialogType1(BuySubscriptionOfferActivity.this, "Subscription Successful", "You just saved a lot on laundry. Expect us at your pickup time", "show-positive-image", cancelListenerActive1, false,  "Finish","");
                                    }
                                } else {

                                    if (reCheckingPayment && !isFinishing() && getApplicationContext() != null) {
                                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                                            @Override
                                            public void run() {
                                                mMainHolderViewScrollView.setVisibility(View.VISIBLE);
                                                mGetPriceAppCompatButton.setVisibility(View.VISIBLE);
                                                mPaidButtonAppCompatButton.setVisibility(View.VISIBLE);
                                                mProgressBarContentLoadingProgressBar.setVisibility(View.GONE);
                                                mReloadImageView.setVisibility(View.GONE);
                                                mInfoTextView.setVisibility(View.GONE);

                                            }
                                        });
                                    }

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();

                                if (reCheckingPayment && !isFinishing() && getApplicationContext() != null) {
                                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                                        @Override
                                        public void run() {
                                            mMainHolderViewScrollView.setVisibility(View.VISIBLE);
                                            mGetPriceAppCompatButton.setVisibility(View.VISIBLE);
                                            mPaidButtonAppCompatButton.setVisibility(View.VISIBLE);
                                            mProgressBarContentLoadingProgressBar.setVisibility(View.GONE);
                                            mReloadImageView.setVisibility(View.GONE);
                                            mInfoTextView.setVisibility(View.GONE);

                                        }
                                    });
                                }
                            }
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        if (reCheckingPayment && !BuySubscriptionOfferActivity.this.isFinishing() && getApplicationContext() != null) {
                            Log.e("SERVER-REQUEST", "anError: " + anError.getErrorDetail());
                            Log.e("SERVER-REQUEST", "anError: " + anError.getMessage());
                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                @Override
                                public void run() {
                                    mMainHolderViewScrollView.setVisibility(View.VISIBLE);
                                    mPaidButtonAppCompatButton.setVisibility(View.VISIBLE);
                                    mGetPriceAppCompatButton.setVisibility(View.VISIBLE);
                                    mProgressBarContentLoadingProgressBar.setVisibility(View.GONE);
                                    mReloadImageView.setVisibility(View.GONE);
                                    mInfoTextView.setVisibility(View.GONE);

                                }
                            });
                        }
                    }
                });

    }

    private void getSubscriptionPrice(String numberOfPeople, String numberOfMonths, String subscriptionPickupDay, String subscriptionPickupTime, String subscriptionPickupLocation) {

        if (!this.isFinishing() && getApplicationContext() != null) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    mMainHolderViewScrollView.setVisibility(View.GONE);
                    mPaidButtonAppCompatButton.setVisibility(View.GONE);
                    mGetPriceAppCompatButton.setVisibility(View.GONE);
                    mProgressBarContentLoadingProgressBar.setVisibility(View.VISIBLE);
                }
            });
        }

        Log.e("SERVER-REQUEST", "numberOfPeople: " + numberOfPeople);
        Log.e("SERVER-REQUEST", "numberOfMonths: " + numberOfMonths);
        Log.e("SERVER-REQUEST", "subscriptionPickupDay: " + subscriptionPickupDay);
        Log.e("SERVER-REQUEST", "subscriptionPickupTime: " + subscriptionPickupTime);
        Log.e("SERVER-REQUEST", "subscriptionPickupLocation: " + subscriptionPickupLocation);

        AndroidNetworking.post(Config.LINK_GET_SUBSCRIPTION_OFFERS)
                .addHeaders("Accept", "application/json")
                .addHeaders("Authorization", "Bearer " + Config.getSharedPreferenceString(BuySubscriptionOfferActivity.this, Config.SHARED_PREF_KEY_USER_CREDENTIALS_USER_PASSWORD_ACCESS_TOKEN))
                .addBodyParameter("subscription_max_number_of_people_in_home", numberOfPeople)
                .addBodyParameter("subscription_number_of_months", numberOfMonths)
                .addBodyParameter("subscription_pickup_day", subscriptionPickupDay)
                .addBodyParameter("subscription_pickup_time", subscriptionPickupTime)
                .addBodyParameter("subscription_pickup_location", subscriptionPickupLocation)
                .addBodyParameter("app_type", "ANDROID")
                .addBodyParameter("app_version_code", String.valueOf(Config.getAppVersionCode(getApplicationContext())))
                .setTag("get_suggestion")
                .setPriority(Priority.HIGH)
                .build().getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        if (!BuySubscriptionOfferActivity.this.isFinishing() && getApplicationContext() != null) {
                            Log.e("SERVER-REQUEST", "response: " + response.toString());
                            try {
                                JSONObject main_response = new JSONObject(response);
                                String myStatus = main_response.getString("status");
                                final String myStatusMessage = main_response.getString("message");

                                if (myStatus.equalsIgnoreCase("success")) {
                                    String packageInfoDesc = main_response.getString("txn_info");
                                    String txnNarration = main_response.getString("txn_narration");
                                    txnReference = main_response.getString("txn_reference");
                                    String merchantId = main_response.getString("merchant_id");
                                    String merchantApiUser = main_response.getString("merchant_api_user");
                                    String merchantApiKey = main_response.getString("merchant_api_key");
                                    String returnUrl = main_response.getString("return_url");
                                    String originalPrice = main_response.getString("subscription_price");
                                    finalPriceNoCurrency = originalPrice;
                                    String currencySymbol = main_response.getString("currency_symbol");
                                    String packageinfo1 = main_response.getString("packageinfo1");
                                    String packageinfo2 = main_response.getString("packageinfo2");
                                    String packageinfo3 = main_response.getString("packageinfo3");
                                    String packageinfo4 = main_response.getString("packageinfo4");
                                    subscriptionCountryId = main_response.getString("subscription_country_id");
                                    packageDescription = packageinfo1 + " | " + packageinfo2 + " | " + packageinfo3 + " | " +packageinfo4;
                                    //if (fragmentOpenStatus == 0) {
                                    //fragmentOpenStatus = 1;
                                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                                        @Override
                                        public void run() {
                                            Config.openFragment(getSupportFragmentManager(), R.id.activity_buysubscriptionoffer_formholder_fragment, ConfirmSubscriptionOrderFragment.newInstance(originalPrice, currencySymbol, packageinfo1, packageinfo2, packageinfo3, packageinfo4, txnNarration, txnReference, merchantId, merchantApiUser, merchantApiKey, returnUrl, packageInfoDesc), "ConfirmSubscriptionOrderFragment", 3);
                                            mProgressBarContentLoadingProgressBar.setVisibility(View.GONE);
                                            mPaidButtonAppCompatButton.setVisibility(View.GONE);
                                            mGetPriceAppCompatButton.setVisibility(View.GONE);
                                        }
                                    });
                                    //}

                                } else {
                                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                                        @Override
                                        public void run() {
                                            Config.showToastType1(BuySubscriptionOfferActivity.this,  myStatusMessage);
                                            mPaidButtonAppCompatButton.setVisibility(View.GONE);
                                            mProgressBarContentLoadingProgressBar.setVisibility(View.GONE);
                                            mMainHolderViewScrollView.setVisibility(View.VISIBLE);
                                            mGetPriceAppCompatButton.setVisibility(View.VISIBLE);
                                        }
                                    });
                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                                new Handler(Looper.getMainLooper()).post(new Runnable() {
                                    @Override
                                    public void run() {
                                        Config.showDialogType1(BuySubscriptionOfferActivity.this, getString(R.string.error), getString(R.string.an_unexpected_error_occured), "", null, false, "", "");
                                        mPaidButtonAppCompatButton.setVisibility(View.GONE);
                                        mProgressBarContentLoadingProgressBar.setVisibility(View.GONE);
                                        mMainHolderViewScrollView.setVisibility(View.VISIBLE);
                                        mGetPriceAppCompatButton.setVisibility(View.VISIBLE);
                                    }
                                });
                            }
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("SERVER-REQUEST", "anError: " + anError.getErrorDetail());
                        Log.e("SERVER-REQUEST", "anError: " + anError.getMessage());
                        if (!BuySubscriptionOfferActivity.this.isFinishing() && getApplicationContext() != null) {
                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                @Override
                                public void run() {
                                    Config.showToastType1(BuySubscriptionOfferActivity.this, getResources().getString(R.string.check_your_internet_connection_and_try_again));
                                    mPaidButtonAppCompatButton.setVisibility(View.GONE);
                                    mProgressBarContentLoadingProgressBar.setVisibility(View.GONE);
                                    mMainHolderViewScrollView.setVisibility(View.VISIBLE);
                                    mGetPriceAppCompatButton.setVisibility(View.VISIBLE);
                                }
                            });
                        }
                    }
                });
    }

    @Override
    public void ConfirmSubscriptionOrderDoneButtonClicked(String transactionId, String action) {

        /*
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
        updateOrderPayment(false, transactionId, "pay_on_pickup", "User will pay on pickup", "PAY-ON-PICKUP", "1");
        cancelListenerActive1 = Config.showDialogType1(OrderCollectionActivity.this, "Order Successful", "We will be on our way. Check progress in the Order Tab", "show-positive-image", cancelListenerActive1, false, "Finish", "");
    */
    }
}