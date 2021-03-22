package com.livetv.normal.view.adapters;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

public class LiveTVCategoryRecyclerViewHolder extends RecyclerView.ViewHolder{

    private ViewDataBinding viewBinding;

    public LiveTVCategoryRecyclerViewHolder(View itemView) {
        super(itemView);
        viewBinding = DataBindingUtil.bind(itemView);
    }

    public ViewDataBinding getViewBinding() {
        return viewBinding;
    }

}
