package com.livetv.normal.view;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.livetv.normal.R;
import com.livetv.normal.databinding.LoadingMoviesFragmentBinding;
import com.livetv.normal.managers.VideoStreamManager;
import com.livetv.normal.model.LiveTVCategory;
import com.livetv.normal.model.ModelTypes.SelectedType;
import com.livetv.normal.utils.Connectivity;
import com.livetv.normal.utils.Dialogs;
import com.livetv.normal.viewmodel.Lifecycle;
import com.livetv.normal.viewmodel.LoadingMoviesViewModel;
import com.livetv.normal.viewmodel.LoadingMoviesViewModelContract;

public class LoadingMoviesFragment extends BaseFragment implements LoadingMoviesViewModelContract.View {
    private boolean isInit = false;
    private LoadingMoviesFragmentBinding loadingMoviesFragmentBinding;
    private LoadingMoviesViewModel loadingMoviesViewModel;
    private int serieId;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getActivity().getIntent().getExtras();
        this.selectedType = (SelectedType) extras.get("selectedType");
        this.mainCategoryId = extras.getInt("mainCategoryId", -1);
        this.movieCategoryId = extras.getInt("movieCategoryId", -1);
        this.serieId = extras.getInt("serieId", -1);
        this.loadingMoviesViewModel = new LoadingMoviesViewModel();
    }

    /* access modifiers changed from: protected */
    public void onConfigurationChanged() {
    }

    public android.view.View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.loadingMoviesFragmentBinding = (LoadingMoviesFragmentBinding) DataBindingUtil.inflate(inflater, R.layout.loading_movies_fragment, container, false);
        this.loadingMoviesFragmentBinding.setLoadingFragmentVM((LoadingMoviesViewModel) getViewModel());
        ((AppCompatActivity) getActivity()).setSupportActionBar(this.loadingMoviesFragmentBinding.toolbar);
        return this.loadingMoviesFragmentBinding.getRoot();
    }

    public void onStart() {
        super.onStart();
        if (!this.isInit) {
            if (this.selectedType == SelectedType.MAIN_CATEGORY) {
                ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle((CharSequence) VideoStreamManager.getInstance().getMainCategory(this.mainCategoryId).getCatName());
                this.loadingMoviesViewModel.loadSubCategories(this.mainCategoryId);
            } else if (this.selectedType == SelectedType.SERIES) {
                ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle((CharSequence) VideoStreamManager.getInstance().getMainCategory(this.mainCategoryId).getMovieCategory(this.movieCategoryId).getMovie(this.serieId).getTitle());
                this.loadingMoviesViewModel.loadSeasons(this.mainCategoryId, this.movieCategoryId, this.serieId);
            }
            this.isInit = true;
        }
    }

    /* access modifiers changed from: protected */
    public Lifecycle.ViewModel getViewModel() {
        return this.loadingMoviesViewModel;
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
        Dialogs.showCantGoBackSnack(this.loadingMoviesFragmentBinding.mainLayout);
        return true;
    }

    public void onError() {
        showError();
    }

    public void onSubCategoriesForMainCategoryLoaded() {
        Bundle extras = new Bundle();
        extras.putSerializable("selectedType", SelectedType.MAIN_CATEGORY);
        extras.putInt("mainCategoryId", this.mainCategoryId);
        launchActivity(MoviesMenuActivity.class, extras);
        getActivity().finish();
    }

    public void onSubCategoriesForMainCategoryLoadedError() {
        showError();
    }

    public void onSeasonsForSerieLoaded() {
        Bundle extras = new Bundle();
        extras.putSerializable("selectedType", SelectedType.SEASONS);
        extras.putInt("mainCategoryId", this.mainCategoryId);
        extras.putInt("movieCategoryId", this.movieCategoryId);
        extras.putInt("serieId", this.serieId);
        launchActivity(MoviesMenuActivity.class, extras);
        getActivity().finish();
    }

    public void onSeasonsForSerieLoadedError() {
        showError();
    }

    public void onProgramsForLiveTVCategoriesLoaded() {
        for (LiveTVCategory liveTVCategory : VideoStreamManager.getInstance().getLiveTVCategoriesList()) {
        }
        launchActivity(LiveTvNewActivity.class);
        getActivity().finish();
    }

    public void onProgramsForLiveTVCategoriesLoadedError() {
        showError();
    }

    public void showError() {
        if (Connectivity.isConnected()) {
            Dialogs.showOneButtonDialog((Activity) getActivity(), (int) R.string.generic_error_title, (int) R.string.generic_loading_message, (OnClickListener) new OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    LoadingMoviesFragment.this.finishActivity();
                }
            });
        } else {
            noInternetConnection(new OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    LoadingMoviesFragment.this.finishActivity();
                }
            });
        }
    }
}
