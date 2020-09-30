package com.livetv.normal.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.KeyEvent;
import com.livetv.normal.R;
import com.livetv.normal.utils.Device;
import com.livetv.normal.utils.Screen;

public class SearchActivity extends BaseActivity {
    private SearchFragment searchFragment;
    private Fragment searchTVFragment;

    public BaseFragment getFragment() {
        return this.searchFragment;
    }

    public android.support.v4.app.Fragment getTVFragment() {
        return this.searchTVFragment;
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.search_activity);
        if (Device.treatAsBox) {
            this.searchTVFragment = new SearchTVFragment();
            getSupportFragmentManager().beginTransaction().add(R.id.search_container, this.searchTVFragment).commit();
            findViewById(R.id.search_container).setBackgroundColor(ContextCompat.getColor(this, R.color.transparent));
        } else {
            this.searchFragment = new SearchFragment();
            getSupportFragmentManager().beginTransaction().add((int) R.id.search_container,  this.searchFragment).commit();
        }
        Screen.SetScreenDimensions(this);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (!Device.treatAsBox) {
            this.searchFragment.onKeyDown(keyCode, event);
        } else {
            ((SearchTVFragment) this.searchTVFragment).onKeyDown(keyCode, event);
        }
        return false;
    }
}
