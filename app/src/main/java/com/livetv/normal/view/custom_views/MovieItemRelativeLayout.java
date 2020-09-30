package com.livetv.normal.view.custom_views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.livetv.normal.utils.Screen;

public class MovieItemRelativeLayout extends RelativeLayout {
    public MovieItemRelativeLayout(Context context) {
        super(context);
    }

    public MovieItemRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MovieItemRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

//    public MovieItemRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
//        super(context, attrs, defStyleAttr, defStyleRes);
//    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int screenWidth = Screen.GetScreenWidth();

        float visibleItems = 3.2f;//default for portrait //hack (3.2) to be able to fit 3 movies in portrait
        if(Screen.getOrientation() == Screen.Orientation.LANDSCAPE) {
            visibleItems = 6;
        }

        int newWidthMeasureSpec = (int) (screenWidth / visibleItems);
        int newHeightMeasureSpec = (int)(newWidthMeasureSpec * 1.5);

        newHeightMeasureSpec = MeasureSpec.makeMeasureSpec(newHeightMeasureSpec, MeasureSpec.EXACTLY);
        newWidthMeasureSpec = MeasureSpec.makeMeasureSpec(newWidthMeasureSpec, MeasureSpec.EXACTLY);
        super.onMeasure(newWidthMeasureSpec, newHeightMeasureSpec);
//        super.onMeasure(newWidthMeasureSpec, heightMeasureSpec);
    }
}
