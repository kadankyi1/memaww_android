package com.memaww.memaww.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.memaww.memaww.Fragments.BulkyItemsFormFragment;
import com.memaww.memaww.Fragments.CollectionFormFragment;
import com.memaww.memaww.Fragments.LightWeightItemsFormFragment;
import com.memaww.memaww.R;
import com.memaww.memaww.Util.Config;

public class BookingActivity extends AppCompatActivity implements View.OnClickListener, CollectionFormFragment.onCollectionFormDoneButtonClickedEventListener, LightWeightItemsFormFragment.onLightWeightItemsFormDoneButtonClickedEventListener, BulkyItemsFormFragment.onBulkyItemsFormDoneButtonClickedEventListener {

    private CardView mCollectionAndDropOffInfoCardView, mLightweighItemsInfoCardView, mBulkyItemsInfoCardView;
    private TextView mCollectionAndDropOffLocationTextView, mCollectionTimeTextView, mContactPersonTextView, mLightWeightItemsTextView, mBulkyItemsTextView;
    private int fragmentOpenStatus = 0, allLightWeightItems = 0, allBulkyItems = 0;
    private AppCompatButton mProceedButton;
    private String collectionAndDropOffLocation = "", collectionTime = "", contactPerson = "", lightWeightItemsJustWash = "", lightWeightItemsWashAndIron = "", bulkyItemsJustWash = "", bulkyItemsWashAndIron = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        mCollectionAndDropOffInfoCardView = findViewById(R.id.activity_book_formitemsholderscrollview_pickup_constraintlayout);
        mLightweighItemsInfoCardView = findViewById(R.id.activity_book_formitemsholderscrollview_justwash_constraintlayout);
        mBulkyItemsInfoCardView = findViewById(R.id.activity_book_formitemsholderscrollview_justwashnonwearables_constraintlayout);
        mProceedButton = findViewById(R.id.activity_book_proceedholder_button);


        mCollectionAndDropOffLocationTextView = findViewById(R.id.activity_book_date_textview);
        mCollectionTimeTextView = findViewById(R.id.activity_book_time_textview);
        mContactPersonTextView = findViewById(R.id.activity_book_collectperson_textview);
        mLightWeightItemsTextView = findViewById(R.id.activity_book_justwashquantity_textview);
        mBulkyItemsTextView = findViewById(R.id.activity_book_justwashnonwearablesquantity_textview);
        mBulkyItemsTextView = findViewById(R.id.activity_book_justwashnonwearablesquantity_textview);

        // NEEDED THINGS TO BE DONE ONE-TIME
        mCollectionAndDropOffInfoCardView.setOnClickListener(this);
        mLightweighItemsInfoCardView.setOnClickListener(this);
        mBulkyItemsInfoCardView.setOnClickListener(this);
        mProceedButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == mCollectionAndDropOffInfoCardView.getId()){
            if(fragmentOpenStatus == 0){
                fragmentOpenStatus = 1;
                Config.openFragment(getSupportFragmentManager(), R.id.activity_book_formholder_fragment, CollectionFormFragment.newInstance("", ""), "CollectionFormFragment", 3);

            }
        } else if(view.getId() == mLightweighItemsInfoCardView.getId()){
            if(fragmentOpenStatus == 0){
                fragmentOpenStatus = 1;
                Config.openFragment(getSupportFragmentManager(), R.id.activity_book_formholder_fragment, LightWeightItemsFormFragment.newInstance("", ""), "LightWeightItemsFormFragment", 3);

            }
        } else if(view.getId() == mBulkyItemsInfoCardView.getId()){
            if(fragmentOpenStatus == 0){
                fragmentOpenStatus = 1;
                Config.openFragment(getSupportFragmentManager(), R.id.activity_book_formholder_fragment, BulkyItemsFormFragment.newInstance("", ""), "BulkyItemsFormFragment", 3);

            }
        } else if(view.getId() == mProceedButton.getId()){
            placeOrder("order_type", "country", "phone", "first_name", "last_name");
        }
    }

    @Override
    public void collectionFormDoneButtonClicked(String collectionLocation, String collectionDateTime, String collectionContactPhone) {
        Config.showToastType1(BookingActivity.this, collectionLocation + ", " + collectionDateTime + ", " + collectionContactPhone);
        fragmentOpenStatus = 0;
        collectionAndDropOffLocation = collectionLocation;
        collectionTime = collectionDateTime;
        contactPerson = collectionContactPhone;


        mCollectionAndDropOffLocationTextView.setText(collectionAndDropOffLocation);
        mCollectionTimeTextView.setText(collectionTime);
        mContactPersonTextView.setText(contactPerson);

    }


    @Override
    public void lightWeightItemsFormDoneButtonClicked(String justWashItemsQuantity, String washAndIronItemsQuantity) {
        Config.showToastType1(BookingActivity.this, justWashItemsQuantity + ", " + washAndIronItemsQuantity);
        fragmentOpenStatus = 0;
        lightWeightItemsJustWash = justWashItemsQuantity;
        lightWeightItemsWashAndIron = washAndIronItemsQuantity;

        allLightWeightItems = Integer.valueOf(lightWeightItemsJustWash) +Integer.valueOf(lightWeightItemsWashAndIron);
        mLightWeightItemsTextView.setText(String.valueOf(allLightWeightItems) + " Items");
    }

    @Override
    public void bulkyItemsFormDoneButtonClicked(String justWashItemsQuantity, String washAndIronItemsQuantity) {
        Config.showToastType1(BookingActivity.this, justWashItemsQuantity + ", " + washAndIronItemsQuantity);
        fragmentOpenStatus = 0;
        bulkyItemsJustWash = justWashItemsQuantity;
        bulkyItemsWashAndIron = washAndIronItemsQuantity;

        allBulkyItems = Integer.valueOf(bulkyItemsJustWash) +Integer.valueOf(bulkyItemsWashAndIron);
        mBulkyItemsTextView.setText(String.valueOf(allBulkyItems) + " Items");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        fragmentOpenStatus = 0;
    }


    public void placeOrder(final String order_type, final String country, final String phone, final String first_name, final String last_name) {

        Config.showToastType1(BookingActivity.this, "Sending order to server");
    }
}