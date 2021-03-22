package com.livetv.normal.view;

import android.os.Bundle;
import androidx.fragment.app.Fragment;

import com.livetv.normal.R;
import com.livetv.normal.utils.Screen;
import com.livetv.normal.utils.Tracking;

public class MainCategoriesMenuActivity extends BaseActivity {
    private MainCategoriesMenuFragment mainCategoriesMenuFragment;

    public BaseFragment getFragment() {
        return this.mainCategoriesMenuFragment;
    }

    public Fragment getTVFragment() {
        return null;
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.main_categories_menu_activity);
        this.mainCategoriesMenuFragment = new MainCategoriesMenuFragment();
        getSupportFragmentManager().beginTransaction().add((int) R.id.main_categories_menu_container,  this.mainCategoriesMenuFragment).commit();
        Screen.SetScreenDimensions(this);
        Tracking.getInstance(this).onStart();
    }
}
