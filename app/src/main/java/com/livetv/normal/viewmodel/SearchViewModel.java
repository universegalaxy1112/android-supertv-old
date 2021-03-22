package com.livetv.normal.viewmodel;

import android.content.Context;
import androidx.databinding.ObservableBoolean;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.livetv.normal.listeners.EditTextDoneListener;
import com.livetv.normal.listeners.MovieSelectedListener;
import com.livetv.normal.managers.VideoStreamManager;
import com.livetv.normal.model.MainCategory;
import com.livetv.normal.model.Movie;
import com.livetv.normal.model.MovieCategory;
import com.livetv.normal.model.Serie;
import com.livetv.normal.model.VideoStream;
import com.livetv.normal.utils.Connectivity;
import com.livetv.normal.utils.DataManager;
import com.livetv.normal.utils.Screen;
import com.livetv.normal.utils.Screen.Orientation;
import com.livetv.normal.view.adapters.MoviesRecyclerAdapter;
import com.livetv.normal.view.custom_views.MovieItemRelativeLayout;
import com.livetv.normal.viewmodel.SearchViewModelContract.View;
import com.livetv.normal.viewmodel.SearchViewModelContract.ViewModel;
import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import rx.Subscriber;

public class SearchViewModel implements ViewModel, MovieSelectedListener, EditTextDoneListener {
    private int columns = 3;
    public ObservableBoolean isConnected = new ObservableBoolean(Connectivity.isConnected());
    private Context mContext;
    private EditText mEditText;
    private GridLayoutManager mLayoutManager;
    /* access modifiers changed from: private */
    public final MainCategory mMainCategory;
    private RecyclerView mMoviesGridRV;
    /* access modifiers changed from: private */
    private List<? extends VideoStream> movies;
    private Pattern pattern;
    /* access modifiers changed from: private */
    public MoviesRecyclerAdapter rowsRecyclerAdapter;
    private int selectedId = -1;
    /* access modifiers changed from: private */
    public VideoStreamManager videoStreamManager = VideoStreamManager.getInstance();
    private View viewCallback;

    public SearchViewModel(Context context, MainCategory mainCategory) {
        this.mContext = context;
        this.mMainCategory = mainCategory;
        this.pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
    }

    public String removeSpecialChars(String s) {
        return this.pattern.matcher(Normalizer.normalize(s, Form.NFD)).replaceAll("");
    }

    public void onViewResumed() {
    }

    public void onViewAttached(@NonNull Lifecycle.View viewCallback2) {
        this.viewCallback = (View) viewCallback2;
    }

    public void onViewDetached() {
        this.viewCallback = null;
    }

    public void showMovieList(RecyclerView moviesGridRV, EditText editText, final boolean searchSerie) {
        this.columns = 3;
        if (Screen.getOrientation() == Orientation.LANDSCAPE) {
            this.columns = 5;
        }
        this.mMoviesGridRV = moviesGridRV;
        this.mLayoutManager = new GridLayoutManager(this.mContext, this.columns, 1, false);
        this.mMoviesGridRV.setLayoutManager(this.mLayoutManager);
       // this.mMoviesGridRV.setHasFixedSize(true);
        this.mEditText = editText;
        this.mEditText.setOnEditorActionListener(new EditText.OnEditorActionListener(){

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                        actionId == EditorInfo.IME_ACTION_DONE ||
                        event != null &&
                                event.getAction() == KeyEvent.ACTION_DOWN &&
                                event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    if (event == null || !event.isShiftPressed()) {
                        SearchViewModel.this.videoStreamManager.searchForMovies(SearchViewModel.this.mMainCategory, SearchViewModel.this.removeSpecialChars(v.getText().toString()), searchSerie).subscribe(new Subscriber<List<? extends VideoStream>>() {
                            public void onCompleted() {
                            }

                            public void onError(Throwable e) {
                            }

                            public void onNext(List<? extends VideoStream> videos) {

                                SearchViewModel.this.movies = videos;
                                List<MovieCategory> movieCategories = mMainCategory.getMovieCategories();
                                int moviecatId =  - 1;
                                if(!movieCategories.get(movieCategories.size()-1).getCatName().equals("search")) {
                                    MovieCategory movieCategory = new MovieCategory();
                                    movieCategory.setCatName("search");
                                    movieCategory.setId(movieCategories.size());
                                    movieCategories.add(movieCategory);
                                }

                                moviecatId = movieCategories.size() - 1;

                                for (VideoStream vs : SearchViewModel.this.movies) {
                                    if (vs instanceof Serie) {
                                        ((Serie) vs).setMovieCategoryIdOwner(moviecatId);
                                    }
                                }
                                SearchViewModel.this.mMainCategory.getMovieCategory(moviecatId).setMovieList(SearchViewModel.this.movies);
                                SearchViewModel.this.rowsRecyclerAdapter.updateMovies(SearchViewModel.this.movies);
                                SearchViewModel.this.rowsRecyclerAdapter.notifyDataSetChanged();
                            }
                        });
                        return true;
                    }
                }
                return false;
            }
        });

        this.movies = new ArrayList();
        this.rowsRecyclerAdapter = new MoviesRecyclerAdapter(this.mMoviesGridRV, this.mContext, this.movies, 0, this, true, true);
        this.mMoviesGridRV.setAdapter(this.rowsRecyclerAdapter);
    }

    public void onConfigurationChanged() {
        if (this.mLayoutManager != null) {
            this.columns = 3;
            if (Screen.getOrientation() == Orientation.LANDSCAPE) {
                this.columns = 5;
            }
            this.mLayoutManager.setSpanCount(this.columns);
        }
    }

    public void onMovieSelected(int selectedRow, int selectedMovie) {
        if (this.movies.get(selectedMovie) instanceof Serie) {
            addRecentSerie((Serie) this.movies.get(selectedMovie));
            this.viewCallback.onSerieAccepted((Serie) this.movies.get(selectedMovie));
            return;
        }
        this.viewCallback.onMovieAccepted((Movie) this.movies.get(selectedMovie));
    }

    private void addRecentSerie(Serie serie) {
        DataManager.getInstance().saveData("lastSerieSelected", new Gson().toJson((Object) serie));
    }

    public void clearAllSelectedMovies() {
        int from = this.mLayoutManager.findFirstVisibleItemPosition();
        int to = this.mLayoutManager.findLastVisibleItemPosition();
        for (int i = from; i <= to; i++) {
            ((MovieItemRelativeLayout) this.mLayoutManager.findViewByPosition(i)).setSelected(false);
        }
    }

    public void onEditTextDone() {
        this.mMoviesGridRV.requestFocus();
        this.mEditText.clearFocus();
        this.selectedId = 0;
        onMovieSelected(this.selectedId, this.selectedId);
        this.viewCallback.closeKeyboard();
    }
}
