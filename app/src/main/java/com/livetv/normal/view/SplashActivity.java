package com.livetv.normal.view;

import android.os.Bundle;
import androidx.fragment.app.Fragment;

import com.livetv.normal.R;
import com.livetv.normal.utils.Device;

public class SplashActivity extends BaseActivity {
    private SplashFragment splashFragment;

    public BaseFragment getFragment() {
        return this.splashFragment;
    }

    public Fragment getTVFragment() {
        return null;
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Device.setHDMIStatus();
        //HttpRequest.getInstance().trustAllHosts();
        setContentView((int) R.layout.splash_activity);
        this.splashFragment = new SplashFragment();
        getSupportFragmentManager().beginTransaction().add((int) R.id.splash_container,  this.splashFragment).commit();

    }
}
