package com.livetv.normal.view.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.RequiresApi;
import android.support.v17.leanback.widget.Presenter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import com.google.gson.Gson;
import com.livetv.normal.R;
import com.livetv.normal.LiveTvApplication;
import com.livetv.normal.listeners.StringRequestListener;
import com.livetv.normal.model.Movie;
import com.livetv.normal.model.Serie;
import com.livetv.normal.utils.DataManager;
import com.livetv.normal.utils.networking.NetManager;
import com.livetv.normal.view.exoplayer.VideoActivity;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static com.livetv.normal.BR.movieDetailItem;

public class MovieDetailsPresenter extends Presenter implements OnFocusChangeListener {
    private Context mContext;
    private int mMainCategoryId;

    public MovieDetailsPresenter(int mainCategoryId, Context context) {
        this.mMainCategoryId = mainCategoryId;
        this.mContext = context;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent) {
        return new MoviesPresenterViewHolder(((LayoutInflater) this.mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.movie_details_tv, parent, false));
    }

    @RequiresApi(api = 19)
    public void onBindViewHolder(ViewHolder holder, Object item) {
        final Movie movie = (Movie) item;
        ((MoviesPresenterViewHolder) holder).getViewBinding().setVariable(movieDetailItem, movie);
        LinearLayout mGenresLayout = (LinearLayout) ((MoviesPresenterViewHolder) holder).view.findViewById(R.id.genres);
        mGenresLayout.removeAllViews();
        float corner = this.mContext.getResources().getDimension(R.dimen.corner);
        String[] split = movie.getCategories().split(", ");
        int length = split.length;
        for (int i = 0; i < length; i++) {
            String g = split[i];
            TextView tv = new TextView(this.mContext);
            tv.setText(g);
            tv.setTextColor(mContext.getResources().getColor(R.color.white));
            GradientDrawable shape = new GradientDrawable();
            shape.setShape(GradientDrawable.RECTANGLE);
            shape.setCornerRadius(corner);
            shape.setColor(mContext.getResources().getColorStateList( R.color.colorPrimaryDark));
            tv.setPadding(8, 8, 8, 8);
            tv.setBackground(shape);
            LayoutParams params = new LayoutParams(new LayoutParams(-2, -2));
            params.setMargins(0, 0, 16, 0);
            tv.setLayoutParams(params);
            mGenresLayout.addView(tv);
        }
        long secondsToPlay = 0;
        try {
            secondsToPlay = DataManager.getInstance().getLong("seconds" + movie.getContentId(), 0);
        } catch (Exception e) {
        }
        if (movie.getDirector() == null) {
            ((MoviesPresenterViewHolder) holder).view.findViewById(R.id.director_tv).setVisibility(View.GONE);
        }
        TextView playButton = (TextView) ((MoviesPresenterViewHolder) holder).view.findViewById(R.id.play_button);
        TextView playStartButton = (TextView) ((MoviesPresenterViewHolder) holder).view.findViewById(R.id.play_from_start_button);
        if (secondsToPlay == 0) {
            playButton.setVisibility(View.GONE);
        }
        playButton.setOnFocusChangeListener(this);
        playStartButton.setOnFocusChangeListener(this);
        OnClickListener r0 = new OnClickListener() {
            public void onClick(View v) {
                Log.d("DNLS", "play Button");
                MovieDetailsPresenter.this.onItemClicked(movie, false);
            }
        };
        playButton.setOnClickListener(r0);
        OnClickListener r02 = new OnClickListener() {
            public void onClick(View v) {
                Log.d("DNLS", "play start Button");
                MovieDetailsPresenter.this.onItemClicked(movie, true);
            }
        };
        playStartButton.setOnClickListener(r02);
        ((MoviesPresenterViewHolder) holder).getViewBinding().executePendingBindings();
    }

    public void onUnbindViewHolder(ViewHolder viewHolder) {

    }

    public void onItemClicked(Movie mMovie, boolean fromStart) {
        Log.d("DNLS", "movie: " + mMovie.getTitle());
        int movieId = mMovie.getContentId();
        String[] uris = {mMovie.getStreamUrl()};
        String[] extensions = {mMovie.getStreamUrl().substring(mMovie.getStreamUrl().replace(".mkv.mkv", ".mkv").replace(".mp4.mp4", ".mp4").lastIndexOf(".") + 1)};
        long secondsToPlay = 0;
        if (!fromStart) {
            secondsToPlay = DataManager.getInstance().getLong("seconds" + movieId, 0);
        } else {
            DataManager.getInstance().saveData("seconds" + movieId, Integer.valueOf(0));
        }
        if (mMainCategoryId == 0 || mMainCategoryId == 3) {
            addRecentMovies(mMovie);
        } else if (this.mMainCategoryId == 1 || this.mMainCategoryId == 2 || this.mMainCategoryId == 6) {
            addRecentSerie();
        }
        Intent launchIntent = new Intent(LiveTvApplication.getAppContext(), VideoActivity.class);
        launchIntent.putExtra("uri_list", uris).putExtra("extension_list", extensions).putExtra("movie_id_extra", movieId).putExtra("seconds_to_start", secondsToPlay).putExtra("mainCategoryId", this.mMainCategoryId).putExtra("subsURL", mMovie.getSubtitleUrl()).setAction("com.google.android.exoplayer.demo.action.VIEW_LIST").putExtra("title", mMovie.getTitle()).setFlags(FLAG_ACTIVITY_NEW_TASK);
        this.mContext.startActivity(launchIntent);
    }

    private void addRecentMovies(Movie movie) {
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
        /*String serieType = "";
        if (VideoStreamManager.getInstance().getMainCategory(mMainCategoryId).getModelType().equals(ModelTypes.MOVIE_CATEGORIES)) {
            serieType = "recentMovies";
        } else if(VideoStreamManager.getInstance().getMainCategory(mMainCategoryId).getModelType().equals(ModelTypes.ENTERTAINMENT_CATEGORIES)){
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
       /* String serieType;
        try {
            String lastSelectedSerie = DataManager.getInstance().getString("lastSerieSelected", null);
            if (lastSelectedSerie != null) {
                Serie serie = (Serie) new Gson().fromJson(lastSelectedSerie, Serie.class);
                String str = "";
                String str2 = "";
                if (VideoStreamManager.getInstance().getMainCategory(this.mMainCategoryId).getModelType() == ModelTypes.SERIES_CATEGORIES) {
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
    }

    public void onFocusChange(View v, boolean hasFocus) {
        if (!v.isSelected()) {
            v.setSelected(true);
        } else {
            v.setSelected(false);
        }
    }
}
