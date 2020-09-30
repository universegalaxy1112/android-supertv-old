package com.livetv.normal.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;

import com.google.gson.Gson;
import com.livetv.normal.R;
import com.livetv.normal.LiveTvApplication;
import com.livetv.normal.model.Movie;
import com.livetv.normal.utils.Device;
import com.livetv.normal.view.exoplayer.VideoActivity;

public class MovieDetailsActivity extends BaseActivity {
    private MovieDetailsTVFragment moviesDetailsTVFragment;
    private MovieDetailsFragment moviesMenuFragment;

    public BaseFragment getFragment() {
        return this.moviesMenuFragment;
    }

    public Fragment getTVFragment() {
        return this.moviesDetailsTVFragment;
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.movie_details_activity);
        Bundle extras = getIntent().getExtras();
        int mainCategoryId = extras.getInt("mainCategoryId", 0);
        if (mainCategoryId == 4 || mainCategoryId == 6 || mainCategoryId == 7) {
            onPlaySelected((Movie) new Gson().fromJson(extras.getString("movie"), Movie.class), mainCategoryId);
            finish();
        } else if (Device.treatAsBox) {
            this.moviesDetailsTVFragment = new MovieDetailsTVFragment();
            this.moviesDetailsTVFragment.setArguments(extras);
            getSupportFragmentManager().beginTransaction().add(R.id.movie_details_container, this.moviesDetailsTVFragment).commit();
            findViewById(R.id.movie_details_container).setBackgroundColor(ContextCompat.getColor(this, R.color.transparent));
        } else {
            this.moviesMenuFragment = new MovieDetailsFragment();
            this.moviesMenuFragment.setArguments(extras);
            getSupportFragmentManager().beginTransaction().add((int) R.id.movie_details_container, this.moviesMenuFragment).commit();
        }
    }

    public void onPlaySelected(Movie movie, int mainCategoryId) {
        int movieId = movie.getContentId();
        String[] uris = {movie.getStreamUrl()};
        String[] extensions = {movie.getStreamUrl().substring(movie.getStreamUrl().replace(".mkv.mkv", ".mkv").replace(".mp4.mp4", ".mp4").lastIndexOf(".") + 1)};
        Intent launchIntent = new Intent(LiveTvApplication.getAppContext(), VideoActivity.class);
        launchIntent.putExtra("uri_list", uris).putExtra("extension_list", extensions).putExtra("movie_id_extra", movieId).putExtra("seconds_to_start", 0).putExtra("mainCategoryId", mainCategoryId).putExtra("subsURL", movie.getSubtitleUrl()).setAction("com.google.android.exoplayer.demo.action.VIEW_LIST").putExtra("title", movie.getTitle());
        startActivity(launchIntent);
        overridePendingTransition(R.anim.right_in, R.anim.left_out);
    }
}
