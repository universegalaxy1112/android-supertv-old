package com.livetv.normal.view.adapters;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

public class MainCategoriesRecyclerViewHolder extends RecyclerView.ViewHolder{

    private ViewDataBinding viewBinding;

    public MainCategoriesRecyclerViewHolder(View itemView) {
        super(itemView);
        viewBinding = DataBindingUtil.bind(itemView);
    }

    public ViewDataBinding getViewBinding() {
        return viewBinding;
    }

}
