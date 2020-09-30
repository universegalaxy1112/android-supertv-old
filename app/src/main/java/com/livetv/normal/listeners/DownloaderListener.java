package com.livetv.normal.listeners;

public interface DownloaderListener {
    void onDownloadComplete(String str);

    void onDownloadError(int i);
}
