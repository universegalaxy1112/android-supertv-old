package com.livetv.normal.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Serie extends Movie {
    private int movieCategoryIdOwner = -1;
    private String seasonCountText;
    private List<Season> seasons = new ArrayList();

    public Serie() {
        setPosition(-1);
    }

    public void setSeasons(List<Season> seasons2) {
        this.seasons = seasons2;
    }

    public List<Season> getSeasons() {
        return this.seasons;
    }

    public int getSeasonCount() {
        return this.seasons.size();
    }

    public void addSeason(int position, Season season) {
        season.setPosition(position);
        this.seasons.add(season);
    }

    public void setSeason(int position, Season season) {
        this.seasons.set(position, season);
    }

    public Season getSeason(int season) {
        return (Season) this.seasons.get(season);
    }

    public String getSeasonCountText() {
        return this.seasonCountText;
    }

    public void setSeasonCountText(String seasonCountText2) {
        this.seasonCountText = seasonCountText2;
    }

    public int getMovieCategoryIdOwner() {
        return this.movieCategoryIdOwner;
    }

    public void setMovieCategoryIdOwner(int movieCategoryIdOwner2) {
        this.movieCategoryIdOwner = movieCategoryIdOwner2;
    }

    public Set<VideoStream> searchForMovies(String searchString) {
        Set<VideoStream> searchList = new HashSet<>();
        for (Season season : this.seasons) {
            Set<VideoStream> tmpList = season.searchForMovies(searchString);
            if (!(tmpList == null || tmpList.size() == 0)) {
                searchList.addAll(tmpList);
            }
        }
        return searchList;
    }
}
