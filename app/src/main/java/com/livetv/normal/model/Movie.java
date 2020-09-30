package com.livetv.normal.model;

import android.text.TextUtils;

public class Movie extends VideoStream {
    private String Actors;
    private String Categories;
    private String Description;
    private String Director;
    private String HDBifUrl;
    private String HDFondoUrl;
    private String HDPosterUrl;
    private int Length;
    private String Rating;
    private String ReleaseDate;
    private String SDBifUrl;
    private String SDPosterUrl;
    private int StarRating;
    private int StreamBitrates;
    private String StreamFormat;
    private String StreamQualities;
    private String SubtitleUrl;
    private boolean Watched;
    private boolean fullHD;
    private boolean isHD;
    private String searchActors;

    public String getHDFondoUrl() {
        return this.HDFondoUrl;
    }

    public void setHDFondoUrl(String HDFondoUrl2) {
        this.HDFondoUrl = HDFondoUrl2;
    }

    public String toString() {
        return "Movie{Description='" + this.Description + '\'' + ", Watched=" + this.Watched + ", Length=" + this.Length + ", Rating='" + this.Rating + '\'' + ", StarRating=" + this.StarRating + ", Categories='" + this.Categories + '\'' + ", Director='" + this.Director + '\'' + ", Actors='" + this.Actors + '\'' + ", ReleaseDate='" + this.ReleaseDate + '\'' + ", StreamFormat='" + this.StreamFormat + '\'' + ", StreamBitrates=" + this.StreamBitrates + ", SDBifUrl='" + this.SDBifUrl + '\'' + ", HDBifUrl='" + this.HDBifUrl + '\'' + ", SDPosterUrl='" + this.SDPosterUrl + '\'' + ", HDPosterUrl='" + this.HDPosterUrl + '\'' + ", HDFondoUrl='" + this.HDFondoUrl + '\'' + ", StreamQualities='" + this.StreamQualities + '\'' + ", isHD=" + this.isHD + ", fullHD=" + this.fullHD + ", SubtitleUrl='" + this.SubtitleUrl + '\'' + ", searchActors='" + this.searchActors + '\'' + '}';
    }

    public String getSearchActors() {
        return this.searchActors;
    }

    public void setSearchActors(String searchActors2) {
        this.searchActors = searchActors2;
    }

    public String getDescription() {
        return this.Description;
    }

    public void setDescription(String description) {
        this.Description = description;
    }

    public boolean isWatched() {
        return this.Watched;
    }

    public void setWatched(boolean watched) {
        this.Watched = watched;
    }

    public int getLength() {
        return this.Length;
    }

    public void setLength(int length) {
        this.Length = length;
    }

    public String getRating() {
        return this.Rating;
    }

    public void setRating(String rating) {
        this.Rating = rating;
    }

    public int getStarRating() {
        return this.StarRating;
    }

    public void setStarRating(int satrtRating) {
        this.StarRating = satrtRating;
    }

    public String getCategories() {
        return this.Categories;
    }

    public void setCategories(String categories) {
        this.Categories = categories;
    }

    public String getDirector() {
        return this.Director == null ? "" : this.Director;
    }

    public void setDirector(String director) {
        this.Director = director;
    }

    public String getActors() {
        return this.Actors;
    }

    public void setActors(String actors) {
        this.Actors = actors;
    }

    public String getReleaseDate() {
        return this.ReleaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.ReleaseDate = releaseDate;
    }

    public String getStreamFormat() {
        return this.StreamFormat;
    }

    public void setStreamFormat(String streamFormat) {
        this.StreamFormat = streamFormat;
    }

    public int getStreamBitrates() {
        return this.StreamBitrates;
    }

    public void setStreamBitrates(int streamBitrates) {
        this.StreamBitrates = streamBitrates;
    }

    public String getSDBifUrl() {
        return this.SDBifUrl;
    }

    public void setSDBifUrl(String SDBifUrl2) {
        this.SDBifUrl = SDBifUrl2;
    }

    public String getHDBifUrl() {
        return this.HDBifUrl;
    }

    public void setHDBifUrl(String HDBifUrl2) {
        this.HDBifUrl = HDBifUrl2;
    }

    public String getSDPosterUrl() {
        return this.SDPosterUrl;
    }

    public void setSDPosterUrl(String SDPosterUrl2) {
        this.SDPosterUrl = SDPosterUrl2;
    }

    public String getHDPosterUrl() {
        return this.HDPosterUrl;
    }

    public void setHDPosterUrl(String HDPosterUrl2) {
        this.HDPosterUrl = HDPosterUrl2;
    }

    public String getStreamQualities() {
        return this.StreamQualities;
    }

    public void setStreamQualities(String streamQualities) {
        this.StreamQualities = streamQualities;
    }

    public boolean isHD() {
        return this.isHD;
    }

    public void setHD(boolean HD) {
        this.isHD = HD;
    }

    public boolean isFullHD() {
        return this.fullHD;
    }

    public void setFullHD(boolean fullHD2) {
        this.fullHD = fullHD2;
    }

    public String getSubtitleUrl() {
        return this.SubtitleUrl;
    }

    public void setSubtitleUrl(String subtitleUrl) {
        this.SubtitleUrl = subtitleUrl;
    }

    public boolean contains(String searchString) {
        boolean z = false;
        if (!TextUtils.isEmpty(getActors())) {
            if ((getSearchTitle() != null && getSearchTitle().toLowerCase().contains(searchString.toLowerCase())) || (getSearchActors() != null && getSearchActors().toLowerCase().contains(searchString.toLowerCase()))) {
                z = true;
            }
            return z;
        } else if (getSearchTitle() == null || !getSearchTitle().toLowerCase().contains(searchString.toLowerCase())) {
            return false;
        } else {
            return true;
        }
    }
}
