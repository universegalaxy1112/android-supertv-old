package com.livetv.normal.view.custom_views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.livetv.normal.utils.Screen;

public class LiveTVItemLinearLayout extends LinearLayout {
    public LiveTVItemLinearLayout(Context context) {
        super(context);
    }

    public LiveTVItemLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LiveTVItemLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

//
    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);

        int screenWidth = Screen.GetScreenWidth();
        int screenHeight = Screen.GetScreenHeight();

        int newHeightMeasureSpec = (int)(screenWidth / 4);//default for portrait
        if(Screen.getOrientation() == Screen.Orientation.LANDSCAPE) {
            newHeightMeasureSpec = (int)(screenWidth / 10);
        }
        int newWidthMeasureSpec = widthMeasureSpec;

        newHeightMeasureSpec = MeasureSpec.makeMeasureSpec(newHeightMeasureSpec, MeasureSpec.EXACTLY);
        newWidthMeasureSpec = MeasureSpec.makeMeasureSpec(newWidthMeasureSpec, MeasureSpec.EXACTLY);
        super.onMeasure(newWidthMeasureSpec, newHeightMeasureSpec);
    }
}
