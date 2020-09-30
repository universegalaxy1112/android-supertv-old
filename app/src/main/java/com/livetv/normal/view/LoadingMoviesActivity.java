package com.livetv.normal.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.livetv.normal.R;

public class LoadingMoviesActivity extends BaseActivity {
    private LoadingMoviesFragment loadingMoviesFragment;

    public BaseFragment getFragment() {
        return this.loadingMoviesFragment;
    }

    public Fragment getTVFragment() {
        return null;
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.loading_movies_activity);
        this.loadingMoviesFragment = new LoadingMoviesFragment();
        getSupportFragmentManager().beginTransaction().add((int) R.id.loading_fragment_container,  this.loadingMoviesFragment).commit();
    }
}
