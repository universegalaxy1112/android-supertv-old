package com.livetv.normal;

import android.content.Context;
import androidx.multidex.MultiDexApplication;
import android.text.TextUtils;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DataSource.Factory;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.upstream.HttpDataSource;
import com.google.android.exoplayer2.upstream.TransferListener;
import com.google.gson.Gson;
import com.livetv.normal.model.User;
import com.livetv.normal.utils.DataManager;

public class LiveTvApplication extends MultiDexApplication {
    private static Context applicationContext;
    protected String userAgent;

    public void onCreate() {
        super.onCreate();
        applicationContext = getApplicationContext();

    }

    public static Context getAppContext() {
        return applicationContext;
    }

    public Factory buildDataSourceFactory(DefaultBandwidthMeter bandwidthMeter) {
        return new DefaultDataSourceFactory((Context) this, (TransferListener<? super DataSource>) bandwidthMeter, (Factory) buildHttpDataSourceFactory(bandwidthMeter));
    }

    public HttpDataSource.Factory buildHttpDataSourceFactory(DefaultBandwidthMeter bandwidthMeter) {
        this.userAgent = "";
        String theUser = DataManager.getInstance().getString("theUser", "");
        if (!TextUtils.isEmpty(theUser)) {
            this.userAgent = ((User) new Gson().fromJson(theUser, User.class)).getUser_agent();
        }
        return new DefaultHttpDataSourceFactory(this.userAgent, bandwidthMeter);
    }

    public boolean useExtensionRenderers() {
        return "".equals("withExtensions");
    }
}
