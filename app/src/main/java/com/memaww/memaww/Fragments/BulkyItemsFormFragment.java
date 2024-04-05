package com.memaww.memaww.Fragments;

import android.app.Activity;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.memaww.memaww.R;

public class BulkyItemsFormFragment extends Fragment {

    private EditText mJustWashEditText, mWashAndIronEditText;
    private AppCompatButton mDoneButton;
    private String bulkyItemsJustWashQuantity = "0", bulkyItemsWashAndIronQuantity = "0";

    public interface onBulkyItemsFormDoneButtonClickedEventListener {
        public void bulkyItemsFormDoneButtonClicked(String justWashItemsQuantity, String washAndIronItemsQuantity);
    }

    onBulkyItemsFormDoneButtonClickedEventListener bulkyItemsFormDoneButtonClickedListener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            bulkyItemsFormDoneButtonClickedListener = (onBulkyItemsFormDoneButtonClickedEventListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement onBulkyItemsFormDoneButtonClickedEventListener");
        }
    }

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public BulkyItemsFormFragment() {
        // Required empty public constructor
    }

    public static BulkyItemsFormFragment newInstance(String param1, String param2) {
        BulkyItemsFormFragment fragment = new BulkyItemsFormFragment();
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
        View view = inflater.inflate(R.layout.fragment_bulky_items_form, container, false);
        mJustWashEditText = view.findViewById(R.id.fragment_collectionform_pickuplocation_edittext);
        mWashAndIronEditText = view.findViewById(R.id.fragment_collectionform_pickupdatetime_edittext);
        mDoneButton = view.findViewById(R.id.fragment_collectionform_done_button);

        mDoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!mJustWashEditText.getText().toString().trim().equalsIgnoreCase("")){
                    bulkyItemsJustWashQuantity = mJustWashEditText.getText().toString().trim();
                }
                if(!mWashAndIronEditText.getText().toString().trim().equalsIgnoreCase("")){
                    bulkyItemsWashAndIronQuantity = mWashAndIronEditText.getText().toString().trim();
                }
                bulkyItemsFormDoneButtonClickedListener.bulkyItemsFormDoneButtonClicked(bulkyItemsJustWashQuantity,bulkyItemsWashAndIronQuantity);
                getActivity().onBackPressed();
            }
        });
        return view;
    }

}