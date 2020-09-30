package com.livetv.normal.view;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.google.gson.Gson;
import com.livetv.normal.R;
import com.livetv.normal.databinding.MoviesMenuFragmentBinding;
import com.livetv.normal.managers.VideoStreamManager;
import com.livetv.normal.model.ModelTypes.SelectedType;
import com.livetv.normal.model.Movie;
import com.livetv.normal.model.Serie;
import com.livetv.normal.viewmodel.Lifecycle;
import com.livetv.normal.viewmodel.MoviesMenuViewModel;
import com.livetv.normal.viewmodel.MoviesMenuViewModelContract;

public class MoviesMenuFragment extends BaseFragment implements MoviesMenuViewModelContract.View {
    private MoviesMenuFragmentBinding moviesMenuFragmentBinding;
    private MoviesMenuViewModel moviesModelViewModel;
    private int serieId;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.moviesModelViewModel = new MoviesMenuViewModel(getContext());
        Bundle extras = getActivity().getIntent().getExtras();
        this.selectedType = (SelectedType) extras.get("selectedType");
        this.mainCategoryId = extras.getInt("mainCategoryId", -1);
        this.movieCategoryId = extras.getInt("movieCategoryId", -1);
        this.serieId = extras.getInt("serieId", -1);
    }

    /* access modifiers changed from: protected */
    public void onConfigurationChanged() {
    }

    public android.view.View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.moviesMenuFragmentBinding = (MoviesMenuFragmentBinding) DataBindingUtil.inflate(inflater, R.layout.movies_menu_fragment, container, false);
        this.moviesMenuFragmentBinding.setMoviesMenuFragmentVM((MoviesMenuViewModel) getViewModel());
        ((AppCompatActivity) getActivity()).setSupportActionBar(this.moviesMenuFragmentBinding.toolbar);
        /*this.moviesMenuFragmentBinding.search.setOnFocusChangeListener(new OnFocusChangeListener() {
            public void onFocusChange(android.view.View v, boolean hasFocus) {
                if (hasFocus) {
                    v.setSelected(true);
                } else {
                    v.setSelected(false);
                }
            }
        });*/
        //this.moviesMenuFragmentBinding.search.requestFocus();
        if (this.selectedType == SelectedType.MAIN_CATEGORY) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle((CharSequence) VideoStreamManager.getInstance().getMainCategory(this.mainCategoryId).getCatName());
            this.moviesModelViewModel.showMovieLists(this.moviesMenuFragmentBinding.categoriesRecyclerview, this.mainCategoryId);
        }
        if (this.selectedType == SelectedType.SEASONS) {
            String seasonTitle = VideoStreamManager.getInstance().getMainCategory(this.mainCategoryId).getMovieCategory(this.movieCategoryId).getMovie(this.serieId).getTitle();
            if (seasonTitle.contains("Temporada")) {
                seasonTitle = seasonTitle.substring(0, seasonTitle.indexOf("Temporada"));
            }
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle((CharSequence) seasonTitle);
            this.moviesModelViewModel.showEpisodeLists(this.moviesMenuFragmentBinding.categoriesRecyclerview, this.mainCategoryId, this.movieCategoryId, this.serieId);
        }
        this.moviesMenuFragmentBinding.mainLayout.setFocusable(false);
        //this.moviesMenuFragmentBinding.categoriesRecyclerview.setNestedScrollingEnabled(true);
        return this.moviesMenuFragmentBinding.getRoot();
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
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode != 4) {
            return false;
        }
        finishActivity();
        return true;
    }

    public void onSerieAccepted(int movieCategory_Id, Serie serie) {
        Bundle extras = new Bundle();
        extras.putSerializable("selectedType", SelectedType.SERIES);
        extras.putInt("mainCategoryId", this.mainCategoryId);
        extras.putInt("movieCategoryId", movieCategory_Id);
        extras.putInt("serieId", serie.getPosition());
        launchActivity(LoadingMoviesActivity.class, extras);
    }

    public void onSearchSelected(boolean isAccepted) {
        if (!isAccepted) {
            this.moviesMenuFragmentBinding.search.setSelected(true);
            return;
        }
        Bundle extras = new Bundle();
        extras.putSerializable("selectedType", this.selectedType);
        extras.putInt("mainCategoryId", this.mainCategoryId);
        extras.putInt("movieCategoryId", this.movieCategoryId);
        launchActivity(SearchActivity.class, extras);
    }

    public void onMovieAccepted(Movie movie) {
        Bundle extras = new Bundle();
        extras.putString("movie", new Gson().toJson((Object) movie));
        extras.putInt("mainCategoryId", this.mainCategoryId);
        startActivity(getLaunchIntent(MovieDetailsActivity.class, extras));
        getActivity().overridePendingTransition(R.anim.right_in, R.anim.left_out);
    }

    public void onShowAsGridSelected(Integer position) {
        Bundle extras = new Bundle();
        if (this.serieId == -1) {
            extras.putSerializable("selectedType", this.selectedType);
            extras.putInt("mainCategoryId", this.mainCategoryId);
            extras.putInt("movieCategoryId", position.intValue());
        } else {
            extras.putSerializable("selectedType", SelectedType.MOVIES);
            extras.putInt("mainCategoryId", this.mainCategoryId);
            extras.putInt("movieCategoryId", this.movieCategoryId);
            extras.putInt("serieId", this.serieId);
            extras.putInt("seasonId", position.intValue());
        }
        launchActivity(MoviesGridActivity.class, extras);
    }
}
