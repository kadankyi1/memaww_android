package com.memaww.memaww.Fragments;

import android.app.Activity;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.memaww.memaww.R;
import com.memaww.memaww.Util.Config;


public class ConfirmOrderFragment extends Fragment {

    private TextView mSubTotalPriceTextView, mDiscountAmountTextView, mFinalPriceTextView;
    private AppCompatButton mPayOnlineAppCompatButton, mPayOnPickUpAppCompatButton;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";
    private static final String ARG_PARAM4 = "param4";
    private static final String ARG_PARAM5 = "param5";

    public interface onConfirmOrderDoneButtonClickedEventListener {
        public void confirmOrderDoneButtonClicked(String discount);
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

    public ConfirmOrderFragment() {

    }

    public static ConfirmOrderFragment newInstance(String param1, String param2, String param3, String param4, String param5) {
        ConfirmOrderFragment fragment = new ConfirmOrderFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        args.putString(ARG_PARAM3, param3);
        args.putString(ARG_PARAM4, param4);
        args.putString(ARG_PARAM5, param5);
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

        mPayOnlineAppCompatButton.setText("Pay Online - " + mFinalPrice);
        mPayOnPickUpAppCompatButton.setText("Pay-On-Pickup");

        //confirmOrderDoneButtonClickedListener.confirmOrderDoneButtonClicked(discount);

        mPayOnlineAppCompatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Config.showToastType1(getActivity(), "send to payment portal");
            }
        });
        mPayOnPickUpAppCompatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Config.showToastType1(getActivity(), "Update on server and mark order for assigned for pickup");
            }
        });

        if(mAllowPayOnline.equalsIgnoreCase("yes")){
            mPayOnlineAppCompatButton.setVisibility(View.GONE);
        }
        if(mAllowPayOnPickUp.equalsIgnoreCase("yes")){
            mPayOnPickUpAppCompatButton.setVisibility(View.GONE);
        }
        return view;
    }
}