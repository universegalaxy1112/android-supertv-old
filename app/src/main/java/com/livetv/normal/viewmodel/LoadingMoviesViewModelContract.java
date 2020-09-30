package com.livetv.normal.viewmodel;

import com.livetv.normal.listeners.BaseResponseListener;

public interface LoadingMoviesViewModelContract {

    public interface View extends Lifecycle.View, BaseResponseListener {
        void onProgramsForLiveTVCategoriesLoaded();

        void onProgramsForLiveTVCategoriesLoadedError();

        void onSeasonsForSerieLoaded();

        void onSeasonsForSerieLoadedError();

        void onSubCategoriesForMainCategoryLoaded();

        void onSubCategoriesForMainCategoryLoadedError();
    }

    public interface ViewModel extends Lifecycle.ViewModel {
        void loadSeasons(int i, int i2, int i3);

        void loadSubCategories(int i);
    }
}
