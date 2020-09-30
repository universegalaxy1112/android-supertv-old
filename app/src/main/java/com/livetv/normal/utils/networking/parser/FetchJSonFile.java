//package com.livetv.normal.utils.networking.parser;
//import android.net.Uri;
//import android.os.AsyncTask;
//import android.util.Log;
//
//
//import com.livetv.normal.model.Setting;
//import com.livetv.normal.model.VideoStream;
//import com.livetv.normal.listeners.BaseResponseListener;
//
//import org.json.JSONException;
//
//import java.net.MalformedURLException;
//import java.net.ProtocolException;
//import java.net.URL;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.net.HttpURLConnection;
//import java.util.List;
//
//public class FetchJSonFile extends AsyncTask<String, Void, List<VideoStream> > {
//
//    final String LOG_TAG = this.getClass().getSimpleName();
//
//    BaseResponseListener callback;
//
//    public FetchJSonFile(BaseResponseListener baseResponseListener) {
//        super();
//
//        callback = baseResponseListener;
//    }
//
//    @Override
//    protected List<VideoStream> doInBackground(String... param) {
//
//        HttpURLConnection urlConnection = null;
//        BufferedReader reader = null;
//        try {
////            final String SERVER_PATH = "http://209.126.120.157:84";
////            final String SLASH_PATH = "/";
////            final String AUT_PATH = "yXhj9M0pf9NZ9u4qh";
////            final String CATEGROY_PATH = param[1];
////            final String EXTENSION_PATH = ".php?";
////            final String CAT_PARAM = "cat";
//
//            String liveTVURL = "http://209.126.120.157:84/yXhj9M0pf9NZ9u4qh";
//            switch(param[0]) {
//                case ModelTypes.MOVIE_CATEGORIES:
//                case ModelTypes.SERIES_CATEGORIES:
//                case ModelTypes.SERIES_KIDS_CATEGORIES:
//                case ModelTypes.EVENTS_CATEGORIES:
//                case ModelTypes.ADULTS_CATEGORIES:
//                case ModelTypes.LIVE_TV_CATEGORIES:
//                case ModelTypes.KARAOKE_CATEGORIES:
//                case ModelTypes.MUSIC_CATEGORIES:
//                case ModelTypes.ENTERTAINMENT_CATEGORIES:
//                    liveTVURL += "/categorias.php?t="+param[0];
//                    break;
////                case ModelTypes.SETTING:
////                    liveTVURL += "/movie.php?cat=Settings";
////                    break;
//            }
//
//
////            String path = SERVER_PATH + SLASH_PATH + AUT_PATH + SLASH_PATH + CATEGROY_PATH + EXTENSION_PATH;
////            Uri uri = Uri.parse(path).buildUpon().appendQueryParameter(CAT_PARAM, param[0]).build();
//            Uri uri = Uri.parse(liveTVURL);
//            Log.i(LOG_TAG, "URI :" + uri.toString());
//            URL url = new URL(uri.toString());
//
//            urlConnection = (HttpURLConnection) url.openConnection();
//            urlConnection.setRequestMethod("GET");
//            urlConnection.connect();
//
//            InputStream inputStream = urlConnection.getInputStream();
//            StringBuffer buffer = new StringBuffer();
//            if (inputStream == null) {
//                // Nothing to do.
//                return null;
//            }
//
//            reader = new BufferedReader(new InputStreamReader(inputStream));
//
//            String line;
//            while ((line = reader.readLine()) != null) {
//                Log.i(LOG_TAG, "Line : "+line);
//                buffer.append(line + "\n");
//            }
//
//
//            if (buffer.length() == 0) {
//                // Stream was empty.  No point in parsing.
//                return null;
//            }
//
//
//            return ParserJSonFile.getParsedArray(param[0], buffer.toString());
//
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        } catch (ProtocolException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//        return null;
//
//    }
//
//    @Override
//    protected void onPostExecute(List<VideoStream> videoStreams) {
//        super.onPostExecute(videoStreams);
//
//        for(VideoStream videoStream : videoStreams) {
//            Setting setting = (Setting) videoStream ;
//            ;//Log.d("FetchJSonFile","Setting title: " + setting.getTitle());
//            ;//Log.d("FetchJSonFile","\t\t HDPoster: " + setting.getHDPosterUrl());
//            ;//Log.d("FetchJSonFile","\t\t SDPoster: " + setting.getSDPosterUrl());
//        }
//
////        callback.callbackJsonResponse(videoStreams);
//        callback.onCompleted();
//    }
//
//    @Override
//    protected void onProgressUpdate(Void... values) {
//        super.onProgressUpdate(values);
//    }
//}