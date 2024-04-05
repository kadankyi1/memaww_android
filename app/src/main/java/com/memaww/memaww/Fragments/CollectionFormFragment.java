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

public class CollectionFormFragment extends Fragment {

    private EditText mLocationEditText, mDateTimeEditText, ContactPersonPhoneEditText;
    private AppCompatButton mDoneButton;
    private String collectionLoc = "Not set", collectionDate = "Not Set", collectionPhone = "Not Set";

    public interface onCollectionFormDoneButtonClickedEventListener {
        public void collectionFormDoneButtonClicked(String collectionLocation, String collectionDateTime, String collectionContactPhone);
    }

    onCollectionFormDoneButtonClickedEventListener collectionFormDoneButtonClickedListener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            collectionFormDoneButtonClickedListener = (onCollectionFormDoneButtonClickedEventListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement onCollectionFormDoneButtonClickedEventListener");
        }
    }

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public CollectionFormFragment() {
        // Required empty public constructor
    }

    public static CollectionFormFragment newInstance(String param1, String param2) {
        CollectionFormFragment fragment = new CollectionFormFragment();
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
        View view = inflater.inflate(R.layout.fragment_collection_form, container, false);
        mLocationEditText = view.findViewById(R.id.fragment_collectionform_pickuplocation_edittext);
        mDateTimeEditText = view.findViewById(R.id.fragment_collectionform_pickupdatetime_edittext);
        ContactPersonPhoneEditText = view.findViewById(R.id.fragment_collectionform_contactpersonphone_edittext);
        mDoneButton = view.findViewById(R.id.fragment_collectionform_done_button);

        mDoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!mLocationEditText.getText().toString().trim().equalsIgnoreCase("")){
                    collectionLoc = mLocationEditText.getText().toString().trim();
                }
                if(!mDateTimeEditText.getText().toString().trim().equalsIgnoreCase("")){
                    collectionDate = mDateTimeEditText.getText().toString().trim();
                }
                if(!ContactPersonPhoneEditText.getText().toString().trim().equalsIgnoreCase("")){
                    collectionPhone = ContactPersonPhoneEditText.getText().toString().trim();
                }

                collectionFormDoneButtonClickedListener.collectionFormDoneButtonClicked(collectionLoc, collectionDate, collectionPhone);
                getActivity().onBackPressed();
            }
        });
        return view;
    }

}