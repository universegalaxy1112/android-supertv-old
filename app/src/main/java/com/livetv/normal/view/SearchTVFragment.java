package com.livetv.normal.view;

import android.annotation.TargetApi;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.leanback.app.SearchSupportFragment;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.leanback.app.BackgroundManager;
import androidx.leanback.widget.ArrayObjectAdapter;
import androidx.leanback.widget.HeaderItem;
import androidx.leanback.widget.ListRow;
import androidx.leanback.widget.ListRowPresenter;
import androidx.leanback.widget.ObjectAdapter;
import androidx.leanback.widget.OnItemViewClickedListener;
import androidx.leanback.widget.OnItemViewSelectedListener;
import androidx.leanback.widget.Presenter;
import androidx.leanback.widget.Presenter.ViewHolder;
import androidx.leanback.widget.Row;
import androidx.leanback.widget.RowPresenter;
import androidx.appcompat.app.AppCompatActivity;
import androidx.leanback.widget.SpeechRecognitionCallback;

import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.google.gson.Gson;
import com.livetv.normal.R;
import com.livetv.normal.listeners.DialogListener;
import com.livetv.normal.managers.VideoStreamManager;
import com.livetv.normal.model.MainCategory;
import com.livetv.normal.model.ModelTypes;
import com.livetv.normal.model.ModelTypes.SelectedType;
import com.livetv.normal.model.Movie;
import com.livetv.normal.model.MovieCategory;
import com.livetv.normal.model.Serie;
import com.livetv.normal.model.VideoStream;
import com.livetv.normal.utils.DataManager;
import com.livetv.normal.utils.Dialogs;
import com.livetv.normal.view.adapters.MoviesPresenter;
import com.livetv.normal.viewmodel.Lifecycle;
import com.livetv.normal.viewmodel.SearchViewModel;
import com.livetv.normal.viewmodel.SearchViewModelContract;

import java.net.URI;
import java.net.URISyntaxException;
import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Pattern;
import rx.Subscriber;

public class SearchTVFragment extends SearchSupportFragment implements SearchViewModelContract.View,  SearchSupportFragment.SearchResultProvider{
    private static final int BACKGROUND_UPDATE_DELAY = 300;
    public static final int BLOCKED_OR_NEVER_ASKED = 2;
    public static final int DENIED = 1;
    public static final int GRANTED = 0;
    private static final int REQUEST_RECORD_AUDIO_STATE = 4;
    private static final int REQUEST_SPEECH = 16;
    boolean denyAll = false;
    /* access modifiers changed from: private */
    public BackgroundManager mBackgroundManager;
    private Timer mBackgroundTimer;
    /* access modifiers changed from: private */
    public URI mBackgroundURI;
    /* access modifiers changed from: private */
    public final Handler mHandler = new Handler();
    /* access modifiers changed from: private */
    public MainCategory mMainCategory;
    private DisplayMetrics mMetrics;
    /* access modifiers changed from: private */
    public ArrayObjectAdapter mRowsAdapter;
    protected int mainCategoryId;
    protected int movieCategoryId;
    /* access modifiers changed from: private */
    public List<? extends VideoStream> movies;
    private Pattern pattern;
    private boolean searchSerie = false;
    private SearchViewModel searchViewModel;
    protected SelectedType selectedType;

    private final class ItemViewClickedListener implements OnItemViewClickedListener  {
        private ItemViewClickedListener() {
        }

        public void onItemClicked(ViewHolder itemViewHolder, Object item, RowPresenter.ViewHolder rowViewHolder, Row row) {
            if (item instanceof Serie) {
                Serie serie = (Serie) item;
                Bundle extras = new Bundle();
                extras.putSerializable("selectedType", SelectedType.SERIES);
                extras.putInt("mainCategoryId", SearchTVFragment.this.mainCategoryId);
                extras.putInt("movieCategoryId", serie.getMovieCategoryIdOwner());
                extras.putInt("serieId", serie.getPosition());
                SearchTVFragment.this.launchActivity(LoadingMoviesActivity.class, extras);
            } else if (item instanceof Movie) {
                Movie movie = (Movie) item;
                Bundle extras2 = new Bundle();
                extras2.putString("movie", new Gson().toJson((Object) movie));
                extras2.putInt("mainCategoryId", SearchTVFragment.this.mainCategoryId);
                SearchTVFragment.this.startActivity(SearchTVFragment.this.getLaunchIntent(MovieDetailsActivity.class, extras2));
                SearchTVFragment.this.requireActivity().overridePendingTransition(R.anim.right_in, R.anim.left_out);
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
                        SearchTVFragment.this.mBackgroundURI = new URI(((Movie) item).getHDFondoUrl());
                    }
                    SearchTVFragment.this.startBackgroundTimer();
                } catch (URISyntaxException e) {
                }
            }
        }
    }

    @Override
    public ObjectAdapter getResultsAdapter() {
        return mRowsAdapter;

    }

    @Override
    public boolean onQueryTextChange(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        Objects.requireNonNull(requireActivity()).findViewById(R.id.progressBar).setVisibility(View.VISIBLE);

        VideoStreamManager.getInstance().searchForMovies(mMainCategory, removeSpecialChars(s), searchSerie).subscribe(new Subscriber<List<? extends VideoStream>>() {
            public void onCompleted() {
            }

            public void onError(Throwable e) {
                Objects.requireNonNull(requireActivity()).findViewById(R.id.progressBar).setVisibility(View.GONE);
                Toast.makeText(requireActivity(), R.string.time_out, Toast.LENGTH_SHORT).show();
                hideKeyboard();
            }

            public void onNext(List<? extends VideoStream> videos) {
                SearchTVFragment.this.mRowsAdapter.clear();
                SearchTVFragment.this.movies = videos;
                Objects.requireNonNull(requireActivity()).findViewById(R.id.progressBar).setVisibility(View.GONE);
                List<MovieCategory> movieCategories = VideoStreamManager.getInstance().getMainCategory(mainCategoryId).getMovieCategories();
                int moviecatId =  - 1;
                if(!movieCategories.get(movieCategories.size()-1).getCatName().equals("search")) {
                    MovieCategory movieCategory = new MovieCategory();
                    movieCategory.setCatName("search");
                    movieCategory.setId(movieCategories.size());
                    movieCategories.add(movieCategory);
                }

                moviecatId = movieCategories.size() - 1;

                for (VideoStream vs : SearchTVFragment.this.movies) {
                    if (vs instanceof Serie) {
                        ((Serie) vs).setMovieCategoryIdOwner(moviecatId);
                    }
                }
                movieCategories.get(moviecatId).setMovieList(SearchTVFragment.this.movies);
                ArrayObjectAdapter listRowAdapter = new ArrayObjectAdapter((Presenter) new MoviesPresenter(SearchTVFragment.this.requireActivity().getApplicationContext()));
                listRowAdapter.addAll(0, SearchTVFragment.this.movies);
                SearchTVFragment.this.mRowsAdapter.add(new ListRow(new HeaderItem("Resultados"), listRowAdapter));
            }
        });
        return false;
    }

    private void hideKeyboard() {

        requireActivity();
        InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = requireActivity().getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(requireActivity());
        }
        if(imm != null)
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

    }

    private class UpdateBackgroundTask extends TimerTask {

        private UpdateBackgroundTask() {

        }

        public void run() {
            SearchTVFragment.this.mHandler.post(new Runnable() {
                public void run() {
                    if (SearchTVFragment.this.mBackgroundURI != null) {
                        SearchTVFragment.this.updateBackground(SearchTVFragment.this.mBackgroundURI.toString());
                    } else {
                        SearchTVFragment.this.clearBackground();
                    }
                }
            });
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = requireActivity().getIntent().getExtras();
        assert extras != null;
        this.selectedType = (SelectedType) extras.get("selectedType");
        this.mainCategoryId = extras.getInt("mainCategoryId", 0);
        this.movieCategoryId = extras.getInt("movieCategoryId", 0);
        this.mMainCategory = new MainCategory();
        switch (this.mainCategoryId) {
            case 1:
                this.mMainCategory.setModelType(ModelTypes.SERIES_CATEGORIES);
                break;
            case 2:
                this.mMainCategory.setModelType(ModelTypes.SERIES_KIDS_CATEGORIES);
                break;
            default:
                this.mMainCategory.setModelType(ModelTypes.MOVIE_CATEGORIES);
                break;
        }
        this.searchViewModel = new SearchViewModel(requireActivity(), VideoStreamManager.getInstance().getMainCategory(this.mainCategoryId));
        if ((this.mainCategoryId == 1 || this.mainCategoryId == 2) && this.selectedType == SelectedType.MAIN_CATEGORY) {
            this.searchSerie = true;
        }
        this.movies = new ArrayList();
        this.pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        this.mRowsAdapter = new ArrayObjectAdapter( new ListRowPresenter());
        prepareBackgroundManager();
        setSearchResultProvider(this);
        setOnItemViewSelectedListener(new ItemViewSelectedListener());
        setOnItemViewClickedListener(new ItemViewClickedListener());
        if (getPermissionStatus("android.permission.RECORD_AUDIO") != 0) {
            setupAudioRecognition();
        } else {
            requestRecordAudioPermission();
        }
    }

    public boolean hasResults() {
        return this.mRowsAdapter.size() > 0;
    }

    /* access modifiers changed from: protected */
    public Lifecycle.ViewModel getViewModel() {
        return this.searchViewModel;
    }

    /* access modifiers changed from: protected */
    public Lifecycle.View getLifecycleView() {
        return this;
    }

    public void onSerieAccepted(Serie serie) {
    }

    public void onMovieAccepted(Movie movie) {

    }

    @Override
    public void startRecognition() {
        if (getPermissionStatus("android.permission.RECORD_AUDIO") == 0) {
            super.startRecognition();
        } else {
            requestRecordAudioPermission();
        }
    }

    public void closeKeyboard() {
        requireActivity().getWindow().setSoftInputMode(2);
    }

    public void launchActivity(Class classToLaunch, Bundle extras) {
        Intent launchIntent = new Intent(requireActivity(), classToLaunch);
        launchIntent.putExtras(extras);
        startActivity(launchIntent);
        requireActivity().overridePendingTransition(R.anim.right_in, R.anim.left_out);
    }




    public String removeSpecialChars(String s) {
        return this.pattern.matcher(Normalizer.normalize(s, Form.NFD)).replaceAll("");
    }



    public Intent getLaunchIntent(Class classToLaunch, Bundle extras) {
        Intent launchIntent = new Intent(requireActivity(), classToLaunch);
        launchIntent.putExtras(extras);
        return launchIntent;
    }

    public void requestRecordAudioPermission() {
        if (VERSION.SDK_INT < 23 || getPermissionStatus("android.permission.RECORD_AUDIO") == 0) {
            return;
        }
        this.denyAll = false;
        int accept = R.string.accept;
        int message = R.string.permission_audio;
        if (getPermissionStatus("android.permission.RECORD_AUDIO") == 2) {
            this.denyAll = true;
            accept = R.string.config;
            message = R.string.permission_audio_config;
        }
        Dialogs.showTwoButtonsDialog(requireActivity(), accept, (int) R.string.cancel, message, (DialogListener) new DialogListener() {
            @TargetApi(23)
            public void onAccept() {
                if (!SearchTVFragment.this.denyAll) {
                    DataManager.getInstance().saveData("audioPermissionRequested", Boolean.TRUE);
                    SearchTVFragment.this.requestPermissions(new String[]{"android.permission.RECORD_AUDIO"}, 4);
                    return;
                }
                Intent intent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
                intent.setData(Uri.fromParts("package", SearchTVFragment.this.requireActivity().getPackageName(), null));
                SearchTVFragment.this.startActivityForResult(intent, 4169);
            }

            public void onCancel() {
            }
        });
    }

    public int getPermissionStatus(String androidPermissionName) {
        if (ContextCompat.checkSelfPermission(requireActivity(), androidPermissionName) == 0) {
            return 0;
        }
        if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), androidPermissionName) || !DataManager.getInstance().getBoolean("audioPermissionRequested", false)) {
            return 1;
        }
        return 2;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 16:
                if (resultCode == -1) {
                    setSearchQuery(data, true);
                    return;
                }
                return;
            case 4169:
                if (getPermissionStatus("android.permission.RECORD_AUDIO") == 0) {
                    setupAudioRecognition();
                    return;
                }
                return;
            default:
        }
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 4 && getPermissionStatus("android.permission.RECORD_AUDIO") == 0) {
            setupAudioRecognition();
        }
    }

    public void setupAudioRecognition() {
        setSpeechRecognitionCallback(new SpeechRecognitionCallback() {
            public void recognizeSpeech() {
                try {
                    SearchTVFragment.this.startActivityForResult(SearchTVFragment.this.getRecognizerIntent(), 16);
                } catch (ActivityNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
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
        this.mBackgroundManager.setColor(ContextCompat.getColor(requireActivity(), R.color.colorBackgroundLight));
        this.mBackgroundTimer.cancel();
    }

    /* access modifiers changed from: protected */
    public void updateBackground(String uri) {
        Glide.with(requireActivity()).load(uri).centerCrop().into(new SimpleTarget<GlideDrawable>(this.mMetrics.widthPixels, this.mMetrics.heightPixels) {
            public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                SearchTVFragment.this.mBackgroundManager.setDrawable(resource);
            }
        });
        this.mBackgroundTimer.cancel();
    }

    private void prepareBackgroundManager() {
        this.mBackgroundManager = BackgroundManager.getInstance(requireActivity());
        this.mBackgroundManager.attach(requireActivity().getWindow());
        this.mBackgroundManager.setColor(ContextCompat.getColor(requireActivity(), R.color.colorBackgroundLight));
        this.mMetrics = new DisplayMetrics();
        requireActivity().getWindowManager().getDefaultDisplay().getMetrics(this.mMetrics);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode != 4) {
            return false;
        }
        requireActivity().finish();
        return true;
    }
}
