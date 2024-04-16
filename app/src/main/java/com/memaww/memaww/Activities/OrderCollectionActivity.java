package com.memaww.memaww.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;
import androidx.core.widget.ContentLoadingProgressBar;

import android.app.Dialog;
import android.content.DialogInterface;
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
    private String collectionAndDropOffLocation = "", collectionAndDropOffLocationGPS = "", collectionTime = "", contactPerson = "", lightWeightItemsJustWash = "", lightWeightItemsWashAndIron = "",
            lightWeightItemsJustIron = "", bulkyItemsJustWash = "", bulkyItemsWashAndIron = "", specialNotesOnOrder = "", discountOnOrder = "";

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
                Config.openFragment(getSupportFragmentManager(), R.id.activity_ordercollection_formholder_fragment, CollectionFormFragment.newInstance("", ""), "CollectionFormFragment", 3);

            }
        } else if (view.getId() == mLightweighItemsInfoCardView.getId()) {
            if (fragmentOpenStatus == 0) {
                fragmentOpenStatus = 1;
                Config.openFragment(getSupportFragmentManager(), R.id.activity_ordercollection_formholder_fragment, LightWeightItemsFormFragment.newInstance("", ""), "LightWeightItemsFormFragment", 3);

            }
        } else if (view.getId() == mBulkyItemsInfoCardView.getId()) {
            if (fragmentOpenStatus == 0) {
                fragmentOpenStatus = 1;
                Config.openFragment(getSupportFragmentManager(), R.id.activity_ordercollection_formholder_fragment, BulkyItemsFormFragment.newInstance("", ""), "BulkyItemsFormFragment", 3);

            }
        } else if (view.getId() == mSpecialInstructionsCardView.getId()) {
            if (fragmentOpenStatus == 0) {
                fragmentOpenStatus = 1;
                Config.openFragment(getSupportFragmentManager(), R.id.activity_ordercollection_formholder_fragment, SpecialNotesFormFragment.newInstance("", ""), "SpecialNotesFormFragment", 3);

            }
        } else if (view.getId() == mDiscountCardView.getId()) {
            if (fragmentOpenStatus == 0) {
                fragmentOpenStatus = 1;
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
    public void collectionFormDoneButtonClicked(String collectionLocation, String collectionLocationGPS, String collectionDateTime, String collectionContactPhone) {
        Config.showToastType1(OrderCollectionActivity.this, collectionLocation + ", " + collectionLocationGPS + ", " + collectionDateTime + ", " + collectionContactPhone);
        fragmentOpenStatus = 0;
        if(collectionLocationGPS.equalsIgnoreCase("0")){
            if(!collectionLocation.equalsIgnoreCase("Not Set")  &&  !collectionLocation.trim().equalsIgnoreCase("")){
                collectionAndDropOffLocation = collectionLocation;
                mCollectionAndDropOffLocationTextView.setText(collectionAndDropOffLocation);
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
    public void confirmOrderDoneButtonClicked(String discount) {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        fragmentOpenStatus = 0;
        mCollectionAndDropOffInfoCardView.setVisibility(View.VISIBLE);
        mLightweighItemsInfoCardView.setVisibility(View.VISIBLE);
        mBulkyItemsInfoCardView.setVisibility(View.VISIBLE);
        mSpecialInstructionsCardView.setVisibility(View.VISIBLE);
        mDiscountCardView.setVisibility(View.VISIBLE);
        mProceedButton.setVisibility(View.VISIBLE);
        mLoadingContentLoadingProgressBar.setVisibility(View.INVISIBLE);
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
        Log.e("SERVER-REQUEST", "specialNotes: " + specialNotes);
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
                                String discountPercentage = main_response.getString("discount_percentage");

                                String payOnline = main_response.getString("pay_online");
                                String payOnPickup = main_response.getString("pay_on_pickup");
                                String originalPrice = main_response.getString("original_price");
                                String discountAmount = main_response.getString("discount_amount");
                                String priceFinal = main_response.getString("price_final");

                                final String myStatusMessage = main_response.getString("message");
                                if (myStatus.equalsIgnoreCase("success")) {
                                    if (fragmentOpenStatus == 0) {
                                        fragmentOpenStatus = 1;
                                        Config.openFragment(getSupportFragmentManager(), R.id.activity_ordercollection_formholder_fragment, ConfirmOrderFragment.newInstance(originalPrice, discountAmount, priceFinal, priceFinal, priceFinal), "ConfirmOrderFragment", 3);

                                    }
                                    //cancelListenerActive1 = Config.showDialogType1(OrderCollectionActivity.this, "Confirm Order", "Final Price: " + myPrice, "show-positive-image", cancelListenerActive1, false,  "Ok","");
                                    //return;

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