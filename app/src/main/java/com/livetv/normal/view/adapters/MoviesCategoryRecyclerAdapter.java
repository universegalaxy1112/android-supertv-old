package com.livetv.normal.view.adapters;

import android.app.Activity;
import android.content.Context;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.livetv.normal.BR;
import com.livetv.normal.LiveTvApplication;
import com.livetv.normal.R;
import com.livetv.normal.listeners.LoadMoviesForCategoryResponseListener;
import com.livetv.normal.listeners.MovieAcceptedListener;
import com.livetv.normal.listeners.MovieSelectedListener;
import com.livetv.normal.listeners.SearchSelectedListener;
import com.livetv.normal.listeners.ShowAsGridListener;
import com.livetv.normal.managers.VideoStreamManager;
import com.livetv.normal.model.ModelTypes;
import com.livetv.normal.model.Movie;
import com.livetv.normal.model.MovieCategory;
import com.livetv.normal.utils.Device;
import com.livetv.normal.utils.Screen;
import com.livetv.normal.utils.networking.NetManager;
import com.livetv.normal.view.custom_views.CustomLayoutManager;

import java.util.List;

public class MoviesCategoryRecyclerAdapter extends RecyclerView.Adapter<MoviesCategoryRecyclerViewHolder> implements MovieSelectedListener, LoadMoviesForCategoryResponseListener, View.OnFocusChangeListener {

    private final int[] timeOutPerRow;
    private List<MovieCategory> mMoviesList;
    private final Context mContext;
    private final MovieAcceptedListener mMovieAcceptedListener;
    private final MovieSelectedListener mMovieSelectedListener;
    private final ShowAsGridListener mShowAsGridListener;
    private final int mMainCategoryPosition;
    private final SearchSelectedListener mSearchSelectedListener;

    private boolean treatAsBox = false;

    private int minTimeout = 45;
    private int maxTimeout = 60;
    private int[] lastSelectedItemByRow;

    public MoviesCategoryRecyclerAdapter(Context context, List<MovieCategory> movies, int mainCategoryPosition, MovieAcceptedListener movieAcceptedListener, MovieSelectedListener movieSelectedListener, ShowAsGridListener showAsGridListener, SearchSelectedListener searchSelectedListener) {
        mContext = context;
        mMoviesList = movies;
//        mMoviesList = VideoStreamManager.getInstance().getMainCategory(mainCategoryPosition).getMovieCategories();

////        mMoviesList
//        int idToRemove = -1;
//        for(int i = 0; i < mMoviesList.size(); i++) {
//            MovieCategory cat = mMoviesList.get(i);
//            if(cat.getCatName().toLowerCase().contains("vistas") && cat.getCatName().toLowerCase().contains("recientes")) {
//                String recentMovies = "";
//                switch(VideoStreamManager.getInstance().getMainCategory(mainCategoryPosition).getModelType()) {//main category
//                    case ModelTypes.MOVIE_CATEGORIES:
//                        recentMovies = DataManager.getInstance().getString("recentMovies","");
//                        break;
//                    case ModelTypes.SERIES_CATEGORIES:
//                        recentMovies = DataManager.getInstance().getString("recentSeries","");
//                        break;
//                    case ModelTypes.SERIES_KIDS_CATEGORIES:
//                        recentMovies = DataManager.getInstance().getString("recentKidsSeries","");
//                        break;
//                }
//
//                if(TextUtils.isEmpty(recentMovies)) {
//                    Log.d("ramon","removing vistas recientes");
//                    idToRemove = i;
//                    break;
////                    mMoviesList.remove(cat);
//                }
//            }
//        }
//        if(idToRemove != -1) {
//            mMoviesList.remove(idToRemove);
//        }

        mMovieAcceptedListener = movieAcceptedListener;
        mMovieSelectedListener = movieSelectedListener;
        mShowAsGridListener = showAsGridListener;
        mMainCategoryPosition = mainCategoryPosition;
        mSearchSelectedListener = searchSelectedListener;

        lastSelectedItemByRow = new int[mMoviesList.size()];
        timeOutPerRow = new int[mMoviesList.size()];
        for(int i = 0; i < timeOutPerRow.length; i++) {
            timeOutPerRow[i] = minTimeout;
        }

        if(Device.canTreatAsBox()) {
//        if(Device.canTreatAsBox() && Screen.getOrientation() == Screen.Orientation.LANDSCAPE ) { //only in landscape
            treatAsBox = true;
        }

    }

    @Override
    public MoviesCategoryRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View convertView = inflater.inflate(R.layout.movies_menu_category_item, parent, false);
        return new MoviesCategoryRecyclerViewHolder(convertView);
    }

    @Override
    public void onBindViewHolder(final MoviesCategoryRecyclerViewHolder holder, int position) {
        MovieCategory movieCategory = mMoviesList.get(position);
        List<Movie> movieList = (List<Movie>) movieCategory.getMovieList();
        holder.getViewBinding().getRoot().setVisibility(View.VISIBLE);
        //movieCategory should not be null
        holder.getViewBinding().setVariable(BR.movieCategory, movieCategory);

        if(position % 2 == 0) {
            ((LinearLayout)holder.getViewBinding().getRoot().findViewById(R.id.movies_menu_category_item_container)).setBackgroundColor(ContextCompat.getColor(LiveTvApplication.getAppContext(), R.color.colorBackground));
        }
        else {
            ((LinearLayout)holder.getViewBinding().getRoot().findViewById(R.id.movies_menu_category_item_container)).setBackgroundColor(ContextCompat.getColor(LiveTvApplication.getAppContext(), R.color.colorBackgroundLight));
        }

        boolean needsRedraw = true;
        if(treatAsBox) { //only in landscape
            holder.getViewBinding().getRoot().findViewById(R.id.rv_item_header).setFocusable(false);
            holder.getViewBinding().getRoot().findViewById(R.id.rv_item_header).findViewById(R.id.show_as_grid).setVisibility(View.GONE);
        }
        else {
            holder.getViewBinding().getRoot().findViewById(R.id.rv_item_header).setOnFocusChangeListener(this);
            holder.getViewBinding().getRoot().findViewById(R.id.rv_item_header).setTag(position);//clicks

            //holder.getViewBinding().getRoot().findViewById(R.id.show_as_grid).setTag(position);
            holder.getViewBinding().setVariable(BR.categoryAdapter, this); //bind this adapter for click events
        }
        if(movieCategory.hasErrorLoading()){
//            holder.getViewBinding().getRoot().findViewById(R.id.movies_error_text).setVisibility(View.VISIBLE);
            holder.getViewBinding().getRoot().findViewById(R.id.movies_error_refresh).setVisibility(View.VISIBLE);
            holder.getViewBinding().getRoot().findViewById(R.id.movies_error_refresh).setTag(position);//clicks
            holder.getViewBinding().getRoot().findViewById(R.id.movies_error_refresh).setOnFocusChangeListener(this);
            holder.getViewBinding().getRoot().findViewById(R.id.movies_error_refresh).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    timeOutPerRow[(Integer) v.getTag()] = timeOutPerRow[(Integer) v.getTag()] + 15;
                    if(timeOutPerRow[(Integer) v.getTag()] >= maxTimeout) {
                        timeOutPerRow[(Integer) v.getTag()] = maxTimeout;
                    }

                    mMoviesList.get((Integer) v.getTag()).setLoaded(false);
                    mMoviesList.get((Integer) v.getTag()).setLoading(false);
                    mMoviesList.get((Integer) v.getTag()).setErrorLoading(false);
                    notifyItemChanged((Integer) v.getTag());
                }
            });

            holder.getViewBinding().getRoot().findViewById(R.id.progressBar).setVisibility(View.GONE);
            holder.getViewBinding().getRoot().findViewById(R.id.movies_recyclerview).setVisibility(View.GONE);
        }
        else {
            if (!movieCategory.isLoaded() && (movieList == null || movieList.size() == 0)) {
                //the first time the list will be empty and we need to load it
                if (!movieCategory.isLoading()) {
                    movieCategory.setLoading(true);
                    ;//Log.d("liveTV", "Load this movieCategory " + movieCategory.getCatName());
                    needsRedraw = false;
//                    holder.getViewBinding().getRoot().findViewById(R.id.movies_error_text).setVisibility(View.GONE);
                    holder.getViewBinding().getRoot().findViewById(R.id.movies_error_refresh).setVisibility(View.GONE);
                    holder.getViewBinding().getRoot().findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
                    holder.getViewBinding().getRoot().findViewById(R.id.movies_recyclerview).setVisibility(View.GONE);
                    NetManager.getInstance().retrieveMoviesForSubCategory(VideoStreamManager.getInstance().getMainCategory(mMainCategoryPosition), movieCategory, this, timeOutPerRow[position]);
                }
            }
            if (movieCategory.getCatName().contains("ecientes") && (movieList == null || movieList.size() == 0)) {
                holder.getViewBinding().getRoot().findViewById(R.id.progressBar).setVisibility(View.GONE);
                holder.getViewBinding().getRoot().findViewById(R.id.movies_error_refresh).setVisibility(View.GONE);
                holder.getViewBinding().getRoot().findViewById(R.id.movies_recyclerview).setVisibility(View.GONE);
                ((TextView)holder.getViewBinding().getRoot().findViewById(R.id.movies_error_text)).setText("Sin contenido");
                holder.getViewBinding().getRoot().findViewById(R.id.movies_error_text).setVisibility(View.VISIBLE);
                needsRedraw = false;
//                return;
            }

//            if (movieCategory.isLoaded() && (movieList == null || movieList.size() == 0)) {
//                needsRedraw = false;
//                holder.getViewBinding().getRoot().setVisibility(View.GONE);
//                ViewGroup.LayoutParams lp = holder.getViewBinding().getRoot().getLayoutParams();
//                lp.height = 0;
//            }
            if (needsRedraw) {
                if (movieCategory.getCatName().contains("ecientes")){
                    holder.getViewBinding().getRoot().findViewById(R.id.movies_error_text).setVisibility(View.GONE);
                }
//                if (movieCategory.getCatName().contains("ecientes") && movieCategory.isLoaded() && (movieList == null || movieList.size() == 0)) {
//                    holder.getViewBinding().getRoot().setVisibility(View.GONE);
//                    return;
//                }
                CustomLayoutManager layoutManager = new CustomLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false, Screen.GetScreenWidth(), Screen.GetScreenWidth() / 4);
                RecyclerView rv = ((RecyclerView) holder.getViewBinding().getRoot().findViewById(R.id.movies_recyclerview));

                holder.getViewBinding().getRoot().findViewById(R.id.progressBar).setVisibility(View.GONE);
                holder.getViewBinding().getRoot().findViewById(R.id.movies_error_refresh).setVisibility(View.GONE);
                holder.getViewBinding().getRoot().findViewById(R.id.movies_error_text).setVisibility(View.GONE);
                rv.setVisibility(View.VISIBLE);
                rv.setLayoutManager(layoutManager);
                //rv.setHasFixedSize(true);

//                if(treatAsBox) {
//                    movieList.add(0,null);
//                }
                MoviesRecyclerAdapter rowsRecyclerAdapter = new MoviesRecyclerAdapter(rv, mContext, movieList, position, this, false, true);
                if(treatAsBox && (movieCategory.getCatName().contains("ettings") || movieCategory.getCatName().equals(""))) {
                    rowsRecyclerAdapter.setTreatAsBox(false);
                }
                else {
                    rowsRecyclerAdapter.setTreatAsBox(treatAsBox);
                }
                rv.setAdapter(rowsRecyclerAdapter);

////                holder.getViewBinding().getRoot().findViewById(R.id.show_as_grid).setTag(position);
//                holder.getViewBinding().setVariable(BR.categoryAdapter, this); //bind this adapter for click events

//                holder.getViewBinding().executePendingBindings();

//                LinearSnapHelper snapHelper = new LinearSnapHelper();
//                snapHelper.attachToRecyclerView(rv);
                //rv.setOnFlingListener(null);
                //layoutManager.scrollToPosition(lastSelectedItemByRow[position]);
            }
        }
        holder.getViewBinding().executePendingBindings();
    }

    public void onResume() {
        if(
                VideoStreamManager.getInstance().getMainCategory(mMainCategoryPosition).getModelType().equals(ModelTypes.MOVIE_CATEGORIES) ||
                        VideoStreamManager.getInstance().getMainCategory(mMainCategoryPosition).getModelType().equals(ModelTypes.SERIES_CATEGORIES) ||
                        VideoStreamManager.getInstance().getMainCategory(mMainCategoryPosition).getModelType().equals(ModelTypes.SERIES_KIDS_CATEGORIES) ||
                        VideoStreamManager.getInstance().getMainCategory(mMainCategoryPosition).getModelType().equals(ModelTypes.KARAOKE_CATEGORIES)
                ) {
            NetManager.getInstance().retrieveMoviesForSubCategory(VideoStreamManager.getInstance().getMainCategory(mMainCategoryPosition), mMoviesList.get(0), this, timeOutPerRow[0]);
            mMoviesList = VideoStreamManager.getInstance().getMainCategory(mMainCategoryPosition).getMovieCategories();
            //this.notifyDataSetChanged();
            this.notifyItemChanged(0);
        }
    }

    @Override
    public void onMoviesForCategoryCompleted(MovieCategory movieCategory) {
        movieCategory.setLoaded(true);
        if(!movieCategory.hasErrorLoading()) {
            movieCategory.setLoading(false);
            movieCategory.setErrorLoading(false);
            if(treatAsBox && movieCategory.getCatName().contains("ettings")) {
                movieCategory.setCatName("");//solo mostrar LUPA

                if(movieCategory.getMovieList().size() > 1) {
                    movieCategory.getMovieList().remove(1);
                }
                movieCategory.getMovieList().get(0).setTitle("Buscar");
                ((Movie)movieCategory.getMovieList().get(0)).setHDPosterUrl("lupita");
            }
            ;//Log.d("liveTV", "Adding MovieCategory " + movieCategory.getCatName() + " in position " + movieCategory.getId());
            VideoStreamManager.getInstance().getMainCategory(mMainCategoryPosition).addMovieCategory(movieCategory.getId(), movieCategory);
        }
        this.notifyItemChanged(movieCategory.getId());
    }

    @Override
    public void onMoviesForCategoryCompletedError(MovieCategory movieCategory) {
        movieCategory.setLoaded(true);
        movieCategory.setLoading(false);
        movieCategory.setErrorLoading(true);
        this.notifyItemChanged(movieCategory.getId());
//        ;//Log.d("liveTV","onMoviesForCategoryCompletedError set error true");
//        movieCategory.setErrorLoading(true);
    }

    @Override
    public void onError() {

    }

    public void onClickItem(View view) {
        ;//Log.d("liveTV","click on " + view.getTag());
        mShowAsGridListener.onShowAsGridSelected((Integer)view.getTag());
    }

    @Override
    public int getItemCount() {
        return mMoviesList.size();
    }

    @Override
    public void onMovieSelected(int rowPosition, int itemPosition) {

        if(treatAsBox && rowPosition == 0) {
            mSearchSelectedListener.onSearchSelected(true);
            return;
        }

        if(treatAsBox && itemPosition == -1) { //only in landscape
            mShowAsGridListener.onShowAsGridSelected(rowPosition);
        }
        else {
//            currentSelectedRow = rowPosition;
            lastSelectedItemByRow[rowPosition] = itemPosition;

            mMovieSelectedListener.onMovieSelected(rowPosition, itemPosition);

            ;//Log.d("liveTV", "Movie Selected is " + itemPosition);
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        /*if(hasFocus) {
            if(v.getId() == R.id.movies_error_refresh) {
                v.setSelected(true);
            }
            else {
                v.findViewById(R.id.show_as_grid).setSelected(true);
            }
        }
        else {
            if(v.getId() == R.id.movies_error_refresh) {
                v.setSelected(false);
            }
            else {
                v.findViewById(R.id.show_as_grid).setSelected(false);
            }
        }*/
    }
}
