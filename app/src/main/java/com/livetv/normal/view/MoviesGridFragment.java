package com.livetv.normal.view;

import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.google.gson.Gson;
import com.livetv.normal.R;
import com.livetv.normal.databinding.MoviesGridFragmentBinding;
import com.livetv.normal.managers.VideoStreamManager;
import com.livetv.normal.model.ModelTypes.SelectedType;
import com.livetv.normal.model.Movie;
import com.livetv.normal.model.MovieCategory;
import com.livetv.normal.model.Serie;
import com.livetv.normal.viewmodel.Lifecycle;
import com.livetv.normal.viewmodel.MoviesGridViewModel;
import com.livetv.normal.viewmodel.MoviesGridViewModelContract;

public class MoviesGridFragment extends BaseFragment implements MoviesGridViewModelContract.View {
    private MoviesGridFragmentBinding moviesGridFragmentBinding;
    private MoviesGridViewModel moviesGridViewModel;
    private int seasonId;
    private int serieId;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getActivity().getIntent().getExtras();
        this.selectedType = (SelectedType) extras.get("selectedType");
        this.mainCategoryId = extras.getInt("mainCategoryId", -1);
        this.movieCategoryId = extras.getInt("movieCategoryId", -1);
        this.serieId = extras.getInt("serieId", -1);
        this.seasonId = extras.getInt("seasonId", -1);
        this.moviesGridViewModel = new MoviesGridViewModel(getContext(), this.selectedType);
    }

    /* access modifiers changed from: protected */
    public void onConfigurationChanged() {
        this.moviesGridViewModel.onConfigurationChanged();
    }

    public android.view.View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        String title;
        this.moviesGridFragmentBinding = (MoviesGridFragmentBinding) DataBindingUtil.inflate(inflater, R.layout.movies_grid_fragment, container, false);
        this.moviesGridFragmentBinding.setMoviesGridFragmentVM((MoviesGridViewModel) getViewModel());
        ((AppCompatActivity) getActivity()).setSupportActionBar(this.moviesGridFragmentBinding.toolbar);
        String str = "";
        if (this.serieId == -1) {
            title = ((MovieCategory) VideoStreamManager.getInstance().getMainCategory(this.mainCategoryId).getMovieCategories().get(this.movieCategoryId)).getCatName();
        } else {
            title = ((Serie) ((MovieCategory) VideoStreamManager.getInstance().getMainCategory(this.mainCategoryId).getMovieCategories().get(this.movieCategoryId)).getMovie(this.serieId)).getSeason(this.seasonId).getName();
        }
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle((CharSequence) title);
        this.moviesGridViewModel.showMovieList(this.moviesGridFragmentBinding.mainCategoriesRecyclerview, this.mainCategoryId, this.movieCategoryId, this.serieId, this.seasonId);
        return this.moviesGridFragmentBinding.getRoot();
    }

    /* access modifiers changed from: protected */
    public Lifecycle.ViewModel getViewModel() {
        return this.moviesGridViewModel;
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

    public void onMovieAccepted(Movie movie) {
        Bundle extras = new Bundle();
        extras.putString("movie", new Gson().toJson((Object) movie));
        extras.putInt("mainCategoryId", this.mainCategoryId);
        startActivity(getLaunchIntent(MovieDetailsActivity.class, extras));
        getActivity().overridePendingTransition(R.anim.right_in, R.anim.left_out);
    }

    public void onSerieAccepted(Serie serie) {
        Bundle extras = new Bundle();
        extras.putSerializable("selectedType", SelectedType.SERIES);
        extras.putInt("mainCategoryId", this.mainCategoryId);
        extras.putInt("movieCategoryId", this.movieCategoryId);
        extras.putInt("serieId", serie.getPosition());
        launchActivity(LoadingMoviesActivity.class, extras);
    }
}
