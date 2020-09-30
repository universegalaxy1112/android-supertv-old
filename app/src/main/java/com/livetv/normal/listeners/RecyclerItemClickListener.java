package com.livetv.normal.listeners;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;

public class RecyclerItemClickListener implements RecyclerView.OnItemTouchListener {
    GestureDetector mGestureDetector;
    /* access modifiers changed from: private */
    public OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(View view, int i);

        void onLongItemClick(View view, int i);
    }

    public RecyclerItemClickListener(Context context, final RecyclerView recyclerView, OnItemClickListener listener) {
        this.mListener = listener;
        this.mGestureDetector = new GestureDetector(context, new SimpleOnGestureListener() {
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }

            public void onLongPress(MotionEvent e) {
                View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                if (child != null && RecyclerItemClickListener.this.mListener != null) {
                    RecyclerItemClickListener.this.mListener.onLongItemClick(child, recyclerView.getChildAdapterPosition(child));
                }
            }
        });
    }

    public boolean onInterceptTouchEvent(RecyclerView view, MotionEvent e) {
        View childView = view.findChildViewUnder(e.getX(), e.getY());
        if (childView == null || this.mListener == null || !this.mGestureDetector.onTouchEvent(e)) {
            return false;
        }
        this.mListener.onItemClick(childView, view.getChildAdapterPosition(childView));
        return true;
    }

    public void onTouchEvent(RecyclerView view, MotionEvent motionEvent) {
    }

    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
    }
}
