package com.memaww.memaww.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.memaww.memaww.ListDataGenerators.MyOrdersListDataGenerator;
import com.memaww.memaww.Models.OrderModel;
import com.memaww.memaww.R;
import com.memaww.memaww.Util.Config;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MyOrdersFragment extends Fragment implements View.OnClickListener {

    private ImageView mBackImageView;
    private RecyclerView mOrdersListRecyclerView;
    private RecyclerView mRecyclerview;
    private LinearLayoutManager mLinearlayoutmanager;
    private ContentLoadingProgressBar mLoadingContentLoadingProgressBar;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public MyOrdersFragment() {
        // Required empty public constructor
    }

    public static MyOrdersFragment newInstance(String param1, String param2) {
        MyOrdersFragment fragment = new MyOrdersFragment();
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
        View view = inflater.inflate(R.layout.fragment_my_orders, container, false);

        mBackImageView = view.findViewById(R.id.fragment_myorders_menuholder_back_imageview);
        mOrdersListRecyclerView = view.findViewById(R.id.fragment_myorders_orderslist_recyclerview);
        mLoadingContentLoadingProgressBar = view.findViewById(R.id.fragment_myorders_orderslist_recyclerview);


        //ONE-TIME ACTIONS
        mLinearlayoutmanager = new LinearLayoutManager(getActivity().getApplicationContext());
        mRecyclerview.setItemViewCacheSize(20);
        mRecyclerview.setDrawingCacheEnabled(true);
        mRecyclerview.setHasFixedSize(true);
        mRecyclerview.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        mRecyclerview.setLayoutManager(mLinearlayoutmanager);
        mRecyclerview.setAdapter(new RecyclerViewAdapter());
        getMyOrders()

        return view;
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == mBackImageView.getId()){
            getActivity().onBackPressed();
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
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_read, parent, false);
            vh = new ArticleViewHolder(v);

            return vh;
        }


        public class ArticleViewHolder extends RecyclerView.ViewHolder  {
            private ConstraintLayout m_parent_holder_constraintlayout, m_tag_holder_constraintlayout;
            private ImageView m_audio_image;
            private TextView m_title_textview, m_body_textview, m_tag_textview;

            private View.OnClickListener innerClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    allOnClickHandlers(v, getAdapterPosition());
                }
            };

            public ArticleViewHolder(View v) {
                super(v);
                m_parent_holder_constraintlayout = v.findViewById(R.id.article_parent);
                m_title_textview = v.findViewById(R.id.fragment_today_heraldofglorytitle_textview);
                m_body_textview = v.findViewById(R.id.fragment_today_heraldofglorybody_textview);
                m_tag_holder_constraintlayout = v.findViewById(R.id.tag_holder);
                m_audio_image = v.findViewById(R.id.fragment_today_heraldofgloryimage_roundedcornerimageview);
                m_tag_textview = v.findViewById(R.id.fragment_today_heraldofglorylabel_textview);

                m_parent_holder_constraintlayout.setOnClickListener(innerClickListener);
                m_title_textview.setOnClickListener(innerClickListener);
                m_body_textview.setOnClickListener(innerClickListener);
                m_tag_holder_constraintlayout.setOnClickListener(innerClickListener);
                m_audio_image.setOnClickListener(innerClickListener);
                m_tag_textview.setOnClickListener(innerClickListener);
            }
        }

        @Override
        public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {

            if(
                    !getActivity().isFinishing()
                            && !ArticleListDataGenerator.getAllData().get(position).getArticle_image().equalsIgnoreCase("")
            ){

                loadImageView(getActivity().getApplicationContext(), ArticleListDataGenerator.getAllData().get(position).getArticle_image().trim(), ((ArticleViewHolder) holder).m_audio_image, null);

            } else {
                ((ArticleViewHolder) holder).m_audio_image.setImageResource(R.drawable.testimg);
            }

            if(ArticleListDataGenerator.getAllData().get(position).getArticle_title().length() > 20){
                ((ArticleViewHolder) holder).m_title_textview.setText(ArticleListDataGenerator.getAllData().get(position).getArticle_title().substring(0, 19).trim() + "...");
            } else {
                ((ArticleViewHolder) holder).m_title_textview.setText(ArticleListDataGenerator.getAllData().get(position).getArticle_title());
            }

            if(ArticleListDataGenerator.getAllData().get(position).getArticle_body().length() > 200){
                ((ArticleViewHolder) holder).m_body_textview.setText(ArticleListDataGenerator.getAllData().get(position).getArticle_body().substring(0, 199).trim() + "...");
            } else {
                ((ArticleViewHolder) holder).m_body_textview.setText(ArticleListDataGenerator.getAllData().get(position).getArticle_body());
            }

            ((ArticleViewHolder) holder).m_tag_textview.setText(ArticleListDataGenerator.getAllData().get(position).getArticle_type());

            if(ArticleListDataGenerator.getAllData().get(position).getArticle_type().equalsIgnoreCase("HERALD OF GLORY")){
                //((ArticleViewHolder) holder).m_tag_holder_constraintlayout.setBackground(getActivity().getResources().getDrawable(R.drawable.curved_bottom_left_corners_gold, null));
            } else if(ArticleListDataGenerator.getAllData().get(position).getArticle_type().equalsIgnoreCase("SPECIAL ARTICLE")){
                //((ArticleViewHolder) holder).m_tag_holder_constraintlayout.setBackground(getActivity().getResources().getDrawable(R.drawable.rounded_corners_specialarticle, null));
            } else if(ArticleListDataGenerator.getAllData().get(position).getArticle_type().equalsIgnoreCase("GLORY NEWS")){
                //((ArticleViewHolder) holder).m_tag_holder_constraintlayout.setBackground(getActivity().getResources().getDrawable(R.drawable.rounded_corners_glorynews, null));
            } else if(ArticleListDataGenerator.getAllData().get(position).getArticle_type().equalsIgnoreCase("BIBLE READING PLAN")){
                //((ArticleViewHolder) holder).m_tag_holder_constraintlayout.setBackground(getActivity().getResources().getDrawable(R.drawable.rounded_corners_bible_reading, null));
            }

        }

        @Override
        public int getItemCount() {
            return ArticleListDataGenerator.getAllData().size();
        }

    }

    public void getMyOrders() {

        if (!getActivity().isFinishing() && getActivity().getApplicationContext() != null) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    mRecyclerview.setVisibility(View.INVISIBLE);
                    mLoadingContentLoadingProgressBar.setVisibility(View.VISIBLE);
                }
            });
        }

        AndroidNetworking.post(Config.LINK_COLLECTION_REQUEST_ORDER)
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
                                        thisOrder.setOrderBeingWorkedOnStatus(k.getInt("order_being_worked_on_status"));
                                        thisOrder.setOrderPaymentStatus(k.getInt("order_payment_status"));
                                        thisOrder.setOrderPaymentDetails(k.getString("order_payment_details"));
                                        thisOrder.setOrderFlagged(k.getBoolean("order_flagged"));
                                        thisOrder.setOrderFlaggedReason(k.getString("order_flagged_reason"));
                                        thisOrder.setCreatedAt(k.getString("created_at"));
                                        thisOrder.setUpdatedAt(k.getString("updated_at"));
                                        MyOrdersListDataGenerator.addOneData(thisOrder);
                                    }

                                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                                        @Override
                                        public void run() {
                                            if (!getActivity().isFinishing()) {
                                                mRecyclerview.getAdapter().notifyItemInserted(MyOrdersListDataGenerator.getAllData().size());
                                                mLoadingContentLoadingProgressBar.setVisibility(View.INVISIBLE);
                                                mRecyclerview.setVisibility(View.VISIBLE);
                                            }
                                        }
                                    });
                                } else {

                                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                                        @Override
                                        public void run() {
                                            mLoadingContentLoadingProgressBar.setVisibility(View.INVISIBLE);
                                            mRecyclerview.setVisibility(View.VISIBLE);
                                            Config.showToastType1(getActivity(), "No Orders found");
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
                                }
                            });
                        }
                    }
                });

    }
}