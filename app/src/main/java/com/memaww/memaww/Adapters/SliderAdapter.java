package com.memaww.memaww.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.PagerAdapter;

import com.memaww.memaww.R;
import com.memaww.memaww.Util.Config;


/**
 * Created by zatana on 10/30/18.
 */

public class SliderAdapter extends PagerAdapter {

    Context context;
    LayoutInflater layoutInflater;

    public SliderAdapter(Context context){
        this.context = context;
    }


    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public boolean isViewFromObject(View view, Object o) {
        return view == o;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view;
        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        if(position == 0){
            view = layoutInflater.inflate(R.layout.slider_layout_info1, container, false);
        } else {
            view = layoutInflater.inflate(R.layout.slider_layout_info2, container, false);
        }

        container.addView(view);
        return view;
    }



    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        Log.e("memoryManage", "destroyItem STARTED SLIDER-ADAPTER");
        Config.freeMemory();
        container.removeView((ConstraintLayout) object);
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return super.getItemPosition(object);
    }
}
