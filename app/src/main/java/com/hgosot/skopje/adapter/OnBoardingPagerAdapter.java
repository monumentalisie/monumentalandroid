package com.hgosot.skopje.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.hgosot.skopje.R;
import com.squareup.picasso.Picasso;

public  class OnBoardingPagerAdapter extends PagerAdapter {
    private Context context;
    int[] mResources = {
            R.drawable.map_big,
           R.drawable.map_big,
            R.drawable.map_big

    };

    public OnBoardingPagerAdapter(Context context ) {
        this.context = context;

    }

    @Override
    public int getCount() {
        return mResources.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        ImageView imageView = new ImageView(context);

        Picasso.get().load(mResources[position]).fit().into(imageView);
        container.addView(imageView);
        return imageView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

}