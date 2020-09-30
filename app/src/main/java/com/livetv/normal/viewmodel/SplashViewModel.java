package com.livetv.normal.viewmodel;

import android.app.ProgressDialog;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import com.google.gson.Gson;
import com.livetv.normal.listeners.DownloaderListener;
import com.livetv.normal.listeners.StringRequestListener;
import com.livetv.normal.model.User;
import com.livetv.normal.utils.DataManager;
import com.livetv.normal.utils.Device;
import com.livetv.normal.utils.Downloader;
import com.livetv.normal.utils.networking.NetManager;
import com.livetv.normal.viewmodel.SplashViewModelContract.View;
import com.livetv.normal.viewmodel.SplashViewModelContract.ViewModel;
import org.json.JSONException;
import org.json.JSONObject;

public class SplashViewModel implements ViewModel, StringRequestListener, DownloaderListener {
    private NetManager netManager = NetManager.getInstance();
    private User user;
    /* access modifiers changed from: private */
    public View viewCallback;

    public void onViewResumed() {
    }

    public void onViewAttached(@NonNull Lifecycle.View viewCallback2) {
        this.viewCallback = (View) viewCallback2;
    }

    public void onViewDetached() {
        this.viewCallback = null;
    }

    public void login() {
        String usr = "";
        String password = "";
        String id = "";
        String theUser = DataManager.getInstance().getString("theUser", "");
        if (!TextUtils.isEmpty(theUser)) {
            this.user = (User) new Gson().fromJson(theUser, User.class);
            usr = this.user.getName();
            password = this.user.getPassword();
            id = this.user.getDeviceId();
        }
        if (TextUtils.isEmpty(usr) || TextUtils.isEmpty(password) || TextUtils.isEmpty(id)) {
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    SplashViewModel.this.viewCallback.onLoginCompleted(false);
                }
            }, 2000);
        } else {
            this.netManager.performSplashLogin(usr, password, id, this);
        }
    }

    public void checkForUpdate() {
        this.netManager.performCheckForUpdate(this);
    }

    public void downloadUpdate(String location, ProgressDialog progress) {
        Downloader.getInstance().performDownload(location, progress, this);
    }

    public void onCompleted(String response) {
        if (!TextUtils.isEmpty(response)) {
            try {
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.has("status") && "1".equals(jsonObject.getString("status"))) {
                    String userAgent = jsonObject.getString("user-agent");
                    if (!TextUtils.isEmpty(userAgent)) {
                        this.user.setUser_agent(userAgent);
                        this.user.setExpiration_date(jsonObject.getString("expire_date"));
                        this.user.setDevice(Device.getModel() + " - " + Device.getFW());
                        this.user.setVersion(Device.getVersion());
                        this.user.setDeviceId(Device.getIdentifier());
                        DataManager.getInstance().saveData("theUser", new Gson().toJson((Object) this.user));
                        if (!jsonObject.isNull("pin")) {
                            DataManager.getInstance().saveData("adultsPassword", jsonObject.getString("pin"));
                        }
                        this.viewCallback.onLoginCompleted(true);
                        return;
                    }
                } else {
                    String errorFound = jsonObject.getString("error_found");
                    viewCallback.onLoginError(errorFound);
                    return;
                }
                if (jsonObject.has("android_version")) {
                    if (!Device.getVersionInstalled().replaceAll("\\.", "").equals(jsonObject.getString("android_version"))) {
                        this.viewCallback.onCheckForUpdateCompleted(true, jsonObject.getString("link_android") + "/android" + jsonObject.getString("android_version") + ".apk");
                        return;
                    }
                    this.viewCallback.onCheckForUpdateCompleted(false, null);
                    return;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        this.viewCallback.onLoginCompleted(false);
    }

    public void onError() {
        this.viewCallback.onLoginCompleted(false);
    }

    public void onDownloadError(int error) {
        this.viewCallback.onDownloadUpdateError(error);
    }

    public void onDownloadComplete(String location) {
        this.viewCallback.onDownloadUpdateCompleted(location);
    }
}
