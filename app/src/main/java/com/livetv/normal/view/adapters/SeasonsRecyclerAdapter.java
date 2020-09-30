package com.livetv.normal.view.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.android.databinding.library.baseAdapters.BR;
import com.livetv.normal.LiveTvApplication;
import com.livetv.normal.R;
import com.livetv.normal.listeners.LoadEpisodesForSerieResponseListener;
import com.livetv.normal.listeners.MovieAcceptedListener;
import com.livetv.normal.listeners.MovieSelectedListener;
import com.livetv.normal.listeners.ShowAsGridListener;
import com.livetv.normal.model.Episode;
import com.livetv.normal.model.Season;
import com.livetv.normal.model.Serie;
import com.livetv.normal.utils.Device;
import com.livetv.normal.utils.Screen;
import com.livetv.normal.utils.networking.NetManager;
import com.livetv.normal.view.custom_views.CustomLayoutManager;

import java.util.List;

public class SeasonsRecyclerAdapter extends RecyclerView.Adapter<MoviesCategoryRecyclerViewHolder> implements MovieSelectedListener, LoadEpisodesForSerieResponseListener, View.OnFocusChangeListener {

    private final List<Season> mSeasonsList;
    private final Context mContext;
    private final MovieAcceptedListener mMovieAcceptedListener;
    private final MovieSelectedListener mMovieSelectedListener;
    private final ShowAsGridListener mShowAsGridListener;
    private final Serie mSerie;

    private boolean treatAsBox = false;

    private int currentSelectedRow = 0;
    private int[] lastSelectedItemByRow;

    public SeasonsRecyclerAdapter(Context context, Serie serie, MovieAcceptedListener movieAcceptedListener, MovieSelectedListener movieSelectedListener, ShowAsGridListener showAsGridListener) {
        mContext = context;
        mSerie = serie;
        mSeasonsList = serie.getSeasons();
        mMovieAcceptedListener = movieAcceptedListener;
        mMovieSelectedListener = movieSelectedListener;
        mShowAsGridListener = showAsGridListener;

        lastSelectedItemByRow = new int[mSeasonsList.size()];

        if(Device.canTreatAsBox()) {
//        if(Device.canTreatAsBox() && Screen.getOrientation() == Screen.Orientation.LANDSCAPE ) { //only in landscape
            treatAsBox = true;
        }

        ;//Log.d("liveTV","SeasonsRecyclerAdapter array size is  " + mSeasonsList.size());
    }

    @Override
    public MoviesCategoryRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View convertView = inflater.inflate(R.layout.movies_menu_season_item, parent, false);
        return new MoviesCategoryRecyclerViewHolder(convertView);
    }

    @Override
    public void onBindViewHolder(final MoviesCategoryRecyclerViewHolder holder, int position) {
        Season season = mSeasonsList.get(position);
        List<Episode> episodeList = (List<Episode>) season.getEpisodeList();

//        //movieCategory should not be null

        holder.getViewBinding().setVariable(BR.seasonModel, season);
//        ((TextView)holder.getViewBinding().getRoot().findViewById(R.id.season_title)).setText("Season " + position);

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
            holder.getViewBinding().setVariable(BR.seasonAdapter, this); //bind this adapter for click events
        }


        if(episodeList == null || episodeList.size() == 0) {
            //the first time the list will be empty and we need to load it
            if(!season.isLoading()) {
                season.setLoading(true);
                ;//Log.d("liveTV","Load this season " + position);
                needsRedraw = false;
                NetManager.getInstance().retrieveEpisodesForSerie(mSerie, season, this);
            }
        }
        if (needsRedraw){
            CustomLayoutManager layoutManager = new CustomLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false, Screen.GetScreenWidth(), Screen.GetScreenWidth() / 4);

            RecyclerView rv = ((RecyclerView) holder.getViewBinding().getRoot().findViewById(R.id.movies_recyclerview));

            holder.getViewBinding().getRoot().findViewById(R.id.progressBar).setVisibility(View.GONE);
            rv.setVisibility(View.VISIBLE);

            rv.setLayoutManager(layoutManager);
            //rv.setHasFixedSize(true);

            MoviesRecyclerAdapter rowsRecyclerAdapter = new MoviesRecyclerAdapter(rv, mContext, episodeList, position, this, false, true);//last true is to show title of caps
            rowsRecyclerAdapter.setTreatAsBox(treatAsBox);
            rv.setAdapter(rowsRecyclerAdapter);

//            //clicks
//            holder.getViewBinding().getRoot().findViewById(R.id.rv_item_header).setOnFocusChangeListener(this);
//            holder.getViewBinding().getRoot().findViewById(R.id.rv_item_header).setTag(position);
//            holder.getViewBinding().setVariable(BR.seasonAdapter, this); //bind this adapter for click events

            holder.getViewBinding().executePendingBindings();

//            LinearSnapHelper snapHelper = new LinearSnapHelper();
//            snapHelper.attachToRecyclerView(rv);
            rv.setOnFlingListener(null);
           // layoutManager.scrollToPosition(lastSelectedItemByRow[position]);
        }
    }

    @Override
    public void onEpisodesForSerieCompleted(Season season) {

        season.setLoading(false);

        ;//Log.d("liveTV","Adding Season " + season.getPosition() + " in position " + season.getPosition());
//        VideoStreamManager.getInstance().getSerie(mSeriePosition).setSeason(season.getPosition(), season);
////        VideoStreamManager.getInstance().getMainCategory(mMainCategoryPosition).addMovieCategory(movieCategory.getPosition(), movieCategory);

        this.notifyItemChanged(season.getPosition());
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
        return mSeasonsList.size();
    }

    @Override
    public void onMovieSelected(int rowPosition, int itemPosition) {//, View child) {
        if(treatAsBox && itemPosition == -1) { //only in landscape
            mShowAsGridListener.onShowAsGridSelected(rowPosition);
        }
        else {
            currentSelectedRow = rowPosition;
            lastSelectedItemByRow[rowPosition] = itemPosition;

            mMovieSelectedListener.onMovieSelected(rowPosition, itemPosition);

            ;//Log.d("liveTV", "Movie Selected is " + itemPosition);
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        ;//Log.d("liveTV","aaaaa focus changed" + v.getTag());
        if(hasFocus) {
            v.findViewById(R.id.show_as_grid).setSelected(true);
        }
        else {
            v.findViewById(R.id.show_as_grid).setSelected(false);
        }
    }
}
