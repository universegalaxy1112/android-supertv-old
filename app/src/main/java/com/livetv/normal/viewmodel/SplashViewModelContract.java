package com.livetv.normal.viewmodel;

import android.app.ProgressDialog;

public interface SplashViewModelContract {

    public interface View extends Lifecycle.View {
        void onCheckForUpdateCompleted(boolean z, String str);

        void onDownloadUpdateCompleted(String str);

        void onDownloadUpdateError(int i);

        void onLoginCompleted(boolean z);

        void onLoginError(String errorFound);

    }

    public interface ViewModel extends Lifecycle.ViewModel {
        void checkForUpdate();

        void downloadUpdate(String str, ProgressDialog progressDialog);

        void login();
    }
}
