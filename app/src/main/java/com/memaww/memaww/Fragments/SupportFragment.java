package com.memaww.memaww.Fragments;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.memaww.memaww.Activities.MainActivity;
import com.memaww.memaww.ListDataGenerators.MyMessagesListDataGenerator;
import com.memaww.memaww.Models.MessageModel;
import com.memaww.memaww.R;
import com.memaww.memaww.Services.MyFirebaseMessagingService;
import com.memaww.memaww.Util.Config;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SupportFragment extends Fragment {

    private SwipeRefreshLayout mMainParentSwipeRefreshLayout;
    private RecyclerView mRecyclerview;
    private ImageView mBackgroundImageImageView;
    private TextView mBackgroundTextTextView;
    private ConstraintLayout mSendMessageConstraintLayout;
    private EditText mMessageBoxEditText;
    private LinearLayoutManager mLinearlayoutmanager;
    private Dialog.OnCancelListener cancelListenerActive1;
    private ContentLoadingProgressBar mLoadingContentLoadingProgressBar;
    private Thread backgroundThread1 = null;
    private String currentMessage = "";

    public SupportFragment() {
        // Required empty public constructor
    }

    public static SupportFragment newInstance() {
        SupportFragment fragment = new SupportFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_support, container, false);

        mMainParentSwipeRefreshLayout = view.findViewById(R.id.fragment_supportfragment_mainparent_swiperefreshlayout);
        mRecyclerview = view.findViewById(R.id.fragment_supportfragment_orderslist_recyclerview);
        mBackgroundImageImageView = view.findViewById(R.id.fragment_supportfragment_noordersimage_imageview);
        mBackgroundTextTextView = view.findViewById(R.id.fragment_supportfragment_noorderstext_textview);
        mSendMessageConstraintLayout = view.findViewById(R.id.fragment_supportfragment_sendmessage_constraintayout);
        mMessageBoxEditText = view.findViewById(R.id.fragment_specialnotesform_pickuplocation_edittext_layout_holder);
        mLoadingContentLoadingProgressBar = view.findViewById(R.id.fragment_supportfragment_loader);


        //ONE-TIME ACTIONS
        mLinearlayoutmanager = new LinearLayoutManager(getActivity().getApplicationContext());
        mRecyclerview.setItemViewCacheSize(20);
        mRecyclerview.setDrawingCacheEnabled(true);
        mRecyclerview.setHasFixedSize(true);
        mRecyclerview.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        mRecyclerview.setLayoutManager(mLinearlayoutmanager);
        mRecyclerview.setAdapter(new RecyclerViewAdapter());
        backgroundThread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                getMyMessages(true);
            }
        });
        backgroundThread1.start();

        mMainParentSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                backgroundThread1 = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        getMyMessages(true);
                    }
                });
                backgroundThread1.start();
            }
        });

        mSendMessageConstraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!mMessageBoxEditText.getText().toString().trim().equalsIgnoreCase("")){
                    view.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.main_activity_onclick_icon_anim));
                    currentMessage = mMessageBoxEditText.getText().toString().trim();
                    backgroundThread1 = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            sendMessage(currentMessage);
                        }
                    });
                    backgroundThread1.start();
                    mMessageBoxEditText.getText().clear();
                }
            }
        });

        MyFirebaseMessagingService.
                Notification.
                getInstance().
                getNewOrder().
                observe(getViewLifecycleOwner(), new Observer<String>() {
                    @Override
                    public void onChanged(String s) {
                        //TODO: update your ui here...
                        Log.e("FIREBASE-MSG", "NEW MESSAGE FOR UI");
                        getMyMessages(false);
                    }
                });


        return view;
    }



    private void allOnClickHandlers(View v, int position){

    }


    private class RecyclerViewAdapter extends RecyclerView.Adapter {

        @Override
        public int getItemViewType(int position) {
            if(MyMessagesListDataGenerator.getAllData().get(position).getSenderUserId() == 1){
                return 1;
            }
            return 0;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            RecyclerView.ViewHolder vh;
            if(viewType == 1){
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_admin_message, parent, false);
                vh = new AdminMessageViewHolder(v);
            } else {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_user_message, parent, false);
                vh = new UserMessageViewHolder(v);
            }

            return vh;
        }

        public class AdminMessageViewHolder extends RecyclerView.ViewHolder  {
            private CardView mParentHolderCardView;
            private TextView mMessageTextView, mDateTextView;

            private View.OnClickListener innerClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    allOnClickHandlers(v, getAdapterPosition());
                }
            };

            public AdminMessageViewHolder(View v) {
                super(v);
                mParentHolderCardView = v.findViewById(R.id.list_item_message_the_parent_cardview);
                mMessageTextView = v.findViewById(R.id.list_item_message_text_textview);
                mDateTextView = v.findViewById(R.id.list_item_message_date_textview);

                mParentHolderCardView.setOnClickListener(innerClickListener);
                mMessageTextView.setOnClickListener(innerClickListener);
                mDateTextView.setOnClickListener(innerClickListener);
            }
        }

        public class UserMessageViewHolder extends RecyclerView.ViewHolder  {
            private CardView mParentHolderCardView;
            private TextView mMessageTextView, mDateTextView;

            private View.OnClickListener innerClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    allOnClickHandlers(v, getAdapterPosition());
                }
            };

            public UserMessageViewHolder(View v) {
                super(v);
                mParentHolderCardView = v.findViewById(R.id.list_item_message_the_parent_cardview);
                mMessageTextView = v.findViewById(R.id.list_item_message_text_textview);
                mDateTextView = v.findViewById(R.id.list_item_message_date_textview);

                mParentHolderCardView.setOnClickListener(innerClickListener);
                mMessageTextView.setOnClickListener(innerClickListener);
                mDateTextView.setOnClickListener(innerClickListener);
            }
        }

        @Override
        public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
            if(holder.getItemViewType() == 1) {
                ((SupportFragment.RecyclerViewAdapter.AdminMessageViewHolder) holder).mMessageTextView.setText(String.valueOf(MyMessagesListDataGenerator.getAllData().get(position).getMessageText()));
                ((SupportFragment.RecyclerViewAdapter.AdminMessageViewHolder) holder).mDateTextView.setText(String.valueOf(MyMessagesListDataGenerator.getAllData().get(position).getMessageDate()));
            } else {
                ((SupportFragment.RecyclerViewAdapter.UserMessageViewHolder) holder).mMessageTextView.setText(String.valueOf(MyMessagesListDataGenerator.getAllData().get(position).getMessageText()));
                ((SupportFragment.RecyclerViewAdapter.UserMessageViewHolder) holder).mDateTextView.setText(String.valueOf(MyMessagesListDataGenerator.getAllData().get(position).getMessageDate()));
            }
        }

        @Override
        public int getItemCount() {
            return MyMessagesListDataGenerator.getAllData().size();
        }

    }

    public void getMyMessages(boolean firstCall) {

        if (!getActivity().isFinishing() && getActivity().getApplicationContext() != null) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    if(firstCall){
                        mRecyclerview.setVisibility(View.INVISIBLE);
                        mBackgroundImageImageView.setVisibility(View.INVISIBLE);
                        mBackgroundTextTextView.setVisibility(View.INVISIBLE);
                        mLoadingContentLoadingProgressBar.setVisibility(View.VISIBLE);
                    }
                }
            });
        }

        AndroidNetworking.post(Config.LINK_GET_MY_MESSAGES)
                .addHeaders("Accept", "application/json")
                .addHeaders("Authorization", "Bearer " + Config.getSharedPreferenceString(getActivity(), Config.SHARED_PREF_KEY_USER_CREDENTIALS_USER_PASSWORD_ACCESS_TOKEN))
                .addBodyParameter("app_type", "ANDROID")
                .addBodyParameter("app_version_code", String.valueOf(Config.getAppVersionCode(getActivity().getApplicationContext())))
                .setTag("get_my_messages")
                .setPriority(Priority.HIGH)
                .build().getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        if (!getActivity().isFinishing() && getActivity().getApplicationContext() != null) {
                            //Log.e("SERVER-REQUEST", "response: " + response.toString());
                            try {
                                JSONObject main_response = new JSONObject(response);
                                String myStatus = main_response.getString("status");
                                final String myStatusMessage = main_response.getString("message");
                                JSONArray myMessagesArray = main_response.getJSONArray("data");
                                if (myMessagesArray.length() > 0) {
                                    MyMessagesListDataGenerator.getAllData().clear();

                                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                                        @Override
                                        public void run() {
                                            mRecyclerview.getAdapter().notifyDataSetChanged();
                                        }
                                    });
                                    for (int i = myMessagesArray.length()-1; i > -1; i--) {
                                        MessageModel thisMessage = new MessageModel();
                                        final JSONObject k = myMessagesArray.getJSONObject(i);
                                        thisMessage.setMessageId(k.getLong("message_id"));
                                        thisMessage.setMessageText(k.getString("message_text"));
                                        thisMessage.setSenderUserId(k.getLong("message_sender_user_id"));
                                        thisMessage.setReceiverUserId(k.getLong("message_receiver_id"));
                                        thisMessage.setMessageDate(k.getString("nice_date"));
                                        MyMessagesListDataGenerator.addOneData(thisMessage);
                                    }

                                    if (!getActivity().isFinishing() && getActivity().getApplicationContext() != null) {
                                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                                        @Override
                                        public void run() {
                                                mRecyclerview.getAdapter().notifyItemInserted(MyMessagesListDataGenerator.getAllData().size());

                                            if(firstCall){
                                                mLoadingContentLoadingProgressBar.setVisibility(View.INVISIBLE);
                                                mBackgroundImageImageView.setVisibility(View.INVISIBLE);
                                                mBackgroundTextTextView.setVisibility(View.INVISIBLE);
                                                mRecyclerview.setVisibility(View.VISIBLE);
                                                mMainParentSwipeRefreshLayout.setRefreshing(false);
                                            }
                                                mRecyclerview.scrollToPosition(MyMessagesListDataGenerator.getAllData().size()-1);
                                        }
                                    });
                                    }
                                } else {

                                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                                        @Override
                                        public void run() {

                                            if(firstCall) {
                                                mBackgroundImageImageView.setVisibility(View.VISIBLE);
                                                mBackgroundTextTextView.setVisibility(View.VISIBLE);
                                                mBackgroundTextTextView.setText("No Messages found");
                                                mLoadingContentLoadingProgressBar.setVisibility(View.INVISIBLE);
                                                mRecyclerview.setVisibility(View.VISIBLE);
                                                mMainParentSwipeRefreshLayout.setRefreshing(false);
                                                //Config.showToastType1(getActivity(), "No Messages found");
                                            }
                                        }
                                    });
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                new Handler(Looper.getMainLooper()).post(new Runnable() {
                                    @Override
                                    public void run() {
                                        Config.showToastType1(getActivity(), getString(R.string.an_unexpected_error_occured));
                                        if(firstCall) {
                                            mRecyclerview.setVisibility(View.INVISIBLE);
                                            mLoadingContentLoadingProgressBar.setVisibility(View.VISIBLE);
                                            mMainParentSwipeRefreshLayout.setRefreshing(false);
                                        }
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


    public void sendMessage(String message) {
        if (!getActivity().isFinishing() && getActivity().getApplicationContext() != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("MMM d");
            String currentDateandTime = sdf.format(new Date());
            MessageModel thisMessage = new MessageModel();
            //thisMessage.setMessageId(k.getLong(""));
            thisMessage.setMessageText(message);
            thisMessage.setSenderUserId(Integer.valueOf(Config.getSharedPreferenceString(getActivity(),Config.SHARED_PREF_KEY_USER_CREDENTIALS_USER_ID_SHORT)));
            thisMessage.setReceiverUserId(1);
            thisMessage.setMessageDate(currentDateandTime);
            MyMessagesListDataGenerator.addOneData(thisMessage);
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    mRecyclerview.getAdapter().notifyItemInserted(MyMessagesListDataGenerator.getAllData().size()-1);
                    mRecyclerview.scrollToPosition(MyMessagesListDataGenerator.getAllData().size()-1);
                }
            });
        }

        AndroidNetworking.post(Config.LINK_SEND_MESSAGE)
                .addHeaders("Accept", "application/json")
                .addHeaders("Authorization", "Bearer " + Config.getSharedPreferenceString(getActivity(), Config.SHARED_PREF_KEY_USER_CREDENTIALS_USER_PASSWORD_ACCESS_TOKEN))
                .addBodyParameter("message", message)
                .addBodyParameter("app_type", "ANDROID")
                .addBodyParameter("app_version_code", String.valueOf(Config.getAppVersionCode(getActivity().getApplicationContext())))
                .setTag("get_my_messages")
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

                                if(!myStatus.trim().equalsIgnoreCase("success")){
                                    MyMessagesListDataGenerator.getAllData().remove(MyMessagesListDataGenerator.getAllData().size()-1);
                                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                                        @Override
                                        public void run() {
                                            Config.showToastType1(getActivity(), myStatusMessage);
                                            mRecyclerview.getAdapter().notifyItemRemoved(MyMessagesListDataGenerator.getAllData().size());
                                        }
                                    });
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                                new Handler(Looper.getMainLooper()).post(new Runnable() {
                                    @Override
                                    public void run() {
                                        Log.e("SERVER-REQUEST", getString(R.string.an_unexpected_error_occured));
                                    }
                                });
                            }
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("SERVER-REQUEST", "anError: " + anError.getErrorBody());
                            Log.e("SERVER-REQUEST", "anError: " + anError.getErrorDetail());
                        if (!getActivity().isFinishing() && getActivity().getApplicationContext() != null) {
                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                @Override
                                public void run() {
                                    Config.showToastType1(getActivity(), getResources().getString(R.string.check_your_internet_connection_and_try_again));
                                }
                            });
                        }
                    }
                });

    }
}