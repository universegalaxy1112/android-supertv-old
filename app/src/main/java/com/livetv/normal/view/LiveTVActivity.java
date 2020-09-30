/*
package com.livetv.normal.view;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import com.livetv.normal.R;
import com.livetv.normal.LiveTvApplication;
import com.livetv.normal.R;
import com.livetv.normal.listeners.LiveProgramSelectedListener;
import com.livetv.normal.listeners.LiveTVToggleUIListener;
import com.livetv.normal.model.LiveProgram;
import com.livetv.normal.utils.Device;
import com.livetv.normal.utils.Screen;
import com.livetv.normal.utils.Screen.Orientation;
import com.livetv.normal.utils.Tracking;
import com.livetv.normal.view.exoplayer.VideoActivity;
import com.livetv.normal.view.exoplayer.VideoFragment;

public class LiveTVActivity extends BaseActivity implements LiveProgramSelectedListener, LiveTVToggleUIListener {
    private static Orientation lastOrientation = Orientation.NONE;
    private LiveTVFragment liveTVFragment;
    private LiveTVPlaybackFragment liveTVPlaybackFragment;
    private boolean tvFragmentShown = true;

    */
/* renamed from: vf *//*

    private VideoFragment f94vf;
    private VideoFragment vfTV;

    public BaseFragment getFragment() {
        this.liveTVFragment = (LiveTVFragment) getSupportFragmentManager().findFragmentById(R.id.programming_container);
        return this.liveTVFragment;
    }

    public Fragment getTVFragment() {
        return liveTVPlaybackFragment;
    }

    */
/* access modifiers changed from: protected *//*

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.live_tv_activity);
        Screen.SetScreenDimensions(this);
        if (savedInstanceState == null) {
            lastOrientation = Orientation.NONE;
            setupUI();
        }
        if (Screen.getOrientation() == Orientation.LANDSCAPE && lastOrientation == Orientation.PORTRAIT) {
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    ((LiveTVFragment) LiveTVActivity.this.getSupportFragmentManager().findFragmentById(R.id.programming_container)).hideChannels();
                }
            }, 500);
        }
        lastOrientation = Screen.getOrientation();
        Tracking.getInstance(this).setAction("LIVE TV");
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode != 4) {
            return false;
        }
        if (this.tvFragmentShown) {
            finish();
            return true;
        }
        onToggleUI(true);
        this.tvFragmentShown = true;
        return true;
    }

    public void setupUI() {
        if (Device.treatAsBox) {
            this.liveTVPlaybackFragment = new LiveTVPlaybackFragment();
            this.vfTV = new VideoFragment();
            this.vfTV.hideControls(this);
        } else {
            this.liveTVFragment = new LiveTVFragment();
            this.f94vf = new VideoFragment();
            this.f94vf.hideControls(this);
        }
        if (Device.treatAsBox) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.video_player_container, vfTV, "Frag_Top_tag");
            transaction.add(R.id.programming_container, this.liveTVPlaybackFragment, "Frag_Bottom_tag");
            transaction.commit();
            return;
        }
        FragmentTransaction transaction2 = getSupportFragmentManager().beginTransaction();
        transaction2.add(R.id.video_player_container, this.f94vf, "Frag_Top_tag");
        transaction2.add(R.id.programming_container, this.liveTVFragment, "Frag_Bottom_tag");
        transaction2.commit();
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    public void onLiveProgramSelected(LiveProgram liveProgram, int programPosition) {
        Log.d("DNLS", "program Selected: " + liveProgram.getTitle());
        String[] uris = {liveProgram.getStreamUrl()};
        String[] extensions = {liveProgram.getStreamUrl().substring(liveProgram.getStreamUrl().replace(".mkv.mkv", ".mkv").replace(".mp4.mp4", ".mp4").lastIndexOf(".") + 1)};
        Intent launchIntent = new Intent(LiveTvApplication.getAppContext(), VideoActivity.class);
        launchIntent.putExtra("uri_list", uris).putExtra("extension_list", extensions).setAction("com.google.android.exoplayer.demo.action.VIEW_LIST");
        if (Device.treatAsBox) {
            this.tvFragmentShown = false;
            onToggleUI(this.tvFragmentShown);
            this.vfTV = (VideoFragment) getSupportFragmentManager().findFragmentById(R.id.video_player_container);
            this.vfTV.onNewIntent(launchIntent);
            this.vfTV.onStart();
            this.vfTV.onResume();
            return;
        }
        this.f94vf = (VideoFragment) getSupportFragmentManager().findFragmentById(R.id.video_player_container);
        this.f94vf.onNewIntent(launchIntent);
        this.f94vf.onStart();
        this.f94vf.onResume();
    }

    public void onToggleUI(boolean show) {
        if (this.liveTVFragment != null) {
            if (show) {
                this.liveTVFragment.showChannels();
            } else {
                this.liveTVFragment.toggleChannels();
            }
        }
        if (this.liveTVPlaybackFragment != null) {
            if (show) {
                getSupportFragmentManager().beginTransaction().setCustomAnimations(17498112, 17498112).show(this.liveTVPlaybackFragment).commit();
            } else {
                getSupportFragmentManager().beginTransaction().setCustomAnimations(17498112, 17498113).hide(this.liveTVPlaybackFragment).commit();
            }
        }
        if (show) {
            findViewById(R.id.live_tv_action_bar).setVisibility(0);
        } else {
            findViewById(R.id.live_tv_action_bar).setVisibility(8);
        }
    }
}
*/
