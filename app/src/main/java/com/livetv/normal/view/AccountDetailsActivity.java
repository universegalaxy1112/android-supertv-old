package com.livetv.normal.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.livetv.normal.R;
import com.livetv.normal.utils.Device;

public class AccountDetailsActivity extends BaseActivity {
    private AccountDetailsFragment accountDetailsFragment;

    public BaseFragment getFragment() {
        return this.accountDetailsFragment;
    }

    public Fragment getTVFragment() {
        return null;
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.account_details_activity);
        if(Device.treatAsBox) {
            setTheme(R.style.AppTheme_NoActionBar);
        }
        Bundle extras = getIntent().getExtras();
        this.accountDetailsFragment = new AccountDetailsFragment();
        getSupportFragmentManager().beginTransaction().add((int) R.id.account_details_container,  this.accountDetailsFragment).commit();
    }
}
