package com.livetv.normal.model;

public interface ModelTypes {
    public static final String ADULTS_CATEGORIES = "adultos";
    public static final String ENTERTAINMENT_CATEGORIES = "entertainment";
    public static final String EVENTS_CATEGORIES = "eventos";
    public static final String KARAOKE_CATEGORIES = "karaoke";
    public static final String LIVE_TV_CATEGORIES = "livetv";
    public static final String MOVIE_CATEGORIES = "peliculas";
    public static final String MUSIC_CATEGORIES = "musica";
    public static final String SERIES_CATEGORIES = "series";
    public static final String SERIES_KIDS_CATEGORIES = "serieskids";
    public static final String SETTINGS = "settings";

    public enum SelectedType {
        MAIN_CATEGORY,
        SERIES,
        SEASONS,
        MOVIES
    }
}
