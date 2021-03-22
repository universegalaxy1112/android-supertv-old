package com.livetv.normal.view;

import android.os.Bundle;
import androidx.annotation.RequiresApi;
import androidx.leanback.app.DetailsSupportFragment;
import androidx.core.content.ContextCompat;
import androidx.leanback.app.BackgroundManager;
import androidx.leanback.widget.ArrayObjectAdapter;
import androidx.leanback.widget.ClassPresenterSelector;
import androidx.leanback.widget.DetailsOverviewRow;
import androidx.leanback.widget.FullWidthDetailsOverviewRowPresenter;
import androidx.leanback.widget.ListRow;
import androidx.leanback.widget.ListRowPresenter;
import androidx.leanback.widget.PresenterSelector;
import android.util.DisplayMetrics;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.google.gson.Gson;
import com.livetv.normal.R;
import com.livetv.normal.databinding.MovieDetailsFragmentBinding;
import com.livetv.normal.model.Movie;
import com.livetv.normal.view.adapters.MovieDetailsPresenter;
import com.livetv.normal.viewmodel.MovieDetailsViewModel;

import java.net.URI;
import java.net.URISyntaxException;

public class MovieDetailsTVFragment extends DetailsSupportFragment {
    private ArrayObjectAdapter mAdapter;
    /* access modifiers changed from: private */
    public BackgroundManager mBackgroundManager;
    /* access modifiers changed from: private */
    public DetailsOverviewRow mDetailsOverviewRow;
    private FullWidthDetailsOverviewRowPresenter mFullWidthDetailsOverviewRowPresenter;
    private SimpleTarget<GlideDrawable> mGlideDrawableSimpleTarget = new SimpleTarget<GlideDrawable>() {
        public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
            MovieDetailsTVFragment.this.mDetailsOverviewRow.setImageDrawable(resource);
        }
    };
    private DisplayMetrics mMetrics;
    private Movie mMovie;
    private int mainCategoryId;
    private MovieDetailsFragmentBinding movieDetailsFragmentBinding;
    private MovieDetailsViewModel movieDetailsViewModel;

    @RequiresApi(api = 23)
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mainCategoryId = getArguments().getInt("mainCategoryId", 0);
        this.mMovie = (Movie) new Gson().fromJson(getArguments().getString("movie"), Movie.class);
        setupAdapter();
        setupDetailsOverviewRow();
        if (this.mMovie.getHDFondoUrl() != null) {
            URI uri = null;
            try {
                uri = new URI(this.mMovie.getHDFondoUrl());
            } catch (URISyntaxException e) {
            }
            setupBackground(uri.toString());
        }
    }

    /* access modifiers changed from: 0000 */
    @RequiresApi(api = 23)
    public void setupAdapter() {
        this.mFullWidthDetailsOverviewRowPresenter = new FullWidthDetailsOverviewRowPresenter(new MovieDetailsPresenter(this.mainCategoryId, getActivity().getApplicationContext()));
        ClassPresenterSelector classPresenterSelector = new ClassPresenterSelector();
        classPresenterSelector.addClassPresenter(DetailsOverviewRow.class, this.mFullWidthDetailsOverviewRowPresenter);
        classPresenterSelector.addClassPresenter(ListRow.class, new ListRowPresenter());
        this.mAdapter = new ArrayObjectAdapter((PresenterSelector) classPresenterSelector);
        setAdapter(this.mAdapter);
    }

    /* access modifiers changed from: 0000 */
    public void setupDetailsOverviewRow() {
        this.mDetailsOverviewRow = new DetailsOverviewRow(this.mMovie);
        this.mAdapter.add(this.mDetailsOverviewRow);
        loadImage(this.mMovie.getHDPosterUrl());
    }

    private void loadImage(String url) {
        Glide.with(getActivity()).load(url).listener( new RequestListener<String, GlideDrawable>() {
            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                return false;
            }

            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                return false;
            }
        }).into(this.mGlideDrawableSimpleTarget);
    }

    /* access modifiers changed from: 0000 */
    public void setupBackground(String uri) {
        if (!uri.isEmpty()) {
            this.mBackgroundManager = BackgroundManager.getInstance(getActivity());
            this.mBackgroundManager.attach(getActivity().getWindow());
            this.mBackgroundManager.setColor(ContextCompat.getColor(getActivity(), R.color.colorBackgroundLight));
            this.mMetrics = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(this.mMetrics);
            Glide.with(this).load(uri).centerCrop().into(new SimpleTarget<GlideDrawable>(this.mMetrics.widthPixels, this.mMetrics.heightPixels) {
                public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                    MovieDetailsTVFragment.this.mBackgroundManager.setDrawable(resource);
                }
            });
        }
    }
}
