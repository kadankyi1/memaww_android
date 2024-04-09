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
    private String loginResponse, serverResponse = "";


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
            placeOrder();
        }
    }

    public void placeOrder() {

        if (!getActivity().isFinishing() && getActivity().getApplicationContext() != null) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    mGoodOrderCardView.setVisibility(View.INVISIBLE);
                    mFastOrderCardView.setVisibility(View.INVISIBLE);
                    mLoadingContentLoadingProgressBar.setVisibility(View.VISIBLE);
                }
            });
        }

        serverResponse = "";


        AndroidNetworking.post(Config.LINK_COLLECTION_REQUEST_ORDER)
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
                                    mLoadingContentLoadingProgressBar.setVisibility(View.INVISIBLE);
                                    mGoodOrderCardView.setVisibility(View.VISIBLE);
                                    mFastOrderCardView.setVisibility(View.VISIBLE);
                                    Config.showDialogType1(getActivity(), "", "We will call you back shortly to take your order information. Thank you.", "show-positive-image", null, false,  "Ok","");
                                    return;

                                } else {
                                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                                        @Override
                                        public void run() {
                                            Config.showToastType1(getActivity(), myStatusMessage);
                                            mLoadingContentLoadingProgressBar.setVisibility(View.INVISIBLE);
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
                                        mLoadingContentLoadingProgressBar.setVisibility(View.INVISIBLE);
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
                                    mLoadingContentLoadingProgressBar.setVisibility(View.INVISIBLE);
                                    mGoodOrderCardView.setVisibility(View.VISIBLE);
                                    mFastOrderCardView.setVisibility(View.VISIBLE);

                                }
                            });
                        }
                    }
                });

    }
}