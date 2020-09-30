package com.livetv.normal.view.exoplayer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;

import com.livetv.normal.R;
import com.livetv.normal.listeners.LiveTVToggleUIListener;
import com.livetv.normal.view.exoplayernew.VideoPlayFragment;

public class VideoActivity extends AppCompatActivity implements LiveTVToggleUIListener {

    private VideoPlayFragment videoFragment;
    protected int mainCategoryId = -1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.video_activity);


    }

    @Override
    protected void onStart() {
        super.onStart();

        Intent intent = getIntent();
        mainCategoryId = intent.getIntExtra("mainCategoryId", 0);
        videoFragment = new VideoPlayFragment();
        if (mainCategoryId == 4 || intent.getIntExtra("type", 0) == 2)
            videoFragment.hidePlayBack();
        videoFragment.setLiveTVToggleListener(this);
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.video_container, videoFragment).commit();
    }

    @Override
    public void onNewIntent(Intent intent) {
        videoFragment.onNewIntent(intent);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

           finish();
            return false;
        }
        if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
            videoFragment.dispatchKeyEvent(event);
            return false;
        }
        if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
            // videoPlayFragment.toggleMute();
            sendBroadcast(new Intent("toggle"));

            videoFragment.dispatchKeyEvent(event);
            return false;
        }
        if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
            videoFragment.dispatchKeyEvent(event);
            return false;
        }
        if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER) {
            videoFragment.toggleTitle();
            videoFragment.dispatchKeyEvent(event);
            return false;
        }
        if (keyCode == KeyEvent.KEYCODE_MEDIA_FAST_FORWARD || keyCode == KeyEvent.KEYCODE_FORWARD || keyCode == 272 || keyCode == 274 || keyCode == KeyEvent.KEYCODE_BUTTON_R1 || keyCode == KeyEvent.KEYCODE_BUTTON_R2 || keyCode == KeyEvent.KEYCODE_RIGHT_BRACKET) {
            videoFragment.doForwardVideo();
            return true;
        }
        if (keyCode == KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE) {
            videoFragment.playPause();
            return true;
        }
        if (keyCode == KeyEvent.KEYCODE_MEDIA_REWIND) {
            videoFragment.doRewindVideo();
            return true;
        }
        /*videoPlayFragment.controlVolumn(event);
        super.dispatchKeyEvent(event);*/
        return false;
    }

    @Override
    public void onToggleUI(boolean show) {
        if (mainCategoryId == 4 || getIntent().getIntExtra("type", 0) == 2)
            videoFragment.toggleTitle();

    }
}
