package com.memaww.memaww.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.memaww.memaww.Fragments.InviteFragment;
import com.memaww.memaww.Fragments.MyOrdersFragment;
import com.memaww.memaww.Fragments.ProfileFragment;
import com.memaww.memaww.Fragments.PlaceOrderFragment;
import com.memaww.memaww.Fragments.SupportFragment;
import com.memaww.memaww.R;
import com.memaww.memaww.Services.BackgroundService;
import com.memaww.memaww.Util.Config;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, NavigationBarView.OnItemSelectedListener {

    private MyPageAdapter pageAdapter;
    private BottomNavigationView mMainMenuBottomNavigationView;
    private ViewPager mFragmentsHolderViewPager;
    private ImageView mInfoIconImageView;
    private ConstraintLayout mNewNotificationIconHolderConstraintLayout, mNotificationIconHolderConstraintLayout;
    private Thread generalBackgroundThread = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mMainMenuBottomNavigationView = findViewById(R.id.activity_main_bottomnavigationview);
        mFragmentsHolderViewPager = findViewById(R.id.activity_main_fragmentsholder_viewpager);
        mInfoIconImageView = findViewById(R.id.activity_main_constraintlayout2_infoicon_imageview);
        mNewNotificationIconHolderConstraintLayout = findViewById(R.id.activity_main_constraintlayout_notificationiconholder_constraintlayout);
        mNotificationIconHolderConstraintLayout = findViewById(R.id.activity_main_constraintlayout2_profileicon_holder_constraintlayout);

        // SETTING LISTENERS
        mMainMenuBottomNavigationView.setOnItemSelectedListener(this);
        mFragmentsHolderViewPager.addOnPageChangeListener(viewListener);
        mInfoIconImageView.setOnClickListener(this);
        mNotificationIconHolderConstraintLayout.setOnClickListener(this);

        Log.e("FIREBASE-MSG", "SHARED_PREF_KEY_USER_CREDENTIALS_USER_HAS_NEW_NOTIFICATION: " + String.valueOf(Config.getSharedPreferenceBoolean(getApplicationContext(), Config.SHARED_PREF_KEY_USER_CREDENTIALS_USER_HAS_NEW_NOTIFICATION)));
        if(Config.getSharedPreferenceBoolean(getApplicationContext(), Config.SHARED_PREF_KEY_USER_CREDENTIALS_USER_HAS_NEW_NOTIFICATION)){
            mNewNotificationIconHolderConstraintLayout.setVisibility(View.VISIBLE);
        } else {
            mNewNotificationIconHolderConstraintLayout.setVisibility(View.GONE);
        }

        // TEMPORARY NEEDED ACTIONS
        mMainMenuBottomNavigationView.setSelectedItemId(R.id.start_order);
        List<Fragment> fragmentsList = getFragments();
        pageAdapter = new MyPageAdapter(getSupportFragmentManager(), fragmentsList);
        mFragmentsHolderViewPager.setAdapter(pageAdapter);
        mFragmentsHolderViewPager.setCurrentItem(2);


        generalBackgroundThread = new Thread(new Runnable() {
            @Override
            public void run() {
                BackgroundService.updateUserInfo(getApplicationContext(), Config.getSharedPreferenceString(getApplicationContext(), Config.SHARED_PREF_KEY_USER_CREDENTIALS_USER_FCM_TOKEN));
            }
        });
        generalBackgroundThread.start();


        Log.e("VERSION-CODE", "SHARED_PREF_KEY_USER_CREDENTIALS_USER_APP_MINIMUM_VERSION_CODE: " + Config.getSharedPreferenceString(getApplicationContext(), Config.SHARED_PREF_KEY_USER_CREDENTIALS_USER_APP_MINIMUM_VERSION_CODE));
        Log.e("VERSION-CODE", "SUCCESS: " + Config.getAppVersionCode(getApplicationContext()));
        if(!Config.getSharedPreferenceString(getApplicationContext(), Config.SHARED_PREF_KEY_USER_CREDENTIALS_USER_APP_MINIMUM_VERSION_CODE).trim().equalsIgnoreCase("")){
            if(Integer.valueOf(Config.getSharedPreferenceString(getApplicationContext(), Config.SHARED_PREF_KEY_USER_CREDENTIALS_USER_APP_MINIMUM_VERSION_CODE)) > Integer.valueOf(Config.getAppVersionCode(getApplicationContext()))){
                Config.openActivity(MainActivity.this, UpdateActivity.class, 1, 2, 0, "", "");
                return;
            }
        }
    }


    @Override
    public void onClick(View view) {
        if(view.getId() == mInfoIconImageView.getId()){
            view.startAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.main_activity_onclick_icon_anim));
            Config.openActivity(MainActivity.this, ReaderWebViewActivity.class, 1, 0, 1, Config.WEBVIEW_KEY_URL, "https://memaww.com");
        } else if(view.getId() == mNotificationIconHolderConstraintLayout.getId()){
            mNewNotificationIconHolderConstraintLayout.setVisibility(View.GONE);
            Config.openActivity(MainActivity.this, NotificationsActivity.class, 0, 0, 0, "", "");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(Config.getSharedPreferenceBoolean(getApplicationContext(), Config.SHARED_PREF_KEY_USER_CREDENTIALS_USER_HAS_NEW_NOTIFICATION)){
            mNewNotificationIconHolderConstraintLayout.setVisibility(View.VISIBLE);
        } else {
            mNewNotificationIconHolderConstraintLayout.setVisibility(View.GONE);
        }
    }

    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener(){

        @Override
        public void onPageScrolled(int i, float v, int i1) {

        }

        @Override
        public void onPageSelected(int i) {
            if(i == 0){
                mMainMenuBottomNavigationView.setSelectedItemId(R.id.myorders);
            } else if(i == 1){
                mMainMenuBottomNavigationView.setSelectedItemId(R.id.invite);
            } else if(i == 2){
                mMainMenuBottomNavigationView.setSelectedItemId(R.id.start_order);
            } else if(i == 3){
                mMainMenuBottomNavigationView.setSelectedItemId(R.id.support);
            } else if(i == 4){
                mMainMenuBottomNavigationView.setSelectedItemId(R.id.profile);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    @Override
    public void onBackPressed() {

        if(mFragmentsHolderViewPager.getCurrentItem() != 2){
            mFragmentsHolderViewPager.setCurrentItem(2);
        }  else {
            super.onBackPressed();
        }
    }

    private List<Fragment> getFragments(){
        List<Fragment> fList = new ArrayList<Fragment>();

        fList.add(MyOrdersFragment.newInstance());
        fList.add(InviteFragment.newInstance());
        fList.add(PlaceOrderFragment.newInstance());
        fList.add(SupportFragment.newInstance());
        fList.add(ProfileFragment.newInstance());
        return fList;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.myorders:
                mFragmentsHolderViewPager.setCurrentItem(0);
                return true;

            case R.id.invite:
                mFragmentsHolderViewPager.setCurrentItem(1);
                return true;

            case R.id.start_order:
                mFragmentsHolderViewPager.setCurrentItem(2);
                return true;

            case R.id.support:
                mFragmentsHolderViewPager.setCurrentItem(3);
                return true;

            case R.id.profile:
                mFragmentsHolderViewPager.setCurrentItem(4);
                return true;
        }
        return false;
    }


    // THE FRAGMENT ADAPTER
    private class MyPageAdapter extends FragmentPagerAdapter {
        private List<Fragment> fragmentList;

        public MyPageAdapter(FragmentManager fragmentManager, List<Fragment> fragmentsList ) {
            super(fragmentManager);
            this.fragmentList = fragmentsList;
        }

        @Override
        public Fragment getItem(int position) {
            return this.fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return this.fragmentList.size();
        }
    }

}