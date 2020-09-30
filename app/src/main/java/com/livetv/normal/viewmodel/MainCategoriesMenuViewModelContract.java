package com.livetv.normal.viewmodel;

import android.support.v7.widget.RecyclerView;
import com.livetv.normal.model.MainCategory;

public interface MainCategoriesMenuViewModelContract {

    public interface View extends Lifecycle.View {
        void onAccountPressed();

        void onMainCategorySelected(MainCategory mainCategory);
    }

    public interface ViewModel extends Lifecycle.ViewModel {
        void onConfigurationChanged();

        void showMainCategories(RecyclerView recyclerView);
    }
}
