package com.hgosot.skopje.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hgosot.skopje.R;


import com.hgosot.skopje.data.Tour;
import com.hgosot.skopje.utils.FileUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

public class TourListAdapter extends RecyclerView.Adapter<TourListAdapter.ViewHolder> {
    private List<Tour> mDataset;
    private OnItemClickListener onItemClickListener;
    private Context context;


    public TourListAdapter(List<Tour> myDataset, Context context) {
        this.context = context;
        mDataset = myDataset;
    }

    public void setOnAdapterClickListener(OnItemClickListener listener) {
        onItemClickListener = listener;

    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView tvTitle;
        public TextView tvSubTitle;
        public ImageView ivTourImage;

        public ViewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_tour_title);
            tvSubTitle = itemView.findViewById(R.id.tv_tour_subtitle);
            ivTourImage = itemView.findViewById(R.id.iv_tour_image);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {

            onItemClickListener.onToursListClicked(mDataset.get(getAdapterPosition()));

        }
    }


    // Create new views (invoked by the layout manager)
    @Override
    public TourListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                         int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.tour_list_item, parent, false);

        ViewHolder vh = new ViewHolder(itemView);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        Tour tour = mDataset.get(position);
        holder.tvTitle.setText(tour.getName().toUpperCase());
        holder.tvSubTitle.setText(tour.getTitle());

        String imageUrl = mDataset.get(position).getImage();

        if (!TextUtils.isEmpty(mDataset.get(position).getTitle())) {
            String path = FileUtils.getPlaceImage("o_" + mDataset.get(position).getId()+".jpg");
//            Picasso.get().load(path).fit().into(holder.ivTourImage);
            Glide.with(holder.itemView.getContext()).load(path).centerCrop().into(holder.ivTourImage);
        } else {
            String path = FileUtils.getPlaceImage(mDataset.get(position).getId()+".jpg");
//            Picasso.get().load(path).fit().into(holder.ivTourImage);
            Glide.with(holder.itemView.getContext()).load(path).centerCrop().into(holder.ivTourImage);
        }
//        if (!TextUtils.isEmpty(imageUrl)) {
//            if (URLUtil.isHttpUrl(imageUrl) || URLUtil.isHttpsUrl(imageUrl)) {
//                Picasso.get().load(imageUrl).fit().into(holder.ivTourImage);
//            } else {
//                // get image from drawable folder by name
//               // int id = context.getResources().getIdentifier(imageUrl, "drawable", context.getPackageName());
//
//                String path = FileUtils.getPlaceImage(imageUrl);
//                Picasso.get().load(path).fit().into(holder.ivTourImage);
//            }
//        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public interface OnItemClickListener {
        void onToursListClicked(Tour tour);
    }
}

