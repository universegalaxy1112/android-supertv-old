package com.livetv.normal.viewmodel;

import android.content.Context;
import androidx.databinding.ObservableBoolean;
import androidx.annotation.NonNull;
import android.util.Log;
import android.view.View.OnClickListener;
import com.google.gson.Gson;
import com.livetv.normal.databinding.MovieDetailsFragmentBinding;
import com.livetv.normal.listeners.OptionsMenuOpenListener;
import com.livetv.normal.listeners.StringRequestListener;
import com.livetv.normal.managers.VideoStreamManager;
import com.livetv.normal.model.Movie;
import com.livetv.normal.model.Serie;
import com.livetv.normal.utils.DataManager;
import com.livetv.normal.utils.networking.NetManager;
import com.livetv.normal.viewmodel.MovieDetailsViewModelContract.View;
import com.livetv.normal.viewmodel.MovieDetailsViewModelContract.ViewModel;

public class MovieDetailsViewModel implements ViewModel {
    private boolean hidePlayFromStart = false;
    public ObservableBoolean isFavorite;
    private boolean isMovies = false;
    public ObservableBoolean isSeen;
    private boolean isSerie = false;
    private Context mContext;
    private int mMainCategoryId = 0;
    private Movie mMovie;
    /* access modifiers changed from: private */
    public OptionsMenuOpenListener mOptionsMenuListener;
    private VideoStreamManager videoStreamManager = VideoStreamManager.getInstance();
    private View viewCallback;

    public MovieDetailsViewModel(Context context, int mainCategoryId) {
        this.mContext = context;
        if (mainCategoryId == 0) {
            this.isMovies = true;
        } else if (mainCategoryId == 1 || mainCategoryId == 2 || mainCategoryId == 6) {
            this.isSerie = true;
            this.mMainCategoryId = mainCategoryId;
        }
    }

    public void onViewResumed() {
    }

    public void onViewAttached(@NonNull Lifecycle.View viewCallback2) {
        this.viewCallback = (View) viewCallback2;
    }

    public void onViewDetached() {
        this.viewCallback = null;
    }

    public void showMovieDetails(Movie movie, MovieDetailsFragmentBinding movieDetailsBinding, OptionsMenuOpenListener optionsMenuListener, int mainCategoryId) {
        if (mainCategoryId == 4) {
            this.hidePlayFromStart = true;
        }
        if (this.hidePlayFromStart) {
            this.isSeen = new ObservableBoolean(false);
        } else {
            this.isSeen = new ObservableBoolean(this.videoStreamManager.isLocalSeen(String.valueOf(movie.getContentId())));
        }
        this.isFavorite = new ObservableBoolean(this.videoStreamManager.isLocalFavorite(String.valueOf(movie.getContentId())));
        this.mMovie = movie;
        this.mOptionsMenuListener = optionsMenuListener;
        movieDetailsBinding.options.setOnClickListener(new OnClickListener() {
            public void onClick(android.view.View view) {
                MovieDetailsViewModel.this.mOptionsMenuListener.openMenu();
            }
        });
        movieDetailsBinding.setMovieDetailItem(this.mMovie);
        movieDetailsBinding.playButton.requestFocus();
    }

    public void onClickFavorite(android.view.View view) {
        if (this.isFavorite.get()) {
            this.videoStreamManager.removeLocalFavorite(String.valueOf(this.mMovie.getContentId()));
            this.isFavorite.set(false);
        } else {
            this.videoStreamManager.setLocalFavorite(String.valueOf(this.mMovie.getContentId()));
            this.isFavorite.set(true);
        }
        this.isFavorite.notifyChange();
        DataManager.getInstance().saveData("favoriteMovies", this.videoStreamManager.getFavoriteMovies());
    }

    public void onClickPlayStart(android.view.View view) {
        onPlay(true);
    }

    public void onClickPlay(android.view.View view) {
        onPlay(false);
    }

    private void onPlay(boolean fromStart) {
                if (!this.videoStreamManager.getSeenMovies().contains(String.valueOf(this.mMovie.getContentId()))) {
                    this.videoStreamManager.setLocalSeen(String.valueOf(this.mMovie.getContentId()));
                    if (!this.hidePlayFromStart) {
                        this.isSeen.set(true);
                    }
                }
                this.isSeen.notifyChange();
                if (this.isMovies) {
                    addRecentMovies(this.mMovie);
                }
                if (this.isSerie) {
                    addRecentSerie();
        }
        DataManager.getInstance().saveData("seenMovies", this.videoStreamManager.getSeenMovies());
        this.viewCallback.onPlaySelected(this.mMovie, fromStart);
    }

    private void addRecentMovies(Movie movie) {
       /* String serieType = "";
        if (videoStreamManager.getMainCategory(mMainCategoryId).getModelType().equals(ModelTypes.MOVIE_CATEGORIES)) {
            serieType = "recentMovies";
        } else if(videoStreamManager.getMainCategory(mMainCategoryId).getModelType().equals(ModelTypes.ENTERTAINMENT_CATEGORIES)){
            serieType = "recentEntertainment";
        }
        String recentMovies = DataManager.getInstance().getString(serieType,"");
        if (TextUtils.isEmpty(recentMovies)) {
            List<Movie> movies = new ArrayList<>();
            movies.add(movie);
            DataManager.getInstance().saveData(serieType, new Gson().toJson(movies));
        }
        else {
            List<Movie> movieList = new Gson().fromJson(recentMovies, new TypeToken<List<Movie>>() { }.getType());
            boolean needsToAdd = true;
            Iterator it = movieList.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                if (movie.getContentId() == ((Movie) it.next()).getContentId()) {
                    needsToAdd = false;
                    break;
                }
            }
            if (needsToAdd) {
                if (movieList.size() == 10) {
                    movieList.remove(9);
                }
                movieList.add(0, movie);
                DataManager.getInstance().saveData(serieType, new Gson().toJson( movieList));
            }
        }*/
        /*String recentMovies = DataManager.getInstance().getString("recentMovies", "");
        if (TextUtils.isEmpty(recentMovies)) {
            List<Movie> movies = new ArrayList<>();
            movies.add(movie);
            DataManager.getInstance().saveData("recentMovies", new Gson().toJson((Object) movies));
            return;
        }
        List<Movie> movieList = (List) new Gson().fromJson(recentMovies, new TypeToken<List<Movie>>() {
        }.getType());
        boolean needsToAdd = true;
        Iterator it = movieList.iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            if (movie.getContentId() == ((Movie) it.next()).getContentId()) {
                needsToAdd = false;
                break;
            }
        }
        if (needsToAdd) {
            if (movieList.size() == 10) {
                movieList.remove(9);
            }
            movieList.add(0, movie);
            DataManager.getInstance().saveData("recentMovies", new Gson().toJson((Object) movieList));
        }*/
        NetManager.getInstance().addRecent(Integer.toString(movie.getCategoryType()), Integer.toString(movie.getContentId()), new StringRequestListener() {
            @Override
            public void onCompleted(String response) {
                Log.i("TAG", "success log recent");

            }

            @Override
            public void onError() {
                Log.i("TAG", "fail log recent");

            }
        });
    }

    private void addRecentSerie() {

       /* String recentSeries;
        VideoStreamManager videoStreamManager=  VideoStreamManager.getInstance();
        String lastSelectedSerie = DataManager.getInstance().getString("lastSerieSelected", null);
        if (lastSelectedSerie != null) {
            Serie serie = (Serie) new Gson().fromJson(lastSelectedSerie, Serie.class);
            String serieType;
            if (videoStreamManager.getMainCategory(mMainCategoryId).getModelType() == ModelTypes.SERIES_CATEGORIES) {
                serieType = "recentSeries";
            } else if (videoStreamManager.getMainCategory(mMainCategoryId).getModelType() == ModelTypes.SERIES_KIDS_CATEGORIES) {
                serieType = "recentKids";
            } else if (videoStreamManager.getMainCategory(mMainCategoryId).getModelType() == ModelTypes.KARAOKE_CATEGORIES) {
                serieType = "recentKara";
            } else {
                serieType = "recentSeries";
            }
            recentSeries = DataManager.getInstance().getString(serieType, "");

            if (TextUtils.isEmpty(recentSeries)) {
                List<Serie> series = new ArrayList<>();
                series.add(serie);

                DataManager.getInstance().saveData(serieType, new Gson().toJson(series));
            } else {
                List<Serie> serieList = new Gson().fromJson(recentSeries, new TypeToken<List<Serie>>() {
                }.getType());
                boolean needsToAdd = true;
                for (Serie ser : serieList) {
                    if (serie.getContentId() == ser.getContentId()) {
                        needsToAdd = false;
                        break;
                    }
                }
                if (needsToAdd) {
                    if (serieList.size() == 10) {
                        serieList.remove(9);
                    }
                    serieList.add(0, serie);
                    DataManager.getInstance().saveData(serieType, new Gson().toJson(serieList));
                }
            }
        }*/

        try {
            String lastSelectedSerie = DataManager.getInstance().getString("lastSerieSelected", null);
            if (lastSelectedSerie != null) {
                Serie serie = new Gson().fromJson(lastSelectedSerie, Serie.class);
                NetManager.getInstance().addRecent(Integer.toString(serie.getCategoryType()), Integer.toString(serie.getContentId()), new StringRequestListener() {
                    @Override
                    public void onCompleted(String response) {
                        Log.i("TAG", "success log recent");
                    }

                    @Override
                    public void onError() {
                        Log.i("TAG", "fail log recent");
                    }
                });

            }
        }catch(Exception e) {
            e.printStackTrace();
        }

        /*String serieType;
        try {
            String lastSelectedSerie = DataManager.getInstance().getString("lastSerieSelected", null);
            if (lastSelectedSerie != null) {
                Serie serie = (Serie) new Gson().fromJson(lastSelectedSerie, Serie.class);
                String str = "";
                String str2 = "";
                if (this.videoStreamManager.getMainCategory(this.mMainCategoryId).getModelType() == ModelTypes.SERIES_CATEGORIES) {
                    serieType = "recentSeries";
                } else {
                    serieType = "recentKidsSeries";
                }
                String recentSeries = DataManager.getInstance().getString(serieType, "");
                if (TextUtils.isEmpty(recentSeries)) {
                    List<Serie> series = new ArrayList<>();
                    series.add(serie);
                    DataManager.getInstance().saveData(serieType, new Gson().toJson((Object) series));
                    return;
                }
                List<Serie> serieList = (List) new Gson().fromJson(recentSeries, new TypeToken<List<Serie>>() {
                }.getType());
                boolean needsToAdd = true;
                Iterator it = serieList.iterator();
                while (true) {
                    if (!it.hasNext()) {
                        break;
                    }
                    if (serie.getContentId() == ((Serie) it.next()).getContentId()) {
                        needsToAdd = false;
                        break;
                    }
                }
                if (needsToAdd) {
                    if (serieList.size() == 10) {
                        serieList.remove(9);
                    }
                    serieList.add(0, serie);
                    DataManager.getInstance().saveData(serieType, new Gson().toJson((Object) serieList));
                }
            }
        } catch (Exception e) {
        }*/
    }
}
