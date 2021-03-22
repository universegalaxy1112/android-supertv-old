package com.livetv.normal.view;

import android.os.Bundle;
import android.os.Handler;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import android.view.KeyEvent;

import com.livetv.normal.R;
import com.livetv.normal.utils.Device;

public abstract class BaseActivity extends AppCompatActivity {
    private Handler handler = new Handler();
    /* access modifiers changed from: private */
    public boolean isHandlingEvent = false;

    public abstract BaseFragment getFragment();

    public abstract Fragment getTVFragment();

    /* access modifiers changed from: protected */
    public void onPause() {
        super.onPause();
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        if (!Device.isHDMIStatusSet) {
            Device.setHDMIStatus();
        }
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        if (Device.treatAsBox) {
            setTheme(R.style.Theme_Leanback);
        }
        super.onCreate(savedInstanceState);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (this.isHandlingEvent) {
            return false;
        }
        this.isHandlingEvent = true;
        this.handler.postDelayed(new Runnable() {
            public void run() {
                BaseActivity.this.isHandlingEvent = false;
            }
        }, 300);
        if (!Device.treatAsBox || getTVFragment() == null) {
            return getFragment().onKeyDown(keyCode, event);
        }
        return super.onKeyDown(keyCode, event);
    }
}
