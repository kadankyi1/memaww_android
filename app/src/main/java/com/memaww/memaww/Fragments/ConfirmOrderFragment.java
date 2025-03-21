package com.memaww.memaww.Fragments;

import android.app.Activity;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.memaww.memaww.R;
import com.memaww.memaww.Util.Config;

import gh.com.payswitch.thetellerandroid.thetellerManager;


public class ConfirmOrderFragment extends Fragment {

    private TextView mSubTotalPriceTextView, mDiscountAmountTextView, mFinalPriceTextView;
    private AppCompatButton mPayOnlineAppCompatButton, mPayOnPickUpAppCompatButton;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";
    private static final String ARG_PARAM4 = "param4";
    private static final String ARG_PARAM5 = "param5";
    private static final String ARG_PARAM6 = "param6";
    private static final String ARG_PARAM7 = "param7";
    private static final String ARG_PARAM8 = "param8";
    private static final String ARG_PARAM9 = "param9";
    private static final String ARG_PARAM10 = "param10";
    private static final String ARG_PARAM11 = "param11";
    private static final String ARG_PARAM12 = "param12";
    private static final String ARG_PARAM13 = "param13";

    public interface onConfirmOrderDoneButtonClickedEventListener {
        public void confirmOrderDoneButtonClicked(String transactionId, String action);
    }

    onConfirmOrderDoneButtonClickedEventListener confirmOrderDoneButtonClickedListener;


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            confirmOrderDoneButtonClickedListener = (onConfirmOrderDoneButtonClickedEventListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement onConfirmOrderDoneButtonClickedEventListener");
        }
    }

    private String mSubTotalPrice;
    private String mDiscountAmount;
    private String mFinalPrice;
    private String mAllowPayOnline;
    private String mAllowPayOnPickUp;
    private String finalPriceNoCurrency;
    private String txnNarration;
    private String txnReference;
    private String merchantId;
    private String merchantApiUser;
    private String merchantApiKey;
    private String returnUrl;
    private String userEmail;

    public ConfirmOrderFragment() {

    }

    public static ConfirmOrderFragment newInstance(String param1, String param2, String param3, String param4, String param5, String param6, String param7, String param8, String param9, String param10, String param11, String param12, String param13) {
        ConfirmOrderFragment fragment = new ConfirmOrderFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        args.putString(ARG_PARAM3, param3);
        args.putString(ARG_PARAM4, param4);
        args.putString(ARG_PARAM5, param5);
        args.putString(ARG_PARAM6, param6);
        args.putString(ARG_PARAM7, param7);
        args.putString(ARG_PARAM8, param8);
        args.putString(ARG_PARAM9, param9);
        args.putString(ARG_PARAM10, param10);
        args.putString(ARG_PARAM11, param11);
        args.putString(ARG_PARAM12, param12);
        args.putString(ARG_PARAM13, param13);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mSubTotalPrice = getArguments().getString(ARG_PARAM1);
            mDiscountAmount = getArguments().getString(ARG_PARAM2);
            mFinalPrice = getArguments().getString(ARG_PARAM3);
            mAllowPayOnline = getArguments().getString(ARG_PARAM4);
            mAllowPayOnPickUp = getArguments().getString(ARG_PARAM5);
            finalPriceNoCurrency = getArguments().getString(ARG_PARAM6);
            txnNarration = getArguments().getString(ARG_PARAM7);
            txnReference = getArguments().getString(ARG_PARAM8);
            merchantId = getArguments().getString(ARG_PARAM9);
            merchantApiUser = getArguments().getString(ARG_PARAM10);
            merchantApiKey = getArguments().getString(ARG_PARAM11);
            returnUrl = getArguments().getString(ARG_PARAM12);
            userEmail = getArguments().getString(ARG_PARAM13);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_confirm_order, container, false);

        mSubTotalPriceTextView = view.findViewById(R.id.fragment_confirmorder_subtotalvalue_textview);
        mDiscountAmountTextView = view.findViewById(R.id.fragment_confirmorder_discounttotalvalue_textview);
        mFinalPriceTextView = view.findViewById(R.id.fragment_confirmorder_finaltotalvalue_textview);
        mPayOnlineAppCompatButton = view.findViewById(R.id.fragment_confirmorder_payonline_button);
        mPayOnPickUpAppCompatButton = view.findViewById(R.id.fragment_confirmorder_payonpickup_button);

        mSubTotalPriceTextView.setText(mSubTotalPrice);
        mDiscountAmountTextView.setText(mDiscountAmount);
        mFinalPriceTextView.setText(mFinalPrice);

        mPayOnlineAppCompatButton.setText("Pay Online | " + mFinalPrice);
        mPayOnPickUpAppCompatButton.setText("Pay-On-Pickup");

        //confirmOrderDoneButtonClickedListener.confirmOrderDoneButtonClicked(discount);

        mPayOnlineAppCompatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Config.showToastType1(getActivity(), "send to payment portal");

                Log.e("THETELLER", txnReference);
                new thetellerManager(getActivity())
                        //.setAmount(Double.parseDouble("0.10"))
                        .setAmount(Double.parseDouble(finalPriceNoCurrency))
                        .setEmail(userEmail)
                        .setfName(Config.getSharedPreferenceString(getActivity().getApplicationContext(), Config.SHARED_PREF_KEY_USER_CREDENTIALS_USER_FIRST_NAME))
                        .setlName(Config.getSharedPreferenceString(getActivity().getApplicationContext(), Config.SHARED_PREF_KEY_USER_CREDENTIALS_USER_LAST_NAME))
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
            }
        });

        mPayOnPickUpAppCompatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Config.showToastType1(getActivity(), "Update on server and mark order for assigned for pickup");
                confirmOrderDoneButtonClickedListener.confirmOrderDoneButtonClicked(txnReference, "pay_on_pickup");
                getActivity().onBackPressed();
            }
        });

        if(!mAllowPayOnline.equalsIgnoreCase("yes")){
            mPayOnlineAppCompatButton.setVisibility(View.GONE);
        }
        if(!mAllowPayOnPickUp.equalsIgnoreCase("yes")){
            mPayOnPickUpAppCompatButton.setVisibility(View.GONE);
        }
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("CONFIRM-ORDER", "This is being onDestroy");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.e("CONFIRM-ORDER", "This is being onDetach");
    }
}