package com.livetv.normal.view;

import android.os.Bundle;
import androidx.fragment.app.Fragment;

import com.livetv.normal.R;
import com.livetv.normal.utils.Screen;

public class MoviesGridActivity extends BaseActivity {
    private MoviesGridFragment moviesGridFragment;

    public BaseFragment getFragment() {
        return this.moviesGridFragment;
    }

    public Fragment getTVFragment() {
        return null;
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.movies_grid_activity);
        this.moviesGridFragment = new MoviesGridFragment();
        getSupportFragmentManager().beginTransaction().add((int) R.id.movies_grid_container,  this.moviesGridFragment).commit();
        Screen.SetScreenDimensions(this);
    }
}
