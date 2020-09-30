package com.livetv.normal.view.adapters;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class LiveTVRecyclerViewHolder extends RecyclerView.ViewHolder{

    private ViewDataBinding viewBinding;

    public LiveTVRecyclerViewHolder(View itemView) {
        super(itemView);
        viewBinding = DataBindingUtil.bind(itemView);
    }

    public ViewDataBinding getViewBinding() {
        return viewBinding;
    }

}
