package com.memaww.memaww.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.memaww.memaww.Activities.OrderCollectionActivity;
import com.memaww.memaww.ListDataGenerators.MyOrdersListDataGenerator;
import com.memaww.memaww.Models.OrderModel;
import com.memaww.memaww.R;
import com.memaww.memaww.Util.Config;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MyOrdersFragment extends Fragment implements View.OnClickListener {

    //private RecyclerView mOrdersListRecyclerView;
    private SwipeRefreshLayout mMainParentSwipeRefreshLayout;
    private RecyclerView mRecyclerview;
    private ImageView mBackgroundImageImageView;
    private TextView mBackgroundTextTextView;
    private LinearLayoutManager mLinearlayoutmanager;
    private ContentLoadingProgressBar mLoadingContentLoadingProgressBar;
    private Thread backgroundThread = null;

    public MyOrdersFragment() {
        // Required empty public constructor
    }

    public static MyOrdersFragment newInstance(String param1, String param2) {
        MyOrdersFragment fragment = new MyOrdersFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_orders, container, false);

        mMainParentSwipeRefreshLayout = view.findViewById(R.id.fragment_myorders_mainparent_swiperefreshlayout);
        mRecyclerview = view.findViewById(R.id.fragment_myorders_orderslist_recyclerview);
        mLoadingContentLoadingProgressBar = view.findViewById(R.id.fragment_myorders_loader);
        mBackgroundImageImageView = view.findViewById(R.id.fragment_myorders_noordersimage_imageview);
        mBackgroundTextTextView = view.findViewById(R.id.fragment_myorders_noorderstext_textview);


        //ONE-TIME ACTIONS
        mLinearlayoutmanager = new LinearLayoutManager(getActivity().getApplicationContext());
        mRecyclerview.setItemViewCacheSize(20);
        mRecyclerview.setDrawingCacheEnabled(true);
        mRecyclerview.setHasFixedSize(true);
        mRecyclerview.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        mRecyclerview.setLayoutManager(mLinearlayoutmanager);
        mRecyclerview.setAdapter(new RecyclerViewAdapter());

        backgroundThread = new Thread(new Runnable() {
            @Override
            public void run() {
                getMyOrders();
            }
        });
        backgroundThread.start();

        mMainParentSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                backgroundThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        MyOrdersListDataGenerator.getAllData().clear();
                        getMyOrders();
                    }
                });
                backgroundThread.start();
            }
        });

        return view;
    }

    @Override
    public void onClick(View view) {

    }



    private void allOnClickHandlers(View v, int position){
        if(v.getId() == R.id.list_item_order_image_roundedcornerimageview){
            Config.showDialogType1(getActivity(), "1", "Price: " + MyOrdersListDataGenerator.getAllData().get(position).getOrderPriceCurrency() + " "  + MyOrdersListDataGenerator.getAllData().get(position).getOrderFinalPrice(), "", null, false, "", "");
        }

    }


    private class RecyclerViewAdapter extends RecyclerView.Adapter {

        @Override
        public int getItemViewType(int position) {
            if(MyOrdersListDataGenerator.getAllData().get(position).getOrderId() == 0){
                return 0;
            }
            return 1;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            RecyclerView.ViewHolder vh;
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_order, parent, false);
            vh = new OrderViewHolder(v);

            return vh;
        }


        public class OrderViewHolder extends RecyclerView.ViewHolder  {
            private ConstraintLayout mTagHolderConstraintlayout;
            private ImageView mInfoImageView;
            private TextView mOrderIdTextview, mItemsCountTextview, mPriceTextview, mStatusTextView, mDateTextView, mLocationTextView;

            private View.OnClickListener innerClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    allOnClickHandlers(v, getAdapterPosition());
                }
            };

            public OrderViewHolder(View v) {
                super(v);
                mTagHolderConstraintlayout = v.findViewById(R.id.tag_holder);
                mInfoImageView = v.findViewById(R.id.list_item_order_image_roundedcornerimageview);
                mOrderIdTextview = v.findViewById(R.id.list_item_order_title_textview);
                mItemsCountTextview = v.findViewById(R.id.list_item_order_body_textview);
                mPriceTextview = v.findViewById(R.id.list_item_order_orderamount_textview);
                mStatusTextView = v.findViewById(R.id.list_item_order_label_textview);
                mDateTextView = v.findViewById(R.id.list_item_order_orderdate_textview);
                mLocationTextView = v.findViewById(R.id.list_item_order_loc_textview);

                mTagHolderConstraintlayout.setOnClickListener(innerClickListener);
                mInfoImageView.setOnClickListener(innerClickListener);
                mOrderIdTextview.setOnClickListener(innerClickListener);
                mItemsCountTextview.setOnClickListener(innerClickListener);
                mPriceTextview.setOnClickListener(innerClickListener);
                mStatusTextView.setOnClickListener(innerClickListener);
                mDateTextView.setOnClickListener(innerClickListener);
                mLocationTextView.setOnClickListener(innerClickListener);
            }
        }

        @Override
        public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {

            ((OrderViewHolder) holder).mDateTextView.setText(String.valueOf(MyOrdersListDataGenerator.getAllData().get(position).getCreatedAtShortDate()));
            ((OrderViewHolder) holder).mOrderIdTextview.setText("#" + String.valueOf(MyOrdersListDataGenerator.getAllData().get(position).getOrderId()));
            ((OrderViewHolder) holder).mItemsCountTextview.setText(" x " + MyOrdersListDataGenerator.getAllData().get(position).getOrderAllItemsQuantity());
            ((OrderViewHolder) holder).mPriceTextview.setText(MyOrdersListDataGenerator.getAllData().get(position).getOrderPriceCurrency() + MyOrdersListDataGenerator.getAllData().get(position).getOrderFinalPrice()); // + String.valueOf(MyOrdersListDataGenerator.getAllData().get(position).get()));

            ((OrderViewHolder) holder).mLocationTextView.setText(MyOrdersListDataGenerator.getAllData().get(position).getOrderCollectionLocationRaw());
            ((OrderViewHolder) holder).mStatusTextView.setText(MyOrdersListDataGenerator.getAllData().get(position).getOrderPaymentStatusMessage());

            if(MyOrdersListDataGenerator.getAllData().get(position).getOrderBeingWorkedOnStatus() == 0){
                ((OrderViewHolder) holder).mTagHolderConstraintlayout.setBackgroundColor(getResources().getColor(R.color.color_gray_light_v2));
            } else if(MyOrdersListDataGenerator.getAllData().get(position).getOrderBeingWorkedOnStatus() == 2){
                ((OrderViewHolder) holder).mTagHolderConstraintlayout.setBackgroundColor(getResources().getColor(R.color.color_blue_light));
            } else if(MyOrdersListDataGenerator.getAllData().get(position).getOrderBeingWorkedOnStatus() == 3){
                ((OrderViewHolder) holder).mTagHolderConstraintlayout.setBackgroundColor(getResources().getColor(R.color.color_orange_light_v1));
            } else if(MyOrdersListDataGenerator.getAllData().get(position).getOrderBeingWorkedOnStatus() == 4){
                ((OrderViewHolder) holder).mTagHolderConstraintlayout.setBackgroundColor(getResources().getColor(R.color.color_yellow_light_v1));
            } else if(MyOrdersListDataGenerator.getAllData().get(position).getOrderBeingWorkedOnStatus() == 5){
                ((OrderViewHolder) holder).mTagHolderConstraintlayout.setBackgroundColor(getResources().getColor(R.color.color_orange_light_v1));
            } else if(MyOrdersListDataGenerator.getAllData().get(position).getOrderBeingWorkedOnStatus() == 6){
                ((OrderViewHolder) holder).mTagHolderConstraintlayout.setBackgroundColor(getResources().getColor(R.color.color_green_light_v1));
            } else {
                ((OrderViewHolder) holder).mTagHolderConstraintlayout.setBackgroundColor(getResources().getColor(R.color.color_red_light_v1));
            }
            /*

            if(ArticleListDataGenerator.getAllData().get(position).getArticle_body().length() > 200){
                ((OrderViewHolder) holder).m_body_textview.setText(ArticleListDataGenerator.getAllData().get(position).getArticle_body().substring(0, 199).trim() + "...");
            } else {
                ((OrderViewHolder) holder).m_body_textview.setText(ArticleListDataGenerator.getAllData().get(position).getArticle_body());
            }

            ((OrderViewHolder) holder).m_tag_textview.setText(ArticleListDataGenerator.getAllData().get(position).getArticle_type());

            if(ArticleListDataGenerator.getAllData().get(position).getArticle_type().equalsIgnoreCase("HERALD OF GLORY")){
                //((OrderViewHolder) holder).m_tag_holder_constraintlayout.setBackground(getActivity().getResources().getDrawable(R.drawable.curved_bottom_left_corners_gold, null));
            } else if(ArticleListDataGenerator.getAllData().get(position).getArticle_type().equalsIgnoreCase("SPECIAL ARTICLE")){
                //((OrderViewHolder) holder).m_tag_holder_constraintlayout.setBackground(getActivity().getResources().getDrawable(R.drawable.rounded_corners_specialarticle, null));
            } else if(ArticleListDataGenerator.getAllData().get(position).getArticle_type().equalsIgnoreCase("GLORY NEWS")){
                //((OrderViewHolder) holder).m_tag_holder_constraintlayout.setBackground(getActivity().getResources().getDrawable(R.drawable.rounded_corners_glorynews, null));
            } else if(ArticleListDataGenerator.getAllData().get(position).getArticle_type().equalsIgnoreCase("BIBLE READING PLAN")){
                //((OrderViewHolder) holder).m_tag_holder_constraintlayout.setBackground(getActivity().getResources().getDrawable(R.drawable.rounded_corners_bible_reading, null));
            }
             */

        }

        @Override
        public int getItemCount() {
            return MyOrdersListDataGenerator.getAllData().size();
        }

    }

    public void getMyOrders() {

        if (!getActivity().isFinishing() && getActivity().getApplicationContext() != null) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    mRecyclerview.setVisibility(View.INVISIBLE);
                    mBackgroundImageImageView.setVisibility(View.INVISIBLE);
                    mBackgroundTextTextView.setVisibility(View.INVISIBLE);
                    mLoadingContentLoadingProgressBar.setVisibility(View.VISIBLE);
                }
            });
        }

        AndroidNetworking.post(Config.LINK_GET_MY_ORDERS)
                .addHeaders("Accept", "application/json")
                .addHeaders("Authorization", "Bearer " + Config.getSharedPreferenceString(getActivity(), Config.SHARED_PREF_KEY_USER_CREDENTIALS_USER_PASSWORD_ACCESS_TOKEN))
                .addBodyParameter("app_type", "ANDROID")
                .addBodyParameter("app_version_code", String.valueOf(Config.getAppVersionCode(getActivity().getApplicationContext())))
                .setTag("get_suggestion")
                .setPriority(Priority.HIGH)
                .build().getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        if (!getActivity().isFinishing() && getActivity().getApplicationContext() != null) {
                            Log.e("SERVER-REQUEST", "response: " + response.toString());
                            try {
                                JSONObject main_response = new JSONObject(response);
                                String myStatus = main_response.getString("status");
                                final String myStatusMessage = main_response.getString("message");
                                JSONArray myOrdersArray = main_response.getJSONArray("data");
                                if (myOrdersArray.length() > 0) {
                                    MyOrdersListDataGenerator.getAllData().clear();

                                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                                        @Override
                                        public void run() {
                                            mRecyclerview.getAdapter().notifyDataSetChanged();
                                        }
                                    });
                                    for (int i = 0; i < myOrdersArray.length(); i++) {
                                        OrderModel thisOrder = new OrderModel();
                                        final JSONObject k = myOrdersArray.getJSONObject(i);
                                        thisOrder.setOrderId(k.getLong("order_id"));
                                        thisOrder.setOrderSysId(k.getString("order_sys_id"));
                                        thisOrder.setOrderUserId(k.getLong("order_user_id"));
                                        thisOrder.setOrderLaundryspId(k.getLong("order_laundrysp_id"));
                                        thisOrder.setOrderCollectionBikerName(k.getString("order_collection_biker_name"));
                                        thisOrder.setOrderCollectionLocationRaw(k.getString("order_collection_location_raw"));
                                        thisOrder.setOrderCollectionLocationGps(k.getString("order_collection_location_gps"));
                                        thisOrder.setOrderCollectionDate(k.getString("order_collection_date"));
                                        thisOrder.setOrderCollectionContactPersonPhone(k.getString("order_collection_contact_person_phone"));
                                        thisOrder.setOrderDropoffLocationRaw(k.getString("order_dropoff_location_raw"));
                                        thisOrder.setOrderDropoffLocationGps(k.getString("order_dropoff_location_gps"));
                                        thisOrder.setOrderDropoffDate(k.getString("order_dropoff_date"));
                                        thisOrder.setOrderDropoffContactPersonPhone(k.getString("order_dropoff_contact_person_phone"));
                                        thisOrder.setOrderDropoffBikerName(k.getString("order_dropoff_biker_name"));
                                        thisOrder.setOrderLightweightitemsJustWashQuantity(k.getInt("order_lightweightitems_just_wash_quantity"));
                                        thisOrder.setOrderLightweightitemsWashAndIronQuantity(k.getInt("order_lightweightitems_wash_and_iron_quantity"));
                                        thisOrder.setOrderBulkyitemsJustWashQuantity(k.getInt("order_bulkyitems_just_wash_quantity"));
                                        thisOrder.setOrderBulkyitemsWashAndIronQuantity(k.getInt("order_bulkyitems_wash_and_iron_quantity"));
                                        thisOrder.setOrderAllItemsQuantity(k.getString("all_items"));
                                        thisOrder.setOrderBeingWorkedOnStatus(k.getInt("order_status"));
                                        thisOrder.setOrderPaymentStatusMessage(k.getString("order_status_message"));
                                        thisOrder.setOrderPaymentStatus(k.getInt("order_payment_status"));
                                        thisOrder.setOrderPaymentDetails(k.getString("order_payment_details"));
                                        thisOrder.setOrderFinalPrice(k.getString("order_final_price_in_user_countrys_currency"));
                                        thisOrder.setOrderPriceCurrency(k.getString("order_user_countrys_currency"));
                                        thisOrder.setOrderFlagged(k.getInt("order_flagged"));
                                        thisOrder.setOrderFlaggedReason(k.getString("order_flagged_reason"));
                                        thisOrder.setCreatedAt(k.getString("created_at"));
                                        thisOrder.setCreatedAtShortDate(k.getString("order_date"));
                                        thisOrder.setUpdatedAt(k.getString("updated_at"));
                                        MyOrdersListDataGenerator.addOneData(thisOrder);
                                    }

                                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                                        @Override
                                        public void run() {
                                            if (!getActivity().isFinishing()  && getActivity().getApplicationContext() != null) {
                                                mRecyclerview.getAdapter().notifyItemInserted(MyOrdersListDataGenerator.getAllData().size());
                                                mLoadingContentLoadingProgressBar.setVisibility(View.INVISIBLE);
                                                mBackgroundImageImageView.setVisibility(View.INVISIBLE);
                                                mBackgroundTextTextView.setVisibility(View.INVISIBLE);
                                                mRecyclerview.setVisibility(View.VISIBLE);
                                                mMainParentSwipeRefreshLayout.setRefreshing(false);
                                            }
                                        }
                                    });
                                } else {

                                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                                        @Override
                                        public void run() {
                                            mBackgroundImageImageView.setVisibility(View.VISIBLE);
                                            mBackgroundTextTextView.setVisibility(View.VISIBLE);
                                            mBackgroundTextTextView.setText("No Orders found");
                                            mLoadingContentLoadingProgressBar.setVisibility(View.INVISIBLE);
                                            mRecyclerview.setVisibility(View.VISIBLE);
                                            mMainParentSwipeRefreshLayout.setRefreshing(false);
                                            //Config.showToastType1(getActivity(), "No Orders found");
                                        }
                                    });
                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                                new Handler(Looper.getMainLooper()).post(new Runnable() {
                                    @Override
                                    public void run() {
                                        Config.showToastType1(getActivity(), getString(R.string.an_unexpected_error_occured));
                                        mRecyclerview.setVisibility(View.INVISIBLE);
                                        mLoadingContentLoadingProgressBar.setVisibility(View.VISIBLE);
                                        mMainParentSwipeRefreshLayout.setRefreshing(false);
                                    }
                                });
                            }
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("SERVER-REQUEST", "anError: " + anError.toString());
                        if (!getActivity().isFinishing() && getActivity().getApplicationContext() != null) {
                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                @Override
                                public void run() {
                                    Config.showToastType1(getActivity(), getResources().getString(R.string.check_your_internet_connection_and_try_again));
                                    mRecyclerview.setVisibility(View.INVISIBLE);
                                    mLoadingContentLoadingProgressBar.setVisibility(View.VISIBLE);
                                    mMainParentSwipeRefreshLayout.setRefreshing(false);
                                }
                            });
                        }
                    }
                });

    }
}