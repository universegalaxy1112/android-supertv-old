package com.livetv.normal.view.adapters;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

public class MoviesCategoryRecyclerViewHolder extends RecyclerView.ViewHolder{

//    public TextView mTextView;
//    public RecyclerView mRecyclerViewRow;
//    public View rootView;
//
//    public MoviesCategoryRecyclerViewHolder(View itemView) {
//        super(itemView);
//        rootView = itemView;
//        mRecyclerViewRow =(RecyclerView)itemView.findViewById(R.id.movies_recyclerview);
//        mTextView =(TextView)itemView.findViewById(R.id.category_title);
//    }

    private ViewDataBinding viewBinding;

    public MoviesCategoryRecyclerViewHolder(View itemView) {
        super(itemView);
        viewBinding = DataBindingUtil.bind(itemView);
    }

    public ViewDataBinding getViewBinding() {
        return viewBinding;
    }
}
