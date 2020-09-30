package com.livetv.normal.listeners;

import com.livetv.normal.model.MainCategory;
import com.livetv.normal.model.MovieCategory;
import java.util.List;

public interface LoadSubCategoriesResponseListener extends BaseResponseListener {
    void onSubCategoriesLoaded(MainCategory mainCategory, List<MovieCategory> list);

    void onSubCategoriesLoadedError();
}
