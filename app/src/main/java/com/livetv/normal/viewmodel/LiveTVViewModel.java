package com.livetv.normal.viewmodel;

import android.content.Context;
import android.databinding.ObservableBoolean;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.LinearLayout;

import com.livetv.normal.BR;
import com.livetv.normal.databinding.ActivityLivetvnewBinding;
import com.livetv.normal.listeners.LiveProgramSelectedListener;
import com.livetv.normal.listeners.LiveTVCategorySelectedListener;
import com.livetv.normal.managers.VideoStreamManager;
import com.livetv.normal.model.LiveProgram;
import com.livetv.normal.model.LiveTVCategory;
import com.livetv.normal.utils.Connectivity;
import com.livetv.normal.view.adapters.LiveCategoryAdapter;
import com.livetv.normal.view.adapters.LivetvAdapterNew;
import com.livetv.normal.view.exoplayernew.VideoPlayFragment;
import com.livetv.normal.R;

import java.util.ArrayList;
import java.util.List;


public class LiveTVViewModel implements LiveTVViewModelContract.ViewModel, LiveProgramSelectedListener, LiveTVCategorySelectedListener {

    public ObservableBoolean isConnected;
    private LiveTVViewModelContract.View viewCallback;
    private VideoStreamManager videoStreamManager;
    private Context mContext;
    private LivetvAdapterNew rowsRecyclerAdapter;
    private ActivityLivetvnewBinding activityLiveBinding;
    private boolean isFullscreen = false;
    public LiveTVViewModel(Context context ) {
        isConnected = new ObservableBoolean(Connectivity.isConnected());
        videoStreamManager = VideoStreamManager.getInstance();
        mContext = context;
    }

    @Override
    public void onViewResumed() {

    }
    @Override
    public void onViewAttached(@NonNull Lifecycle.View viewCallback) {
        this.viewCallback = (LiveTVViewModelContract.View) viewCallback;
    }

    @Override
    public void onViewDetached() {
        this.viewCallback = null;
    }

    @Override
    public void showProgramList(final ActivityLivetvnewBinding activityLiveBinding){
        this.activityLiveBinding=activityLiveBinding;

        final List<LiveProgram> liveProgramList = videoStreamManager.getAllLivePrograms();
        rowsRecyclerAdapter =new LivetvAdapterNew(mContext,liveProgramList,activityLiveBinding.programmingRecycler,this);
        GridLayoutManager mLayoutManager = new GridLayoutManager(mContext, 1);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        activityLiveBinding.programmingRecycler.setLayoutManager(mLayoutManager);
        activityLiveBinding.programmingRecycler.setAdapter(rowsRecyclerAdapter);
        final List<LiveTVCategory> categoryList = new ArrayList<>();
        LiveTVCategory allCat = new LiveTVCategory();
        allCat.setCatName("Todos");
        allCat.setPosition(-1);
        categoryList.add(allCat);
        categoryList.addAll(videoStreamManager.getLiveTVCategoriesList());
        LiveCategoryAdapter liveCategoryAdapter = new LiveCategoryAdapter(mContext, categoryList, activityLiveBinding.liveCategoryRecycler, this);
        GridLayoutManager mCategoryLayOutManager = new GridLayoutManager(mContext,1);
        mCategoryLayOutManager.setOrientation(LinearLayoutManager.VERTICAL);
        activityLiveBinding.liveCategoryRecycler.setLayoutManager(mCategoryLayOutManager);
        activityLiveBinding.liveCategoryRecycler.setAdapter(liveCategoryAdapter);
        activityLiveBinding.setVariable(com.livetv.normal.BR.currentCategory,categoryList.get(0));
    }

    public void toggleFullscreen(View view) {
        if(isFullscreen) {
            minimize(null);
        }else{
            fullScreen(null);
        }
    }
    public void minimize(View view){
        if(!isFullscreen) return;
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(activityLiveBinding.exoPlayerVirtual.getMeasuredWidth(), activityLiveBinding.exoPlayerVirtual.getMeasuredHeight());
        int margin = (int)(mContext.getResources().getDisplayMetrics().density*16);
        layoutParams.setMargins(margin,margin,20,20);
        activityLiveBinding.exoPlayer.setLayoutParams(layoutParams);
        activityLiveBinding.exoPlayer.findViewById(R.id.top_bar).setVisibility(View.GONE);
        activityLiveBinding.exoPlayer.findViewById(R.id.detail_bar).setVisibility(View.GONE);
        isFullscreen = false;
        showPrograms();
    }

    public void fullScreen(VideoPlayFragment fragment){
        if(isFullscreen) {
            if(fragment != null) {
                fragment.toggleDetail();
            }
            return;
        }        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((AppCompatActivity)mContext).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(activityLiveBinding.parent.getMeasuredWidth(),
                activityLiveBinding.parent.getMeasuredHeight(),
                1.0f);
        layoutParams.setMargins(0,0,0,0);
        activityLiveBinding.exoPlayer.setLayoutParams(layoutParams);
        activityLiveBinding.programmingRecycler.setVisibility(View.GONE);
        activityLiveBinding.liveCategoryRecycler.setVisibility(View.GONE);
        isFullscreen = true;
    }

    public void showCategories(View view) {
        if(isFullscreen) return;
        activityLiveBinding.programmingRecycler.setVisibility(View.GONE);
        activityLiveBinding.liveCategoryRecycler.setVisibility(View.VISIBLE);
       // activityLiveBinding.logo.requestFocus();
    }

    private void showPrograms() {
        activityLiveBinding.programmingRecycler.setVisibility(View.VISIBLE);
        activityLiveBinding.liveCategoryRecycler.setVisibility(View.GONE);
        //activityLiveBinding.logo.requestFocus();
    }

    public void toggleTitle(VideoPlayFragment videoPlayFragment) {
        if(videoPlayFragment != null && isFullscreen) {
            videoPlayFragment.toggleTitle();
        }
    }

    @Override
    public void onLiveTVCategorySelected(LiveTVCategory category) {
        if(category.getPosition() == -1)
            rowsRecyclerAdapter.updateChannels(videoStreamManager.getAllLivePrograms());
        else if(videoStreamManager.getLiveTVCategory(category.getPosition()) != null)
            rowsRecyclerAdapter.updateChannels(videoStreamManager.getLiveTVCategory(category.getPosition()).getLivePrograms());
        activityLiveBinding.setVariable(BR.currentCategory,category);
        activityLiveBinding.programmingRecycler.init();
        activityLiveBinding.programmingRecycler.scrollToPosition(0);
        showPrograms();
    }

    @Override
    public void onLiveProgramSelected(LiveProgram liveProgram, int programPosition) {
        viewCallback.onProgramAccepted(liveProgram);
        activityLiveBinding.setVariable(BR.currentProgram,liveProgram);
    }

}