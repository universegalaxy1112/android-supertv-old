package com.livetv.normal.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Season {
    private List<? extends VideoStream> episodeList = new ArrayList();
    private boolean isLoading = false;
    private String name;
    private int position = -1;

    public List<? extends VideoStream> getEpisodeList() {
        return this.episodeList;
    }

    public VideoStream getEpisode(int position2) {
        return (VideoStream) this.episodeList.get(position2);
    }

    public void setEpisodeList(List<? extends VideoStream> list) {
        this.episodeList = list;
    }

    public boolean isLoading() {
        return this.isLoading;
    }

    public void setLoading(boolean loading) {
        this.isLoading = loading;
    }

    public int getPosition() {
        return this.position;
    }

    public void setPosition(int position2) {
        this.position = position2;
        setName("Temporada " + (position2 + 1));
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name2) {
        this.name = name2;
    }

    public Set<VideoStream> searchForMovies(String searchString) {
        Set<VideoStream> searchList = new HashSet<>();
        for (VideoStream videoStream : this.episodeList) {
            if (videoStream.contains(searchString)) {
                searchList.add(videoStream);
            }
        }
        return searchList;
    }
}
