package com.livetv.normal.view;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v17.leanback.app.BrowseSupportFragment;

import com.livetv.normal.R;
import com.livetv.normal.model.ModelTypes.SelectedType;
import com.livetv.normal.utils.Dialogs;
import com.livetv.normal.utils.Screen;
import com.livetv.normal.viewmodel.Lifecycle;

public abstract class BaseTVFragment extends BrowseSupportFragment {
    protected int mainCategoryId;
    protected int movieCategoryId;
    protected SelectedType selectedType;

    /* access modifiers changed from: protected */
    public abstract Lifecycle.View getLifecycleView();

    /* access modifiers changed from: protected */
    public abstract Lifecycle.ViewModel getViewModel();

    /* access modifiers changed from: protected */
    public abstract void onConfigurationChanged();

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Screen.SetScreenDimensions(getActivity());
        onConfigurationChanged();
    }

    public void onResume() {
        super.onResume();
        getViewModel().onViewResumed();
    }

    public void onStart() {
        super.onStart();
        getViewModel().onViewAttached(getLifecycleView());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void onStop() {
        super.onStop();
        getViewModel().onViewDetached();
    }

    public void noInternetConnection() {
        Dialogs.showOneButtonDialog(getActivity(), (int) R.string.no_connection_title, (int) R.string.no_connection_message, (OnClickListener) new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                BaseTVFragment.this.finishActivity();
            }
        });
    }

    public void noInternetConnection(OnClickListener listener) {
        Dialogs.showOneButtonDialog(getActivity(), (int) R.string.no_connection_title, (int) R.string.no_connection_message, listener);
    }

    public void launchActivity(Class classToLaunch) {
        startActivity(new Intent(getActivity(), classToLaunch));
        getActivity().overridePendingTransition(R.anim.right_in, R.anim.left_out);
    }

    public void launchActivity(Class classToLaunch, Bundle extras) {
        Intent launchIntent = new Intent(getActivity(), classToLaunch);
        launchIntent.putExtras(extras);
        startActivity(launchIntent);
        getActivity().overridePendingTransition(R.anim.right_in, R.anim.left_out);
    }

    public Intent getLaunchIntent(Class classToLaunch, Bundle extras) {
        Intent launchIntent = new Intent(getActivity(), classToLaunch);
        launchIntent.putExtras(extras);
        return launchIntent;
    }

    public void finishActivity() {
        getActivity().finish();
        getActivity().overridePendingTransition(R.anim.left_in, R.anim.right_out);
    }
}
