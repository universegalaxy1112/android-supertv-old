package com.livetv.normal.view.adapters;

import android.app.Activity;
import android.content.Context;
import androidx.leanback.widget.Presenter;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.livetv.normal.R;
import com.livetv.normal.model.Movie;

import static com.livetv.normal.BR.moviesMenuItem;

public class MoviesPresenter extends Presenter {
    private Context mContext;

    public MoviesPresenter(Context context) {
        this.mContext = context;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent) {
        return new MoviesPresenterViewHolder(((LayoutInflater) this.mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.movies_menu_tv_item, parent, false));
    }

    public void onBindViewHolder(ViewHolder holder, Object item) {
        Movie movie = (Movie) item;
        ((MoviesPresenterViewHolder) holder).getViewBinding().setVariable(moviesMenuItem, movie);
        if (movie.getPosition() != -1) {
            Glide.with(this.mContext).load(movie.getHDPosterUrl()).error((int) R.drawable.imageview_placeholder).into((ImageView) ((MoviesPresenterViewHolder) holder).getViewBinding().getRoot().findViewById(R.id.image_container).findViewById(R.id.movie_image));
        } else {
            ((ImageView) ((MoviesPresenterViewHolder) holder).getViewBinding().getRoot().findViewById(R.id.image_container).findViewById(R.id.movie_image)).setImageDrawable(mContext.getDrawable(R.drawable.more_item));
        }
        ((MoviesPresenterViewHolder) holder).getViewBinding().executePendingBindings();
    }

    public void onUnbindViewHolder(ViewHolder viewHolder) {
    }
}
