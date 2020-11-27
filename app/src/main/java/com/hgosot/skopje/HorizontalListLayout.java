package com.hgosot.skopje;

import android.content.Context;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.hgosot.skopje.adapter.BaseListAdapter;

import java.util.List;

public abstract class HorizontalListLayout extends FrameLayout {

    protected BaseListAdapter adapter;

    protected PageIndicatorLayout pageIndicatorLayout;
    protected RecyclerView recyclerView;
    private TextView titleView;
    private ImageView iconView;
    private TextView actionView;

    public HorizontalListLayout(Context context) {
        this(context, null);
    }

    public HorizontalListLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HorizontalListLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void init(Context context) {
        LayoutInflater.from(context).inflate(getLayoutId(), this);

        titleView = findViewById(R.id.title);
        iconView = findViewById(R.id.image);
        actionView = findViewById(R.id.action_view);

        recyclerView = findViewById(R.id.list);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        adapter = getAdapter();
        recyclerView.setAdapter(adapter);
        pageIndicatorLayout = findViewById(R.id.page_indicator_layout);
        SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (pageIndicatorLayout != null) {
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        int position = layoutManager.findFirstCompletelyVisibleItemPosition();
                        if (position != -1) {
                            pageIndicatorLayout.updatePageIndicator(position);
                        }
                    }
                }
            }
        });
    }

    @Deprecated
    public void setData(String title, List objects) {
        if (!TextUtils.isEmpty(title)) {
            titleView.setText(title);
            titleView.setVisibility(VISIBLE);
        } else if (titleView != null) {
            titleView.setVisibility(GONE);
        }

        if (pageIndicatorLayout != null) {
            pageIndicatorLayout.init(objects.size());
        }

        adapter.clear();
        adapter.addAll(objects);
        adapter.notifyDataSetChanged();
/*
            if (pageIndicatorLayout.getCurrentPosition() > 0) {
                list.scrollToPosition(pageIndicatorLayout.getCurrentPosition());
            }
*/
    }

    public void setTitle(CharSequence title) {
        setTitle(title, -0);
    }

    public void setTitle(CharSequence title, int color) {
        if (!TextUtils.isEmpty(title)) {
            titleView.setText(title);
            titleView.setVisibility(VISIBLE);
        } else if (titleView != null) {
            titleView.setVisibility(GONE);
        }
        if (color != 0) {
            titleView.setTextColor(getResources().getColor(color));
        }
    }

    public void setData(List objects) {
        Log.d("HLL", "HorizontalListLayout: setData ");
        if (pageIndicatorLayout != null) {
            pageIndicatorLayout.init(objects.size());
        }

        adapter.clear();
        adapter.addAll(objects);
        adapter.notifyDataSetChanged();
/*
            if (pageIndicatorLayout.getCurrentPosition() > 0) {
                list.scrollToPosition(pageIndicatorLayout.getCurrentPosition());
            }
*/
    }

    public void setImage(@DrawableRes int image) {
        iconView.setVisibility(VISIBLE);
        iconView.setImageResource(image);
    }

    public void setImageColor(@ColorRes int color) {
        DrawableCompat.setTint(iconView.getDrawable(), ContextCompat.getColor(getContext(), color));
    }

    public void setActionText(@StringRes int text) {
        actionView.setVisibility(VISIBLE);
        actionView.setText(text);
    }

    public void setActionListener(View.OnClickListener listener) {
        actionView.setVisibility(VISIBLE);
        actionView.setOnClickListener(listener);
    }

    public void setIsPageIndicator(boolean isPageIndicator) {
        pageIndicatorLayout.setVisibility(isPageIndicator ? VISIBLE : GONE);
    }

    protected int getLayoutId() {
        return R.layout.layout_horizontal_list;
    }

    abstract protected BaseListAdapter getAdapter();

    public Object getItem(int index) {
        return adapter.get(index);
    }

    public void setItemListener(BaseListAdapter.ItemListener itemListener) {
        adapter.setItemListener(itemListener);
    }

    public void setAdapter(BaseListAdapter adapter) {
        this.adapter = adapter;
    }
}
