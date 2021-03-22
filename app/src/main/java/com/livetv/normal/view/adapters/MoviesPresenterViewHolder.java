package com.livetv.normal.view.adapters;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.leanback.widget.Presenter.ViewHolder;
import android.view.View;

public class MoviesPresenterViewHolder extends ViewHolder {
    private ViewDataBinding viewBinding;

    public MoviesPresenterViewHolder(View itemView) {
        super(itemView);
        this.viewBinding = DataBindingUtil.bind(itemView);
    }

    public ViewDataBinding getViewBinding() {
        return this.viewBinding;
    }
}
