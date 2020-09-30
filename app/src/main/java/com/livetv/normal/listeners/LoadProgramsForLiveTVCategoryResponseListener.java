package com.livetv.normal.listeners;

import com.livetv.normal.model.LiveTVCategory;

public interface LoadProgramsForLiveTVCategoryResponseListener extends BaseResponseListener {
    void onProgramsForLiveTVCategoriesCompleted();

    void onProgramsForLiveTVCategoryCompleted(LiveTVCategory liveTVCategory);

    void onProgramsForLiveTVCategoryError(LiveTVCategory liveTVCategory);
}
