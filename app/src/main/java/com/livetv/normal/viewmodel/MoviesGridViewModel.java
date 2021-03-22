package com.livetv.normal.viewmodel;

import android.content.Context;
import androidx.databinding.ObservableBoolean;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.gson.Gson;
import com.livetv.normal.listeners.MovieSelectedListener;
import com.livetv.normal.managers.VideoStreamManager;
import com.livetv.normal.model.ModelTypes;
import com.livetv.normal.model.ModelTypes.SelectedType;
import com.livetv.normal.model.Movie;
import com.livetv.normal.model.MovieCategory;
import com.livetv.normal.model.Serie;
import com.livetv.normal.model.VideoStream;
import com.livetv.normal.utils.Connectivity;
import com.livetv.normal.utils.DataManager;
import com.livetv.normal.utils.Screen;
import com.livetv.normal.utils.Screen.Orientation;
import com.livetv.normal.view.adapters.MoviesRecyclerAdapter;
import com.livetv.normal.viewmodel.MoviesGridViewModelContract.View;
import com.livetv.normal.viewmodel.MoviesGridViewModelContract.ViewModel;
import java.util.List;

public class MoviesGridViewModel implements ViewModel, MovieSelectedListener {
    public ObservableBoolean isConnected = new ObservableBoolean(Connectivity.isConnected());
    private Context mContext;
    private GridLayoutManager mLayoutManager;
    private int mMainCategoryPosition;
    private int mMovieCategoryPosition;
    private int mSeasonId = -1;
    private int mSerieId = -1;
    private VideoStreamManager videoStreamManager = VideoStreamManager.getInstance();
    private View viewCallback;

    public MoviesGridViewModel(Context context, SelectedType catPosition) {
        this.mContext = context;
    }

    public void onViewResumed() {
    }

    public void onViewAttached(@NonNull Lifecycle.View viewCallback2) {
        this.viewCallback = (View) viewCallback2;
    }

    public void onViewDetached() {
        this.viewCallback = null;
    }

    public void showMovieList(RecyclerView moviesGridRV, int mainCategoryPosition, int movieCategoryPosition, int serieId, int seasonId) {
        List<? extends VideoStream> movies;
        int columns = 3;
        if (Screen.getOrientation() == Orientation.LANDSCAPE) {
            columns = 5;
        }
        this.mLayoutManager = new GridLayoutManager(this.mContext, columns, 1, false);
        moviesGridRV.setLayoutManager(this.mLayoutManager);
        //moviesGridRV.setHasFixedSize(true);
        this.mMainCategoryPosition = mainCategoryPosition;
        this.mMovieCategoryPosition = movieCategoryPosition;
        this.mSerieId = serieId;
        this.mSeasonId = seasonId;
        if (this.mSerieId == -1) {
            movies = (this.videoStreamManager.getMainCategory(this.mMainCategoryPosition).getMovieCategories().get(this.mMovieCategoryPosition)).getMovieList();
        } else {
            movies = ((Serie)(this.videoStreamManager.getMainCategory(this.mMainCategoryPosition).getMovieCategories().get(this.mMovieCategoryPosition).getMovie(serieId))).getSeason(seasonId).getEpisodeList();
        }
        moviesGridRV.setAdapter(new MoviesRecyclerAdapter(moviesGridRV, this.mContext, movies, 0, this, true, true));
    }

    public void onConfigurationChanged() {
        if (this.mLayoutManager != null) {
            int columns = 3;
            if (Screen.getOrientation() == Orientation.LANDSCAPE) {
                columns = 5;
            }
            this.mLayoutManager.setSpanCount(columns);
        }
    }

    public void onMovieSelected(int selectedRow, int selectedMovie) {
        if (this.mSerieId != -1) {
            this.viewCallback.onMovieAccepted((Movie) ((Serie) ((MovieCategory) this.videoStreamManager.getMainCategory(this.mMainCategoryPosition).getMovieCategories().get(this.mMovieCategoryPosition)).getMovie(this.mSerieId)).getSeason(this.mSeasonId).getEpisode(selectedMovie));
        } else if (this.videoStreamManager.getMainCategory(this.mMainCategoryPosition).getModelType() == ModelTypes.MOVIE_CATEGORIES || this.videoStreamManager.getMainCategory(this.mMainCategoryPosition).getModelType() == ModelTypes.ENTERTAINMENT_CATEGORIES || this.videoStreamManager.getMainCategory(this.mMainCategoryPosition).getModelType() == ModelTypes.EVENTS_CATEGORIES || this.videoStreamManager.getMainCategory(this.mMainCategoryPosition).getModelType() == ModelTypes.ADULTS_CATEGORIES) {
            this.viewCallback.onMovieAccepted((Movie) ((MovieCategory) this.videoStreamManager.getMainCategory(this.mMainCategoryPosition).getMovieCategories().get(this.mMovieCategoryPosition)).getMovie(selectedMovie));
        } else if (this.videoStreamManager.getMainCategory(this.mMainCategoryPosition).getModelType() == ModelTypes.SERIES_CATEGORIES || this.videoStreamManager.getMainCategory(this.mMainCategoryPosition).getModelType() == ModelTypes.KARAOKE_CATEGORIES || this.videoStreamManager.getMainCategory(this.mMainCategoryPosition).getModelType() == ModelTypes.SERIES_KIDS_CATEGORIES) {
            Serie serie = (Serie) ((MovieCategory) this.videoStreamManager.getMainCategory(this.mMainCategoryPosition).getMovieCategories().get(this.mMovieCategoryPosition)).getMovie(selectedMovie);
            addRecentSerie(serie);
            this.viewCallback.onSerieAccepted(serie);
        }
    }

    private void addRecentSerie(Serie serie) {
        DataManager.getInstance().saveData("lastSerieSelected", new Gson().toJson((Object) serie));
    }
}
