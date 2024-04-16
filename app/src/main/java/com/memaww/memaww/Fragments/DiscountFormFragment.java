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

public class DiscountFormFragment extends Fragment {

    private EditText mDiscountEditText;
    private AppCompatButton mDoneButton;
    private String discount = "No Discount Applied";

    public interface onDiscountFormDoneButtonClickedEventListener {
        public void discountFormDoneButtonClicked(String discount);
    }

    onDiscountFormDoneButtonClickedEventListener discountFormDoneButtonClickedListener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            discountFormDoneButtonClickedListener = (onDiscountFormDoneButtonClickedEventListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement onDiscountFormDoneButtonClickedEventListener");
        }
    }

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public DiscountFormFragment() {
        // Required empty public constructor
    }

    public static DiscountFormFragment newInstance(String param1, String param2) {
        DiscountFormFragment fragment = new DiscountFormFragment();
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
        View view = inflater.inflate(R.layout.fragment_discount_form, container, false);

        mDiscountEditText = view.findViewById(R.id.fragment_collectionform_discountcode_edittext);
        mDoneButton = view.findViewById(R.id.fragment_discountform_done_button);

        mDoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!mDiscountEditText.getText().toString().trim().equalsIgnoreCase("")){
                    discount = mDiscountEditText.getText().toString().trim();
                }
                discountFormDoneButtonClickedListener.discountFormDoneButtonClicked(discount);
                getActivity().onBackPressed();
            }
        });
        return view;
    }

}