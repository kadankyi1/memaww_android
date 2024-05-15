package com.memaww.memaww.Fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.os.Looper;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.memaww.memaww.R;
import com.memaww.memaww.Util.Config;

import java.util.Calendar;

public class CollectionFormFragment extends Fragment {

    private TimePicker mDateTimeTimePicker;
    FusedLocationProviderClient mFusedLocationClient;
    int PERMISSION_ID = 44;
    private EditText mLocationEditText, ContactPersonPhoneEditText;
    private TextView mGetMyLocationTextView;
    private AppCompatButton mDoneButton;
    private Boolean useCurrentLocation = false;
    private String collectionLoc = "Not set", collectionDate = "Not Set", collectionPhone = "Not Set", collectionLocGPS = "0";

    public interface onCollectionFormDoneButtonClickedEventListener {
        public void collectionFormDoneButtonClicked(String collectionLocation, String collectionLocationGPS, String collectionDateTime, String collectionContactPhone);
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
        mDateTimeTimePicker = view.findViewById(R.id.fragment_collectionform_pickupdatetime_timepicker);
        ContactPersonPhoneEditText = view.findViewById(R.id.fragment_collectionform_contactpersonphone_edittext);
        mDoneButton = view.findViewById(R.id.fragment_collectionform_done_button);
        mGetMyLocationTextView = view.findViewById(R.id.fragment_collectionform_usegps_textview);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());


        //mDateTimeTimePicker.setIs24HourView(true);//set Timer to 24 hours Format
        mDateTimeTimePicker.setCurrentHour(Calendar.getInstance().get(Calendar.HOUR_OF_DAY));


        mGetMyLocationTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // method to get the location
                useCurrentLocation = true;
                getLastLocation();
            }
        });

        mDoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!mLocationEditText.getText().toString().trim().equalsIgnoreCase("")){
                    collectionLoc = mLocationEditText.getText().toString().trim();
                }
                //if(!mDateTimeTimePicker.getText().toString().trim().equalsIgnoreCase("")){
                    collectionDate = String.valueOf(mDateTimeTimePicker.getHour()) + " : " + String.valueOf(mDateTimeTimePicker.getMinute());
                //}
                if(!ContactPersonPhoneEditText.getText().toString().trim().equalsIgnoreCase("")){
                    collectionPhone = ContactPersonPhoneEditText.getText().toString().trim();
                }

                collectionFormDoneButtonClickedListener.collectionFormDoneButtonClicked(collectionLoc, collectionLocGPS, collectionDate, collectionPhone);
                getActivity().onBackPressed();
            }
        });
        return view;
    }


    @SuppressLint("MissingPermission")
    private void getLastLocation() {
        // check if permissions are given
        if (checkPermissions()) {

            // check if location is enabled
            if (isLocationEnabled()) {

                // getting last
                // location from
                // FusedLocationClient
                // object
                mFusedLocationClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        Location location = task.getResult();
                        if (location == null) {
                            requestNewLocationData();
                        } else {
                            if(useCurrentLocation){
                                collectionLocGPS = location.getLatitude() + "," + location.getLongitude() + "";
                                mLocationEditText.setText("Current Location");
                            }
                            //Config.showToastType1(getActivity(), location.getLatitude() + " " + location.getLongitude() + "");
                        }
                    }
                });
            } else {
                Config.showToastType1(getActivity(), "Please turn on your location...");
                //Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                //startActivity(intent);
            }
        } else {
            // if permissions aren't available,
            // request for permissions
            requestPermissions();
        }
    }

    @SuppressLint("MissingPermission")
    private void requestNewLocationData() {

        // Initializing LocationRequest
        // object with appropriate methods
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(5);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);

        // setting LocationRequest
        // on FusedLocationClient
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
    }

    private LocationCallback mLocationCallback = new LocationCallback() {

        @Override
        public void onLocationResult(LocationResult locationResult) {
            if(useCurrentLocation) {
                Location mLastLocation = locationResult.getLastLocation();
                collectionLocGPS = mLastLocation.getLatitude() + "," + mLastLocation.getLongitude() + "";
                mLocationEditText.setText("Set To Your Current Location");
            }
            //Config.showToastType1(getActivity(), mLastLocation.getLatitude() + " " + mLastLocation.getLongitude() + "");
        }
    };

    // method to check for permissions
    private boolean checkPermissions() {
        return ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;

        // If we want background location
        // on Android 10.0 and higher,
        // use:
        // ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    // method to request for permissions
    private void requestPermissions() {
        ActivityCompat.requestPermissions(getActivity(), new String[]{
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_ID);
    }

    // method to check
    // if location is enabled
    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getActivity().getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    // If everything is alright then
    @Override
    public void
    onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_ID) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (checkPermissions()) {
            getLastLocation();
        }
    }

}