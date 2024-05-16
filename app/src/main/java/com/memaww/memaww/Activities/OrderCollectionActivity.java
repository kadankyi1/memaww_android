package com.memaww.memaww.Activities;

import static gh.com.payswitch.thetellerandroid.thetellerConstants.theteller_results;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;
import androidx.core.widget.ContentLoadingProgressBar;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.memaww.memaww.Fragments.BulkyItemsFormFragment;
import com.memaww.memaww.Fragments.CollectionFormFragment;
import com.memaww.memaww.Fragments.ConfirmOrderFragment;
import com.memaww.memaww.Fragments.DiscountFormFragment;
import com.memaww.memaww.Fragments.LightWeightItemsFormFragment;
import com.memaww.memaww.Fragments.SpecialNotesFormFragment;
import com.memaww.memaww.R;
import com.memaww.memaww.Util.Config;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

import co.paystack.android.Paystack;
import co.paystack.android.PaystackSdk;
import co.paystack.android.Transaction;
import co.paystack.android.model.Card;
import co.paystack.android.model.Charge;
import gh.com.payswitch.thetellerandroid.thetellerManager;

public class OrderCollectionActivity extends AppCompatActivity implements View.OnClickListener, CollectionFormFragment.onCollectionFormDoneButtonClickedEventListener,
        LightWeightItemsFormFragment.onLightWeightItemsFormDoneButtonClickedEventListener, BulkyItemsFormFragment.onBulkyItemsFormDoneButtonClickedEventListener,
        SpecialNotesFormFragment.onSpecialNotesFormDoneButtonClickedEventListener, DiscountFormFragment.onDiscountFormDoneButtonClickedEventListener,
        ConfirmOrderFragment.onConfirmOrderDoneButtonClickedEventListener {

    private ImageView mBackImageView;
    private CardView mCollectionAndDropOffInfoCardView, mLightweighItemsInfoCardView, mBulkyItemsInfoCardView, mSpecialInstructionsCardView, mDiscountCardView;
    private TextView mCollectionAndDropOffLocationTextView, mCollectionTimeTextView, mContactPersonTextView, mLightWeightItemsTextView,
            mBulkyItemsTextView, mSpecialNotesTextView, mDiscountTextView;
    private int fragmentOpenStatus = 0, allLightWeightItems = 0, allBulkyItems = 0;
    private AppCompatButton mProceedButton;
    private ContentLoadingProgressBar mLoadingContentLoadingProgressBar;
    private Dialog.OnCancelListener cancelListenerActive1;
    private String collectionAndDropOffLocation = "", collectionAndDropOffLocationGPS = "", collectionTime = "", contactPerson = "", lightWeightItemsJustWash = "0", lightWeightItemsWashAndIron = "0",
            lightWeightItemsJustIron = "0", bulkyItemsJustWash = "0", bulkyItemsWashAndIron = "0", specialNotesOnOrder = "", discountOnOrder = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_collection);


        mBackImageView = findViewById(R.id.activity_ordercollection_menuholder_back_imageview);
        mCollectionAndDropOffInfoCardView = findViewById(R.id.activity_ordercollection_formitemsholderscrollview_pickup_constraintlayout);
        mLightweighItemsInfoCardView = findViewById(R.id.activity_ordercollection_formitemsholderscrollview_justwash_constraintlayout);
        mBulkyItemsInfoCardView = findViewById(R.id.activity_ordercollection_formitemsholderscrollview_justwashnonwearables_constraintlayout);
        mSpecialInstructionsCardView = findViewById(R.id.activity_ordercollection_formitemsholderscrollview_specialnote_constraintlayout);
        mDiscountCardView = findViewById(R.id.activity_ordercollection_formitemsholderscrollview_discount_constraintlayout);
        mProceedButton = findViewById(R.id.activity_ordercollection_proceedholder_button);

        mCollectionAndDropOffLocationTextView = findViewById(R.id.activity_ordercollection_date_textview);
        mCollectionTimeTextView = findViewById(R.id.activity_ordercollection_time_textview);
        mContactPersonTextView = findViewById(R.id.activity_ordercollection_collectperson_textview);
        mLightWeightItemsTextView = findViewById(R.id.activity_ordercollection_justwashquantity_textview);
        mBulkyItemsTextView = findViewById(R.id.activity_ordercollection_justwashnonwearablesquantity_textview);
        mSpecialNotesTextView = findViewById(R.id.activity_ordercollection_specialnotesquantity_textview);
        mDiscountTextView = findViewById(R.id.activity_ordercollection_discountquantity_textview);
        mLoadingContentLoadingProgressBar = findViewById(R.id.activity_booking_loader);

        // NEEDED THINGS TO BE DONE ONE-TIME
        mBackImageView.setOnClickListener(this);
        mCollectionAndDropOffInfoCardView.setOnClickListener(this);
        mLightweighItemsInfoCardView.setOnClickListener(this);
        mBulkyItemsInfoCardView.setOnClickListener(this);
        mSpecialInstructionsCardView.setOnClickListener(this);
        mDiscountCardView.setOnClickListener(this);
        mProceedButton.setOnClickListener(this);

        cancelListenerActive1 = new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                finish();
            }
        };

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == mBackImageView.getId()) {
            //finish();
            onBackPressed();
        } else if (view.getId() == mCollectionAndDropOffInfoCardView.getId()) {
            if (fragmentOpenStatus == 0) {
                fragmentOpenStatus = 1;
                mProceedButton.setVisibility(View.INVISIBLE);
                Config.openFragment(getSupportFragmentManager(), R.id.activity_ordercollection_formholder_fragment, CollectionFormFragment.newInstance("", ""), "CollectionFormFragment", 3);

            }
        } else if (view.getId() == mLightweighItemsInfoCardView.getId()) {
            if (fragmentOpenStatus == 0) {
                fragmentOpenStatus = 1;
                mProceedButton.setVisibility(View.INVISIBLE);
                Config.openFragment(getSupportFragmentManager(), R.id.activity_ordercollection_formholder_fragment, LightWeightItemsFormFragment.newInstance("", ""), "LightWeightItemsFormFragment", 3);

            }
        } else if (view.getId() == mBulkyItemsInfoCardView.getId()) {
            if (fragmentOpenStatus == 0) {
                fragmentOpenStatus = 1;
                mProceedButton.setVisibility(View.INVISIBLE);
                Config.openFragment(getSupportFragmentManager(), R.id.activity_ordercollection_formholder_fragment, BulkyItemsFormFragment.newInstance("", ""), "BulkyItemsFormFragment", 3);

            }
        } else if (view.getId() == mSpecialInstructionsCardView.getId()) {
            if (fragmentOpenStatus == 0) {
                fragmentOpenStatus = 1;
                mProceedButton.setVisibility(View.INVISIBLE);
                Config.openFragment(getSupportFragmentManager(), R.id.activity_ordercollection_formholder_fragment, SpecialNotesFormFragment.newInstance("", ""), "SpecialNotesFormFragment", 3);

            }
        } else if (view.getId() == mDiscountCardView.getId()) {
            if (fragmentOpenStatus == 0) {
                fragmentOpenStatus = 1;
                mProceedButton.setVisibility(View.INVISIBLE);
                Config.openFragment(getSupportFragmentManager(), R.id.activity_ordercollection_formholder_fragment, DiscountFormFragment.newInstance("", ""), "DiscountFormFragment", 3);
            }
        } else if (view.getId() == mProceedButton.getId()) {

            //SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            //String currentDateandTime = sdf.format(new Date());

            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            String currentDateandTime = sdf.format(new Date());

            placeOrder(collectionAndDropOffLocation, collectionAndDropOffLocationGPS, currentDateandTime, contactPerson, "", collectionAndDropOffLocationGPS, "", lightWeightItemsJustWash, lightWeightItemsWashAndIron, lightWeightItemsJustIron, bulkyItemsJustWash, bulkyItemsWashAndIron, specialNotesOnOrder, discountOnOrder);

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("PAYMENT", theteller_results);

        try {
            JSONObject payment_response = new JSONObject(theteller_results);
            updateOrderPayment(payment_response.getString("transaction_id"), payment_response.getString("status"), payment_response.getString("reason"), "PaySwitch", "1");
            if(payment_response.getString("status").trim().equalsIgnoreCase("approved")){
                cancelListenerActive1 = Config.showDialogType1(OrderCollectionActivity.this, "Order Successful", "We will be on our way. Check progress in the Order Tab", "show-positive-image", cancelListenerActive1, false,  "Finish","");
            } else {
                Config.showToastType1(OrderCollectionActivity.this, payment_response.getString("reason"));
                popFragment();
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Config.showToastType1(OrderCollectionActivity.this, "Payment error. Try again and if it persists, please call us");
            popFragment();
        }

    }

    @Override
    public void collectionFormDoneButtonClicked(String collectionLocation, String collectionLocationGPS, String collectionDateTime, String collectionContactPhone) {
        Log.e("collectionFormDone", collectionLocation + ", " + collectionLocationGPS + ", " + collectionDateTime + ", " + collectionContactPhone);
        fragmentOpenStatus = 0;
        mProceedButton.setVisibility(View.VISIBLE);
        if(collectionLocationGPS.equalsIgnoreCase("0")){
            if(!collectionLocation.equalsIgnoreCase("Not Set")  &&  !collectionLocation.trim().equalsIgnoreCase("")){
                collectionAndDropOffLocation = collectionLocation;
                mCollectionAndDropOffLocationTextView.setText((collectionAndDropOffLocation.length() > 15) ? collectionAndDropOffLocation.substring(0, 13)+"..." : collectionAndDropOffLocation);
            }
        } else {
            collectionAndDropOffLocationGPS = collectionLocationGPS;
            mCollectionAndDropOffLocationTextView.setText("Current Location");
        }

        if(!collectionContactPhone.equalsIgnoreCase("Not Set") &&  !collectionContactPhone.trim().equalsIgnoreCase("")){
            contactPerson = collectionContactPhone;
            mContactPersonTextView.setText(contactPerson);
        }

        if(!collectionDateTime.equalsIgnoreCase("Not Set") &&  !collectionDateTime.trim().equalsIgnoreCase("")){
            collectionTime = collectionDateTime;
            mCollectionTimeTextView.setText(collectionTime);
        }


    }


    @Override
    public void lightWeightItemsFormDoneButtonClicked(String justWashItemsQuantity, String washAndIronItemsQuantity, String justIronItemsQuantity) {
        //Config.showToastType1(OrderCollectionActivity.this, justWashItemsQuantity + ", " + washAndIronItemsQuantity);
        fragmentOpenStatus = 0;
        mProceedButton.setVisibility(View.VISIBLE);
        lightWeightItemsJustWash = justWashItemsQuantity;
        lightWeightItemsWashAndIron = washAndIronItemsQuantity;
        lightWeightItemsJustIron = justIronItemsQuantity;

        allLightWeightItems = Integer.valueOf(lightWeightItemsJustWash) + Integer.valueOf(lightWeightItemsWashAndIron) + Integer.valueOf(lightWeightItemsJustIron);
        mLightWeightItemsTextView.setText(String.valueOf(allLightWeightItems) + " Items");
    }

    @Override
    public void bulkyItemsFormDoneButtonClicked(String justWashItemsQuantity, String washAndIronItemsQuantity) {
        //Config.showToastType1(OrderCollectionActivity.this, justWashItemsQuantity + ", " + washAndIronItemsQuantity);
        fragmentOpenStatus = 0;
        mProceedButton.setVisibility(View.VISIBLE);
        bulkyItemsJustWash = justWashItemsQuantity;
        bulkyItemsWashAndIron = washAndIronItemsQuantity;

        allBulkyItems = Integer.valueOf(bulkyItemsJustWash) + Integer.valueOf(bulkyItemsWashAndIron);
        mBulkyItemsTextView.setText(String.valueOf(allBulkyItems) + " Items");
    }


    @Override
    public void specialNotesFormDoneButtonClicked(String specialNotes) {
        specialNotesOnOrder = specialNotes;
        mSpecialNotesTextView.setText(specialNotesOnOrder);
    }


    @Override
    public void discountFormDoneButtonClicked(String discount) {
        discountOnOrder = discount;
        mDiscountTextView.setText(discountOnOrder);
    }

    @Override
    public void confirmOrderDoneButtonClicked(String transactionId, String action) {
        updateOrderPayment(transactionId, "pay_on_pickup", "User will pay on pickup", "PAY-ON-PICKUP", "1");
        cancelListenerActive1 = Config.showDialogType1(OrderCollectionActivity.this, "Order Successful", "We will be on our way. Check progress in the Order Tab", "show-positive-image", cancelListenerActive1, false, "Finish", "");
    }

    public void popFragment(){
        fragmentOpenStatus = 0;
        mProceedButton.setVisibility(View.VISIBLE);
        getSupportFragmentManager().popBackStack();
        mCollectionAndDropOffInfoCardView.setVisibility(View.VISIBLE);
        mLightweighItemsInfoCardView.setVisibility(View.VISIBLE);
        mBulkyItemsInfoCardView.setVisibility(View.VISIBLE);
        mSpecialInstructionsCardView.setVisibility(View.VISIBLE);
        mDiscountCardView.setVisibility(View.VISIBLE);
        mProceedButton.setVisibility(View.VISIBLE);
        mLoadingContentLoadingProgressBar.setVisibility(View.INVISIBLE);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();

        fragmentOpenStatus = 0;
        mProceedButton.setVisibility(View.VISIBLE);
        mCollectionAndDropOffInfoCardView.setVisibility(View.VISIBLE);
        mLightweighItemsInfoCardView.setVisibility(View.VISIBLE);
        mBulkyItemsInfoCardView.setVisibility(View.VISIBLE);
        mSpecialInstructionsCardView.setVisibility(View.VISIBLE);
        mDiscountCardView.setVisibility(View.VISIBLE);
        mProceedButton.setVisibility(View.VISIBLE);
        mLoadingContentLoadingProgressBar.setVisibility(View.INVISIBLE);
    }


    /*
    // This is the subroutine you will call after creating the charge
    // adding a card and setting the access_code
    public void performCharge(Card card){
        //create a Charge object
        Charge charge = new Charge();
        charge.setCard(card); //sets the card to charge
        charge.setAmount(100);

        PaystackSdk.chargeCard(OrderCollectionActivity.this, charge, new Paystack.TransactionCallback() {
            @Override
            public void onSuccess(Transaction transaction) {
                // This is called only after transaction is deemed successful.
                // Retrieve the transaction, and send its reference to your server
                // for verification.
            }


            @Override
            public void beforeValidate(Transaction transaction) {
                // This is called only before requesting OTP.
                // Save reference so you may send to server. If
                // error occurs with OTP, you should still verify on server.
            }

           @Override
           public void showLoading(Boolean isProcessing) {
               // This is called whenever the SDK makes network requests.
               // Use this to display loading indicators in your application UI
           }

            @Override
            public void onError(Throwable error, Transaction transaction) {
                //handle error here
            }

        });
    }
    */


    public void updateOrderPayment(final String orderId, final String orderPayStatus, final String orderPaymentDetails,
                                   final String orderPaymentMethod, final String purge) {

        Log.e("SERVER-REQUEST", "orderId: " + orderId);
        Log.e("SERVER-REQUEST", "orderPayStatus: " + orderPayStatus);
        Log.e("SERVER-REQUEST", "orderPaymentDetails: " + orderPaymentDetails);
        Log.e("SERVER-REQUEST", "orderPaymentMethod: " + orderPaymentMethod);
        Log.e("SERVER-REQUEST", "purge: " + purge);
        Log.e("SERVER-REQUEST", "app_version_code: " + String.valueOf(Config.getAppVersionCode(getApplicationContext())));

        AndroidNetworking.post(Config.LINK_UPDATE_ORDER_PAYMENT_STATUS)
                .addHeaders("Accept", "application/json")
                .addHeaders("Authorization", "Bearer " + Config.getSharedPreferenceString(OrderCollectionActivity.this, Config.SHARED_PREF_KEY_USER_CREDENTIALS_USER_PASSWORD_ACCESS_TOKEN))
                .addBodyParameter("order_id", orderId)
                .addBodyParameter("order_payment_status", orderPayStatus)
                .addBodyParameter("order_payment_details", orderPaymentDetails)
                .addBodyParameter("order_payment_method", orderPaymentMethod)
                .addBodyParameter("purge", "1")
                .addBodyParameter("app_type", "ANDROID")
                .addBodyParameter("app_version_code", String.valueOf(Config.getAppVersionCode(getApplicationContext())))
                .setTag("update_order")
                .setPriority(Priority.HIGH)
                .build().getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        if (!OrderCollectionActivity.this.isFinishing() && getApplicationContext() != null) {
                            Log.e("SERVER-REQUEST", "response: " + response.toString());
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        if (!OrderCollectionActivity.this.isFinishing() && getApplicationContext() != null) {
                            Log.e("SERVER-REQUEST", "anError: " + anError.getErrorDetail());
                            Log.e("SERVER-REQUEST", "anError: " + anError.getMessage());
                        }
                    }
                });

    }

    public void placeOrder(final String collect_loc_raw, final String collect_loc_gps, final String collect_datetime
            , final String contact_person_phone, final String drop_loc_raw, final String drop_loc_gps, final String drop_datetime
            , final String smallitems_justwash_quantity, final String smallitems_washandiron_quantity, final String smallitems_justiron_quantity
            , final String bigitems_justwash_quantity, final String bigitems_washandiron_quantity, final String specialNotes, final String discount) {

        if (!this.isFinishing() && getApplicationContext() != null) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    mCollectionAndDropOffInfoCardView.setVisibility(View.INVISIBLE);
                    mLightweighItemsInfoCardView.setVisibility(View.INVISIBLE);
                    mBulkyItemsInfoCardView.setVisibility(View.INVISIBLE);
                    mSpecialInstructionsCardView.setVisibility(View.INVISIBLE);
                    mDiscountCardView.setVisibility(View.INVISIBLE);
                    mProceedButton.setVisibility(View.INVISIBLE);
                    mLoadingContentLoadingProgressBar.setVisibility(View.VISIBLE);
                }
            });
        }

        Log.e("SERVER-REQUEST", "collect_loc_raw: " + collect_loc_raw);
        Log.e("SERVER-REQUEST", "collect_loc_gps: " + collect_loc_gps);
        Log.e("SERVER-REQUEST", "collect_datetime: " + collect_datetime);
        Log.e("SERVER-REQUEST", "contact_person_phone: " + contact_person_phone);
        Log.e("SERVER-REQUEST", "drop_loc_raw: " + drop_loc_raw);
        Log.e("SERVER-REQUEST", "drop_loc_gps: " + drop_loc_gps);
        Log.e("SERVER-REQUEST", "drop_datetime: " + drop_datetime);
        Log.e("SERVER-REQUEST", "smallitems_justwash_quantity: " + smallitems_justwash_quantity);
        Log.e("SERVER-REQUEST", "smallitems_washandiron_quantity: " + smallitems_washandiron_quantity);
        Log.e("SERVER-REQUEST", "smallitems_justiron_quantity: " + smallitems_justiron_quantity);
        Log.e("SERVER-REQUEST", "bigitems_justwash_quantity: " + bigitems_justwash_quantity);
        Log.e("SERVER-REQUEST", "bigitems_washandiron_quantity: " + bigitems_washandiron_quantity);
        Log.e("SERVER-REQUEST", "special_instructions: " + specialNotes);
        Log.e("SERVER-REQUEST", "discount_code: " + discount);
        Log.e("SERVER-REQUEST", "app_version_code: " + String.valueOf(Config.getAppVersionCode(getApplicationContext())));


        AndroidNetworking.post(Config.LINK_COLLECTION_ORDER)
                .addHeaders("Accept", "application/json")
                .addHeaders("Authorization", "Bearer " + Config.getSharedPreferenceString(OrderCollectionActivity.this, Config.SHARED_PREF_KEY_USER_CREDENTIALS_USER_PASSWORD_ACCESS_TOKEN))
                .addBodyParameter("collect_loc_raw", collect_loc_raw)
                .addBodyParameter("collect_loc_gps", collect_loc_gps)
                .addBodyParameter("collect_datetime", collect_datetime)
                .addBodyParameter("contact_person_phone", contact_person_phone)
                .addBodyParameter("drop_loc_raw", drop_loc_raw)
                .addBodyParameter("drop_loc_gps", drop_loc_gps)
                .addBodyParameter("drop_datetime", drop_datetime)
                .addBodyParameter("smallitems_justwash_quantity", smallitems_justwash_quantity)
                .addBodyParameter("smallitems_washandiron_quantity", smallitems_washandiron_quantity)
                .addBodyParameter("smallitems_justiron_quantity", smallitems_justiron_quantity)
                .addBodyParameter("bigitems_justwash_quantity", bigitems_justwash_quantity)
                .addBodyParameter("bigitems_washandiron_quantity", bigitems_washandiron_quantity)
                .addBodyParameter("discount_code", discount)
                .addBodyParameter("special_instructions", specialNotes)
                .addBodyParameter("app_type", "ANDROID")
                .addBodyParameter("app_version_code", String.valueOf(Config.getAppVersionCode(getApplicationContext())))
                .setTag("get_suggestion")
                .setPriority(Priority.HIGH)
                .build().getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        if (!OrderCollectionActivity.this.isFinishing() && getApplicationContext() != null) {
                            Log.e("SERVER-REQUEST", "response: " + response.toString());
                            try {
                                JSONObject main_response = new JSONObject(response);
                                String myStatus = main_response.getString("status");
                                final String myStatusMessage = main_response.getString("message");

                                if (myStatus.equalsIgnoreCase("success")) {
                                    String discountPercentage = main_response.getString("discount_percentage");
                                    String payOnline = main_response.getString("pay_online");
                                    String payOnPickup = main_response.getString("pay_on_pickup");
                                    String originalPrice = main_response.getString("original_price");
                                    String discountAmount = main_response.getString("discount_amount");
                                    String priceFinal = main_response.getString("price_final");
                                    String priceFinalNoCurrency = main_response.getString("price_final_no_currency");
                                    String txnNarration = main_response.getString("txn_narration");
                                    String txnReference = main_response.getString("txn_reference");
                                    String merchantId = main_response.getString("merchant_id");
                                    String merchantApiUser = main_response.getString("merchant_api_user");
                                    String merchantApiKey = main_response.getString("merchant_api_key");
                                    String returnUrl = main_response.getString("return_url");
                                    String userEmail = main_response.getString("user_email");
                                    if (fragmentOpenStatus == 0) {
                                        fragmentOpenStatus = 1;
                                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                                            @Override
                                            public void run() {
                                                Config.openFragment(getSupportFragmentManager(), R.id.activity_ordercollection_formholder_fragment, ConfirmOrderFragment.newInstance(originalPrice, discountAmount, priceFinal, payOnline, payOnPickup, priceFinalNoCurrency, txnNarration, txnReference, merchantId, merchantApiUser, merchantApiKey, returnUrl, userEmail), "ConfirmOrderFragment", 3);
                                                mLoadingContentLoadingProgressBar.setVisibility(View.INVISIBLE);
                                            }
                                        });
                                    }

                                } else {
                                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                                        @Override
                                        public void run() {
                                            Config.showToastType1(OrderCollectionActivity.this,  myStatusMessage);
                                            mCollectionAndDropOffInfoCardView.setVisibility(View.VISIBLE);
                                            mLightweighItemsInfoCardView.setVisibility(View.VISIBLE);
                                            mBulkyItemsInfoCardView.setVisibility(View.VISIBLE);
                                            mSpecialInstructionsCardView.setVisibility(View.VISIBLE);
                                            mDiscountCardView.setVisibility(View.VISIBLE);
                                            mProceedButton.setVisibility(View.VISIBLE);
                                            mLoadingContentLoadingProgressBar.setVisibility(View.INVISIBLE);

                                        }
                                    });
                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                                new Handler(Looper.getMainLooper()).post(new Runnable() {
                                    @Override
                                    public void run() {
                                        Config.showDialogType1(OrderCollectionActivity.this, getString(R.string.error), getString(R.string.an_unexpected_error_occured), "", null, false, "", "");
                                        mCollectionAndDropOffInfoCardView.setVisibility(View.VISIBLE);
                                        mLightweighItemsInfoCardView.setVisibility(View.VISIBLE);
                                        mBulkyItemsInfoCardView.setVisibility(View.VISIBLE);
                                        mSpecialInstructionsCardView.setVisibility(View.VISIBLE);
                                        mDiscountCardView.setVisibility(View.VISIBLE);
                                        mProceedButton.setVisibility(View.VISIBLE);
                                        mLoadingContentLoadingProgressBar.setVisibility(View.INVISIBLE);
                                    }
                                });
                            }
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("SERVER-REQUEST", "anError: " + anError.getErrorDetail());
                        Log.e("SERVER-REQUEST", "anError: " + anError.getMessage());
                        if (!OrderCollectionActivity.this.isFinishing() && getApplicationContext() != null) {
                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                @Override
                                public void run() {
                                    Config.showToastType1(OrderCollectionActivity.this, getResources().getString(R.string.check_your_internet_connection_and_try_again));
                                    mCollectionAndDropOffInfoCardView.setVisibility(View.VISIBLE);
                                    mLightweighItemsInfoCardView.setVisibility(View.VISIBLE);
                                    mBulkyItemsInfoCardView.setVisibility(View.VISIBLE);
                                    mSpecialInstructionsCardView.setVisibility(View.VISIBLE);
                                    mDiscountCardView.setVisibility(View.VISIBLE);
                                    mProceedButton.setVisibility(View.VISIBLE);
                                    mLoadingContentLoadingProgressBar.setVisibility(View.INVISIBLE);

                                }
                            });
                        }
                    }
                });

    }

}