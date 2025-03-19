package com.memaww.memaww.Fragments;

import android.app.Activity;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.memaww.memaww.Activities.ReaderWebViewActivity;
import com.memaww.memaww.R;
import com.memaww.memaww.Util.Config;

import gh.com.payswitch.thetellerandroid.thetellerManager;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ConfirmSubscriptionOrderFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ConfirmSubscriptionOrderFragment extends Fragment {

    private TextView mSubTotalPriceTextView, mOfferInfoTextView, mFinalPriceTextView, mPackageInfo1TextView, mPackageInfo2TextView,
            mPackageInfo3TextView, mPackageInfo4TextView, mTermsTextView;
    private AppCompatButton mPayOnlineAppCompatButton;

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

    public interface onConfirmSubscriptionOrderDoneButtonClickedEventListener {
        public void ConfirmSubscriptionOrderDoneButtonClicked(String transactionId, String action);
    }

    onConfirmSubscriptionOrderDoneButtonClickedEventListener ConfirmSubscriptionOrderDoneButtonClickedListener;


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            ConfirmSubscriptionOrderDoneButtonClickedListener = (onConfirmSubscriptionOrderDoneButtonClickedEventListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement onConfirmSubscriptionOrderDoneButtonClickedEventListener");
        }
    }

    private String mFinalPrice;
    private String mCurrencySymbol;
    private String mPackageInfo1;
    private String mPackageInfo2;
    private String mPackageInfo3;
    private String mPackageInfo4;
    private String txnNarration;
    private String txnReference;
    private String merchantId;
    private String merchantApiUser;
    private String merchantApiKey;
    private String returnUrl;
    private String packageInfoDesc;

    public ConfirmSubscriptionOrderFragment() {

    }

    public static ConfirmSubscriptionOrderFragment newInstance(String param1, String param2, String param3, String param4, String param5, String param6, String param7, String param8, String param9, String param10, String param11, String param12, String param13) {
        ConfirmSubscriptionOrderFragment fragment = new ConfirmSubscriptionOrderFragment();
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
            mFinalPrice = getArguments().getString(ARG_PARAM1);
            mCurrencySymbol = getArguments().getString(ARG_PARAM2);
            mPackageInfo1 = getArguments().getString(ARG_PARAM3);
            mPackageInfo2 = getArguments().getString(ARG_PARAM4);
            mPackageInfo3 = getArguments().getString(ARG_PARAM5);
            mPackageInfo4 = getArguments().getString(ARG_PARAM6);
            txnNarration = getArguments().getString(ARG_PARAM7);
            txnReference = getArguments().getString(ARG_PARAM8);
            merchantId = getArguments().getString(ARG_PARAM9);
            merchantApiUser = getArguments().getString(ARG_PARAM10);
            merchantApiKey = getArguments().getString(ARG_PARAM11);
            returnUrl = getArguments().getString(ARG_PARAM12);
            packageInfoDesc = getArguments().getString(ARG_PARAM13);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_confirm_subscription_order, container, false);

        mFinalPriceTextView = view.findViewById(R.id.fragment_confirmsubscriptionorder_totallabel_textview);
        mOfferInfoTextView = view.findViewById(R.id.fragment_confirmsubscriptionorder_subtotallabel_textview);

        mPackageInfo1TextView = view.findViewById(R.id.activity_buysubscription_packageincludesinfo1_textview);
        mPackageInfo2TextView = view.findViewById(R.id.activity_buysubscription_packageincludesinfo2_textview);
        mPackageInfo3TextView = view.findViewById(R.id.activity_buysubscription_packageincludesinfo3_textview);
        mPackageInfo4TextView = view.findViewById(R.id.activity_buysubscription_packageincludesinfo4_textview);

        mTermsTextView = view.findViewById(R.id.fragment_confirmsubscriptionorder_terms_textview);
        mPayOnlineAppCompatButton = view.findViewById(R.id.fragment_confirmsubscriptionorder_payonline_button);

        mPackageInfo1TextView.setText(mPackageInfo1);
        mPackageInfo2TextView.setText(mPackageInfo2);
        mPackageInfo3TextView.setText(mPackageInfo3);
        mPackageInfo4TextView.setText(mPackageInfo4);
        mOfferInfoTextView.setText(packageInfoDesc);
        mFinalPriceTextView.setText(mCurrencySymbol + mFinalPrice);

        mTermsTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Config.openActivity(getActivity(), ReaderWebViewActivity.class, 0, 0, 1, Config.WEBVIEW_KEY_URL, "https://memaww.com/fair-use-policy");
            }
        });

        mPayOnlineAppCompatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Config.showToastType1(getActivity(), "send to payment portal");

                Log.e("THETELLER", txnReference);
                new thetellerManager(getActivity())
                        //.setAmount(Double.parseDouble("0.10"))
                        .setAmount(Double.parseDouble(mFinalPrice))
                        .setEmail(Config.getSharedPreferenceString(getActivity().getApplicationContext(), Config.SHARED_PREF_KEY_USER_CREDENTIALS_USER_PHONE) + "@memaww.com")
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