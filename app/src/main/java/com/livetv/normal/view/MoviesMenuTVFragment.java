package com.livetv.normal.view;

import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.leanback.app.BackgroundManager;
import androidx.leanback.widget.ArrayObjectAdapter;
import androidx.leanback.widget.HeaderItem;
import androidx.leanback.widget.ListRow;
import androidx.leanback.widget.ListRowPresenter;
import androidx.leanback.widget.OnItemViewClickedListener;
import androidx.leanback.widget.OnItemViewSelectedListener;
import androidx.leanback.widget.Presenter;
import androidx.leanback.widget.Presenter.ViewHolder;
import androidx.leanback.widget.Row;
import androidx.leanback.widget.RowPresenter;
import android.util.DisplayMetrics;
import android.view.View.OnClickListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.google.gson.Gson;
import com.livetv.normal.R;
import com.livetv.normal.databinding.MoviesMenuFragmentBinding;
import com.livetv.normal.listeners.LoadEpisodesForSerieResponseListener;
import com.livetv.normal.listeners.LoadMoviesForCategoryResponseListener;
import com.livetv.normal.managers.VideoStreamManager;
import com.livetv.normal.model.ListRowComparator;
import com.livetv.normal.model.ModelTypes.SelectedType;
import com.livetv.normal.model.Movie;
import com.livetv.normal.model.MovieCategory;
import com.livetv.normal.model.Season;
import com.livetv.normal.model.Serie;
import com.livetv.normal.model.VideoStream;
import com.livetv.normal.utils.DataManager;
import com.livetv.normal.utils.networking.NetManager;
import com.livetv.normal.view.adapters.MoviesPresenter;
import com.livetv.normal.view.adapters.SortedArrayObjectAdapter;
import com.livetv.normal.viewmodel.Lifecycle;
import com.livetv.normal.viewmodel.MoviesMenuViewModel;
import com.livetv.normal.viewmodel.MoviesMenuViewModelContract;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Comparator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MoviesMenuTVFragment extends BaseTVFragment implements MoviesMenuViewModelContract.View, LoadMoviesForCategoryResponseListener, LoadEpisodesForSerieResponseListener {
    private static final int BACKGROUND_UPDATE_DELAY = 300;
    public BackgroundManager mBackgroundManager;
    private Timer mBackgroundTimer;
    /* access modifiers changed from: private */
    public URI mBackgroundURI;
    /* access modifiers changed from: private */
    public final Handler mHandler = new Handler();
    private DisplayMetrics mMetrics;
    private SortedArrayObjectAdapter mRowsAdapter;
    private MoviesMenuFragmentBinding moviesMenuFragmentBinding;
    private MoviesMenuViewModel moviesModelViewModel;
    /* access modifiers changed from: private */
    public int serieId;

    private final class ItemViewClickedListener implements OnItemViewClickedListener {
        private ItemViewClickedListener() {
        }

        public void onItemClicked(ViewHolder itemViewHolder, Object item, RowPresenter.ViewHolder rowViewHolder, Row row) {
            if (item instanceof Serie) {
                Serie serie = (Serie) item;
                MoviesMenuTVFragment.this.addRecentSerie(serie);
                Bundle extras = new Bundle();
                extras.putSerializable("selectedType", SelectedType.SERIES);
                extras.putInt("mainCategoryId", MoviesMenuTVFragment.this.mainCategoryId);
                extras.putInt("movieCategoryId", serie.getMovieCategoryIdOwner());
                extras.putInt("serieId", serie.getPosition());
                MoviesMenuTVFragment.this.launchActivity(LoadingMoviesActivity.class, extras);
            } else if (item instanceof Movie) {
                Movie movie = (Movie) item;
                Bundle extras2 = new Bundle();
                if (((Movie) item).getPosition() != -1) {
                    extras2.putString("movie", new Gson().toJson((Object) movie));
                    extras2.putInt("mainCategoryId", MoviesMenuTVFragment.this.mainCategoryId);
                    MoviesMenuTVFragment.this.startActivity(MoviesMenuTVFragment.this.getLaunchIntent(MovieDetailsActivity.class, extras2));
                    MoviesMenuTVFragment.this.getActivity().overridePendingTransition(R.anim.right_in, R.anim.left_out);
                    return;
                }
                if (MoviesMenuTVFragment.this.serieId == -1) {
                    extras2.putSerializable("selectedType", MoviesMenuTVFragment.this.selectedType);
                    extras2.putInt("mainCategoryId", MoviesMenuTVFragment.this.mainCategoryId);
                    extras2.putInt("movieCategoryId", movie.getLength());
                } else {
                    extras2.putSerializable("selectedType", SelectedType.MOVIES);
                    extras2.putInt("mainCategoryId", MoviesMenuTVFragment.this.mainCategoryId);
                    extras2.putInt("movieCategoryId", MoviesMenuTVFragment.this.movieCategoryId);
                    extras2.putInt("serieId", MoviesMenuTVFragment.this.serieId);
                    extras2.putInt("seasonId", movie.getLength());
                }
                MoviesMenuTVFragment.this.launchActivity(MoviesGridActivity.class, extras2);
            }
        }
    }

    private final class ItemViewSelectedListener implements OnItemViewSelectedListener {
        private ItemViewSelectedListener() {
        }

        public void onItemSelected(ViewHolder itemViewHolder, Object item, RowPresenter.ViewHolder rowViewHolder, Row row) {
            if (item instanceof Movie) {
                try {
                    if (!(((Movie) item).getPosition() == -1 || ((Movie) item).getHDFondoUrl() == null)) {
                        MoviesMenuTVFragment.this.mBackgroundURI = new URI(((Movie) item).getHDFondoUrl());
                    }
                    MoviesMenuTVFragment.this.startBackgroundTimer();
                } catch (URISyntaxException e) {
                }
            }
        }
    }

    private class UpdateBackgroundTask extends TimerTask {
        private UpdateBackgroundTask() {
        }

        public void run() {
            MoviesMenuTVFragment.this.mHandler.post(new Runnable() {
                public void run() {
                    if (MoviesMenuTVFragment.this.mBackgroundURI != null) {
                        MoviesMenuTVFragment.this.updateBackground(MoviesMenuTVFragment.this.mBackgroundURI.toString());
                    } else {
                        MoviesMenuTVFragment.this.clearBackground();
                    }
                }
            });
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.moviesModelViewModel = new MoviesMenuViewModel(getActivity().getApplicationContext());
        Bundle extras = getActivity().getIntent().getExtras();
        this.selectedType = (SelectedType) extras.get("selectedType");
        this.mainCategoryId = extras.getInt("mainCategoryId", -1);
        this.movieCategoryId = extras.getInt("movieCategoryId", -1);
        this.serieId = extras.getInt("serieId", -1);
    }

    @Override
    public void onResume() {
        super.onResume();
       /* if(
                VideoStreamManager.getInstance().getMainCategory(mainCategoryId).getModelType() == ModelTypes.MOVIE_CATEGORIES ||
                        VideoStreamManager.getInstance().getMainCategory(mainCategoryId).getModelType() == ModelTypes.SERIES_CATEGORIES ||
                        VideoStreamManager.getInstance().getMainCategory(mainCategoryId).getModelType() == ModelTypes.SERIES_KIDS_CATEGORIES ||
                        VideoStreamManager.getInstance().getMainCategory(mainCategoryId).getModelType() == ModelTypes.KARAOKE_CATEGORIES
        ) {
            NetManager.getInstance().retrieveMoviesForSubCategory(VideoStreamManager.getInstance().getMainCategory(mainCategoryId), VideoStreamManager.getInstance().getMainCategory(this.mainCategoryId).getMovieCategories().get(1), this, 30);
            //this.notifyDataSetChanged();
        }*/
    }

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        prepareBackgroundManager();
        setHeadersState(1);
        setHeadersTransitionOnBackEnabled(true);
        setBrandColor(ContextCompat.getColor(getActivity(), R.color.colorPrimaryDarkAlpha));
        this.mRowsAdapter = new SortedArrayObjectAdapter((Comparator) new ListRowComparator(), (Presenter) new ListRowPresenter());
        loadData();
        setAdapter(this.mRowsAdapter);
        if (this.selectedType == SelectedType.MAIN_CATEGORY) {
            setSearchAffordanceColor(ContextCompat.getColor(getActivity(), R.color.colorAccent));
            setOnSearchClickedListener(new OnClickListener() {
                public void onClick(android.view.View v) {
                    Bundle extras = new Bundle();
                    extras.putSerializable("selectedType", MoviesMenuTVFragment.this.selectedType);
                    extras.putInt("mainCategoryId", MoviesMenuTVFragment.this.mainCategoryId);
                    extras.putInt("movieCategoryId", MoviesMenuTVFragment.this.movieCategoryId);
                    MoviesMenuTVFragment.this.launchActivity(SearchActivity.class, extras);
                }
            });
        }
        setOnItemViewClickedListener(new ItemViewClickedListener());
        setOnItemViewSelectedListener(new ItemViewSelectedListener());
    }

    private void loadData() {
        if (this.selectedType == SelectedType.MAIN_CATEGORY) {
            List<MovieCategory> mCategoriesList = VideoStreamManager.getInstance().getMainCategory(this.mainCategoryId).getMovieCategories();
            setTitle(VideoStreamManager.getInstance().getMainCategory(this.mainCategoryId).getCatName());
            for (int i = 0; i < (mainCategoryId == 4 ? mCategoriesList.size() : mCategoriesList.size() - 1); i++) {
                NetManager.getInstance().retrieveMoviesForSubCategory(VideoStreamManager.getInstance().getMainCategory(this.mainCategoryId), (MovieCategory) mCategoriesList.get(mainCategoryId == 4 ? i : i + 1), this, 60);
            }
        }
        if (this.selectedType == SelectedType.SEASONS) {
            String seasonTitle = VideoStreamManager.getInstance().getMainCategory(this.mainCategoryId).getMovieCategory(this.movieCategoryId).getMovie(this.serieId).getTitle();
            if (seasonTitle.contains("Temporada")) {
                seasonTitle = seasonTitle.substring(0, seasonTitle.indexOf("Temporada"));
            }
            setTitle(seasonTitle);
            Serie serie = (Serie) VideoStreamManager.getInstance().getMainCategory(this.mainCategoryId).getMovieCategory(this.movieCategoryId).getMovie(this.serieId);
            List<Season> seasonList = serie.getSeasons();
            for (int i2 = 0; i2 < seasonList.size(); i2++) {
                NetManager.getInstance().retrieveEpisodesForSerie(serie, (Season) seasonList.get(i2), this);
            }
        }
    }

    private void loadRow(MovieCategory category) {
        if(category.getCatName().equals("search")) return;
        HeaderItem header = new HeaderItem((long) category.getId(), category.getCatName());
        Movie showAsGrid = new Movie();
        showAsGrid.setTitle("Ver Todas");
        showAsGrid.setPosition(-1);
        List<? extends VideoStream> movieList = category.getMovieList();
        ArrayObjectAdapter listRowAdapter = new ArrayObjectAdapter((Presenter) new MoviesPresenter(getActivity().getApplicationContext()));
        listRowAdapter.addAll(0, movieList);
        showAsGrid.setLength(category.getId());
        ListRow r = new ListRow(header, listRowAdapter);
        r.setId((long) category.getId());
        this.mRowsAdapter.add(r);
    }

    private void loadRow(Season season) {
        HeaderItem header = new HeaderItem((long) season.getPosition(), season.getName());
        List<? extends VideoStream> episodeList = season.getEpisodeList();
        ArrayObjectAdapter listRowAdapter = new ArrayObjectAdapter((Presenter) new MoviesPresenter(getActivity().getApplicationContext()));
        listRowAdapter.addAll(0, episodeList);
        ListRow r = new ListRow(header, listRowAdapter);
        r.setId((long) season.getPosition());
        this.mRowsAdapter.add(r);
        this.mRowsAdapter.notify();
    }

    /* access modifiers changed from: protected */
    public Lifecycle.ViewModel getViewModel() {
        return this.moviesModelViewModel;
    }

    /* access modifiers changed from: protected */
    public Lifecycle.View getLifecycleView() {
        return this;
    }

    /* access modifiers changed from: protected */
    public void onConfigurationChanged() {
    }

    public void onMovieAccepted(Movie movie) {
    }

    public void onShowAsGridSelected(Integer position) {
    }

    public void onSerieAccepted(int selectedRow, Serie serie) {
    }

    public void onSearchSelected(boolean isAccepted) {
    }

    public void onError() {
    }

    public void onMoviesForCategoryCompleted(MovieCategory movieCategory) {
        loadRow(movieCategory);
    }

    public void onMoviesForCategoryCompletedError(MovieCategory movieCategory) {
    }

    public void onEpisodesForSerieCompleted(Season season) {
        loadRow(season);
    }

    /* access modifiers changed from: private */
    public void startBackgroundTimer() {
        if (this.mBackgroundTimer != null) {
            this.mBackgroundTimer.cancel();
        }
        this.mBackgroundTimer = new Timer();
        this.mBackgroundTimer.schedule(new UpdateBackgroundTask(), 300);
    }

    /* access modifiers changed from: protected */
    public void clearBackground() {
        this.mBackgroundManager.setColor(ContextCompat.getColor(getActivity(), R.color.colorBackgroundLight));
        this.mBackgroundTimer.cancel();
    }

    /* access modifiers changed from: protected */
    public void updateBackground(String uri) {
        Glide.with(requireActivity()).load(uri).centerCrop().into(new SimpleTarget<GlideDrawable>(this.mMetrics.widthPixels, this.mMetrics.heightPixels) {
            public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                MoviesMenuTVFragment.this.mBackgroundManager.setDrawable(resource);
            }
        });
        this.mBackgroundTimer.cancel();
    }

    private void prepareBackgroundManager() {
        this.mBackgroundManager = BackgroundManager.getInstance(getActivity());
        this.mBackgroundManager.attach(getActivity().getWindow());
        this.mBackgroundManager.setColor(ContextCompat.getColor(getActivity(), R.color.colorBackgroundLight));
        this.mMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(this.mMetrics);
    }

    /* access modifiers changed from: private */
    public void addRecentSerie(Serie serie) {
        DataManager.getInstance().saveData("lastSerieSelected", new Gson().toJson((Object) serie));
    }
}
