package com.memaww.memaww.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
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
import com.memaww.memaww.Fragments.MyOrdersFragment;
import com.memaww.memaww.ListDataGenerators.MyNotificationsListDataGenerator;
import com.memaww.memaww.Models.NotificationModel;
import com.memaww.memaww.Models.OrderModel;
import com.memaww.memaww.R;
import com.memaww.memaww.Util.Config;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class NotificationsActivity extends AppCompatActivity implements View.OnClickListener {

    //private RecyclerView mOrdersListRecyclerView;
    private SwipeRefreshLayout mMainParentSwipeRefreshLayout;
    private RecyclerView mRecyclerview;
    private ImageView mBackgroundImageImageView, mBackImageView;
    private TextView mBackgroundTextTextView;
    private LinearLayoutManager mLinearlayoutmanager;
    private ContentLoadingProgressBar mLoadingContentLoadingProgressBar;
    private Thread backgroundThread1 = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);
        Config.setSharedPreferenceBoolean(getApplicationContext(), Config.SHARED_PREF_KEY_USER_CREDENTIALS_USER_HAS_NEW_NOTIFICATION, false);


        mMainParentSwipeRefreshLayout = findViewById(R.id.notifications_activity_mainparent_swiperefreshlayout);
        mRecyclerview = findViewById(R.id.notifications_activity_orderslist_recyclerview);
        mLoadingContentLoadingProgressBar = findViewById(R.id.notifications_activity_loader);
        mBackgroundImageImageView = findViewById(R.id.notifications_activity_noordersimage_imageview);
        mBackgroundTextTextView = findViewById(R.id.notifications_activity_noorderstext_textview);
        mBackImageView = findViewById(R.id.activity_notification_menuholder_back_imageview);


        //ONE-TIME ACTIONS
        mLinearlayoutmanager = new LinearLayoutManager(getApplicationContext());
        mRecyclerview.setItemViewCacheSize(20);
        mRecyclerview.setDrawingCacheEnabled(true);
        mRecyclerview.setHasFixedSize(true);
        mRecyclerview.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        mRecyclerview.setLayoutManager(mLinearlayoutmanager);
        mRecyclerview.setAdapter(new RecyclerViewAdapter());

        backgroundThread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                getMyNotifications();
            }
        });
        backgroundThread1.start();

        mMainParentSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                backgroundThread1 = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        MyNotificationsListDataGenerator.getAllData().clear();
                        getMyNotifications();
                    }
                });
                backgroundThread1.start();
            }
        });

        mBackImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }


    private void allOnClickHandlers(View v, int position){}

    @Override
    public void onClick(View view) {

    }


    private class RecyclerViewAdapter extends RecyclerView.Adapter {

        @Override
        public int getItemViewType(int position) {
            return 1;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            RecyclerView.ViewHolder vh;
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_notification, parent, false);
            vh = new RecyclerViewAdapter.OrderViewHolder(v);

            return vh;
        }


        public class OrderViewHolder extends RecyclerView.ViewHolder  {
            private TextView mMessageTitleTextView, mMessageBodyTextView, mMessageDateTextView;

            private View.OnClickListener innerClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    allOnClickHandlers(v, getAdapterPosition());
                }
            };

            public OrderViewHolder(View v) {
                super(v);
                mMessageTitleTextView = v.findViewById(R.id.list_item_notification_title_textview);
                mMessageBodyTextView = v.findViewById(R.id.list_item_notification_notificationdate_textview);
                mMessageDateTextView = v.findViewById(R.id.list_item_notifications_date_textview);

                mMessageTitleTextView.setOnClickListener(innerClickListener);
                mMessageBodyTextView.setOnClickListener(innerClickListener);
                mMessageDateTextView.setOnClickListener(innerClickListener);
            }
        }

        @Override
        public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
            ((RecyclerViewAdapter.OrderViewHolder) holder).mMessageTitleTextView.setText(MyNotificationsListDataGenerator.getAllData().get(position).getNotificationTitle());
            ((RecyclerViewAdapter.OrderViewHolder) holder).mMessageBodyTextView.setText(MyNotificationsListDataGenerator.getAllData().get(position).getNotificationBody());
            ((RecyclerViewAdapter.OrderViewHolder) holder).mMessageDateTextView.setText(MyNotificationsListDataGenerator.getAllData().get(position).getNotificationDate());
        }

        @Override
        public int getItemCount() {
            return MyNotificationsListDataGenerator.getAllData().size();
        }

    }

    public void getMyNotifications() {

        if (!NotificationsActivity.this.isFinishing() && getApplicationContext() != null) {
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

        AndroidNetworking.post(Config.LINK_GET_MY_NOTIFICATIONS)
                .addHeaders("Accept", "application/json")
                .addHeaders("Authorization", "Bearer " + Config.getSharedPreferenceString(NotificationsActivity.this, Config.SHARED_PREF_KEY_USER_CREDENTIALS_USER_PASSWORD_ACCESS_TOKEN))
                .addBodyParameter("app_type", "ANDROID")
                .addBodyParameter("app_version_code", String.valueOf(Config.getAppVersionCode(getApplicationContext())))
                .setTag("get_suggestion")
                .setPriority(Priority.HIGH)
                .build().getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        if (!NotificationsActivity.this.isFinishing() && getApplicationContext() != null) {
                            Log.e("SERVER-REQUEST", "response: " + response.toString());
                            try {
                                JSONObject main_response = new JSONObject(response);
                                String myStatus = main_response.getString("status");
                                final String myStatusMessage = main_response.getString("message");
                                JSONArray myOrdersArray = main_response.getJSONArray("data");
                                if (myOrdersArray.length() > 0) {
                                    MyNotificationsListDataGenerator.getAllData().clear();

                                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                                        @Override
                                        public void run() {
                                            mRecyclerview.getAdapter().notifyDataSetChanged();
                                        }
                                    });
                                    for (int i = 0; i < myOrdersArray.length(); i++) {
                                        NotificationModel thisNotification = new NotificationModel();
                                        final JSONObject k = myOrdersArray.getJSONObject(i);
                                        thisNotification.setNotificationTitle(k.getString("notification_title"));
                                        thisNotification.setNotificationBody(k.getString("notification_body"));
                                        thisNotification.setNotificationDate(k.getString("notification_date"));
                                        MyNotificationsListDataGenerator.addOneData(thisNotification);
                                    }

                                    if (!NotificationsActivity.this.isFinishing()  && getApplicationContext() != null) {
                                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                                            @Override
                                            public void run() {
                                                mRecyclerview.getAdapter().notifyItemInserted(MyNotificationsListDataGenerator.getAllData().size());
                                                mLoadingContentLoadingProgressBar.setVisibility(View.INVISIBLE);
                                                mBackgroundImageImageView.setVisibility(View.INVISIBLE);
                                                mBackgroundTextTextView.setVisibility(View.INVISIBLE);
                                                mRecyclerview.setVisibility(View.VISIBLE);
                                                mMainParentSwipeRefreshLayout.setRefreshing(false);
                                            }
                                        });
                                    }
                                } else {

                                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                                        @Override
                                        public void run() {
                                            mBackgroundImageImageView.setVisibility(View.VISIBLE);
                                            mBackgroundTextTextView.setVisibility(View.VISIBLE);
                                            mBackgroundTextTextView.setText("No Notifications found");
                                            mLoadingContentLoadingProgressBar.setVisibility(View.INVISIBLE);
                                            mRecyclerview.setVisibility(View.VISIBLE);
                                            mMainParentSwipeRefreshLayout.setRefreshing(false);
                                            //Config.showToastType1(NotificationsActivity.this, "No Orders found");
                                        }
                                    });
                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                                new Handler(Looper.getMainLooper()).post(new Runnable() {
                                    @Override
                                    public void run() {
                                        Config.showToastType1(NotificationsActivity.this, getString(R.string.an_unexpected_error_occured));
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
                        if (!NotificationsActivity.this.isFinishing() && getApplicationContext() != null) {
                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                @Override
                                public void run() {
                                    Config.showToastType1(NotificationsActivity.this, getResources().getString(R.string.check_your_internet_connection_and_try_again));
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