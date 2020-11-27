package com.hgosot.skopje;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;

import com.hgosot.skopje.adapter.BaseListAdapter;
import com.hgosot.skopje.adapter.ImageListAdapter;

public class ImageHorizontalListLayout extends HorizontalListLayout {

    public ImageHorizontalListLayout(Context context) {
        this(context, null);
    }

    public ImageHorizontalListLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ImageHorizontalListLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected BaseListAdapter getAdapter() {
        return new ImageListAdapter();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_image_horizontal_list;
    }

    public void setSize(int width, int height) {
        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) recyclerView.getLayoutParams();
        params.height = height;
        params.width = width;
    }
}
