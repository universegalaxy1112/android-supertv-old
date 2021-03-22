package com.livetv.normal.view;

import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.google.gson.Gson;
import com.livetv.normal.R;
import com.livetv.normal.databinding.SearchFragmentBinding;
import com.livetv.normal.managers.VideoStreamManager;
import com.livetv.normal.model.ModelTypes.SelectedType;
import com.livetv.normal.model.Movie;
import com.livetv.normal.model.Serie;
import com.livetv.normal.viewmodel.Lifecycle;
import com.livetv.normal.viewmodel.SearchViewModel;
import com.livetv.normal.viewmodel.SearchViewModelContract;

public class SearchFragment extends BaseFragment implements SearchViewModelContract.View {
    private SearchFragmentBinding searchFragmentBinding;
    private SearchViewModel searchViewModel;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getActivity().getIntent().getExtras();
        this.selectedType = (SelectedType) extras.get("selectedType");
        this.mainCategoryId = extras.getInt("mainCategoryId", 0);
        this.movieCategoryId = extras.getInt("movieCategoryId", 0);
        this.searchViewModel = new SearchViewModel(getContext(), VideoStreamManager.getInstance().getMainCategory(this.mainCategoryId));
    }

    public android.view.View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.searchFragmentBinding = (SearchFragmentBinding) DataBindingUtil.inflate(inflater, R.layout.search_fragment, container, false);
        this.searchFragmentBinding.setSearchFragmentVM((SearchViewModel) getViewModel());
        ((AppCompatActivity) getActivity()).setSupportActionBar(this.searchFragmentBinding.toolbar);
        boolean searchSerie = false;
        if ((this.mainCategoryId == 1 || this.mainCategoryId == 2) && this.selectedType == SelectedType.MAIN_CATEGORY) {
            searchSerie = true;
        }
        this.searchViewModel.showMovieList(this.searchFragmentBinding.mainCategoriesRecyclerview, this.searchFragmentBinding.editText, searchSerie);
        return this.searchFragmentBinding.getRoot();
    }

    /* access modifiers changed from: protected */
    public void onConfigurationChanged() {
        this.searchViewModel.onConfigurationChanged();
    }

    /* access modifiers changed from: protected */
    public Lifecycle.ViewModel getViewModel() {
        return this.searchViewModel;
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
        getActivity().finish();
        return true;
    }

    public void onSerieAccepted(Serie serie) {
        Bundle extras = new Bundle();
        extras.putSerializable("selectedType", SelectedType.SERIES);
        extras.putInt("mainCategoryId", this.mainCategoryId);
        extras.putInt("movieCategoryId", serie.getMovieCategoryIdOwner());
        extras.putInt("serieId", serie.getPosition());
        launchActivity(LoadingMoviesActivity.class, extras);
    }

    public void onMovieAccepted(Movie movie) {
        Bundle extras = new Bundle();
        extras.putString("movie", new Gson().toJson((Object) movie));
        extras.putInt("mainCategoryId", this.mainCategoryId);
        startActivity(getLaunchIntent(MovieDetailsActivity.class, extras));
        getActivity().overridePendingTransition(R.anim.right_in, R.anim.left_out);
    }

    public void closeKeyboard() {
        getActivity().getWindow().setSoftInputMode(2);
    }
}
