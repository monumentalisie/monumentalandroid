package com.hgosot.skopje;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;

public class PageIndicatorLayout extends LinearLayout {

    public static final int MODE_SIMPLE = 0;
    public static final int MODE_ADVANCED = 1;

    private int mode;

    public PageIndicatorLayout(Context context) {
        this(context, null);
    }

    public PageIndicatorLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PageIndicatorLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.PageIndicatorLayout,
                0, 0
        );

        try {
            mode = a.getInteger(R.styleable.PageIndicatorLayout_pageIndicatorMode, MODE_SIMPLE);
        } finally {
            // release the TypedArray so that it can be reused.
            a.recycle();
        }

    }

    int currentPosition = -1;

    public void init(int totalItems) {
        if (currentPosition > -1 || totalItems <= 1) {
            return;
        }
        if (mode == MODE_ADVANCED) {
            /// Converts 16 dip into its equivalent px
            int pageIndicatorWidthPx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16, getResources().getDisplayMetrics());
            for (int i = 0; i < totalItems; i++) {
                View pageIndicator = new View(getContext());
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(pageIndicatorWidthPx, pageIndicatorWidthPx);
                pageIndicator.setLayoutParams(layoutParams);
                if (i == 0) {
                    pageIndicator.setBackgroundResource(R.drawable.page_indicator_current);
                } else if (i == 1) {
                    pageIndicator.setBackgroundResource(R.drawable.page_indicator_next);
                } else {
                    pageIndicator.setBackgroundResource(R.drawable.page_indicator_other);
                }
                addView(pageIndicator);
            }
        } else {
            /// Converts 8 dip into its equivalent px
            int pageIndicatorWidthPx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics());
            for (int i = 0; i < totalItems; i++) {
                View pageIndicator = new View(getContext());
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(pageIndicatorWidthPx, pageIndicatorWidthPx);
                layoutParams.setMargins(pageIndicatorWidthPx / 2, pageIndicatorWidthPx / 2, pageIndicatorWidthPx / 2, pageIndicatorWidthPx / 2);
                pageIndicator.setLayoutParams(layoutParams);
                if (i == 0) {
                    pageIndicator.setBackgroundResource(R.drawable.page_indicator_current_simple);
                } else {
                    pageIndicator.setBackgroundResource(R.drawable.page_indicator_other_simple);
                }
                addView(pageIndicator);
            }
        }
        currentPosition = 0;
    }

    public synchronized void updatePageIndicator(int position) {
        if (position == currentPosition) {
            return;
        }
        if (mode == MODE_ADVANCED) {
            for (int i = 0; i < getChildCount(); i++) {
                View pageIndicator = getChildAt(i);
                if (i == position) {
                    pageIndicator.setBackgroundResource(R.drawable.page_indicator_current);
                } else if (i == position - 1 || i == position + 1) {
                    pageIndicator.setBackgroundResource(R.drawable.page_indicator_next);
                } else {
                    pageIndicator.setBackgroundResource(R.drawable.page_indicator_other);
                }
            }
        } else {
            for (int i = 0; i < getChildCount(); i++) {
                View pageIndicator = getChildAt(i);
                if (i == position) {
                    pageIndicator.setBackgroundResource(R.drawable.page_indicator_current_simple);
                } else {
                    pageIndicator.setBackgroundResource(R.drawable.page_indicator_other_simple);
                }
            }
        }
        currentPosition = position;
    }

    public void setMode(int mode) {
        this.mode = mode;
        invalidate();
    }
}
