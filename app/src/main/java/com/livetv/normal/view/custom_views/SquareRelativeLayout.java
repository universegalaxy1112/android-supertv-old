package com.livetv.normal.view.custom_views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.livetv.normal.utils.Screen;

public class SquareRelativeLayout extends RelativeLayout {
    public SquareRelativeLayout(Context context) {
        super(context);
    }

    public SquareRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SquareRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
//
//        widthMeasureSpec = MeasureSpec.makeMeasureSpec(widthMeasureSpec, MeasureSpec.EXACTLY);
//        super.onMeasure(widthMeasureSpec, widthMeasureSpec);

        super.onMeasure(widthMeasureSpec, widthMeasureSpec);

        int screenWidth = Screen.GetScreenWidth();
        int screenHeight = Screen.GetScreenHeight();

        int newWidthMeasureSpec = (int)(screenWidth / 2.2);//default for portrait
        if(Screen.getOrientation() == Screen.Orientation.LANDSCAPE) {
            newWidthMeasureSpec = (int)(screenWidth / 4);
        }
//        int newWidthMeasureSpec = screenWidth;

        newWidthMeasureSpec = MeasureSpec.makeMeasureSpec(newWidthMeasureSpec, MeasureSpec.EXACTLY);
        super.onMeasure(newWidthMeasureSpec, newWidthMeasureSpec);

    }
}
