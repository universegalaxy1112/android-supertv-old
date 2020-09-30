package com.livetv.normal.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MovieCategory extends BaseCategory {
    private boolean hasErrorLoading = false;
    private boolean isLoaded = false;
    private boolean isLoading = false;
    private List<? extends VideoStream> movieList = new ArrayList();

    public List<? extends VideoStream> getMovieList() {
        return this.movieList;
    }

    public VideoStream getMovie(int position) {
        return (VideoStream) this.movieList.get(position);
    }

    public void setMovieList(List<? extends VideoStream> list) {
        this.movieList = list;
    }

    public boolean isLoading() {
        return this.isLoading;
    }

    public void setLoading(boolean loading) {
        this.isLoading = loading;
    }

    public boolean isLoaded() {
        return this.isLoaded;
    }

    public void setLoaded(boolean loaded) {
        this.isLoaded = loaded;
    }

    public boolean hasErrorLoading() {
        return this.hasErrorLoading;
    }

    public void setErrorLoading(boolean errorLoading) {
        this.hasErrorLoading = errorLoading;
    }

    public Set<VideoStream> searchForMovies(String searchString, boolean searchSerie) {
        Set<VideoStream> searchList = new HashSet<>();
        for (VideoStream videoStream : this.movieList) {
            if (videoStream instanceof Serie) {
                if (!searchSerie) {
                    Set<VideoStream> tmpList = ((Serie) videoStream).searchForMovies(searchString);
                    if (!(tmpList == null || tmpList.size() == 0)) {
                        searchList.addAll(tmpList);
                    }
                } else if (((Serie) videoStream).getSearchTitle() != null && ((Serie) videoStream).getSearchTitle().toLowerCase().contains(searchString)) {
                    searchList.add(videoStream);
                }
            } else if (((Movie) videoStream).contains(searchString)) {
                searchList.add(videoStream);
            }
        }
        return searchList;
    }
}
