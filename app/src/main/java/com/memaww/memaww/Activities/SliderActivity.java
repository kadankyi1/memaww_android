package com.memaww.memaww.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.memaww.memaww.Adapters.SliderAdapter;
import com.memaww.memaww.R;
import com.memaww.memaww.Util.Config;
import com.memaww.memaww.Util.LocaleHelper;

public class SliderActivity extends AppCompatActivity implements View.OnClickListener {

    private ViewPager mSlideViewPager;
    private LinearLayout mDotlayout;
    private SliderAdapter sliderAdapter;
    private TextView[] mDots;
    private int mCurrenPage;
    private Button mNextBtn, mSkipBtn;
    private Button mBackBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slider);

        mSlideViewPager = findViewById(R.id.slider_activity_view_pager);
        mDotlayout = findViewById(R.id.slider_activity_linear_layout);
        mSkipBtn = findViewById(R.id.slider_activity_skip_button);
        mBackBtn = findViewById(R.id.slider_activity_back_button);
        mNextBtn = findViewById(R.id.slider_activity_next_button);

        sliderAdapter = new SliderAdapter(this);
        mSlideViewPager.setAdapter(sliderAdapter);
        addDotsIndicator(0);
        mSlideViewPager.addOnPageChangeListener(viewListener);
        mSlideViewPager.setOffscreenPageLimit(0);

        mSkipBtn.setOnClickListener(this);
        mBackBtn.setOnClickListener(this);
        mNextBtn.setOnClickListener(this);
    }



    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.slider_activity_next_button){
            if(mSlideViewPager.getCurrentItem() == 2){
                Config.openActivity(SliderActivity.this, LoginActivity.class, 1, 0, 0, "", "");
            } else {
                mSlideViewPager.setCurrentItem(mCurrenPage+1);
            }
        } else if(view.getId() == R.id.slider_activity_back_button){
            mSlideViewPager.setCurrentItem(mCurrenPage-1);
        } else if(view.getId() == R.id.slider_activity_skip_button){
            Config.openActivity(SliderActivity.this, LoginActivity.class, 1, 0, 0, "", "");
        }

    }

    public void addDotsIndicator(int position){

        mDotlayout.removeAllViews();
        mDots = new TextView[3];
        for (int i = 0; i < 3; i++ ){
            mDots[i] = new TextView (this);
            mDots[i].setText(Html.fromHtml("&#8226;"));
            mDots[i].setTextSize(25);
            mDots[i].setTextColor(getResources().getColor(R.color.color_dot_inactive));

            mDotlayout.addView(mDots[i]);
        }

        if(mDots.length > 0){
            mDots[position].setTextColor(getResources().getColor(R.color.white));
        }

    }

    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener(){
        @Override
        public void onPageScrolled(int i, float v, int i1) {}

        @Override
        public void onPageSelected(int i) {
            addDotsIndicator(i);
            mCurrenPage = i;
            if(i == 0){
                mNextBtn.setEnabled(true);
                mBackBtn.setEnabled(false);
                mBackBtn.setVisibility(View.GONE);
                mSkipBtn.setVisibility(View.VISIBLE);
                mNextBtn.setText("Next");
                mBackBtn.setText("");
            } else if(i == 1){
                mNextBtn.setEnabled(true);
                mBackBtn.setEnabled(true);
                mSkipBtn.setVisibility(View.GONE);
                mBackBtn.setVisibility(View.VISIBLE);
                mNextBtn.setText("Next");
                mBackBtn.setText("Back");

            } else if(i == 2){
                mNextBtn.setEnabled(true);
                mBackBtn.setEnabled(true);
                mSkipBtn.setVisibility(View.GONE);
                mBackBtn.setVisibility(View.VISIBLE);
                mNextBtn.setText("Sign-In");
                mBackBtn.setText("Back");

            }
        }
        @Override
        public void onPageScrollStateChanged(int state) {}
    };

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase));
    }

    @Override
    protected void onResume() {
        super.onResume();

        mSlideViewPager = findViewById(R.id.slider_activity_view_pager);
        mDotlayout = findViewById(R.id.slider_activity_linear_layout);
        mBackBtn = findViewById(R.id.slider_activity_back_button);
        mNextBtn = findViewById(R.id.slider_activity_next_button);
        mDots = new TextView[2];
        sliderAdapter = new SliderAdapter(this);
        ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener(){
            @Override
            public void onPageScrolled(int i, float v, int i1) {}

            @Override
            public void onPageSelected(int i) {
                addDotsIndicator(i);
                mCurrenPage = i;
                if(i == 0){
                    mNextBtn.setEnabled(true);
                    mBackBtn.setEnabled(false);
                    mBackBtn.setVisibility(View.GONE);
                    mSkipBtn.setVisibility(View.VISIBLE);
                    mNextBtn.setText("Next");
                    mBackBtn.setText("");
                }  else if(i == 1){
                    mNextBtn.setEnabled(true);
                    mBackBtn.setEnabled(true);
                    mSkipBtn.setVisibility(View.GONE);
                    mBackBtn.setVisibility(View.VISIBLE);
                    mNextBtn.setText("Next");
                    mBackBtn.setText("Back");

                } else if(i == 2){
                    mNextBtn.setEnabled(true);
                    mBackBtn.setEnabled(true);
                    mSkipBtn.setVisibility(View.GONE);
                    mBackBtn.setVisibility(View.VISIBLE);
                    mNextBtn.setText("Finish");
                    mBackBtn.setText("Back");

                }
            }
            @Override
            public void onPageScrollStateChanged(int state) {}
        };
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e("memoryManage", "onStop STARTED SLIDER-ACTIVITY");
        mSlideViewPager = null;
        mDotlayout = null;
        sliderAdapter = null;
        mDots = null;
        mNextBtn = null;
        mBackBtn = null;
        viewListener = null;
        Config.freeMemory();
    }

    @Override
    public void finish() {
        super.finish();
        Log.e("memoryManage", "finish STARTED SLIDER-ACTIVITY");
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        Config.freeMemory();
    }


}