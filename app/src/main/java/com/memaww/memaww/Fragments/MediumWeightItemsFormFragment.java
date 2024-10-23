package com.memaww.memaww.Fragments;

import android.app.Activity;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.memaww.memaww.R;

public class MediumWeightItemsFormFragment extends Fragment {

    private EditText mJustWashEditText, mWashAndIronEditText, mJustIronEditText;
    private AppCompatButton mDoneButton;
    private String mediumWeightJustWashQuantity = "0", mediumWeightWashAndIronQuantity = "0", mediumWeightJustIronQuantity = "0";

    public interface onmediumWeightItemsFormDoneButtonClickedEventListener {
        public void mediumWeightItemsFormDoneButtonClicked(String justWashItemsQuantity, String washAndIronItemsQuantity, String justIronItemsQuantity);
    }

    onmediumWeightItemsFormDoneButtonClickedEventListener mediumWeightItemsFormDoneButtonClickedListener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mediumWeightItemsFormDoneButtonClickedListener = (onmediumWeightItemsFormDoneButtonClickedEventListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement onmediumWeightItemsFormDoneButtonClickedEventListener");
        }
    }

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public MediumWeightItemsFormFragment() {
        // Required empty public constructor
    }

    public static MediumWeightItemsFormFragment newInstance(String param1, String param2) {
        MediumWeightItemsFormFragment fragment = new MediumWeightItemsFormFragment();
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
        View view = inflater.inflate(R.layout.fragment_medium_weight_items_form, container, false);
        mJustWashEditText = view.findViewById(R.id.fragment_collectionform_mediumweight_pickuplocation_edittext);
        mWashAndIronEditText = view.findViewById(R.id.fragment_collectionform_mediumweight_pickupdatetime_timepicker);
        mJustIronEditText = view.findViewById(R.id.fragment_collectionform_mediumweight_justiron_edittext);
        mDoneButton = view.findViewById(R.id.fragment_collectionform_mediumweight_done_button);

        mDoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!mJustWashEditText.getText().toString().trim().equalsIgnoreCase("")){
                    mediumWeightJustWashQuantity = mJustWashEditText.getText().toString().trim();
                }
                if(!mWashAndIronEditText.getText().toString().trim().equalsIgnoreCase("")){
                    mediumWeightWashAndIronQuantity = mWashAndIronEditText.getText().toString().trim();
                }
                if(!mJustIronEditText.getText().toString().trim().equalsIgnoreCase("")){
                    mediumWeightJustIronQuantity = mJustIronEditText.getText().toString().trim();
                }
                mediumWeightItemsFormDoneButtonClickedListener.mediumWeightItemsFormDoneButtonClicked(mediumWeightJustWashQuantity, mediumWeightWashAndIronQuantity, mediumWeightJustIronQuantity);
                getActivity().onBackPressed();
            }
        });
        return view;
    }

}