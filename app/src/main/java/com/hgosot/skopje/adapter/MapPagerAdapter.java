package com.hgosot.skopje.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hgosot.skopje.R;
import com.hgosot.skopje.data.Place;
import com.hgosot.skopje.utils.FileUtils;
import com.hgosot.skopje.utils.RoundedCornersTransformation;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.List;

public class MapPagerAdapter extends PagerAdapter {

    private List<Place> contentList;
    Context mContext;
    private OnPagerItemClickListener onItemClickListener;
    private String tourID;

    public MapPagerAdapter(Context context, List<Place> places, String tourId) {
        tourID = tourId;
        this.contentList = places;
        mContext = context;
    }

    public void setOnItemClickListener(OnPagerItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }


    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return contentList.size();
    }

    @Override
    public Object instantiateItem(ViewGroup view, final int position) {
        LayoutInflater inflater = LayoutInflater.from(mContext);

        ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.map_pager_item, view, false);
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onItemClickListener.onPageClicked(position);


            }
        });
        ImageView ivPlace = layout.findViewById(R.id.iv_place);
        TextView tvTitle = layout.findViewById(R.id.tv_title);
        TextView tvContent = layout.findViewById(R.id.tv_content);

        tvTitle.setText(contentList.get(position).getName());
        tvContent.setText(contentList.get(position).getInfoText());

//        int id = mContext.getResources().getIdentifier(contentList.get(position).getImage()
//                , "drawable", mContext.getPackageName());
        final int radius = 40;
        final int margin = 5;
        final Transformation transformation = new RoundedCornersTransformation(radius, margin);

        String imagePath = FileUtils.getPlaceImage(tourID,String.valueOf(contentList.get(position).getId()));

        Picasso.get().load(imagePath).transform(transformation).fit().into(ivPlace);


        view.addView(layout);
        return layout;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }


    public interface OnPagerItemClickListener {

        void onPageClicked(int pos);

    }
}
