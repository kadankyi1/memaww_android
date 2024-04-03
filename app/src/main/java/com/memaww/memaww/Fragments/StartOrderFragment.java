package com.memaww.memaww.Fragments;

import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.memaww.memaww.Activities.BookingActivity;
import com.memaww.memaww.Activities.LoginActivity;
import com.memaww.memaww.Activities.MainActivity;
import com.memaww.memaww.R;
import com.memaww.memaww.Util.Config;
import com.memaww.memaww.Util.MyLifecycleHandler;

import org.json.JSONException;
import org.json.JSONObject;

public class StartOrderFragment extends Fragment implements View.OnClickListener {

    private CardView mGoodOrderCardView, mFastOrderCardView;
    private ContentLoadingProgressBar mLoadingContentLoadingProgressBar;
    private String loginResponse;


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public StartOrderFragment() {
        // Required empty public constructor
    }

    public static StartOrderFragment newInstance(String param1, String param2) {
        StartOrderFragment fragment = new StartOrderFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_start_order, container, false);

        mGoodOrderCardView = view.findViewById(R.id.fragment_start_goodorderholder_constraintlayout);
        mFastOrderCardView = view.findViewById(R.id.fragment_start_fastorderholder_constraintlayout);
        mLoadingContentLoadingProgressBar = view.findViewById(R.id.fragment_start_loading_contentloadingprogressbar);


        mGoodOrderCardView.setOnClickListener(this);
        mFastOrderCardView.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == mGoodOrderCardView.getId()){
            Config.openActivity(getActivity(), BookingActivity.class, 0, 0, 0, "", "");
        } else if(view.getId() == mFastOrderCardView.getId()){
            placeOrder("order_type", "country", "phone", "first_name", "last_name");
        }
    }


    public void placeOrder(final String order_type, final String country, final String phone, final String first_name, final String last_name){

        Config.showToastType1(getActivity(), "Sending order to server");
        /*
        if(!getActivity().isFinishing() && getActivity().getApplicationContext() != null) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    mGoodOrderCardView.setVisibility(View.INVISIBLE);
                    mFastOrderCardView.setVisibility(View.INVISIBLE);
                    mLoadingContentLoadingProgressBar.setVisibility(View.VISIBLE);
                }
            });
        }

        loginResponse = "";

        Log.e("SIGN-IN-NETWORK", "country: " + country);
        Log.e("SIGN-IN-NETWORK", "phone: " + phone);
        Log.e("SIGN-IN-NETWORK", "first_name: " + first_name);
        Log.e("SIGN-IN-NETWORK", "last_name: " + last_name);
        Log.e("SIGN-IN-NETWORK", "app_version_code: " + String.valueOf(Config.getAppVersionCode(getActivity().getApplicationContext())));

        AndroidNetworking.post(Config.LINK_LOGIN)
                .addBodyParameter("user_country", country)
                .addBodyParameter("user_phone", phone)
                .addBodyParameter("user_first_name", first_name)
                .addBodyParameter("user_last_name", last_name)
                .addBodyParameter("app_type", "ANDROID")
                .addBodyParameter("app_version_code", String.valueOf(Config.getAppVersionCode(getActivity().getApplicationContext())))
                .setTag("login_activity_login")
                .setPriority(Priority.MEDIUM)
                .build().getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("SIGN-IN-NETWORK", "response: " + response.toString());

                        if(!getActivity().isFinishing() && getActivity().getApplicationContext() != null) {
                            try {
                                Log.e("PSignup", response);
                                JSONObject main_response = new JSONObject(response);
                                String myStatus = main_response.getString("status");
                                final String myStatusMessage = main_response.getString("message");
                                JSONObject user_data_response = new JSONObject(response).getJSONObject("user");

                                if (myStatus.equalsIgnoreCase("success")) {

                                    //STORING THE USER DATA
                                    Config.setSharedPreferenceString(getActivity().getApplicationContext(), Config.SHARED_PREF_KEY_USER_CREDENTIALS_USER_ID_SHORT, user_data_response.getString("user_id"));
                                    Config.setSharedPreferenceString(getActivity().getApplicationContext(), Config.SHARED_PREF_KEY_USER_CREDENTIALS_USER_ID_LONG, user_data_response.getString("user_sys_id"));
                                    Config.setSharedPreferenceString(getActivity().getApplicationContext(), Config.SHARED_PREF_KEY_USER_CREDENTIALS_USER_FIRST_NAME, user_data_response.getString("user_first_name"));
                                    Config.setSharedPreferenceString(getActivity().getApplicationContext(), Config.SHARED_PREF_KEY_USER_CREDENTIALS_USER_LAST_NAME, user_data_response.getString("user_last_name"));
                                    Config.setSharedPreferenceString(getActivity().getApplicationContext(), Config.SHARED_PREF_KEY_USER_CREDENTIALS_USER_PHONE, user_data_response.getString("user_phone"));
                                    Config.setSharedPreferenceString(getActivity().getApplicationContext(), Config.SHARED_PREF_KEY_USER_CREDENTIALS_USER_PASSWORD_ACCESS_TOKEN, main_response.getString("access_token"));

                                    Config.openActivity(getActivity(), MainActivity.class, 1, 2, 0, "", "");
                                    return;

                                } else {
                                    if(MyLifecycleHandler.isApplicationInForeground()){
                                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                                            @Override
                                            public void run() {
                                                Config.showDialogType1(getActivity(), "1", myStatusMessage, "", null, true, "", "");

                                                mGoodOrderCardView.setVisibility(View.VISIBLE);
                                                mFastOrderCardView.setVisibility(View.VISIBLE);
                                                mLoadingContentLoadingProgressBar.setVisibility(View.INVISIBLE);

                                            }
                                        });
                                    } else {
                                        loginResponse = myStatusMessage;
                                    }
                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                                new Handler(Looper.getMainLooper()).post(new Runnable() {
                                    @Override
                                    public void run() {
                                        Config.showDialogType1(getActivity(), getString(R.string.error), getString(R.string.an_unexpected_error_occured), "", null, false, "", "");
                                        mGoodOrderCardView.setVisibility(View.VISIBLE);
                                        mFastOrderCardView.setVisibility(View.VISIBLE);
                                        mLoadingContentLoadingProgressBar.setVisibility(View.INVISIBLE);

                                    }
                                });
                            }
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("SIGN-IN-NETWORK", "anError: " + anError.toString());
                        if(!getActivity().isFinishing() && getActivity().getApplicationContext() != null) {
                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                @Override
                                public void run() {
                                    Config.showToastType1(getActivity(), getResources().getString(R.string.check_your_internet_connection_and_try_again));
                                    mGoodOrderCardView.setVisibility(View.VISIBLE);
                                    mFastOrderCardView.setVisibility(View.VISIBLE);
                                    mLoadingContentLoadingProgressBar.setVisibility(View.INVISIBLE);

                                }
                            });
                        }
                    }
                });
         */
    }

}