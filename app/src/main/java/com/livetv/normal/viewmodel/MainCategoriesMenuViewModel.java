package com.livetv.normal.viewmodel;

import android.content.Context;
import androidx.databinding.ObservableBoolean;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.livetv.normal.listeners.MainCategorySelectedListener;
import com.livetv.normal.managers.VideoStreamManager;
import com.livetv.normal.model.MainCategory;
import com.livetv.normal.utils.Device;
import com.livetv.normal.view.adapters.MainCategoriesRecyclerAdapter;
import com.livetv.normal.viewmodel.MainCategoriesMenuViewModelContract.View;
import com.livetv.normal.viewmodel.MainCategoriesMenuViewModelContract.ViewModel;
import java.util.List;

public class MainCategoriesMenuViewModel implements ViewModel, MainCategorySelectedListener {
    public ObservableBoolean isTV;
    private Context mContext;
    private LinearLayoutManager mLayoutManager;
    private RecyclerView mMainCategoriesRV;
    private List<MainCategory> mainCategoriesList;
    private VideoStreamManager videoStreamManager = VideoStreamManager.getInstance();
    private View viewCallback;

    public MainCategoriesMenuViewModel(Context context) {
        this.mContext = context;
        VideoStreamManager.getInstance().FillMainCategories();
        this.isTV = new ObservableBoolean(Device.canTreatAsBox());
    }

    public void onViewResumed() {
    }

    public void onViewAttached(@NonNull Lifecycle.View viewCallback2) {
        this.viewCallback = (View) viewCallback2;
    }

    public void onViewDetached() {
        this.viewCallback = null;
    }

    public void showMainCategories(RecyclerView mainCategoriesRV) {
        this.mMainCategoriesRV = mainCategoriesRV;
        this.mLayoutManager = new LinearLayoutManager(this.mContext, 0, false);
        this.mMainCategoriesRV.setLayoutManager(this.mLayoutManager);
       // this.mMainCategoriesRV.setHasFixedSize(true);
        this.mainCategoriesList = VideoStreamManager.getInstance().getMainCategoriesList();
        this.mMainCategoriesRV.setAdapter(new MainCategoriesRecyclerAdapter(this.mContext, this.mainCategoriesList, this));
        this.mMainCategoriesRV.requestFocus();
    }

    public void onConfigurationChanged() {
    }

    public void onAccountClick(android.view.View view) {
        this.viewCallback.onAccountPressed();
    }

    public void onMainCategorySelected(int position) {
        android.view.View findViewByPosition = this.mMainCategoriesRV.getLayoutManager().findViewByPosition(position);
        this.viewCallback.onMainCategorySelected((MainCategory) this.mainCategoriesList.get(position));
    }
}
