package com.livetv.normal.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MainCategory extends BaseCategory {
    private int catImageId;
    private String modelType;
    private List<MovieCategory> movieCategories = new ArrayList();

    public String getModelType() {
        return this.modelType;
    }

    public void setModelType(String modelType2) {
        this.modelType = modelType2;
    }

    public int getCatImageId() {
        return this.catImageId;
    }

    public void setCatImageId(int catImageId2) {
        this.catImageId = catImageId2;
    }

    public void setMovieCategories(List<MovieCategory> movieCategories2) {
        this.movieCategories = movieCategories2;
    }

    public void addMovieCategory(int position, MovieCategory movieCategory) {
        this.movieCategories.set(position, movieCategory);
    }

    public List<MovieCategory> getMovieCategories() {
        return this.movieCategories;
    }

    public MovieCategory getMovieCategory(int position) {
        return (MovieCategory) this.movieCategories.get(position);
    }

    public Set<VideoStream> searchForMovies(String searchString, boolean searchSerie) {
        Set<VideoStream> searchList = new HashSet<>();
        for (MovieCategory movieCategory : this.movieCategories) {
            Set<VideoStream> tmpList = movieCategory.searchForMovies(searchString, searchSerie);
            if (!(tmpList == null || tmpList.size() == 0)) {
                searchList.addAll(tmpList);
            }
        }
        return searchList;
    }
}
