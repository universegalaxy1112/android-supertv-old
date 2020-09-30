package com.livetv.normal.view.custom_views;

import android.content.Context;
import android.util.AttributeSet;

public class CustomChannelIcon extends com.github.siyamed.shapeimageview.CircularImageView{
    public CustomChannelIcon(Context context) {
        super(context);
    }

    public CustomChannelIcon(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomChannelIcon(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(heightMeasureSpec, heightMeasureSpec);

//        heightMeasureSpec = MeasureSpec.makeMeasureSpec(heightMeasureSpec, MeasureSpec.EXACTLY);
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
