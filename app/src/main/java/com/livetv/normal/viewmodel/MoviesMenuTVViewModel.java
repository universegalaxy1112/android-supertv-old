package com.livetv.normal.viewmodel;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.livetv.normal.viewmodel.Lifecycle.View;
import com.livetv.normal.viewmodel.MoviesMenuTVViewModelContract.ViewModel;

public class MoviesMenuTVViewModel implements ViewModel {
    public void onViewResumed() {
    }

    public void onViewAttached(@NonNull View viewCallback) {
    }

    public void onViewDetached() {
    }

    public void showMovieLists(RecyclerView categoriesRecyclerview, int mainCategoryPosition) {
    }

    public void showEpisodeLists(RecyclerView categoriesRecyclerview, int mainCategoryId, int movieCategoryId, int serieId) {
    }
}
