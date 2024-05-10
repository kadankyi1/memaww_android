package com.memaww.memaww.Fragments;

import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.memaww.memaww.Activities.MainActivity;
import com.memaww.memaww.Activities.ReaderWebViewActivity;
import com.memaww.memaww.R;
import com.memaww.memaww.Util.Config;


public class ProfileFragment extends Fragment implements View.OnClickListener {

    private ConstraintLayout mFullnameConstraintLayout, mPhoneConstraintLayout,
            mAddressConstraintLayout, mEmailAddressConstraintLayout, mServicePolicyConstraintLayout, mRateAppConstraintLayout;
    private TextView mFullnameTextView, mPhoneTextView, mAddressTextView, mEmailAddressTextView;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public ProfileFragment() {
        // Required empty public constructor
    }

    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        mFullnameConstraintLayout = view.findViewById(R.id.fragment_profile_namemenuholder_constraintLayout);
        mPhoneConstraintLayout = view.findViewById(R.id.fragment_phonemenuholder_constraintLayout);
        mAddressConstraintLayout = view.findViewById(R.id.fragment_addressmenuholder_constraintLayout);
        mEmailAddressConstraintLayout = view.findViewById(R.id.fragment_emailmenuholder_constraintLayout);
        mServicePolicyConstraintLayout = view.findViewById(R.id.fragment_servicepolicy_constraintLayout);
        mRateAppConstraintLayout = view.findViewById(R.id.fragment_profile_rateapp_constraintLayout);

        mFullnameTextView = view.findViewById(R.id.fragment_profile_namemenutext_textview);
        mPhoneTextView = view.findViewById(R.id.fragment_profile_phonemenutext_textview);
        mAddressTextView = view.findViewById(R.id.fragment_profile_addressmenutext_textview);
        mEmailAddressTextView = view.findViewById(R.id.fragment_profile_emailmenutext_textview);

        mFullnameTextView.setText(Config.getSharedPreferenceString(getActivity().getApplicationContext(),Config.SHARED_PREF_KEY_USER_CREDENTIALS_USER_FIRST_NAME) + " " + Config.getSharedPreferenceString(getActivity().getApplicationContext(),Config.SHARED_PREF_KEY_USER_CREDENTIALS_USER_LAST_NAME));
        mPhoneTextView.setText(Config.getSharedPreferenceString(getActivity().getApplicationContext(),Config.SHARED_PREF_KEY_USER_CREDENTIALS_USER_PHONE));
        mAddressTextView.setText(Config.getSharedPreferenceStringWithPlaceHolder(getActivity().getApplicationContext(),Config.SHARED_PREF_KEY_USER_CREDENTIALS_USER_PICKUP_ADDRESS, "Not Set"));
        mEmailAddressTextView.setText(Config.getSharedPreferenceStringWithPlaceHolder(getActivity().getApplicationContext(),Config.SHARED_PREF_KEY_USER_CREDENTIALS_USER_EMAIL, "Not Set"));

        mServicePolicyConstraintLayout.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == mServicePolicyConstraintLayout.getId()){
            Config.openActivity(getActivity(), ReaderWebViewActivity.class, 0, 0, 1, Config.WEBVIEW_KEY_URL, "https://memaww.com/service-policy");
        }
    }
}