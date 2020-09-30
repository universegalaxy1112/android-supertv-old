package com.livetv.normal.listeners;

import android.support.v17.leanback.widget.Presenter.ViewHolder;
import com.livetv.normal.model.ImageResponse;

public interface ImageLoadedListener {
    void onLoaded(ImageResponse imageResponse);

    void onLoaded2(ImageResponse imageResponse, ViewHolder viewHolder);
}
