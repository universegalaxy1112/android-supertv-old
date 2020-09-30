package com.livetv.normal.view.custom_views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.livetv.normal.utils.Screen;

public class CategoryItemLinearLayout extends LinearLayout {
    public CategoryItemLinearLayout(Context context) {
        super(context);
    }

    public CategoryItemLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CategoryItemLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

//
    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);

        int screenWidth = Screen.GetScreenWidth();
        int screenHeight = Screen.GetScreenHeight();

        int newHeightMeasureSpec = (int)(screenHeight / 3);//default for portrait
        if(Screen.getOrientation() == Screen.Orientation.LANDSCAPE) {
            newHeightMeasureSpec = (int)(screenHeight / 2.5);
        }
        int newWidthMeasureSpec = screenWidth;

        newHeightMeasureSpec = MeasureSpec.makeMeasureSpec(newHeightMeasureSpec, MeasureSpec.EXACTLY);
        newWidthMeasureSpec = MeasureSpec.makeMeasureSpec(newWidthMeasureSpec, MeasureSpec.EXACTLY);
        super.onMeasure(newWidthMeasureSpec, newHeightMeasureSpec);
    }
}
