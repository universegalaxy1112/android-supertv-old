package com.livetv.normal.utils.networking.parser;

import android.text.TextUtils;
import com.livetv.normal.managers.VideoStreamManager;
import com.livetv.normal.model.Episode;
import com.livetv.normal.model.LiveProgram;
import com.livetv.normal.model.LiveTVCategory;
import com.livetv.normal.model.ModelTypes;
import com.livetv.normal.model.Movie;
import com.livetv.normal.model.MovieCategory;
import com.livetv.normal.model.Serie;
import com.livetv.normal.model.Setting;
import com.livetv.normal.model.VideoStream;
import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ParserJSonFile {
    static final String LOG_TAG = "ParserJsonFile";
    private static Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");

    public static List<MovieCategory> getParsedSubCategories(String data) throws JSONException {
        List<MovieCategory> dataArray = new ArrayList<>();
        JSONArray videoArray = new JSONArray(data);
        for (int i = 0; i < videoArray.length(); i++) {
            if (!videoArray.getString(i).toLowerCase().contains("4_k")) {
                MovieCategory movieCat = new MovieCategory();
                movieCat.setCatName(videoArray.getString(i));
                movieCat.setId(i);
                dataArray.add(movieCat);
            }
        }
        return dataArray;
    }

    public static List<LiveTVCategory> getParsedLiveTVCategories(String data) throws JSONException {
        if (TextUtils.isEmpty(data)) {
            return null;
        }
        List<LiveTVCategory> dataArray = new ArrayList<>();
        JSONArray videoArray = new JSONArray(data);
        for (int i = 0; i < videoArray.length(); i++) {
            LiveTVCategory liveTvCat = new LiveTVCategory();
            JSONObject tmpObj = videoArray.getJSONObject(i);
            liveTvCat.setId(Integer.parseInt(tmpObj.getString("cve")));
            liveTvCat.setCatName(tmpObj.getString("nombre"));
            liveTvCat.setTotalChannels(Integer.parseInt(tmpObj.getString("total_canales")));
            liveTvCat.setPosition(i);
            dataArray.add(liveTvCat);
        }
        return dataArray;
    }

    public static List<LiveProgram> getParsedProgramsForLiveTVCategory(LiveTVCategory liveTVCategory, String data) throws JSONException {
        List<LiveProgram> dataArray = new ArrayList<>();
        JSONArray videoArray = new JSONArray(data);
        for (int i = 0; i < videoArray.length(); i++) {
            LiveProgram liveProgram = new LiveProgram();
            fillObject(liveProgram, videoArray.getJSONObject(i));
            liveProgram.setPosition(i);
            dataArray.add(liveProgram);
        }
        return dataArray;
    }

    public static List<? extends VideoStream> getParsedMoviesForSerie(Serie serie, String data) throws JSONException {
        String JSON_ARRAY_VAR = "";
        List<VideoStream> dataArray = new ArrayList<>();

        JSONObject videosJson = new JSONObject(data);
        JSONArray videoArray = null;
        JSON_ARRAY_VAR = "Capitulos";
        videoArray = videosJson.getJSONArray(JSON_ARRAY_VAR);

        dataArray = new ArrayList<>();

        VideoStream movie;
        for (int i = 0; i < videoArray.length(); i++) {

            movie = new Episode();
            movie.setPosition(i);
            fillObject(movie,videoArray.getJSONObject(i));
            dataArray.add(movie);
        }
        return dataArray;
    }

    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static List<? extends VideoStream> getParsedMovies(String mainCategory, String movieCategory, String data) throws JSONException,NullPointerException {

        String JSON_ARRAY_VAR = "";
        List<VideoStream> dataArray = new ArrayList<>();
        JSONObject videosJson = new JSONObject(data);
        JSONArray videoArray = null;
        JSON_ARRAY_VAR = "Videos";
        videoArray = videosJson.getJSONArray(JSON_ARRAY_VAR);

        dataArray = new ArrayList<>();
        VideoStream movie = null;
        for (int i = 0; i < videoArray.length(); i++) {

            switch (mainCategory) {
                case ModelTypes.MOVIE_CATEGORIES:
                case ModelTypes.ENTERTAINMENT_CATEGORIES:
                case ModelTypes.EVENTS_CATEGORIES:
                case ModelTypes.ADULTS_CATEGORIES:
                    movie = new Movie();
                    break;
                case ModelTypes.SERIES_CATEGORIES:
                case ModelTypes.SERIES_KIDS_CATEGORIES:
                case ModelTypes.KARAOKE_CATEGORIES:
                    movie = new Serie();
                    break;
                default:
                    movie = new Movie();
                    break;
                //case ModelTypes.LIVE_TV_CATEGORIES:
            }
            if(movieCategory.contains("Top Movies"))
                movie = new Movie();
            else if(movieCategory.contains("Top Series"))
                movie = new Serie();
            else if(movieCategory.equals("Infantil")) {
                movie = new Movie();
            }
            movie.setPosition(i);
            fillObject(movie, videoArray.getJSONObject(i));
            dataArray.add(movie);
        }


        return dataArray;

    }

    public static String removeSpecialChars(String s) {
        return pattern.matcher(Normalizer.normalize(s, Form.NFD)).replaceAll("");
    }

    public static void fillObject(VideoStream obj, JSONObject json_obj) {
        try {
            if (json_obj.has("ContentId")) {
                obj.setContentId(Integer.parseInt(json_obj.getString("ContentId")));
            }
            if (json_obj.has("ContentType")) {
                obj.setContentType(json_obj.getString("ContentType"));
            }
            if (json_obj.has("Title")) {
                obj.setTitle(json_obj.getString("Title"));
                obj.setSearchTitle(removeSpecialChars(json_obj.getString("Title")));
            }
            if (json_obj.has("StreamUrl")) {
                obj.setStreamUrl(json_obj.getString("StreamUrl"));
            }
            if (VideoStreamManager.getInstance().getSeenMovies().contains(String.valueOf(obj.getContentId()))) {
                obj.setSeen(true);
            }
            if (VideoStreamManager.getInstance().getFavoriteMovies().contains(String.valueOf(obj.getContentId()))) {
                obj.setFavorite(true);
            }

            if(json_obj.has("tipo"))
                obj.setCategoryType(json_obj.getInt("tipo"));


            if (obj instanceof LiveProgram) {
                LiveProgram liveProgram = (LiveProgram) obj;
                if (json_obj.has("cve")) {
                    liveProgram.setContentId(Integer.parseInt(json_obj.getString("cve")));
                }
                if (json_obj.has("nombre")) {
                    liveProgram.setTitle(json_obj.getString("nombre"));
                }
                if (json_obj.has("icono")) {
                    liveProgram.setIconUrl(json_obj.getString("icono"));
                }
                if (json_obj.has("stream")) {
                    liveProgram.setStreamUrl(json_obj.getString("stream"));
                }
                if (json_obj.has("epg_ahora")) {
                    liveProgram.setEpg_ahora(json_obj.getString("epg_ahora"));
                }
                if (json_obj.has("epg_despues")) {
                    liveProgram.setEpg_despues(json_obj.getString("epg_despues"));
                }
                if (json_obj.has("description") && !json_obj.getString("description").equals(""))
                    liveProgram.setDescription(json_obj.getString("description"));
                if (json_obj.has("title") && !json_obj.getString("title").equals(""))
                    liveProgram.setSub_title(json_obj.getString("title"));
            } else if (obj instanceof Serie) {
                Serie movie = (Serie) obj;
                if (json_obj.has("temporadas")) {
                    movie.setSeasonCountText(json_obj.getString("temporadas"));
                } else if (json_obj.has("seasonCountText")) {
                    movie.setSeasonCountText(json_obj.getString("seasonCountText"));
                }
                if (json_obj.has("Description")) {
                    movie.setDescription(json_obj.getString("Description"));
                }
                if (json_obj.has("Watched")) {
                    movie.setWatched(json_obj.getBoolean("Watched"));
                }
                if (json_obj.has("Length")) {
                    if(TextUtils.isEmpty(json_obj.getString("Length")) || json_obj.getString("Length").equals("null")) {
                        movie.setLength(0);
                    } else {
                        movie.setLength(Integer.parseInt(json_obj.getString("Length")));
                    }
                }
                if (json_obj.has("Rating")) {
                    movie.setRating(json_obj.getString("Rating"));
                }
                if (json_obj.has("StarRating")) {
                    movie.setStarRating(json_obj.getInt("StarRating"));
                }
                if (json_obj.has("Categories")) {
                    movie.setCategories(json_obj.getString("Categories"));
                }
                if (json_obj.has("Director")) {
                    movie.setDirector(json_obj.getString("Director"));
                }
                if (json_obj.has("Actors")) {
                    movie.setActors(json_obj.getString("Actors"));
                    movie.setSearchActors(removeSpecialChars(json_obj.getString("Actors")));
                }
                if (json_obj.has("ReleaseDate")) {
                    movie.setReleaseDate(json_obj.getString("ReleaseDate"));
                }
                if (json_obj.has("StreamFormat")) {
                    movie.setStreamFormat(json_obj.getString("StreamFormat"));
                }
                if (json_obj.has("StreamBitrates")) {
                    movie.setStreamBitrates(Integer.parseInt(json_obj.getString("StreamBitrates")));
                }
                if (json_obj.has("SDBifUrl")) {
                    movie.setSDBifUrl(json_obj.getString("SDBifUrl"));
                }
                if (json_obj.has("HDBifUrl")) {
                    movie.setHDBifUrl(json_obj.getString("HDBifUrl"));
                }
                if (json_obj.has("SDPosterUrl")) {
                    movie.setSDPosterUrl(json_obj.getString("SDPosterUrl"));
                }
                if (json_obj.has("HDPosterUrl")) {
                    movie.setHDPosterUrl(json_obj.getString("HDPosterUrl"));
                }
                if (json_obj.has("StreamQualities")) {
                    movie.setStreamQualities(json_obj.getString("StreamQualities"));
                }
                if (json_obj.has("HDBranded")) {
                    movie.setHDBranded(json_obj.getBoolean("HDBranded"));
                }
                if (json_obj.has("isHD")) {
                    movie.setHD(json_obj.getBoolean("isHD"));
                }
                if (json_obj.has("fullHD")) {
                    movie.setFullHD(json_obj.getBoolean("fullHD"));
                }
                if (json_obj.has("HDFondoUrl")) {
                    movie.setHDFondoUrl(json_obj.getString("HDFondoUrl"));
                }
            } else if (obj instanceof Episode) {
                Episode movie2 = (Episode) obj;
                if (json_obj.has("SubtitleUrl")) {
                    movie2.setSubtitleUrl(json_obj.getString("SubtitleUrl"));
                }
                if (json_obj.has("Description")) {
                    movie2.setDescription(json_obj.getString("Description"));
                }
                if (json_obj.has("Watched")) {
                    movie2.setWatched(json_obj.getBoolean("Watched"));
                }
                if (json_obj.has("Length")) {
                    if(TextUtils.isEmpty(json_obj.getString("Length")) || json_obj.getString("Length").equals("null")) {
                        movie2.setLength(0);
                    } else {
                        movie2.setLength(Integer.parseInt(json_obj.getString("Length")));
                    }
                }
                if (json_obj.has("Rating")) {
                    movie2.setRating(json_obj.getString("Rating"));
                }
                if (json_obj.has("StarRating")) {
                    movie2.setStarRating(json_obj.getInt("StarRating"));
                }
                if (json_obj.has("Categories")) {
                    movie2.setCategories(json_obj.getString("Categories"));
                }
                if (json_obj.has("Director")) {
                    movie2.setDirector(json_obj.getString("Director"));
                }
                if (json_obj.has("Actors")) {
                    movie2.setActors(json_obj.getString("Actors"));
                    movie2.setSearchActors(removeSpecialChars(json_obj.getString("Actors")));
                }
                if (json_obj.has("ReleaseDate")) {
                    movie2.setReleaseDate(json_obj.getString("ReleaseDate"));
                }
                if (json_obj.has("StreamFormat")) {
                    movie2.setStreamFormat(json_obj.getString("StreamFormat"));
                }
                if (json_obj.has("StreamBitrates")) {
                    movie2.setStreamBitrates(Integer.parseInt(json_obj.getString("StreamBitrates")));
                }
                if (json_obj.has("SDBifUrl")) {
                    movie2.setSDBifUrl(json_obj.getString("SDBifUrl"));
                }
                if (json_obj.has("HDBifUrl")) {
                    movie2.setHDBifUrl(json_obj.getString("HDBifUrl"));
                }
                if (json_obj.has("SDPosterUrl")) {
                    movie2.setSDPosterUrl(json_obj.getString("SDPosterUrl"));
                }
                if (json_obj.has("HDPosterUrl")) {
                    movie2.setHDPosterUrl(json_obj.getString("HDPosterUrl"));
                }
                if (json_obj.has("StreamQualities")) {
                    movie2.setStreamQualities(json_obj.getString("StreamQualities"));
                }
                if (json_obj.has("HDBranded")) {
                    movie2.setHDBranded(json_obj.getBoolean("HDBranded"));
                }
                if (json_obj.has("isHD")) {
                    movie2.setHD(json_obj.getBoolean("isHD"));
                }
                if (json_obj.has("fullHD")) {
                    movie2.setFullHD(json_obj.getBoolean("fullHD"));
                }
            } else if (obj instanceof Movie) {
                Movie movie3 = (Movie) obj;
                if (json_obj.has("SubtitleUrl")) {
                    movie3.setSubtitleUrl(json_obj.getString("SubtitleUrl"));
                }
                if (json_obj.has("Description")) {
                    movie3.setDescription(json_obj.getString("Description"));
                }
                if (json_obj.has("Watched")) {
                    movie3.setWatched(json_obj.getBoolean("Watched"));
                }
                if (json_obj.has("Length")) {
                    if(TextUtils.isEmpty(json_obj.getString("Length")) || json_obj.getString("Length").equals("null")) {
                        movie3.setLength(0);
                    } else {
                        movie3.setLength(Integer.parseInt(json_obj.getString("Length")));
                    }
                }
                if (json_obj.has("Rating")) {
                    movie3.setRating(json_obj.getString("Rating"));
                }
                if (json_obj.has("StarRating")) {
                    movie3.setStarRating(json_obj.getInt("StarRating"));
                }
                if (json_obj.has("Categories")) {
                    movie3.setCategories(json_obj.getString("Categories"));
                }
                if (json_obj.has("Director")) {
                    movie3.setDirector(json_obj.getString("Director"));
                }
                if (json_obj.has("Actors")) {
                    movie3.setActors(json_obj.getString("Actors"));
                    movie3.setSearchActors(removeSpecialChars(json_obj.getString("Actors")));
                }
                if (json_obj.has("ReleaseDate")) {
                    movie3.setReleaseDate(json_obj.getString("ReleaseDate"));
                }
                if (json_obj.has("StreamFormat")) {
                    movie3.setStreamFormat(json_obj.getString("StreamFormat"));
                }
                if (json_obj.has("StreamBitrates")) {
                    movie3.setStreamBitrates(Integer.parseInt(json_obj.getString("StreamBitrates")));
                }
                if (json_obj.has("SDBifUrl")) {
                    movie3.setSDBifUrl(json_obj.getString("SDBifUrl"));
                }
                if (json_obj.has("HDBifUrl")) {
                    movie3.setHDBifUrl(json_obj.getString("HDBifUrl"));
                }
                if (json_obj.has("SDPosterUrl")) {
                    movie3.setSDPosterUrl(json_obj.getString("SDPosterUrl"));
                }
                if (json_obj.has("HDPosterUrl")) {
                    movie3.setHDPosterUrl(json_obj.getString("HDPosterUrl"));
                }
                if (json_obj.has("StreamQualities")) {
                    movie3.setStreamQualities(json_obj.getString("StreamQualities"));
                }
                if (json_obj.has("HDBranded")) {
                    movie3.setHDBranded(json_obj.getBoolean("HDBranded"));
                }
                if (json_obj.has("isHD")) {
                    movie3.setHD(json_obj.getBoolean("isHD"));
                }
                if (json_obj.has("fullHD")) {
                    movie3.setFullHD(json_obj.getBoolean("fullHD"));
                }
                if (json_obj.has("HDFondoUrl")) {
                    movie3.setHDFondoUrl(json_obj.getString("HDFondoUrl"));
                }
            } else if (obj instanceof Setting) {
                Setting setting = (Setting) obj;
                if (json_obj.has("SDPosterUrl")) {
                    setting.setSDPosterUrl(json_obj.getString("SDPosterUrl"));
                }
                if (json_obj.has("HDPosterUrl")) {
                    setting.setHDPosterUrl(json_obj.getString("HDPosterUrl"));
                }
            }
        } catch (JSONException e) {
        }
    }
}
