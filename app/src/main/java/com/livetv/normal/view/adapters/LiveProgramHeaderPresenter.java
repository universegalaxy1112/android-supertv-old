package com.livetv.normal.view.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v17.leanback.widget.Presenter;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.livetv.normal.R;
import com.livetv.normal.model.LiveProgram;

import static com.livetv.normal.BR.liveProgramItem;

public class LiveProgramHeaderPresenter extends Presenter {
    private Context mContext;

    public LiveProgramHeaderPresenter(Context context) {
        this.mContext = context;
    }

    public ViewHolder onCreateViewHolder(ViewGroup viewGroup) {
        return new LiveProgramHeaderViewHolder(((LayoutInflater) this.mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.live_program_item_tv, viewGroup, false));
    }

    public void onBindViewHolder(ViewHolder viewHolder, Object o) {
        ((LiveProgramHeaderViewHolder) viewHolder).getViewBinding().setVariable(liveProgramItem, (LiveProgram) o);
        ((LiveProgramHeaderViewHolder) viewHolder).getViewBinding().executePendingBindings();
    }

    public void onUnbindViewHolder(ViewHolder viewHolder) {

    }
}
