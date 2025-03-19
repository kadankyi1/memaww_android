package com.memaww.memaww.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.memaww.memaww.R;
import com.memaww.memaww.Util.Config;

public class InviteFragment extends Fragment {

    private TextView mInviteCodeTextView;
    private Button mSendButton;

    public InviteFragment() {
        // Required empty public constructor
    }

    public static InviteFragment newInstance() {
        InviteFragment fragment = new InviteFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_invite, container, false);


        mInviteCodeTextView = view.findViewById(R.id.fragment_invitefragment_code_textview);
        mSendButton = view.findViewById(R.id.fragment_invitefragment_sendinvite_button);

        mInviteCodeTextView.setText(Config.getSharedPreferenceString(getActivity(), Config.SHARED_PREF_KEY_USER_CREDENTIALS_USER_INVITE_CODE));
        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "You and I will get a discount on our laundry if you use my invite code " + Config.getSharedPreferenceString(getActivity(), Config.SHARED_PREF_KEY_USER_CREDENTIALS_USER_INVITE_CODE) + " to register on the MeMaww Laundry App and place your first order.");
                sendIntent.setType("text/plain");

                Intent shareIntent = Intent.createChooser(sendIntent, null);
                startActivity(shareIntent);
            }
        });
        return view;
    }
}