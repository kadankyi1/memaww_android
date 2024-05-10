package com.memaww.memaww.Fragments;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.memaww.memaww.ListDataGenerators.MyMessagesListDataGenerator;
import com.memaww.memaww.Models.MessageModel;
import com.memaww.memaww.R;
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

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public SupportFragment() {
        // Required empty public constructor
    }

    public static SupportFragment newInstance(String param1, String param2) {
        SupportFragment fragment = new SupportFragment();
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
        getMyOrders();

        mMainParentSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getMyOrders();
            }
        });

        mSendMessageConstraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!mMessageBoxEditText.getText().toString().trim().equalsIgnoreCase("")){
                    sendMessage(mMessageBoxEditText.getText().toString().trim());
                    mMessageBoxEditText.getText().clear();
                }
            }
        });



        return view;
    }



    private void allOnClickHandlers(View v, int position){
        /*
        if(v.getId() == R.id.article_parent
                || v.getId() == R.id.fragment_today_heraldofglorytitle_textview
                || v.getId() == R.id.fragment_today_heraldofglorybody_textview
                || v.getId() == R.id.tag_holder
                || v.getId() == R.id.fragment_today_heraldofgloryimage_roundedcornerimageview
                || v.getId() == R.id.fragment_today_heraldofglorylabel_textview){
            setSharedPreferenceString(getActivity().getApplicationContext(), SHARED_PREF_KEY_ARTICLE_ID, String.valueOf(ArticleListDataGenerator.getAllData().get(position).getArticle_id()));
            setSharedPreferenceString(getActivity().getApplicationContext(), SHARED_PREF_KEY_IMAGE_ARTICLE_IMG_URL, String.valueOf(ArticleListDataGenerator.getAllData().get(position).getArticle_image()));
            setSharedPreferenceString(getActivity().getApplicationContext(), SHARED_PREF_KEY_IMAGE_ARTICLE_TAG_TEXT, ArticleListDataGenerator.getAllData().get(position).getArticle_type());
            setSharedPreferenceString(getActivity().getApplicationContext(), SHARED_PREF_KEY_IMAGE_ARTICLE_UPLOAD_TIME, ArticleListDataGenerator.getAllData().get(position).getCreated_at());
            setSharedPreferenceString(getActivity().getApplicationContext(), SHARED_PREF_KEY_IMAGE_ARTICLE_ARTICLE_TITLE, ArticleListDataGenerator.getAllData().get(position).getArticle_title());
            setSharedPreferenceString(getActivity().getApplicationContext(), SHARED_PREF_KEY_IMAGE_ARTICLE_ARTICLE_BODY, ArticleListDataGenerator.getAllData().get(position).getArticle_body());
            Intent intent = new Intent(getActivity().getApplicationContext(), ImageArticleActivity.class);
            startActivity(intent);
        }
         */
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
                            Log.e("SERVER-REQUEST", "response: " + response.toString());
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
                                    for (int i = 0; i < myMessagesArray.length(); i++) {
                                        MessageModel thisMessage = new MessageModel();
                                        final JSONObject k = myMessagesArray.getJSONObject(i);
                                        thisMessage.setMessageId(k.getLong("message_id"));
                                        thisMessage.setMessageText(k.getString("message_text"));
                                        thisMessage.setSenderUserId(k.getLong("message_sender_user_id"));
                                        thisMessage.setReceiverUserId(k.getLong("message_receiver_id"));
                                        thisMessage.setMessageDate(k.getString("nice_date"));
                                        MyMessagesListDataGenerator.addOneData(thisMessage);
                                    }

                                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                                        @Override
                                        public void run() {
                                            if (!getActivity().isFinishing()) {
                                                mRecyclerview.getAdapter().notifyItemInserted(MyMessagesListDataGenerator.getAllData().size());
                                                mLoadingContentLoadingProgressBar.setVisibility(View.INVISIBLE);
                                                mBackgroundImageImageView.setVisibility(View.INVISIBLE);
                                                mBackgroundTextTextView.setVisibility(View.INVISIBLE);
                                                mRecyclerview.setVisibility(View.VISIBLE);
                                                mRecyclerview.scrollToPosition(MyMessagesListDataGenerator.getAllData().size()-1);
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
                                            mBackgroundTextTextView.setText("No Messages found");
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


    public void sendMessage(String message) {
        if (!getActivity().isFinishing() && getActivity().getApplicationContext() != null) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    SimpleDateFormat sdf = new SimpleDateFormat("MMM d");
                    String currentDateandTime = sdf.format(new Date());

                    MessageModel thisMessage = new MessageModel();
                    //thisMessage.setMessageId(k.getLong(""));
                    thisMessage.setMessageText(message);
                    thisMessage.setSenderUserId(Integer.valueOf(Config.getSharedPreferenceString(getActivity(),Config.SHARED_PREF_KEY_USER_CREDENTIALS_USER_ID_SHORT)));
                    thisMessage.setReceiverUserId(1);
                    thisMessage.setMessageDate(currentDateandTime);
                    MyMessagesListDataGenerator.addOneData(thisMessage);
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