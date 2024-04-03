package com.memaww.memaww.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.memaww.memaww.Fragments.InviteFragment;
import com.memaww.memaww.Fragments.MyOrdersFragment;
import com.memaww.memaww.Fragments.ProfileFragment;
import com.memaww.memaww.Fragments.StartOrderFragment;
import com.memaww.memaww.Fragments.SupportFragment;
import com.memaww.memaww.R;
import com.memaww.memaww.Util.Config;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, NavigationBarView.OnItemSelectedListener {

    private MyPageAdapter pageAdapter;
    private BottomNavigationView mMainMenuBottomNavigationView;
    private ViewPager mFragmentsHolderViewPager;
    private ImageView mInfoIconImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMainMenuBottomNavigationView = findViewById(R.id.activity_main_bottomnavigationview);
        mFragmentsHolderViewPager = findViewById(R.id.activity_main_fragmentsholder_viewpager);
        mInfoIconImageView = findViewById(R.id.activity_main_constraintlayout2_infoicon_imageview);

        // SETTING LISTENERS
        mMainMenuBottomNavigationView.setOnItemSelectedListener(this);
        mFragmentsHolderViewPager.addOnPageChangeListener(viewListener);
        mInfoIconImageView.setOnClickListener(this);

        // TEMPORARY NEEDED ACTIONS
        mMainMenuBottomNavigationView.setSelectedItemId(R.id.start_order);
        List<Fragment> fragmentsList = getFragments();
        pageAdapter = new MyPageAdapter(getSupportFragmentManager(), fragmentsList);
        mFragmentsHolderViewPager.setAdapter(pageAdapter);
        mFragmentsHolderViewPager.setCurrentItem(2);

    }


    @Override
    public void onClick(View view) {
        if(view.getId() == mInfoIconImageView.getId()){
            Config.openActivity(MainActivity.this, ReaderWebViewActivity.class, 1, 0, 1, Config.WEBVIEW_KEY_URL, "https://memaww.com");
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


    private List<Fragment> getFragments(){
        List<Fragment> fList = new ArrayList<Fragment>();

        fList.add(MyOrdersFragment.newInstance("", ""));
        fList.add(InviteFragment.newInstance("", ""));
        fList.add(StartOrderFragment.newInstance("", ""));
        fList.add(SupportFragment.newInstance("", ""));
        fList.add(ProfileFragment.newInstance("", ""));
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