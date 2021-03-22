package com.livetv.normal.viewmodel;

import android.app.Activity;
import android.app.ProgressDialog;
import androidx.databinding.ObservableBoolean;
import androidx.annotation.NonNull;
import android.text.TextUtils;
import com.google.gson.Gson;
import com.livetv.normal.R;
import com.livetv.normal.databinding.AccountDetailsFragmentBinding;
import com.livetv.normal.listeners.DialogListener;
import com.livetv.normal.listeners.DownloaderListener;
import com.livetv.normal.listeners.StringRequestListener;
import com.livetv.normal.model.User;
import com.livetv.normal.utils.Connectivity;
import com.livetv.normal.utils.DataManager;
import com.livetv.normal.utils.Device;
import com.livetv.normal.utils.Dialogs;
import com.livetv.normal.utils.Downloader;
import com.livetv.normal.utils.networking.NetManager;
import com.livetv.normal.utils.networking.WebConfig;
import com.livetv.normal.viewmodel.AccountDetailsViewModelContract.ViewModel;

import org.json.JSONException;
import org.json.JSONObject;

public class AccountDetailsViewModel implements ViewModel {
    public ObservableBoolean isLoading = new ObservableBoolean(false);
    public ObservableBoolean isTV = new ObservableBoolean(Device.canTreatAsBox());
    private final Activity mActivity;
    /* access modifiers changed from: private */
    public AccountDetailsViewModelContract.View viewCallback;

    public AccountDetailsViewModel(Activity activity) {
        this.mActivity = activity;
    }

    public void onViewResumed() {
    }

    public void onViewAttached(@NonNull Lifecycle.View viewCallback2) {
        this.viewCallback = (AccountDetailsViewModelContract.View) viewCallback2;
    }

    public void onViewDetached() {
        this.viewCallback = null;
    }

    public void onGoToMenu(android.view.View view) {
        this.viewCallback.onError();
    }

    public void onCloseSession(android.view.View view) {
        if (Device.canTreatAsBox()) {
            Dialogs.showTwoButtonsDialog(this.mActivity, (int) R.string.accept, (int) R.string.cancel, (int) R.string.end_session_message, (DialogListener) new DialogListener() {
                public void onAccept() {
                    AccountDetailsViewModel.this.closeSession();
                }

                public void onCancel() {
                }
            });
        } else {
            closeSession();
        }
    }

    @Override
    public void checkForUpdate(android.view.View view) {
        NetManager.getInstance().performCheckForUpdate(new StringRequestListener() {
            @Override
            public void onCompleted(String response) {
                if (!TextUtils.isEmpty(response)) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);

                        if (jsonObject.has("android_version")) {
                            if (!Device.getVersionInstalled().replaceAll("\\.", "").equals(jsonObject.getString("android_version"))) {
                                AccountDetailsViewModel.this.viewCallback.onCheckForUpdateCompleted(true, jsonObject.getString("link_android") + "/android" + jsonObject.getString("android_version") + ".apk");
                                return;
                            }
                            AccountDetailsViewModel.this.viewCallback.onCheckForUpdateCompleted(false, null);
                            return;
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

    public void downloadUpdate(String location, ProgressDialog progress) {
        Downloader.getInstance().performDownload(location, progress, new DownloaderListener() {
            @Override
            public void onDownloadComplete(String str) {
                AccountDetailsViewModel.this.viewCallback.onDownloadUpdateCompleted(str);
            }

            @Override
            public void onDownloadError(int i) {

            }
        });
    }

    /* access modifiers changed from: private */
    public void closeSession() {
        if (Connectivity.isConnected()) {
            this.isLoading.set(true);
            String theUser = DataManager.getInstance().getString("theUser", "");
            if (!TextUtils.isEmpty(theUser)) {
                NetManager.getInstance().makeStringRequest(WebConfig.removeUserURL.replace("{USER}", ((User) new Gson().fromJson(theUser, User.class)).getName()), new StringRequestListener() {
                    public void onCompleted(String response) {
                        if (response.toLowerCase().contains("success")) {
                            AccountDetailsViewModel.this.viewCallback.onCloseSessionSelected();
                        }
                    }

                    public void onError() {
                        AccountDetailsViewModel.this.isLoading.set(false);
                    }
                });
                return;
            }
            return;
        }
        this.viewCallback.onCloseSessionNoInternet();
    }

    public void showAccountDetails(AccountDetailsFragmentBinding accountDetailsFragmentBinding) {
        String theUser = DataManager.getInstance().getString("theUser", "");
        if (!TextUtils.isEmpty(theUser)) {
            accountDetailsFragmentBinding.setUser((User) new Gson().fromJson(theUser, User.class));
        } else {
            this.viewCallback.onError();
        }
    }


}
