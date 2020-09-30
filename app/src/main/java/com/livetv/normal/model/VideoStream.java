package com.livetv.normal.model;

import android.databinding.BaseObservable;

public abstract class VideoStream extends BaseObservable {
    private int ContentId;
    private String ContentType;
    private boolean HDBranded;
    private String StreamUrl;
    private String Title;
    private boolean favorite;
    private int position;
    private String searchTitle;
    private boolean seen;
    private int tipo = -1;

    public String getSearchTitle() {
        return this.searchTitle;
    }

    public void setSearchTitle(String searchTitle2) {
        this.searchTitle = searchTitle2;
    }

    public int getContentId() {
        return this.ContentId;
    }

    public void setContentId(int contentId) {
        this.ContentId = contentId;
    }

    public String getContentType() {
        return this.ContentType;
    }

    public void setContentType(String contentType) {
        this.ContentType = contentType;
    }

    public String getTitle() {
        return this.Title == null ? "" : this.Title.trim();
    }

    public void setTitle(String title) {
        this.Title = title == null ? "" : title.trim();
    }

    public String getStreamUrl() {
        return this.StreamUrl;
    }

    public void setStreamUrl(String streamUrl) {
        this.StreamUrl = streamUrl;
    }

    public int getPosition() {
        return this.position;
    }

    public void setPosition(int position2) {
        this.position = position2;
    }

    public boolean contains(String searchString) {
        return getTitle().toLowerCase().contains(searchString.toLowerCase());
    }

    public boolean isHDBranded() {
        return this.HDBranded;
    }

    public void setHDBranded(boolean HDBranded2) {
        this.HDBranded = HDBranded2;
    }

    public boolean isSeen() {
        return this.seen;
    }

    public void setSeen(boolean seen2) {
        this.seen = seen2;
    }

    public boolean isFavorite() {
        return this.favorite;
    }

    public void setFavorite(boolean favorite2) {
        this.favorite = favorite2;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (getContentId() != ((VideoStream) o).getContentId()) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        return this.ContentId;
    }

    public int getCategoryType() {
        return tipo;
    }

    public void setCategoryType(int categoryType) {
        this.tipo = categoryType;
    }
}
