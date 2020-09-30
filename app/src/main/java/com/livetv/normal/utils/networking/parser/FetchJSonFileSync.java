package com.livetv.normal.utils.networking.parser;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.livetv.normal.model.LiveProgram;
import com.livetv.normal.model.LiveTVCategory;
import com.livetv.normal.model.MainCategory;
import com.livetv.normal.model.ModelTypes;
import com.livetv.normal.model.MovieCategory;
import com.livetv.normal.model.Serie;
import com.livetv.normal.model.User;
import com.livetv.normal.model.VideoStream;
import com.livetv.normal.utils.DataManager;
import com.livetv.normal.utils.Device;
import com.livetv.normal.utils.networking.NetManager;
import com.livetv.normal.utils.networking.WebConfig;
import com.livetv.normal.utils.networking.services.HttpRequest;
import java.io.BufferedReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONException;

public class FetchJSonFileSync {
    final String LOG_TAG = getClass().getSimpleName();
    private BufferedReader reader = null;
    private HttpURLConnection urlConnection = null;

    public List<MovieCategory> retrieveSubCategories(MainCategory mainCategory) {
        try {
            String dataFromServer = NetManager.getInstance().makeSyncStringRequest(getSubCategoriesUrl(mainCategory));
            if (!Device.canTreatAsBox() && dataFromServer.contains("\"Settings\",")) {
                dataFromServer = dataFromServer.replace("\"Settings\",", "");
            }
            return ParserJSonFile.getParsedSubCategories(dataFromServer);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<? extends VideoStream> retrieveMovies(String mainCategory, String movieCategory, int timeOut) {
        try {
            String dataFromServer = "";

            /*if(movieCategory.toLowerCase().contains("vistas") && movieCategory.toLowerCase().contains("recientes")) {
                String recentMovies = "";
                switch(mainCategory) {//main category
                    case ModelTypes.MOVIE_CATEGORIES:
                        recentMovies = DataManager.getInstance().getString("recentMovies","");
                        break;
                    case ModelTypes.ENTERTAINMENT_CATEGORIES:
                        recentMovies = DataManager.getInstance().getString("recentEntertainment","");
                        break;
                    case ModelTypes.SERIES_CATEGORIES:
                        recentMovies = DataManager.getInstance().getString("recentSeries","");
                        break;
                    case ModelTypes.SERIES_KIDS_CATEGORIES:
                        recentMovies = DataManager.getInstance().getString("recentKids","");
                        break;
                    case ModelTypes.KARAOKE_CATEGORIES:
                        recentMovies = DataManager.getInstance().getString("recentKara","");
                        break;
                }

                if(!TextUtils.isEmpty(recentMovies)) {
                    dataFromServer = "{\"Videos\": "+recentMovies + "}";
                }
                else {
                    return new ArrayList<>();
                }
            }else*/
            if (movieCategory.toLowerCase().contains("favorite")) {
                String favoriteMovies = "";
                switch (mainCategory) {//main category
                    case ModelTypes.MOVIE_CATEGORIES:
                        favoriteMovies = DataManager.getInstance().getString("favoriteMovies", "");
                        break;
                    case ModelTypes.ENTERTAINMENT_CATEGORIES:
                        favoriteMovies = DataManager.getInstance().getString("favoriteEntertainment", "");
                        break;
                    case ModelTypes.SERIES_CATEGORIES:
                        favoriteMovies = DataManager.getInstance().getString("favoriteSerie", "");
                        break;
                    case ModelTypes.SERIES_KIDS_CATEGORIES:
                        favoriteMovies = DataManager.getInstance().getString("favoriteKids", "");
                        break;
                    case ModelTypes.KARAOKE_CATEGORIES:
                        favoriteMovies = DataManager.getInstance().getString("favoriteKara", "");
                        break;
                }

                if (!TextUtils.isEmpty(favoriteMovies)) {
                    dataFromServer = "{\"Videos\": " + favoriteMovies + "}";
                } else {
                    return new ArrayList<>();
                }
            } else {
                String moviesForCatURL = getMoviesForCategoryUrl(mainCategory, movieCategory);
                dataFromServer = NetManager.getInstance().makeSyncStringRequest(moviesForCatURL, timeOut);
            }
            if (dataFromServer != null)
                return ParserJSonFile.getParsedMovies(mainCategory, movieCategory, dataFromServer);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<? extends VideoStream> retrieveSearchMovies(MainCategory mainCategory, String pattern, int timeOut) {
        int type = -1;
        try {
            String modelType = mainCategory.getModelType();
            switch (modelType) {
                case ModelTypes.MOVIE_CATEGORIES:
                    type = 1;
                    break;
                case ModelTypes.SERIES_CATEGORIES:
                    type = 2;
                    break;
                case ModelTypes.SERIES_KIDS_CATEGORIES:
                    type = 3;
                    break;
                case ModelTypes.EVENTS_CATEGORIES:
                    type = 4;
                    break;
                case ModelTypes.ADULTS_CATEGORIES:
                    type = 5;
                    break;
            }
            return ParserJSonFile.getParsedMovies(mainCategory.getModelType(), "", NetManager.getInstance().makeSearchStringRequest(WebConfig.videoSearchURL.replace("{TYPE}", "" + type).replace("{PATTERN}", pattern), timeOut));
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<? extends VideoStream> retrieveMoviesForSerie(Serie serie, int season) {
        try {
            return ParserJSonFile.getParsedMoviesForSerie(serie, NetManager.getInstance().makeSyncStringRequest(getMoviesForSerieUrl(serie, season)));
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<LiveTVCategory> retrieveLiveTVCategories(MainCategory mainCategory) {
        try {
            return ParserJSonFile.getParsedLiveTVCategories(HttpRequest.getInstance().performRequest(getLiveTVCategoriesUrl(mainCategory)));
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<LiveProgram> retrieveProgramsForLiveTVCategory(LiveTVCategory liveTVCategory) {
        try {
            return ParserJSonFile.getParsedProgramsForLiveTVCategory(liveTVCategory, NetManager.getInstance().makeSyncStringRequest(getProgramsForLiveTVCategoryUrl(liveTVCategory)));
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String getLiveTVCategoriesUrl(MainCategory mainCategory) {
        return WebConfig.liveTVCategoriesURL;
    }

    private String getProgramsForLiveTVCategoryUrl(LiveTVCategory liveTVCategory) {
        return WebConfig.liveTVChannelsURL.replace("{CAT_ID}", liveTVCategory.getId() + "&s=" + DataManager.getInstance().getString("dealerCode", ""));
    }

    private String getSubCategoriesUrl(MainCategory mainCategory) {
        String tmpURL = "/categorias.php";
        try {
            tmpURL = tmpURL + "?t=" + URLEncoder.encode(mainCategory.getModelType(), "utf-8");
        } catch (UnsupportedEncodingException e) {
        }
        return WebConfig.baseURL + tmpURL;
    }

    private String getMoviesForCategoryUrl(String mainCategory, String movieCategory) {
        String tmpURL = "";
        try {
            String mainCategoryEncoded = URLEncoder.encode(movieCategory, "utf-8");
            char c = 65535;
            switch (mainCategory.hashCode()) {
                case -1376501250:
                    if (mainCategory.equals(ModelTypes.EVENTS_CATEGORIES)) {
                        c = 3;
                        break;
                    }
                    break;
                case -1132156642:
                    if (mainCategory.equals(ModelTypes.ADULTS_CATEGORIES)) {
                        c = 4;
                        break;
                    }
                    break;
                case -1102433170:
                    if (mainCategory.equals(ModelTypes.LIVE_TV_CATEGORIES)) {
                        c = 5;
                        break;
                    }
                    break;
                case -1062807844:
                    if (mainCategory.equals(ModelTypes.MUSIC_CATEGORIES)) {
                        c = 7;
                        break;
                    }
                    break;
                case -936045084:
                    if (mainCategory.equals(ModelTypes.KARAOKE_CATEGORIES)) {
                        c = 6;
                        break;
                    }
                    break;
                case -905838985:
                    if (mainCategory.equals(ModelTypes.SERIES_CATEGORIES)) {
                        c = 1;
                        break;
                    }
                    break;
                case -476961436:
                    if (mainCategory.equals(ModelTypes.SERIES_KIDS_CATEGORIES)) {
                        c = 2;
                        break;
                    }
                    break;
                case -474020614:
                    if (mainCategory.equals(ModelTypes.MOVIE_CATEGORIES)) {
                        c = 0;
                        break;
                    }
                    break;
                case 500006792:
                    if (mainCategory.equals(ModelTypes.ENTERTAINMENT_CATEGORIES)) {
                        c = 8;
                        break;
                    }
                    break;
            }
            switch (c) {
                case 0:
                    tmpURL = "/movies.php?CATEGORY=" + mainCategoryEncoded;
                    break;
                case 1:
                    tmpURL = "/series.php?cat=" + mainCategoryEncoded + "&tipo=2";
                    break;
                case 2:
                    tmpURL = "/series.php?cat=" + mainCategoryEncoded + "&tipo=3";
                    break;
                case 3:
                    tmpURL = "/eventos.php?cat=" + mainCategoryEncoded;
                    break;
                case 4:
                    tmpURL = "/adultos.php?cat=" + mainCategoryEncoded;
                    break;
                case 6:
                    tmpURL = "/karaoke.php?cat=" + mainCategoryEncoded;
                    break;
                case 8:
                    tmpURL = "/entertainment.php?cat=" + mainCategoryEncoded;
                    break;
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String theUser = DataManager.getInstance().getString("theUser", "");
        String cve = "";
        if (!TextUtils.isEmpty(theUser)) {
            cve  = ((User) new Gson().fromJson(theUser, User.class)).getName();
        }

        return WebConfig.baseURL + tmpURL + "&s=" + DataManager.getInstance().getString("dealerCode", "") + "&cve=" + cve;
    }

    private String getMoviesForSerieUrl(Serie serie, int season) {
        return WebConfig.baseURL + ("/capitulos_temporada.php" + "?cve=" + serie.getContentId() + "&temporada=Temporada%20" + season);
    }
}
