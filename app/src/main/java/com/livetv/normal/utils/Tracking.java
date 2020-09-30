package com.livetv.normal.utils;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import com.google.gson.Gson;
import com.livetv.normal.R;
import com.livetv.normal.listeners.StringRequestListener;
import com.livetv.normal.model.User;
import com.livetv.normal.utils.networking.NetManager;
import com.livetv.normal.utils.networking.WebConfig;

import org.json.JSONException;
import org.json.JSONObject;

public class Tracking implements StringRequestListener, OnClickListener {
    private static Activity mActivity = null;
    private static Tracking mInstance = null;
    private String action = "IDLE";
    /* access modifiers changed from: private */
    public Handler handler = new Handler();
    public Handler handlerAccount = new Handler();
    /* access modifiers changed from: private */
    public boolean isTracking = false;
    private Runnable trackingThread = new Runnable() {
        public void run() {
            Tracking.this.track();
            if (Tracking.this.isTracking) {
                Tracking.this.handler.postDelayed(this, 40000);
                return;
            }
            try {
                finalize();
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }
    };

    private Runnable accountCheck = new Runnable() {
        public void run() {
            Tracking.this.loginCheck();
            if (Tracking.this.isTracking) {
                Tracking.this.handlerAccount.postDelayed(this, 300000);
                return;
            }
            try {
                finalize();
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }
    };
    private User usr;

    public static Tracking getInstance(Activity activity) {
        if (mInstance == null) {
            mInstance = new Tracking();
        }
        mActivity = activity;
        return mInstance;
    }

    public void onStart() {
        String theUser = DataManager.getInstance().getString("theUser", "");
        if (!TextUtils.isEmpty(theUser)) {
            this.usr = ((User) new Gson().fromJson(theUser, User.class));
        }
        if (!this.isTracking) {
            this.isTracking = true;
            this.handler.removeCallbacks(trackingThread);
            this.handler.post(this.trackingThread);
            this.handlerAccount.removeCallbacks(accountCheck);
            this.handlerAccount.post(this.accountCheck);
        }
    }

    public void onStop() {
        this.isTracking = false;
        this.handler.removeCallbacks(trackingThread);
        this.handlerAccount.removeCallbacks(accountCheck);
    }

    /* access modifiers changed from: private */
    public void track() {
        String url = WebConfig.trackingURL.replace("{USER}", this.usr != null ? usr.getName() : "").replace("{MOVIE}", this.action);
        Log.d("DNLS", "Tracking URL: " + url);
        NetManager.getInstance().makeStringRequest(url, this);
    }

    public void loginCheck() {
        NetManager.getInstance().performSplashLogin(this.usr != null ? usr.getName() : "", this.usr != null ? this.usr.getPassword() : "", this.usr != null ? this.usr.getDeviceId() : "", new StringRequestListener() {
            @Override
            public void onCompleted(String response) {
                if (!TextUtils.isEmpty(response)) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.has("status") && "1".equals(jsonObject.getString("status"))) {
                            Log.d("TAG", "success");
                        }else{
                            if(!Connectivity.isConnected()){
                                Dialogs.showOneButtonDialog(mActivity, R.string.no_connection_title,  R.string.no_connection_message, new DialogInterface.OnClickListener(){
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Log.d("TAG", "no internet");
                                    }
                                });
                                return;
                            }

                            Tracking.getInstance(mActivity).onStop();
                            String errorFound = jsonObject.getString("error_found");
                            switch (errorFound) {
                                case "103":
                                case "104":
                                    showErrorMessage(mActivity.getString(R.string.login_error_change_device), errorFound);
                                    break;
                                case "105":
                                    showErrorMessage(mActivity.getString(R.string.login_error_usr_pss_incorrect), errorFound);
                                    break;
                                case "106":
                                    showErrorMessage(mActivity.getString(R.string.login_error_device_not_registered), errorFound);
                                    break;
                                case "107":
                                    showErrorMessage(mActivity.getString(R.string.login_error_expired), errorFound);
                                    break;
                                case "108": {
                                    showErrorMessage(mActivity.getString(R.string.login_error_change_account).replace("{ID}", Device.getIdentifier()), errorFound);
                                }
                                break;
                                case "109": {
                                    showErrorMessage(mActivity.getString(R.string.login_error_demo), errorFound);
                                }
                                break;
                                case "110": {
                                    showErrorMessage(mActivity.getString(R.string.ip_limitation), errorFound);
                                }
                                break;
                                default:
                                    showErrorMessage("Estimado " + /*LiveTvApplication.getUser().getName() + */"su cuenta a sido desactivada, porfavor comunicate con tu vendedor.", errorFound);
                                    break;
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onError() {

            }
        });
    }

    public void showErrorMessage(String message, final String error_found) {
        try {
            Dialogs.showOneButtonDialog(mActivity, mActivity.getString(R.string.attention), message, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    closeApp();
                }
            });
        }catch (Exception e) {
            e.printStackTrace();
        }

    }



    public void closeApp() {
        if(Connectivity.isConnected()){
            (mActivity).finishAffinity();
            System.exit(0);
        }
        else{
            Dialogs.showOneButtonDialog(mActivity, R.string.no_connection_title,  R.string.no_connection_message, new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    (mActivity).finishAffinity();
                    System.exit(0);
                }
            });
        }

    }

    public void setAction(String action2) {
        this.action = action2;
    }

    public void onError() {
        Log.d("DNLS", "Tracking ERROR");
    }

    public void onCompleted(String response) {
        Log.d("DNLS", "Tracking response: " + response);
        try {
            JSONObject messageJson = new JSONObject(response);
            if (!messageJson.isNull("message") && !TextUtils.isEmpty(messageJson.getString("message"))) {
                Dialogs.showOneButtonDialog(mActivity, "Atención", messageJson.getString("message"), (OnClickListener) this);
            }
        } catch (Exception e) {

        }
    }

    public void onClick(DialogInterface dialog, int which) {
        String url = WebConfig.deleteMessageURL.replace("{USER}", this.usr != null ? this.usr.getName() : "");
        Log.d("DNLS", "Tracking URL: " + url);
        NetManager.getInstance().makeStringRequest(url, this);
    }
}
