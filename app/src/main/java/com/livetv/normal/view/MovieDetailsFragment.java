package com.livetv.normal.view;

import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import com.google.gson.Gson;
import com.livetv.normal.R;
import com.livetv.normal.LiveTvApplication;
import com.livetv.normal.databinding.MovieDetailsFragmentBinding;
import com.livetv.normal.listeners.OptionsMenuOpenListener;
import com.livetv.normal.model.Movie;
import com.livetv.normal.utils.DataManager;
import com.livetv.normal.view.exoplayer.VideoActivity;
import com.livetv.normal.viewmodel.Lifecycle;
import com.livetv.normal.viewmodel.MovieDetailsViewModel;
import com.livetv.normal.viewmodel.MovieDetailsViewModelContract;

public class MovieDetailsFragment extends BaseFragment implements MovieDetailsViewModelContract.View, OptionsMenuOpenListener, OnFocusChangeListener {
    private MovieDetailsFragmentBinding movieDetailsFragmentBinding;
    private MovieDetailsViewModel movieDetailsViewModel;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mainCategoryId = getArguments().getInt("mainCategoryId", 0);
        this.movieDetailsViewModel = new MovieDetailsViewModel(getContext(), this.mainCategoryId);
        setHasOptionsMenu(true);
    }

    /* access modifiers changed from: protected */
    public void onConfigurationChanged() {
        setupUI();
    }

    public android.view.View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.movieDetailsFragmentBinding = (MovieDetailsFragmentBinding) DataBindingUtil.inflate(inflater, R.layout.movie_details_fragment, container, false);
        this.movieDetailsFragmentBinding.setMovieDetailsVM(this.movieDetailsViewModel);
        Movie movie = (Movie) new Gson().fromJson(getArguments().getString("movie"), Movie.class);
        ((AppCompatActivity) getActivity()).setSupportActionBar(this.movieDetailsFragmentBinding.toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
        if (TextUtils.isEmpty(movie.getDirector())) {
            this.movieDetailsFragmentBinding.directorsId.setVisibility(View.GONE);
        }
        if (TextUtils.isEmpty(movie.getActors())) {
            this.movieDetailsFragmentBinding.actorsId.setVisibility(View.VISIBLE);
        }
        this.movieDetailsFragmentBinding.playButton.setOnFocusChangeListener(this);
        this.movieDetailsFragmentBinding.playFromStartButton.setOnFocusChangeListener(this);
        this.movieDetailsFragmentBinding.options.setOnFocusChangeListener(this);
        this.movieDetailsViewModel.showMovieDetails(movie, this.movieDetailsFragmentBinding, this, this.mainCategoryId);
        if (this.mainCategoryId == 4) {
            this.movieDetailsFragmentBinding.actorsId.setVisibility(View.GONE);
            this.movieDetailsFragmentBinding.directorsId.setVisibility(View.GONE);
        }
        return this.movieDetailsFragmentBinding.getRoot();
    }

    private void setupUI() {
        ViewGroup viewGroup = (ViewGroup) getView();
        viewGroup.removeAllViewsInLayout();
        this.movieDetailsFragmentBinding = (MovieDetailsFragmentBinding) DataBindingUtil.inflate(LayoutInflater.from(getActivity()), R.layout.movie_details_fragment, viewGroup, false);
        this.movieDetailsFragmentBinding.setMovieDetailsVM(this.movieDetailsViewModel);
        ((AppCompatActivity) getActivity()).setSupportActionBar(this.movieDetailsFragmentBinding.toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
        this.movieDetailsViewModel.showMovieDetails((Movie) new Gson().fromJson(getArguments().getString("movie"), Movie.class), this.movieDetailsFragmentBinding, this, this.mainCategoryId);
        viewGroup.addView(this.movieDetailsFragmentBinding.getRoot());
    }

    public void openMenu() {
        this.movieDetailsFragmentBinding.toolbar.showOverflowMenu();
    }

    /* access modifiers changed from: protected */
    public Lifecycle.ViewModel getViewModel() {
        return this.movieDetailsViewModel;
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

    public void onPlaySelected(Movie movie, boolean fromStart) {
        int movieId = movie.getContentId();
        String[] uris = {movie.getStreamUrl()};
        String[] extensions = {movie.getStreamUrl().substring(movie.getStreamUrl().replace(".mkv.mkv", ".mkv").replace(".mp4.mp4", ".mp4").lastIndexOf(".") + 1)};
        long secondsToPlay = 0;
        if (!fromStart) {
            secondsToPlay = DataManager.getInstance().getLong("seconds" + movieId, 0);
        } else {
            DataManager.getInstance().saveData("seconds" + movieId, Integer.valueOf(0));
        }
        Intent launchIntent = new Intent(LiveTvApplication.getAppContext(), VideoActivity.class);
        launchIntent.putExtra("uri_list", uris).putExtra("extension_list", extensions).putExtra("movie_id_extra", movieId).putExtra("seconds_to_start", secondsToPlay).putExtra("mainCategoryId", this.mainCategoryId).putExtra("subsURL", movie.getSubtitleUrl()).setAction("com.google.android.exoplayer.demo.action.VIEW_LIST").putExtra("title", movie.getTitle());
        startActivity(launchIntent);
        getActivity().overridePendingTransition(R.anim.right_in, R.anim.left_out);
    }

    public void onFocusChange(android.view.View v, boolean hasFocus) {
        if (!v.isSelected()) {
            v.setSelected(true);
        } else {
            v.setSelected(false);
        }
    }
}
