package com.livetv.normal.listeners;

import com.livetv.normal.model.Serie;

public interface LoadSeasonsForSerieResponseListener extends BaseResponseListener {
    void onSeasonsLoaded(Serie serie, int i);

    void onSeasonsLoadedError();
}
