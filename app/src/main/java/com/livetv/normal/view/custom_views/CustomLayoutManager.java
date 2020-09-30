package com.livetv.normal.view.custom_views;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;


public class CustomLayoutManager extends LinearLayoutManager {
    private int mParentWidth;
    private int mItemWidth;

    public CustomLayoutManager(Context context, int orientation, boolean reverseLayout, int parentWidth, int itemWidth) {
        super(context);
        setOrientation(orientation);
        setReverseLayout(reverseLayout);
        setAutoMeasureEnabled(true);
        mParentWidth = parentWidth;
        mItemWidth = itemWidth;
    }


//    @Override
//    public int getPaddingLeft() {
//        return Math.round(mParentWidth / 2f - mItemWidth / 2f);
//    }
//
//    @Override
//    public int getPaddingRight() {
//        return getPaddingLeft();
//    }
}
