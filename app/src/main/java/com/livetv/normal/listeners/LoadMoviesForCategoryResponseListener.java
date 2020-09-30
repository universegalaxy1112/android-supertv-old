package com.livetv.normal.listeners;

import com.livetv.normal.model.MovieCategory;

public interface LoadMoviesForCategoryResponseListener extends BaseResponseListener {
    void onMoviesForCategoryCompleted(MovieCategory movieCategory);

    void onMoviesForCategoryCompletedError(MovieCategory movieCategory);
}
