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

public class SpecialNotesFormFragment extends Fragment {

    private EditText mSpecialNotesEditText;
    private AppCompatButton mDoneButton;
    private String specialNotes = "No special instructions";

    public interface onSpecialNotesFormDoneButtonClickedEventListener {
        public void specialNotesFormDoneButtonClicked(String specialNotes);
    }

    onSpecialNotesFormDoneButtonClickedEventListener specialNotesFormDoneButtonClickedListener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            specialNotesFormDoneButtonClickedListener = (onSpecialNotesFormDoneButtonClickedEventListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement onSpecialNotesFormDoneButtonClickedEventListener");
        }
    }

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public SpecialNotesFormFragment() {
        // Required empty public constructor
    }

    public static SpecialNotesFormFragment newInstance(String param1, String param2) {
        SpecialNotesFormFragment fragment = new SpecialNotesFormFragment();
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
        View view = inflater.inflate(R.layout.fragment_special_notes_form, container, false);

        mSpecialNotesEditText = view.findViewById(R.id.fragment_specialnotesform_pickuplocation_edittext);
        mDoneButton = view.findViewById(R.id.fragment_specialnotesform_done_button);

        mDoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!mSpecialNotesEditText.getText().toString().trim().equalsIgnoreCase("")){
                    specialNotes = mSpecialNotesEditText.getText().toString().trim();
                }
                specialNotesFormDoneButtonClickedListener.specialNotesFormDoneButtonClicked(specialNotes);
                getActivity().onBackPressed();
            }
        });
        return view;
    }

}