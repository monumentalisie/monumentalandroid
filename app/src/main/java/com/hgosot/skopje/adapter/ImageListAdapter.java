package com.hgosot.skopje.adapter;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.hgosot.skopje.R;
import com.squareup.picasso.Picasso;


public class ImageListAdapter extends BaseListAdapter {


    private static final int ITEM_IMAGE = 1;
    private static final int ITEM_IMAGE_PROFILE = 2;

    public ImageListAdapter() {
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View v1 = inflater.inflate(R.layout.item_image, parent, false);
        viewHolder = new ImageViewHolder(v1);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, final int position) {
        ImageViewHolder imageViewHolder = (ImageViewHolder) viewHolder;
        imageViewHolder.bind(position);
    }

    @Override
    public int getItemViewType(int position) {

        return ITEM_IMAGE;
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {

        public final View mainView;
        @Nullable
        public final ImageView imageView;

        public ImageViewHolder(View view) {
            super(view);
            mainView = view;
            imageView = view.findViewById(R.id.image);
            //todo should be changed as we'll move to some other type of the listener
            if (itemListener != null) {
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        itemListener.onClick(ImageViewHolder.this.getAdapterPosition());
                    }
                });
            }
        }

        public void bind(int position) {
            String image = (String) items.get(position);
//            Picasso.get().load(image).fit().into(imageView);
            Glide.with(itemView.getContext()).load(image).centerCrop().into(imageView);
        }
    }
}
