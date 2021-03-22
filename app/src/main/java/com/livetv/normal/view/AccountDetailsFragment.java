package com.livetv.normal.view;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.res.Resources;
import androidx.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.core.content.FileProvider;
import androidx.appcompat.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import com.livetv.normal.R;
import com.livetv.normal.databinding.AccountDetailsFragmentBinding;
import com.livetv.normal.listeners.DialogListener;
import com.livetv.normal.utils.Connectivity;
import com.livetv.normal.utils.DataManager;
import com.livetv.normal.utils.Device;
import com.livetv.normal.utils.Dialogs;
import com.livetv.normal.viewmodel.AccountDetailsViewModel;
import com.livetv.normal.viewmodel.AccountDetailsViewModelContract;
import com.livetv.normal.viewmodel.Lifecycle;

import java.io.File;

public class AccountDetailsFragment extends BaseFragment implements AccountDetailsViewModelContract.View, OnFocusChangeListener {
    private AccountDetailsFragmentBinding accountDetailsFragmentBinding;
    private AccountDetailsViewModel accountDetailsViewModel;
    private String updateLocation = null;
    public ProgressDialog downloadProgress;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.accountDetailsViewModel = new AccountDetailsViewModel(getActivity());
    }

    /* access modifiers changed from: protected */
    public void onConfigurationChanged() {
        setupUI();
    }

    public android.view.View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.accountDetailsFragmentBinding = (AccountDetailsFragmentBinding) DataBindingUtil.inflate(inflater, R.layout.account_details_fragment, container, false);
        this.accountDetailsFragmentBinding.setAccountDetailsVM(this.accountDetailsViewModel);
        if(getActivity() != null && Device.treatAsBox) {
            accountDetailsFragmentBinding.appBar.setVisibility(View.GONE);
        } else {
            ((AppCompatActivity) getActivity()).setSupportActionBar(this.accountDetailsFragmentBinding.toolbar);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle((CharSequence) "Mi Cuenta");
        }

        if (this.accountDetailsFragmentBinding.closeSetting != null) {
            this.accountDetailsFragmentBinding.closeSetting.setOnFocusChangeListener(new OnFocusChangeListener() {
                public void onFocusChange(android.view.View v, boolean hasFocus) {
                    if (hasFocus) {
                        v.setSelected(true);
                    } else {
                        v.setSelected(false);
                    }
                }
            });
        }
        if (this.accountDetailsFragmentBinding.endSessionButton != null) {
            this.accountDetailsFragmentBinding.endSessionButton.setOnFocusChangeListener(new OnFocusChangeListener() {
                public void onFocusChange(android.view.View v, boolean hasFocus) {
                    if (hasFocus) {
                        v.setSelected(true);
                    } else {
                        v.setSelected(false);
                    }
                }
            });
        }
        this.accountDetailsViewModel.showAccountDetails(this.accountDetailsFragmentBinding);
        return this.accountDetailsFragmentBinding.getRoot();
    }

    private void setupUI() {
        ViewGroup viewGroup = (ViewGroup) getView();
        viewGroup.removeAllViewsInLayout();
        this.accountDetailsFragmentBinding = (AccountDetailsFragmentBinding) DataBindingUtil.inflate(LayoutInflater.from(getActivity()), R.layout.account_details_fragment, viewGroup, false);
        this.accountDetailsFragmentBinding.setAccountDetailsVM(this.accountDetailsViewModel);
        ((AppCompatActivity) getActivity()).setSupportActionBar(this.accountDetailsFragmentBinding.toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle((CharSequence) "Mi Cuenta");
        this.accountDetailsViewModel.showAccountDetails(this.accountDetailsFragmentBinding);
        viewGroup.addView(this.accountDetailsFragmentBinding.getRoot());
    }

    /* access modifiers changed from: protected */
    public Lifecycle.ViewModel getViewModel() {
        return this.accountDetailsViewModel;
    }

    /* access modifiers changed from: protected */
    public Lifecycle.View getLifecycleView() {
        return this;
    }

    /* access modifiers changed from: protected */
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode != 4) {
            return false;
        }
        finishActivity();
        return true;
    }

    public void onFocusChange(android.view.View v, boolean hasFocus) {
        if (!v.isSelected()) {
            v.setSelected(true);
        } else {
            v.setSelected(false);
        }
    }

    @Override
    public void onCheckForUpdateCompleted(boolean hasNewVersion, final String location) {
        this.updateLocation = location;
        if (hasNewVersion) {
            Resources res = getResources();
            Dialogs.showTwoButtonsDialog((Activity) getActivity(), (int) R.string.download, (int) R.string.cancel, getResources().getString(R.string.new_version_available), (DialogListener) new DialogListener() {
                public void onAccept() {
                   if (Connectivity.isConnected()) {
                        downloadProgress = new ProgressDialog(getActivity(), ProgressDialog.THEME_HOLO_LIGHT);
                        downloadProgress.setProgressStyle(1);
                        downloadProgress.setMessage("Downloading");
                        downloadProgress.setIndeterminate(false);
                        downloadProgress.setCancelable(false);
                        downloadProgress.show();
                        accountDetailsViewModel.downloadUpdate(location, downloadProgress);
                    } else {
                        AccountDetailsFragment.this.goToNoConnectionError();
                    }
                }

                public void onCancel() {

                }
            });

        } else {
            //prompt no update
        }
    }

    @Override
    public void onDownloadUpdateCompleted(String location) {
        this.downloadProgress.dismiss();
        try
        {
            File file = new File(location);
            Intent intent;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Uri downloaded_apk = getFileUri(getActivity(), file);
                intent = new Intent(Intent.ACTION_VIEW).setDataAndType(downloaded_apk,
                        "application/vnd.android.package-archive");
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            } else {
                intent = new Intent("android.intent.action.INSTALL_PACKAGE");
                intent.setDataAndType(Uri.fromFile(file),
                        "application/vnd.android.package-archive");
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
            intent.putExtra(Intent.EXTRA_NOT_UNKNOWN_SOURCE, true);
            intent.putExtra("android.intent.extra.RETURN_RESULT", false);
            intent.putExtra("android.intent.extra.INSTALLER_PACKAGE_NAME", getActivity().getPackageName());
            startActivity(intent);
        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    Uri getFileUri(Context context, File file) {
        return FileProvider.getUriForFile(context,
                "com.livetv.normal.fileprovider"
                , file);
    }

    @Override
    public void onDownloadUpdateError(int error) {
        this.downloadProgress.dismiss();
        OnClickListener listener = new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                AccountDetailsFragment.this.getActivity().finish();
            }
        };
        switch (error) {
            case 1:
                Dialogs.showOneButtonDialog((Activity) getActivity(), (int) R.string.verify_unknown_sources, listener);
                return;
            default:
                Dialogs.showOneButtonDialog((Activity) getActivity(), (int) R.string.new_version_generic_error_message, listener);
                return;
        }
    }

    /* access modifiers changed from: private */
    public void goToNoConnectionError() {
        noInternetConnection(new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

            }
        });
    }


    public void onCloseSessionSelected() {
        DataManager.getInstance().saveData("theUser", "");
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finishActivity();
    }

    public void onCloseSessionNoInternet() {
        Dialogs.showOneButtonDialog((Activity) getActivity(), (int) R.string.no_connection_title, (int) R.string.close_session_no_internet, (OnClickListener) new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });
    }

    public void onError() {
        finishActivity();
    }
}
