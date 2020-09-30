package com.livetv.normal.view.custom_views;

import android.support.v17.leanback.widget.HeaderItem;
import com.livetv.normal.model.LiveProgram;

public class LiveProgramHeaderItem extends HeaderItem {
    private static final String TAG = LiveProgramHeaderItem.class.getSimpleName();
    private LiveProgram liveProgram;

    public LiveProgramHeaderItem(LiveProgram liveProgram2) {
        super((long) liveProgram2.getContentId(), liveProgram2.getTitle());
        this.liveProgram = liveProgram2;
    }

    public LiveProgram getLiveProgram() {
        return this.liveProgram;
    }
}
