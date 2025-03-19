package com.memaww.memaww.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.memaww.memaww.Activities.BuySubscriptionOfferActivity;
import com.memaww.memaww.Activities.OrderCollectionActivity;
import com.memaww.memaww.Activities.UpdateActivity;
import com.memaww.memaww.R;
import com.memaww.memaww.Util.Config;

import org.json.JSONException;
import org.json.JSONObject;

import gh.com.payswitch.thetellerandroid.thetellerManager;

public class PlaceOrderFragment extends Fragment implements View.OnClickListener {

    private CardView mGoodOrderCardView, mFastOrderCardView, mActiveSubscriptionCardView, mInactiveSubscriptionCardView;
    private ContentLoadingProgressBar mLoadingContentLoadingProgressBar, mLoadingSubscriptionContentLoadingProgressBar;
    private Thread backgroundThread1 = null, backgroundThread2 = null;
    private AppCompatButton mBuySubscriptionAppCompatButton;
    private TextView mSubscriptionInfoTextView, mPickupsDoneView, mPickupTimeTextView, mItemsWashedTextView;

    public PlaceOrderFragment() {
        // Required empty public constructor
    }

    public static PlaceOrderFragment newInstance() {
        PlaceOrderFragment fragment = new PlaceOrderFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_place_order, container, false);

        mGoodOrderCardView = view.findViewById(R.id.fragment_placeorder_goodorderholder_constraintlayout);
        mFastOrderCardView = view.findViewById(R.id.fragment_placeorder_fastorderholder_constraintlayout);

        mActiveSubscriptionCardView = view.findViewById(R.id.fragment_placeorder_subscriptionactiveorderholder_constraintlayout);
        mSubscriptionInfoTextView = view.findViewById(R.id.fragment_placeorder_subscriptionactive_statuslong_textview);
        mPickupsDoneView = view.findViewById(R.id.fragment_placeorder_subscriptionactive_orderdatevalue_textview);
        mPickupTimeTextView = view.findViewById(R.id.fragment_placeorder_subscriptionactive_orderdeliveryvalue_textview);
        mItemsWashedTextView = view.findViewById(R.id.fragment_placeorder_subscriptionactive_orderitemsvalue_textview);
        mInactiveSubscriptionCardView = view.findViewById(R.id.fragment_placeorder_subscriptionofforderholder_constraintlayout);
        mBuySubscriptionAppCompatButton = view.findViewById(R.id.fragment_placeorder_subscriptionoffbuy_button);

        mLoadingContentLoadingProgressBar = view.findViewById(R.id.fragment_placeorder_loading_contentloadingprogressbar);
        mLoadingSubscriptionContentLoadingProgressBar = view.findViewById(R.id.fragment_placeorder_loadingsubscription_contentloadingprogressbar);

        mGoodOrderCardView.setOnClickListener(this);
        mActiveSubscriptionCardView.setOnClickListener(this);
        mFastOrderCardView.setOnClickListener(this);
        mBuySubscriptionAppCompatButton.setOnClickListener(this);

        backgroundThread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                updateUserInfoAndSubscription("");//Config.getSharedPreferenceString(getActivity().getApplicationContext(), Config.SHARED_PREF_KEY_USER_CREDENTIALS_USER_FCM_TOKEN));
            }
        });
        backgroundThread2.start();
        return view;
    }




    public void updateUserInfoAndSubscription(final String fcm){

        Log.e("updateUserInfo", "fcm-cc: " + fcm);
        if(!getActivity().isFinishing() && getActivity().getApplicationContext() != null) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    mInactiveSubscriptionCardView.setVisibility(View.GONE);
                    mActiveSubscriptionCardView.setVisibility(View.VISIBLE);
                    mLoadingSubscriptionContentLoadingProgressBar.setVisibility(View.VISIBLE);
                    mSubscriptionInfoTextView.setText("Looking for your subscription...");
                    mPickupsDoneView.setText("...");
                    mPickupTimeTextView.setText("...");
                    mItemsWashedTextView.setText("...");
                }
            });
        }


        AndroidNetworking.post(Config.LINK_COLLECTION_UPDATE_USER_INFO)
                .addHeaders("Accept", "application/json")
                .addHeaders("Authorization", "Bearer " + Config.getSharedPreferenceString(getActivity().getApplicationContext(), Config.SHARED_PREF_KEY_USER_CREDENTIALS_USER_PASSWORD_ACCESS_TOKEN))
                .addBodyParameter("fcm_token", fcm)
                .addBodyParameter("fcm_type", "ANDROID")
                .addBodyParameter("app_type", "ANDROID")
                .addBodyParameter("app_version_code", String.valueOf(Config.getAppVersionCode(getActivity().getApplicationContext())))
                .setTag("update_user_info")
                .setPriority(Priority.HIGH)
                .build().getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("updateUserInfo", "response-cc: " + response.toString());
                        try {
                            JSONObject main_response = new JSONObject(response);
                            String myStatus = main_response.getString("status");
                            String myStatusMessage = main_response.getString("message");
                            String appLink = main_response.getString("app_link");
                            Boolean SubscriptionSet = main_response.getBoolean("subscription_set");

                            if(myStatus.trim().equalsIgnoreCase("update")){
                                String minVersionCode = main_response.getString("minvc");
                                Config.setSharedPreferenceString(getActivity().getApplicationContext(), Config.SHARED_PREF_KEY_USER_CREDENTIALS_USER_APP_MINIMUM_VERSION_CODE, minVersionCode);
                                Config.setSharedPreferenceString(getActivity().getApplicationContext(), Config.SHARED_PREF_KEY_GOOGLE_PLAYSTORE_APPID, appLink);
                                Config.openActivity(getActivity(), UpdateActivity.class, 0, 2, 0, "", "");
                                return;
                            }

                            if(SubscriptionSet){
                                JSONObject subscription = new JSONObject(response).getJSONObject("subscription");
                                String minVersionCode = main_response.getString("min_vc");
                                Config.setSharedPreferenceString(getActivity().getApplicationContext(), Config.SHARED_PREF_KEY_USER_CREDENTIALS_USER_APP_MINIMUM_VERSION_CODE, minVersionCode);
                                Config.setSharedPreferenceBoolean(getActivity().getApplicationContext(), Config.SHARED_PREF_KEY_USER_SUBSCRIPTION_IS_SET, true);

                                Config.setSharedPreferenceString(getActivity().getApplicationContext(), Config.SHARED_PREF_KEY_USER_SUBSCRIPTION_INFO, subscription.getString("subscription_info"));
                                Config.setSharedPreferenceString(getActivity().getApplicationContext(), Config.SHARED_PREF_KEY_USER_SUBSCRIPTION_ITEMS_WASHED, String.valueOf(subscription.getString("items_washed_info")));
                                Config.setSharedPreferenceString(getActivity().getApplicationContext(), Config.SHARED_PREF_KEY_USER_SUBSCRIPTION_PICKUPS_DONE, String.valueOf(subscription.getString("pickups_count")));
                                Config.setSharedPreferenceString(getActivity().getApplicationContext(), Config.SHARED_PREF_KEY_USER_SUBSCRIPTION_MAX_NUMBER_OF_PEOPLE, subscription.getString("subscription_max_number_of_people_in_home"));
                                Config.setSharedPreferenceString(getActivity().getApplicationContext(), Config.SHARED_PREF_KEY_USER_SUBSCRIPTION_NUMBER_OF_MONTHS, subscription.getString("subscription_number_of_months"));
                                Config.setSharedPreferenceString(getActivity().getApplicationContext(), Config.SHARED_PREF_KEY_USER_SUBSCRIPTION_PICKUP_TIME, subscription.getString("pickup_final_time"));
                                Config.setSharedPreferenceString(getActivity().getApplicationContext(), Config.SHARED_PREF_KEY_USER_SUBSCRIPTION_PICKUP_DAY, subscription.getString("subscription_pickup_day"));
                                Config.setSharedPreferenceString(getActivity().getApplicationContext(), Config.SHARED_PREF_KEY_USER_SUBSCRIPTION_PICKUP_LOCATION, subscription.getString("subscription_pickup_location"));
                                Config.setSharedPreferenceString(getActivity().getApplicationContext(), Config.SHARED_PREF_KEY_USER_SUBSCRIPTION_PACKAGE_DESCRIPTION, subscription.getString("subscription_package_description"));

                                if(!getActivity().isFinishing() && getActivity().getApplicationContext() != null) {
                                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                                        @Override
                                        public void run() {
                                            mSubscriptionInfoTextView.setText(Config.getSharedPreferenceString(getActivity().getApplicationContext(), Config.SHARED_PREF_KEY_USER_SUBSCRIPTION_INFO));
                                            mPickupsDoneView.setText(Config.getSharedPreferenceString(getActivity().getApplicationContext(), Config.SHARED_PREF_KEY_USER_SUBSCRIPTION_PICKUPS_DONE));
                                            mPickupTimeTextView.setText(Config.getSharedPreferenceString(getActivity().getApplicationContext(), Config.SHARED_PREF_KEY_USER_SUBSCRIPTION_PICKUP_TIME));
                                            mItemsWashedTextView.setText(Config.getSharedPreferenceString(getActivity().getApplicationContext(), Config.SHARED_PREF_KEY_USER_SUBSCRIPTION_ITEMS_WASHED));
                                            mInactiveSubscriptionCardView.setVisibility(View.GONE);
                                            mLoadingSubscriptionContentLoadingProgressBar.setVisibility(View.GONE);
                                            mActiveSubscriptionCardView.setVisibility(View.VISIBLE);
                                        }
                                    });
                                }
                            } else {
                                if(!getActivity().isFinishing() && getActivity().getApplicationContext() != null) {
                                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                                        @Override
                                        public void run() {
                                            mActiveSubscriptionCardView.setVisibility(View.GONE);
                                            mInactiveSubscriptionCardView.setVisibility(View.VISIBLE);
                                        }
                                    });
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            if(!getActivity().isFinishing() && getActivity().getApplicationContext() != null) {
                                new Handler(Looper.getMainLooper()).post(new Runnable() {
                                    @Override
                                    public void run() {
                                        mLoadingSubscriptionContentLoadingProgressBar.setVisibility(View.GONE);
                                    }
                                });
                            }
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("updateUserInfo", "anError-cc: " + anError.getErrorDetail());
                        if(!getActivity().isFinishing() && getActivity().getApplicationContext() != null) {
                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                @Override
                                public void run() {
                                    mLoadingSubscriptionContentLoadingProgressBar.setVisibility(View.GONE);
                                }
                            });
                        }
                    }
                });

    }



    ActivityResultLauncher<Intent> activityResultLaunch = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == 9999) {
                        //Intent data = result.getData();
                        //String myStr = data.getStringExtra("MyData");
                        //Config.showToastType1(getActivity(), "Results is in");

                        if(!getActivity().isFinishing() && getActivity().getApplicationContext() != null) {
                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                @Override
                                public void run() {
                                    mSubscriptionInfoTextView.setText(Config.getSharedPreferenceString(getActivity().getApplicationContext(), Config.SHARED_PREF_KEY_USER_SUBSCRIPTION_INFO));
                                    mPickupsDoneView.setText(Config.getSharedPreferenceString(getActivity().getApplicationContext(), Config.SHARED_PREF_KEY_USER_SUBSCRIPTION_PICKUPS_DONE));
                                    mPickupTimeTextView.setText(Config.getSharedPreferenceString(getActivity().getApplicationContext(), Config.SHARED_PREF_KEY_USER_SUBSCRIPTION_PICKUP_TIME));
                                    mItemsWashedTextView.setText(Config.getSharedPreferenceString(getActivity().getApplicationContext(), Config.SHARED_PREF_KEY_USER_SUBSCRIPTION_ITEMS_WASHED));
                                    mInactiveSubscriptionCardView.setVisibility(View.GONE);
                                    mLoadingSubscriptionContentLoadingProgressBar.setVisibility(View.GONE);
                                    mActiveSubscriptionCardView.setVisibility(View.VISIBLE);
                                }
                            });
                        }
                    }
                }
            });

    @Override
    public void onClick(View view) {
        if(view.getId() == mGoodOrderCardView.getId()){
            Config.openActivity(getActivity(), OrderCollectionActivity.class, 0, 0, 0, "", "");
        } else if(view.getId() == mFastOrderCardView.getId()){
            backgroundThread1 = new Thread(new Runnable() {
                @Override
                public void run() {
                    placeOrder();
                }
            });
            backgroundThread1.start();
        } else if(view.getId() == mBuySubscriptionAppCompatButton.getId()){

            Intent intent = new Intent(getActivity(), BuySubscriptionOfferActivity.class);
            activityResultLaunch.launch(intent);
        }
    }

    public void placeOrder() {

        if (!getActivity().isFinishing() && getActivity().getApplicationContext() != null) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    mGoodOrderCardView.setVisibility(View.GONE);
                    mInactiveSubscriptionCardView.setVisibility(View.GONE);
                    mActiveSubscriptionCardView.setVisibility(View.GONE);
                    mFastOrderCardView.setVisibility(View.GONE);
                    mLoadingContentLoadingProgressBar.setVisibility(View.VISIBLE);
                }
            });
        }


        AndroidNetworking.post(Config.LINK_COLLECTION_CALLBACK_REQUEST_ORDER)
                .addHeaders("Accept", "application/json")
                .addHeaders("Authorization", "Bearer " + Config.getSharedPreferenceString(getActivity(), Config.SHARED_PREF_KEY_USER_CREDENTIALS_USER_PASSWORD_ACCESS_TOKEN))
                .addBodyParameter("app_type", "ANDROID")
                .addBodyParameter("app_version_code", String.valueOf(Config.getAppVersionCode(getActivity().getApplicationContext())))
                .setTag("get_suggestion")
                .setPriority(Priority.HIGH)
                .build().getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        if (!getActivity().isFinishing() && getActivity().getApplicationContext() != null) {
                            Log.e("SERVER-REQUEST", "response: " + response.toString());
                            try {
                                JSONObject main_response = new JSONObject(response);
                                String myStatus = main_response.getString("status");
                                final String myStatusMessage = main_response.getString("message");

                                if (myStatus.equalsIgnoreCase("success")) {

                                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                                        @Override
                                        public void run() {
                                            mLoadingContentLoadingProgressBar.setVisibility(View.GONE);
                                            mGoodOrderCardView.setVisibility(View.VISIBLE);
                                            mFastOrderCardView.setVisibility(View.VISIBLE);
                                            if(Config.getSharedPreferenceBoolean(getActivity().getApplicationContext(), Config.SHARED_PREF_KEY_USER_SUBSCRIPTION_IS_SET)){
                                                mActiveSubscriptionCardView.setVisibility(View.VISIBLE);
                                                mInactiveSubscriptionCardView.setVisibility(View.GONE);
                                            } else {
                                                mActiveSubscriptionCardView.setVisibility(View.GONE);
                                                mInactiveSubscriptionCardView.setVisibility(View.VISIBLE);
                                            }
                                            Config.showDialogType1(getActivity(), "", myStatusMessage, "show-positive-image", null, false,  "Ok","");
                                            return;
                                        }
                                    });

                                } else {
                                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                                        @Override
                                        public void run() {
                                            Config.showToastType1(getActivity(), myStatusMessage);
                                            mLoadingContentLoadingProgressBar.setVisibility(View.GONE);
                                            if(Config.getSharedPreferenceBoolean(getActivity().getApplicationContext(), Config.SHARED_PREF_KEY_USER_SUBSCRIPTION_IS_SET)){
                                                mActiveSubscriptionCardView.setVisibility(View.VISIBLE);
                                                mInactiveSubscriptionCardView.setVisibility(View.GONE);
                                            } else {
                                                mActiveSubscriptionCardView.setVisibility(View.GONE);
                                                mInactiveSubscriptionCardView.setVisibility(View.VISIBLE);
                                            }
                                            mGoodOrderCardView.setVisibility(View.VISIBLE);
                                            mFastOrderCardView.setVisibility(View.VISIBLE);
                                        }
                                    });
                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                                new Handler(Looper.getMainLooper()).post(new Runnable() {
                                    @Override
                                    public void run() {
                                        Config.showDialogType1(getActivity(), getString(R.string.error), getString(R.string.an_unexpected_error_occured), "", null, false, "", "");
                                        mLoadingContentLoadingProgressBar.setVisibility(View.GONE);
                                        if(Config.getSharedPreferenceBoolean(getActivity().getApplicationContext(), Config.SHARED_PREF_KEY_USER_SUBSCRIPTION_IS_SET)){
                                            mActiveSubscriptionCardView.setVisibility(View.VISIBLE);
                                            mInactiveSubscriptionCardView.setVisibility(View.GONE);
                                        } else {
                                            mActiveSubscriptionCardView.setVisibility(View.GONE);
                                            mInactiveSubscriptionCardView.setVisibility(View.VISIBLE);
                                        }
                                        mGoodOrderCardView.setVisibility(View.VISIBLE);
                                        mFastOrderCardView.setVisibility(View.VISIBLE);
                                    }
                                });
                            }
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("SERVER-REQUEST", "anError: " + anError.toString());
                        if (!getActivity().isFinishing() && getActivity().getApplicationContext() != null) {
                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                @Override
                                public void run() {
                                    Config.showToastType1(getActivity(), getResources().getString(R.string.check_your_internet_connection_and_try_again));
                                    mLoadingContentLoadingProgressBar.setVisibility(View.GONE);
                                    if(Config.getSharedPreferenceBoolean(getActivity().getApplicationContext(), Config.SHARED_PREF_KEY_USER_SUBSCRIPTION_IS_SET)){
                                        mActiveSubscriptionCardView.setVisibility(View.VISIBLE);
                                        mInactiveSubscriptionCardView.setVisibility(View.GONE);
                                    } else {
                                        mActiveSubscriptionCardView.setVisibility(View.GONE);
                                        mInactiveSubscriptionCardView.setVisibility(View.VISIBLE);
                                    }
                                    mGoodOrderCardView.setVisibility(View.VISIBLE);
                                    mFastOrderCardView.setVisibility(View.VISIBLE);

                                }
                            });
                        }
                    }
                });

    }

}