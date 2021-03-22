package com.livetv.normal.view;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import com.livetv.normal.R;
import com.livetv.normal.utils.Device;

public class MoviesMenuActivity extends BaseActivity {
    private BaseFragment moviesMenuFragment;
    private Fragment moviesMenuTVFragment;

    public BaseFragment getFragment() {
        return this.moviesMenuFragment;
    }

    public Fragment getTVFragment() {
        return this.moviesMenuTVFragment;
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.movies_menu_activity);
        if (Device.treatAsBox) {
            this.moviesMenuTVFragment = new MoviesMenuTVFragment();
            getSupportFragmentManager().beginTransaction().add(R.id.movies_menu_container, this.moviesMenuTVFragment).commit();
            findViewById(R.id.movies_menu_container).setBackgroundColor(ContextCompat.getColor(this, R.color.transparent));
            return;
        }
        this.moviesMenuFragment = new MoviesMenuFragment();
        getSupportFragmentManager().beginTransaction().add((int) R.id.movies_menu_container,  this.moviesMenuFragment).commit();
    }
}
