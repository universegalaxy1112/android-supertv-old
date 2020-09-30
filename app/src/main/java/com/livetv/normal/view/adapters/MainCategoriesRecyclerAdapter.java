package com.livetv.normal.view.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.databinding.library.baseAdapters.BR;
import com.livetv.normal.R;
import com.livetv.normal.listeners.MainCategorySelectedListener;
import com.livetv.normal.model.MainCategory;

import java.util.List;

public class MainCategoriesRecyclerAdapter extends RecyclerView.Adapter<MainCategoriesRecyclerViewHolder> implements View.OnFocusChangeListener{

    private final List<MainCategory> mMainCategories;
    private final MainCategorySelectedListener mMainCategorySelectedListener;
    private Context mContext;
    private int currentSelectedPosition = 0;

    public MainCategoriesRecyclerAdapter(Context context, List<MainCategory> objects, MainCategorySelectedListener mainCategorySelectedListener) {
        mContext = context;
        mMainCategories = objects;
        mMainCategorySelectedListener = mainCategorySelectedListener;
    }

    @Override
    public MainCategoriesRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View convertView = inflater.inflate(R.layout.main_categories_menu_item, parent, false);
        return new MainCategoriesRecyclerViewHolder(convertView);
    }

    @Override
    public void onBindViewHolder(MainCategoriesRecyclerViewHolder holder, int position) {
        MainCategory mainCategory = mMainCategories.get(position);

        holder.getViewBinding().setVariable(BR.mainCategoryItem, mainCategory); // bind item with Movie model
        holder.getViewBinding().getRoot().setTag(position);
        holder.getViewBinding().setVariable(BR.mainCategoriesAdapter, this); //bind this adapter for click events
        holder.getViewBinding().getRoot().setOnFocusChangeListener(this);
        holder.getViewBinding().executePendingBindings();

    }

    @Override
    public int getItemCount() {
        return mMainCategories.size();
    }

//    @Override
    public void onClickItem(View view) {
        ;//Log.d("liveTV","click on " + view.getTag());
        mMainCategorySelectedListener.onMainCategorySelected((Integer) view.getTag());
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        ;//Log.d("liveTV","onFocusChange " + v.getTag());
        if(hasFocus) {
            v.setSelected(true);
            v.findViewById(R.id.movie_image_selected).setVisibility(View.VISIBLE);
        }
        else {
            v.setSelected(false);
            v.findViewById(R.id.movie_image_selected).setVisibility(View.GONE);
        }
    }
}
