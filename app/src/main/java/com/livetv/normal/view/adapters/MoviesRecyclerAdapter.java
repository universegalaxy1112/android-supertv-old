package com.livetv.normal.view.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import androidx.leanback.widget.Presenter;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.livetv.normal.BR;
import com.livetv.normal.R;
import com.livetv.normal.binding.BindingAdapters;
import com.livetv.normal.listeners.ImageLoadedListener;
import com.livetv.normal.listeners.MovieAcceptedListener;
import com.livetv.normal.listeners.MovieSelectedListener;
import com.livetv.normal.model.ImageResponse;
import com.livetv.normal.model.Movie;
import com.livetv.normal.model.VideoStream;
import com.livetv.normal.utils.Files;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MoviesRecyclerAdapter extends RecyclerView.Adapter<MoviesRecyclerViewHolder> implements View.OnFocusChangeListener, ImageLoadedListener{
    private final boolean mFromGrid;
    private final RecyclerView mRV;
    //implements View.OnClickListener{

    private List<? extends VideoStream> mMovies;
    private Context mContext;
    private int mRowPosition;
    private View previousSelectedView = null;
    private MovieSelectedListener mMovieSelectedListener;
    private MovieAcceptedListener mMovieAcceptedListener;
    private boolean mShowTitle = false;
    private boolean mTreatAsBox = false;
    private Map<Integer, Bitmap> loadedImages = new HashMap<>();
    private File directory;
    private Handler handler = new Handler();

    public MoviesRecyclerAdapter(RecyclerView rv, Context context, List<? extends VideoStream> objects, int rowPosition, MovieSelectedListener movieSelectedListener, boolean fromGrid, boolean showTitle) {
        mContext = context;
        mMovies = objects;
        mRowPosition = rowPosition;
        mMovieSelectedListener = movieSelectedListener;
        mFromGrid = fromGrid;
        mShowTitle = showTitle;
        mRV = rv;
    }

    public MoviesRecyclerAdapter(RecyclerView rv, Context context, List<? extends VideoStream> objects, int rowPosition, MovieSelectedListener movieSelectedListener, boolean fromGrid) {
        mContext = context;
        mMovies = objects;
        mRowPosition = rowPosition;
        mMovieSelectedListener = movieSelectedListener;
        mFromGrid = fromGrid;
        mRV = rv;
    }

    public void setTreatAsBox(boolean treatAsBox) {
        mTreatAsBox = treatAsBox;
    }

    //used in search
    public void updateMovies(List<? extends VideoStream> objects) {
        mMovies = objects;
    }

    @Override
    public MoviesRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View convertView;
        convertView = inflater.inflate(R.layout.movies_menu_item, parent, false);


        directory = Files.GetFile(Files.GetCacheDir());
        if(directory != null && !directory.exists()) {
            directory.mkdirs();
        }


        return new MoviesRecyclerViewHolder(convertView);
    }

    @Override
    public void onBindViewHolder(MoviesRecyclerViewHolder holder, int position) {
        if(mTreatAsBox && position == 0) {
            holder.getViewBinding().getRoot().setTag(new int[]{mRowPosition, -1});
            holder.getViewBinding().setVariable(BR.moviesAdapter, this); //bind this adapter for click events
            holder.getViewBinding().getRoot().setOnFocusChangeListener(this);
            holder.getViewBinding().executePendingBindings();
            ((ImageView)holder.getViewBinding().getRoot().findViewById(R.id.image_container).findViewById(R.id.movie_image)).setImageResource(R.drawable.more_item);
            TextView tv = ((TextView)holder.getViewBinding().getRoot().findViewById(R.id.image_container).findViewById(R.id.movie_title));
            tv.setVisibility(View.VISIBLE);
            tv.setText("Lista completa");
//
        }
        else {
            if(mTreatAsBox) {
                position = position - 1;
            }
            Movie movie = (Movie) mMovies.get(position);

            holder.getViewBinding().setVariable(BR.moviesMenuItem, movie); // bind item with Movie model
            holder.getViewBinding().getRoot().setTag(new int[]{mRowPosition, position});
            holder.getViewBinding().setVariable(BR.moviesAdapter, this); //bind this adapter for click events
            holder.getViewBinding().getRoot().setOnFocusChangeListener(this);

            if (mShowTitle) {
                holder.getViewBinding().getRoot().findViewById(R.id.movie_title).setVisibility(View.VISIBLE);
            }

            // Create imageDir
            if(movie.getHDPosterUrl().equals("lupita")) {
                ((ImageView) holder.getViewBinding().getRoot().findViewById(R.id.image_container).findViewById(R.id.movie_image)).setImageResource(R.drawable.search_icon);
            }
            else {
                File imageFile = new File(directory, movie.getHDPosterUrl().substring(movie.getHDPosterUrl().lastIndexOf("/"), movie.getHDPosterUrl().length()));
                if (imageFile.exists()) {
                    ((ImageView) holder.getViewBinding().getRoot().findViewById(R.id.image_container).findViewById(R.id.movie_image)).setImageBitmap(Files.getBitmap(imageFile.getPath()));
                    return;
                } else {
                    if (loadedImages.containsKey(position)) {
                        ((ImageView) holder.getViewBinding().getRoot().findViewById(R.id.image_container).findViewById(R.id.movie_image)).setImageBitmap(loadedImages.get(position));
                    } else {
                        ((ImageView) holder.getViewBinding().getRoot().findViewById(R.id.image_container).findViewById(R.id.movie_image)).setImageResource(R.drawable.imageview_placeholder);
                        BindingAdapters.retrieveImage(movie.getHDPosterUrl(), this, position);
                    }
                }
            }
            holder.getViewBinding().executePendingBindings();
        }
    }

    @Override
    public int getItemCount() {
        if(mTreatAsBox) {
            return mMovies.size() + 1;
        }
        return mMovies.size();
    }

//    @Override
    public void onClickItem(View view) {
        ;//Log.d("liveTV","click on row " + mRowPosition+ " item " +view.getTag());
//        if(view.isSelected()) {
            int rowPosition = ((int[]) view.getTag())[0];
            int itemPosition = ((int[]) view.getTag())[1];

            mMovieSelectedListener.onMovieSelected(rowPosition, itemPosition);
//            if(!mFromGrid)
//                mMovieSelectedListener.onMovieSelected(rowPosition, itemPosition);
//        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        ;//Log.d("liveTV","focus changed" + v.getTag());
       /* if(hasFocus) {
            v.setSelected(true);
            v.findViewById(R.id.movie_image_selected).setVisibility(View.VISIBLE);
        }
        else {
            v.setSelected(false);
            v.findViewById(R.id.movie_image_selected).setVisibility(View.GONE);
        }*/
    }

    @Override
    public void onLoaded(final ImageResponse response) {
        loadedImages.put(response.getPosition(), response.getBitmap());
//        notifyItemChanged(response.getPosition());
        postAndNotifyAdapter(handler, this, response);

    }

    @Override
    public void onLoaded2(ImageResponse imageResponse, Presenter.ViewHolder viewHolder) {

    }

    protected void postAndNotifyAdapter(final Handler handler, final RecyclerView.Adapter adapter, final ImageResponse response) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (!mRV.isComputingLayout()) {
                    notifyItemChanged(response.getPosition());
                } else {
                    postAndNotifyAdapter(handler, adapter, response);
                }
            }
        });
    }
}
