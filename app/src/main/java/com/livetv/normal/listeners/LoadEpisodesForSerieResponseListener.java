package com.livetv.normal.listeners;

import com.livetv.normal.model.Season;

public interface LoadEpisodesForSerieResponseListener extends BaseResponseListener {
    void onEpisodesForSerieCompleted(Season season);
}
