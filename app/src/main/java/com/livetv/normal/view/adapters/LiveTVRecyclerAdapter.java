package com.livetv.normal.view.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.databinding.library.baseAdapters.BR;
import com.livetv.normal.LiveTvApplication;
import com.livetv.normal.R;
import com.livetv.normal.listeners.LiveProgramSelectedListener;
import com.livetv.normal.model.LiveProgram;

import java.util.List;

public class LiveTVRecyclerAdapter extends RecyclerView.Adapter<LiveTVRecyclerViewHolder> implements View.OnFocusChangeListener{

    private final LiveProgramSelectedListener liveProgramSelectedListener;
    private List<LiveProgram> livePrograms;
    private Context mContext;

    public LiveTVRecyclerAdapter(Context context, List<LiveProgram> objects, LiveProgramSelectedListener listener) {//}, int rowPosition, MovieSelectedListener movieSelectedListener, boolean fromGrid) {
//    public MoviesRecyclerAdapter(Context context, List<Movie> objects, int rowPosition, MovieSelectedListener movieSelectedListener, MovieAcceptedListener movieAcceptedListener) {
        mContext = context;
        livePrograms = objects;
        liveProgramSelectedListener = listener;
    }


    public void updateChannels(List<LiveProgram> programs) {
        livePrograms = programs;
        notifyDataSetChanged();
    }

    @Override
    public LiveTVRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View convertView;
        convertView = inflater.inflate(R.layout.live_program_item, parent, false);
        return new LiveTVRecyclerViewHolder(convertView);
    }

    @Override
    public void onBindViewHolder(LiveTVRecyclerViewHolder holder, int position) {
        LiveProgram liveProgram = livePrograms.get(position);

        if(position % 2 == 0) {
            (holder.getViewBinding().getRoot().findViewById(R.id.program_one)).setBackgroundColor(ContextCompat.getColor(LiveTvApplication.getAppContext(), R.color.colorBackground));
            (holder.getViewBinding().getRoot().findViewById(R.id.program_two)).setBackgroundColor(ContextCompat.getColor(LiveTvApplication.getAppContext(), R.color.colorBackground));
        }
        else {
            (holder.getViewBinding().getRoot().findViewById(R.id.program_one)).setBackgroundColor(ContextCompat.getColor(LiveTvApplication.getAppContext(), R.color.colorBackgroundLight));
            (holder.getViewBinding().getRoot().findViewById(R.id.program_two)).setBackgroundColor(ContextCompat.getColor(LiveTvApplication.getAppContext(), R.color.colorBackgroundLight));
        }

        holder.getViewBinding().setVariable(BR.liveProgramItem, liveProgram); // bind item with Movie model
        holder.getViewBinding().getRoot().setTag(position);
        holder.getViewBinding().getRoot().findViewById(R.id.livetv_program).setTag(position);
        holder.getViewBinding().setVariable(BR.liveTVAdapter, this); //bind this adapter for click events

        holder.getViewBinding().getRoot().findViewById(R.id.livetv_program).setOnFocusChangeListener(this);

        holder.getViewBinding().executePendingBindings();

    }

    @Override
    public int getItemCount() {
        return livePrograms.size();
    }

//    @Override
    public void onClickItem(View view) {
        liveProgramSelectedListener.onLiveProgramSelected(livePrograms.get((Integer) view.getTag()), (Integer) view.getTag());
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if(hasFocus) {
            v.setSelected(true);
            v.findViewById(R.id.program_one).setBackgroundColor(ContextCompat.getColor(LiveTvApplication.getAppContext(), R.color.colorPrimaryDark));
            v.findViewById(R.id.program_two).setBackgroundColor(ContextCompat.getColor(LiveTvApplication.getAppContext(), R.color.colorPrimaryDark));
        }
        else {
            v.setSelected(false);
            int position = (Integer) v.getTag();
            if(position % 2 == 0) {
                v.findViewById(R.id.program_one).setBackgroundColor(ContextCompat.getColor(LiveTvApplication.getAppContext(), R.color.colorBackground));
                v.findViewById(R.id.program_two).setBackgroundColor(ContextCompat.getColor(LiveTvApplication.getAppContext(), R.color.colorBackground));
            }
            else {
                v.findViewById(R.id.program_one).setBackgroundColor(ContextCompat.getColor(LiveTvApplication.getAppContext(), R.color.colorBackgroundLight));
                v.findViewById(R.id.program_two).setBackgroundColor(ContextCompat.getColor(LiveTvApplication.getAppContext(), R.color.colorBackgroundLight));
            }
        }
    }
}
