package com.livetv.normal.utils.networking.services;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.livetv.normal.listeners.StringRequestListener;
import com.livetv.normal.model.LiveProgram;
import com.livetv.normal.model.LiveTVCategory;
import com.livetv.normal.model.MainCategory;
import com.livetv.normal.model.MovieCategory;
import com.livetv.normal.model.Serie;
import com.livetv.normal.model.User;
import com.livetv.normal.model.VideoStream;
import com.livetv.normal.utils.DataManager;
import com.livetv.normal.utils.Device;
import com.livetv.normal.utils.networking.NetManager;
import com.livetv.normal.utils.networking.WebConfig;
import com.livetv.normal.utils.networking.parser.FetchJSonFileSync;

import java.net.URLEncoder;
import java.util.List;

import rx.Observable;
import rx.Observable.OnSubscribe;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class LiveTVServicesManual {
    public static Observable<Boolean> performLogin(final String usr, final String pss, final StringRequestListener stringRequestListener) {
        return Observable.create(new OnSubscribe<Boolean>() {
            public void call(Subscriber<? super Boolean> subscriber) {
                subscriber.onNext(Boolean.valueOf(LiveTVServicesManual.loginRequest(usr, pss, stringRequestListener)));
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    /* access modifiers changed from: private */
    public static boolean loginRequest(String usr, String pss, final StringRequestListener stringRequestListener) {
        String loginUrl;
        try {
            loginUrl = WebConfig.loginURL
                    .replace("{USER}", usr)
                    .replace("{PASS}", pss)
                    .replace("{DEVICE_ID}", Device.getIdentifier())
                    .replace("{MODEL}", URLEncoder.encode(Device.getModel(), "UTF-8"))
                    .replace("{FW}", URLEncoder.encode(Device.getFW(), "UTF-8"))
                    .replace("{COUNTRY}", URLEncoder.encode(Device.getCountry(), "UTF-8"));
        } catch (Exception e) {
            loginUrl = "";
        }
        Log.d("liveTV", "PerformLogin + " + loginUrl);
        if (!TextUtils.isEmpty(loginUrl)) {
            NetManager.getInstance().makeStringRequest(loginUrl, new StringRequestListener() {
                public void onCompleted(String response) {
                    stringRequestListener.onCompleted(response);
                }

                public void onError() {
                    stringRequestListener.onError();
                }
            });
        }
        return true;
    }

    public static boolean loginSplash(String user, String code, String device_id, final StringRequestListener stringRequestListener) {
        String loginCodeUrl;
        try {
            loginCodeUrl = WebConfig.LoginSplash.replace("{USER}", user)
                    .replace("{PASS}", code)
                    .replace("{DEVICE_ID}", device_id);
        } catch (Exception e) {
            loginCodeUrl = "";
        }
        if (!TextUtils.isEmpty(loginCodeUrl)) {
            NetManager.getInstance().makeStringRequest(loginCodeUrl, new StringRequestListener() {
                public void onCompleted(String response) {
                    stringRequestListener.onCompleted(response);
                }

                public void onError() {
                    stringRequestListener.onError();
                }
            });
        }
        return true;
    }

    public static Observable<Boolean> performLoginCode(final String user, final String code, final String device_id, final StringRequestListener stringRequestListener) {
        return Observable.create(new Observable.OnSubscribe<Boolean>() {
            public void call(Subscriber<? super Boolean> subscriber) {
                subscriber.onNext(Boolean.valueOf(LiveTVServicesManual.loginSplash(user, code, device_id, stringRequestListener)));
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.computation())
                .unsubscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    /* access modifiers changed from: private */
    public static boolean loginCodeRequest(String code, final StringRequestListener stringRequestListener) {
        String loginCodeUrl;
        try {
            loginCodeUrl = WebConfig.LoginCodeURL.replace("{CODE}", code);
        } catch (Exception e) {
            loginCodeUrl = "";
        }
        Log.d("liveTV", "codeRequest + " + loginCodeUrl);
        if (!TextUtils.isEmpty(loginCodeUrl)) {
            NetManager.getInstance().makeStringRequest(loginCodeUrl, new StringRequestListener() {
                public void onCompleted(String response) {
                    stringRequestListener.onCompleted(response);
                }

                public void onError() {
                    stringRequestListener.onError();
                }
            });
        }
        return true;
    }

    public static Observable<Boolean> addRecent(final String type, final String cve, final StringRequestListener stringRequestListener) {

        return Observable.create(new Observable.OnSubscribe<Boolean>() {

            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                subscriber.onNext(recentRequest(type, cve, stringRequestListener));
                subscriber.onCompleted();
            }
        })
                .subscribeOn(Schedulers.computation())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private static boolean recentRequest(String type, String cve, final StringRequestListener stringRequestListener) {
        String addRecentUrl;
        String user = "";
        try {
            String theUser = DataManager.getInstance().getString("theUser", "");
            if (!TextUtils.isEmpty(theUser)) {
                user = ( new Gson().fromJson(theUser, User.class)).getName();
            }
            addRecentUrl = WebConfig.addRecent.replace("{USER}", user)
                    .replace("{TIPO}", type)
                    .replace("{CVE}", cve);
        } catch (Exception e) {
            addRecentUrl = "";
        }

        if (!TextUtils.isEmpty(addRecentUrl)) {
            NetManager.getInstance().makeStringRequest(addRecentUrl, new StringRequestListener() {
                @Override
                public void onCompleted(String response) {
                    stringRequestListener.onCompleted(response);

                }
                @Override
                public void onError() {
                    stringRequestListener.onError();
                }
            });
        }
        return true;
    }

    private static boolean codeRequest(final StringRequestListener stringRequestListener) {
        String loginUrl = WebConfig.GetCodeURL;
        Log.d("liveTV", "codeRequest + " + loginUrl);
        if (!TextUtils.isEmpty(loginUrl)) {
            NetManager.getInstance().makeStringRequest(loginUrl, new StringRequestListener() {
                public void onCompleted(String response) {
                    stringRequestListener.onCompleted(response);
                }

                public void onError() {
                    stringRequestListener.onError();
                }
            });
        }
        return true;
    }

    public static Observable<Boolean> performCheckForUpdate(final StringRequestListener stringRequestListener) {
        return Observable.create(new OnSubscribe<Boolean>() {
            public void call(Subscriber<? super Boolean> subscriber) {
                subscriber.onNext(Boolean.valueOf(LiveTVServicesManual.checkForUpdateRequest(stringRequestListener)));
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    /* access modifiers changed from: private */
    public static boolean checkForUpdateRequest(final StringRequestListener stringRequestListener) {
        String checkForUpdateUrl = WebConfig.updateURL;
        if (!TextUtils.isEmpty(checkForUpdateUrl)) {
            NetManager.getInstance().makeStringRequest(checkForUpdateUrl, new StringRequestListener() {
                public void onCompleted(String response) {
                    stringRequestListener.onCompleted(response);
                }

                public void onError() {
                    stringRequestListener.onError();
                }
            });
        }
        return true;
    }

    public static Observable<Boolean> performGetCode(final StringRequestListener stringRequestListener) {
        return Observable.create(new OnSubscribe<Boolean>() {
            public void call(Subscriber<? super Boolean> subscriber) {
                subscriber.onNext(Boolean.valueOf(LiveTVServicesManual.getCodeRequest(stringRequestListener)));
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    /* access modifiers changed from: private */
    public static boolean getCodeRequest(final StringRequestListener stringRequestListener) {
        String getCodeUrl = WebConfig.GetCodeURL;
        if (!TextUtils.isEmpty(getCodeUrl)) {
            NetManager.getInstance().makeStringRequest(getCodeUrl, new StringRequestListener() {
                public void onCompleted(String response) {
                    stringRequestListener.onCompleted(response);
                }

                public void onError() {
                    stringRequestListener.onError();
                }
            });
        }
        return true;
    }

    public static Observable<Boolean> performChangePassCode(final String user, final String password, final String code, final StringRequestListener stringRequestListener) {
        return Observable.create(new OnSubscribe<Boolean>() {
            public void call(Subscriber<? super Boolean> subscriber) {
                subscriber.onNext(Boolean.valueOf(LiveTVServicesManual.setChangePassCode(user, password, code, stringRequestListener)));
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    /* access modifiers changed from: private */
    public static boolean setChangePassCode(String user, String password, String code, final StringRequestListener stringRequestListener) {
        String getCodeUrl = WebConfig.setPassCodeURL.replace("{CODE}", code).replace("{USER}", user).replace("{PASS}", password);
        if (!TextUtils.isEmpty(getCodeUrl)) {
            NetManager.getInstance().makeStringRequest(getCodeUrl, new StringRequestListener() {
                public void onCompleted(String response) {
                    stringRequestListener.onCompleted(response);
                }

                public void onError() {
                    stringRequestListener.onError();
                }
            });
        }
        return true;
    }

    public static Observable<List<? extends VideoStream>> searchVideo(final MainCategory mainCategory, final String pattern, final int timeOut) {
        return Observable.create(new OnSubscribe<List<? extends VideoStream>>() {
            public void call(Subscriber<? super List<? extends VideoStream>> subscriber) {
                subscriber.onNext(LiveTVServicesManual.fetchSearchVideo(mainCategory, pattern, timeOut));
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    /* access modifiers changed from: private */
    public static List<? extends VideoStream> fetchSearchVideo(MainCategory mainCategory, String pattern, int timeOut) {
        return new FetchJSonFileSync().retrieveSearchMovies(mainCategory, pattern, timeOut);
    }

    public static Observable<List<MovieCategory>> getSubCategories(final MainCategory category) {
        return Observable.create(new OnSubscribe<List<MovieCategory>>() {
            public void call(Subscriber<? super List<MovieCategory>> subscriber) {
                subscriber.onNext(LiveTVServicesManual.retrieveSubCategories(category));
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    /* access modifiers changed from: private */
    public static List<MovieCategory> retrieveSubCategories(MainCategory category) {
        return new FetchJSonFileSync().retrieveSubCategories(category);
    }

    public static Observable<List<? extends VideoStream>> getMoviesForSubCat(final String mainCategory, final String movieCategory, final int timeOut) {
        return Observable.create(new OnSubscribe<List<? extends VideoStream>>() {
            public void call(Subscriber<? super List<? extends VideoStream>> subscriber) {
                subscriber.onNext(LiveTVServicesManual.retrieveMoviesForSubCat(mainCategory, movieCategory, timeOut));
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    /* access modifiers changed from: private */
    public static List<? extends VideoStream> retrieveMoviesForSubCat(String mainCategory, String movieCategory, int timeOut) {
        return new FetchJSonFileSync().retrieveMovies(mainCategory, movieCategory, timeOut);
    }

    public static Observable<Integer> getSeasonsForSerie(final Serie serie) {
        return Observable.create(new OnSubscribe<Integer>() {
            public void call(Subscriber<? super Integer> subscriber) {
                subscriber.onNext(LiveTVServicesManual.retrieveSeasonsForSerie(serie));
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    /* access modifiers changed from: private */
    public static Integer retrieveSeasonsForSerie(Serie serie) {
        int seasons;
        try {
            seasons = Integer.parseInt(serie.getSeasonCountText().replaceAll("[^0-9]", ""));
        } catch (Exception e) {
            seasons = 0;
        }
        return Integer.valueOf(seasons);
    }

    public static Observable<List<? extends VideoStream>> getEpisodesForSerie(final Serie serie, final Integer season) {
        return Observable.create(new OnSubscribe<List<? extends VideoStream>>() {
            public void call(Subscriber<? super List<? extends VideoStream>> subscriber) {
                subscriber.onNext(LiveTVServicesManual.retrieveEpisodesForSerie(serie, season));
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    /* access modifiers changed from: private */
    public static List<? extends VideoStream> retrieveEpisodesForSerie(Serie serie, Integer season) {
        return new FetchJSonFileSync().retrieveMoviesForSerie(serie, season.intValue());
    }

    public static Observable<List<LiveTVCategory>> getLiveTVCategories(final MainCategory category) {
        return Observable.create(new OnSubscribe<List<LiveTVCategory>>() {
            public void call(Subscriber<? super List<LiveTVCategory>> subscriber) {
                subscriber.onNext(LiveTVServicesManual.retrieveLiveTVCategories(category));
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    /* access modifiers changed from: private */
    public static List<LiveTVCategory> retrieveLiveTVCategories(MainCategory category) {
        return new FetchJSonFileSync().retrieveLiveTVCategories(category);
    }

    public static Observable<LiveTVCategory> getProgramsForLiveTVCategory(final LiveTVCategory liveTVCategory) {
        return Observable.create(new OnSubscribe<LiveTVCategory>() {
            public void call(Subscriber<? super LiveTVCategory> subscriber) {
                List<LiveProgram> livePrograms = LiveTVServicesManual.retrieveProgramsForLiveTVCategory(liveTVCategory);
                liveTVCategory.setTotalChannels(livePrograms.size());
                liveTVCategory.setLivePrograms(livePrograms);
                subscriber.onNext(liveTVCategory);
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    /* access modifiers changed from: private */
    public static List<LiveProgram> retrieveProgramsForLiveTVCategory(LiveTVCategory liveTVCategory) {
        return new FetchJSonFileSync().retrieveProgramsForLiveTVCategory(liveTVCategory);
    }
}
