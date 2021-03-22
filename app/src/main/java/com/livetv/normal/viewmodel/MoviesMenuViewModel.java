package com.livetv.normal.viewmodel;

import android.content.Context;
import androidx.databinding.ObservableBoolean;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.gson.Gson;
import com.livetv.normal.listeners.MovieAcceptedListener;
import com.livetv.normal.listeners.MovieSelectedListener;
import com.livetv.normal.listeners.SearchSelectedListener;
import com.livetv.normal.listeners.ShowAsGridListener;
import com.livetv.normal.managers.VideoStreamManager;
import com.livetv.normal.model.Episode;
import com.livetv.normal.model.Movie;
import com.livetv.normal.model.Serie;
import com.livetv.normal.model.VideoStream;
import com.livetv.normal.utils.Connectivity;
import com.livetv.normal.utils.DataManager;
import com.livetv.normal.utils.Device;
import com.livetv.normal.view.adapters.MoviesCategoryRecyclerAdapter;
import com.livetv.normal.view.adapters.SeasonsRecyclerAdapter;

public class MoviesMenuViewModel implements MoviesMenuViewModelContract.ViewModel, MovieAcceptedListener, MovieSelectedListener, ShowAsGridListener, SearchSelectedListener {
    public ObservableBoolean isConnected = new ObservableBoolean(Connectivity.isConnected());
    public ObservableBoolean isTV = new ObservableBoolean(Device.canTreatAsBox());
    private RecyclerView mCategoriesRecyclerview;
    private Context mContext;
    private LinearLayoutManager mLayoutManager;
    private int mMainCategoryPosition = -1;
    private int mMovieCategoryPosition = -1;
    private int mSeriePosition = -1;
    private MoviesCategoryRecyclerAdapter mainRecyclerAdapter;
    private SeasonsRecyclerAdapter seasonsRecyclerAdapter;
    private int selectedSubCategory = 0;
    private VideoStreamManager videoStreamManager = VideoStreamManager.getInstance();
    private MoviesMenuViewModelContract.View viewCallback;

    public MoviesMenuViewModel(Context context) {
        this.mContext = context;
    }

    public void onViewResumed() {
        if (this.mainRecyclerAdapter != null) {
            this.mainRecyclerAdapter.onResume();
        }
    }

    public void onViewAttached(@NonNull Lifecycle.View viewCallback2) {
        viewCallback = (MoviesMenuViewModelContract.View) viewCallback2;
    }

    public void onViewDetached() {
        this.viewCallback = null;
    }

    public void showMovieLists(RecyclerView categoriesRecyclerview, int mainCategoryPosition) {
        this.mCategoriesRecyclerview = categoriesRecyclerview;
        this.mMainCategoryPosition = mainCategoryPosition;
        this.mLayoutManager = new LinearLayoutManager(this.mContext, 1, false);
        this.mCategoriesRecyclerview.setLayoutManager(this.mLayoutManager);
       // this.mCategoriesRecyclerview.setHasFixedSize(true);
        this.mCategoriesRecyclerview.setNestedScrollingEnabled(false);
        this.mainRecyclerAdapter = new MoviesCategoryRecyclerAdapter(this.mContext, VideoStreamManager.getInstance().getMainCategory(mainCategoryPosition).getMovieCategories(), this.mMainCategoryPosition, this, this, this, this);
        this.mCategoriesRecyclerview.setAdapter(this.mainRecyclerAdapter);
    }

    public void showEpisodeLists(RecyclerView seasonsRecyclerview, int mainCategoryId, int movieCategoryId, int serieId) {
        this.mCategoriesRecyclerview = seasonsRecyclerview;
        this.mMainCategoryPosition = mainCategoryId;
        this.mMovieCategoryPosition = movieCategoryId;
        this.mSeriePosition = serieId;
        this.mLayoutManager = new LinearLayoutManager(this.mContext, 1, false);
        this.mCategoriesRecyclerview.setLayoutManager(this.mLayoutManager);
       // this.mCategoriesRecyclerview.setHasFixedSize(true);
        this.seasonsRecyclerAdapter = new SeasonsRecyclerAdapter(this.mContext, (Serie) this.videoStreamManager.getMainCategory(mainCategoryId).getMovieCategory(movieCategoryId).getMovie(serieId), this, this, this);
        this.mCategoriesRecyclerview.setAdapter(this.seasonsRecyclerAdapter);
    }

    public void onMovieAccepted(int selectedRow, VideoStream video) {
        if (video instanceof Serie) {
            addRecentSerie((Serie) video);
            this.viewCallback.onSerieAccepted(selectedRow, (Serie) video);
        } else if ((video instanceof Movie) || (video instanceof Episode)) {
            this.viewCallback.onMovieAccepted((Movie) video);
        }
    }

    private void addRecentSerie(Serie serie) {
        DataManager.getInstance().saveData("lastSerieSelected", new Gson().toJson((Object) serie));
    }

    public void onShowAsGridSelected(Integer position) {
        this.viewCallback.onShowAsGridSelected(position);
    }

    public void onMovieSelected(int selectedRow, int selectedMovie) {
        if (this.mSeriePosition == -1) {
            onMovieAccepted(selectedRow, this.videoStreamManager.getMainCategory(this.mMainCategoryPosition).getMovieCategory(selectedRow).getMovie(selectedMovie));
        } else {
            onMovieAccepted(selectedRow, ((Serie) this.videoStreamManager.getMainCategory(this.mMainCategoryPosition).getMovieCategory(this.mMovieCategoryPosition).getMovie(this.mSeriePosition)).getSeason(selectedRow).getEpisode(selectedMovie));
        }
    }

    public void onSearchClick(android.view.View view) {
        this.viewCallback.onSearchSelected(true);
    }

    public void onSearchSelected(boolean isAccepted) {
        this.viewCallback.onSearchSelected(isAccepted);
    }
}
