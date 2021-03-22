package com.livetv.normal.view;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Process;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.livetv.normal.R;
import com.livetv.normal.listeners.DialogListener;
import com.livetv.normal.utils.Connectivity;
import com.livetv.normal.utils.DataManager;
import com.livetv.normal.utils.Device;
import com.livetv.normal.utils.Dialogs;
import com.livetv.normal.viewmodel.Lifecycle;
import com.livetv.normal.viewmodel.SplashViewModel;
import com.livetv.normal.viewmodel.SplashViewModelContract;

import java.io.File;

public class SplashFragment extends BaseFragment implements SplashViewModelContract.View {
    public static final int BLOCKED_OR_NEVER_ASKED = 2;
    public static final int DENIED = 1;
    public static final int GRANTED = 0;
    static final int INSTALL_REQUEST_CODE = 1;
    private static final int REQUEST_STORAGE_STATE = 1;
    boolean denyAll = false;
    /* access modifiers changed from: private */
    public ProgressDialog downloadProgress;
    private boolean isInit = false;
    private int serieId;
    /* access modifiers changed from: private */
    public SplashViewModel splashViewModel;
    private String updateLocation = null;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //HttpRequest.getInstance().trustAllHosts();
        this.splashViewModel = new SplashViewModel();
    }

    /* access modifiers changed from: protected */
    public void onConfigurationChanged() {
    }

    public android.view.View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.splash_fragment, container, false);
    }

    public void onStart() {
        super.onStart();
    }

    public void onResume() {
        super.onResume();
        /*if (!this.isInit) {
            this.splashViewModel.login();
            this.isInit = true;
        }*/

        if (!isInit) {
            if (getPermissionStatus("android.permission.WRITE_EXTERNAL_STORAGE") != 0) {
                requestStoragePermission();
            } else {
                splashViewModel.login();
                isInit = true;
            }
        }
    }

    /* access modifiers changed from: protected */
    public Lifecycle.ViewModel getViewModel() {
        return this.splashViewModel;
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
        Dialogs.showCantGoBackToast();
        return true;
    }

    public void onLoginCompleted(boolean success) {
        if(success){
            launchActivity(MainCategoriesMenuActivity.class);
            finishActivity();
        }
        else{
            if(Connectivity.isConnected()){
                launchActivity(LoginActivity.class);
                finishActivity();
            }
            else{

                noInternetConnection(new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finishActivity();
                    }
                });
            }
        }
    }

    @Override
    public void onLoginError(String errorFound) {
        switch (errorFound) {
            case "103":
            case "104":
                showErrorMessage(getString(R.string.login_error_change_device), errorFound);
                break;
            case "106":
            case "105":
                launchActivity(LoginActivity.class);
                finishActivity();
                break;
            case "107":
                showErrorMessage(getString(R.string.login_error_expired), errorFound);
                break;
            case "108": {
                showErrorMessage(getString(R.string.login_error_change_account).replace("{ID}", Device.getIdentifier()), errorFound);
            }
            break;
            case "109": {
                showErrorMessage(getString(R.string.login_error_demo), errorFound);
            }
            break;
            case "110": {
                showErrorMessage(getString(R.string.ip_limitation), errorFound);
            }
            break;
            default:
                showErrorMessage("Estimado " + /*LiveTvApplication.getUser().getName() + */"su cuenta a sido desactivada, porfavor comunicate con tu vendedor.", errorFound);
                break;
        }
    }

    public void showErrorMessage(String message, final String error_found) {

        if (Connectivity.isConnected()) {
            Dialogs.showOneButtonDialog(getActivity(), getString(R.string.attention), message, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    closeApp();
                }
            });
        } else {
            noInternetConnection(new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    closeApp();
                }
            });
        }

    }

    public void closeApp() {
        getActivity().finishAffinity();
        System.exit(0);
    }

    public void onCheckForUpdateCompleted(boolean hasNewVersion, final String location) {
        this.updateLocation = location;
        if (hasNewVersion) {
            Resources res = getResources();
            Dialogs.showTwoButtonsDialog((Activity) getActivity(), (int) R.string.download, (int) R.string.cancel, getResources().getString(R.string.new_version_available), (DialogListener) new DialogListener() {
                public void onAccept() {
                    if (SplashFragment.this.getPermissionStatus("android.permission.WRITE_EXTERNAL_STORAGE") != 0) {
                        SplashFragment.this.requestStoragePermission();
                    } else if (Connectivity.isConnected()) {
                        downloadProgress = new ProgressDialog(getActivity(), ProgressDialog.THEME_HOLO_LIGHT);
                        downloadProgress.setProgressStyle(1);
                        downloadProgress.setMessage("Downloading");
                        downloadProgress.setIndeterminate(false);
                        downloadProgress.setCancelable(false);
                        downloadProgress.show();
                        splashViewModel.downloadUpdate(location, downloadProgress);
                    } else {
                        SplashFragment.this.goToNoConnectionError();
                    }
                }

                public void onCancel() {
                    splashViewModel.login();
                }
            });

        } else {
            splashViewModel.login();
            finishActivity();
        }


    }

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
            finishActivity();
            startActivity(intent);
        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    Uri getFileUri(Context context, File file) {
        return FileProvider.getUriForFile(context,
                "com.uni.julio.supertv.fileprovider"
                , file);
    }
    public void onDownloadUpdateError(int error) {
        this.downloadProgress.dismiss();
        OnClickListener listener = new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                SplashFragment.this.getActivity().finish();
            }
        };
        switch (error) {
            case 1:
                Dialogs.showOneButtonDialog((Activity) getActivity(), (int) R.string.verify_unknown_sources, listener);
                splashViewModel.login();
                return;
            default:
                Dialogs.showOneButtonDialog((Activity) getActivity(), (int) R.string.new_version_generic_error_message, listener);
                splashViewModel.login();
                return;
        }
    }

    /* access modifiers changed from: private */
    public void goToNoConnectionError() {
        noInternetConnection(new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                SplashFragment.this.launchActivity(LoginActivity.class);
                SplashFragment.this.getActivity().finish();
            }
        });
    }

    public int getPermissionStatus(String androidPermissionName) {
        if (ContextCompat.checkSelfPermission(getActivity(), androidPermissionName) == 0) {
            return 0;
        }
        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), androidPermissionName) || !DataManager.getInstance().getBoolean("storagePermissionRequested", false)) {
            return 1;
        }
        return 2;
    }

    public boolean requestStoragePermission() {
        if (VERSION.SDK_INT < 23 || getPermissionStatus("android.permission.WRITE_EXTERNAL_STORAGE") == 0) {
            return true;
        }
        this.denyAll = false;
        int accept = R.string.accept;
        int message = R.string.permission_storage;
        if (getPermissionStatus("android.permission.WRITE_EXTERNAL_STORAGE") == 2) {
            this.denyAll = true;
            accept = R.string.config;
            message = R.string.permission_storage_config;
        }
        Dialogs.showTwoButtonsDialog((Activity) getActivity(), accept, (int) R.string.cancel, message, (DialogListener) new DialogListener() {
            @TargetApi(23)
            public void onAccept() {
                if (!SplashFragment.this.denyAll) {
                    DataManager.getInstance().saveData("storagePermissionRequested", Boolean.TRUE);
                    SplashFragment.this.requestPermissions(new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"}, 1);
                    return;
                }
                Intent intent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
                intent.setData(Uri.fromParts("package", SplashFragment.this.getActivity().getPackageName(), null));
                SplashFragment.this.startActivityForResult(intent, 4168);
            }

            public void onCancel() {
                SplashFragment.this.finishActivity();
                Process.killProcess(Process.myPid());
            }
        });
        return false;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode != 4168) {
            return;
        }
        if (getPermissionStatus("android.permission.WRITE_EXTERNAL_STORAGE") == 0) {
            //splashViewModel.checkForUpdate();
            splashViewModel.login();
            isInit = true;
        } else {
            finishActivity();
        }
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode != 1) {
            finishActivity();
        }
        if (getPermissionStatus("android.permission.WRITE_EXTERNAL_STORAGE") == 0) {
            //splashViewModel.checkForUpdate();
            splashViewModel.login();
            isInit = true;
        } else {
            finishActivity();
        }
    }

    private void downloadUpdate(String location) {
        if (Connectivity.isConnected()) {
            this.downloadProgress = new ProgressDialog(getActivity());
            this.downloadProgress.setProgressStyle(1);
            this.downloadProgress.setMessage("Downloading");
            this.downloadProgress.setIndeterminate(false);
            this.downloadProgress.setCancelable(false);
            this.downloadProgress.show();
            this.splashViewModel.downloadUpdate(location, this.downloadProgress);
            return;
        }
        goToNoConnectionError();
    }
}
