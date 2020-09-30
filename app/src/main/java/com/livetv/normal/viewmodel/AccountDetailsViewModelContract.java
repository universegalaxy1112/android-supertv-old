package com.livetv.normal.viewmodel;

import android.app.ProgressDialog;

import com.livetv.normal.databinding.AccountDetailsFragmentBinding;

public interface AccountDetailsViewModelContract {

    public interface View extends com.livetv.normal.viewmodel.Lifecycle.View {
        void onCloseSessionNoInternet();

        void onCloseSessionSelected();

        void onError();
        void onCheckForUpdateCompleted(boolean z, String str);

        void onDownloadUpdateCompleted(String str);

        void onDownloadUpdateError(int i);
    }

    public interface ViewModel extends com.livetv.normal.viewmodel.Lifecycle.ViewModel {
        void showAccountDetails(AccountDetailsFragmentBinding accountDetailsFragmentBinding);
        void checkForUpdate(android.view.View view);

        void downloadUpdate(String str, ProgressDialog progressDialog);
    }
}
