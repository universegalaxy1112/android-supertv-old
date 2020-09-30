package com.livetv.normal.binding;

import android.databinding.BindingAdapter;
import android.graphics.BitmapFactory;
import android.os.Build.VERSION;
import android.support.v17.leanback.widget.Presenter;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.livetv.normal.R;
import com.livetv.normal.LiveTvApplication;
import com.livetv.normal.listeners.ImageLoadedListener;
import com.livetv.normal.model.ImageResponse;
import com.livetv.normal.utils.Connectivity;
import com.livetv.normal.utils.Files;
import com.squareup.picasso.Picasso;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class BindingAdapters {
    @BindingAdapter({"hidden"})
    public static void bindHiddenVisibility(View view, boolean hidden) {
        view.setVisibility(hidden ? View.GONE : View.VISIBLE);
    }

    @BindingAdapter({"invisible"})
    public static void bindInvisibleVisibility(View view, boolean invisible) {
        view.setVisibility(invisible ? View.INVISIBLE : View.VISIBLE);
    }

    @BindingAdapter({"imageUrl"})
    public static void loadImage(ImageView imageView, String url) {
        if (!TextUtils.isEmpty(url)) {

            try {
                Picasso.with(imageView.getContext()).load(url).placeholder((int) R.drawable.imageview_placeholder).into(imageView);
            } catch (IllegalArgumentException e) {
                Log.d("DNLS", "Glide failed");
                Glide.with(imageView.getContext().getApplicationContext()).load(url).placeholder((int) R.drawable.imageview_placeholder).centerCrop().into(imageView);
            }
        }
    }

    public static void retrieveImage(final String imagePath, final ImageLoadedListener listener, final int position) {
        if (imagePath.equals("lupita")) {
            ImageResponse ir = new ImageResponse();
            ir.setPosition(position);
            ir.setBitmap(BitmapFactory.decodeResource(LiveTvApplication.getAppContext().getResources(), R.drawable.search_icon));
            listener.onLoaded(ir);
            return;
        }
        new Thread() {
            public void run() {
                File directory = Files.GetFile(Files.GetCacheDir());
                if (directory != null && !directory.exists()) {
                    directory.mkdirs();
                }
                File imageFile = new File(directory, imagePath.substring(imagePath.lastIndexOf("/"), imagePath.length()));
                if (imageFile.exists()) {
                    ImageResponse ir = new ImageResponse();
                    ir.setPosition(position);
                    ir.setBitmap(Files.getBitmap(imageFile.getPath()));
                    listener.onLoaded(ir);
                } else if (Connectivity.isConnected()) {
                    try {
                        HttpURLConnection conn = (HttpURLConnection) new URL(imagePath).openConnection();
                        conn.setConnectTimeout(5000);
                        conn.setReadTimeout(5000);
                        InputStream is = conn.getInputStream();
                        OutputStream output = new FileOutputStream(imageFile);
                        byte[] data = new byte[1024];
                        while (true) {
                            int count = is.read(data);
                            if (count == -1) {
                                break;
                            }
                            output.write(data, 0, count);
                        }
                        output.flush();
                        output.close();
                        is.close();
                        try {
                            Thread.sleep(100);
                        } catch (Exception e) {
                        }
                        conn.disconnect();
                        ImageResponse ir2 = new ImageResponse();
                        ir2.setPosition(position);
                        ir2.setBitmap(Files.getBitmap(imageFile.getPath()));
                        listener.onLoaded(ir2);
                    } catch (Exception e2) {
                    }
                }
            }
        }.start();
    }

    public static void retrieveImage(final String imagePath, final ImageLoadedListener listener, final int position, final Presenter.ViewHolder holder) {
        if (imagePath.equals("lupita")) {
            ImageResponse ir = new ImageResponse();
            ir.setPosition(position);
            ir.setBitmap(BitmapFactory.decodeResource(LiveTvApplication.getAppContext().getResources(), R.drawable.search_icon));
            listener.onLoaded2(ir, holder);
            return;
        }
        new Thread() {
            public void run() {
                File directory = Files.GetFile(Files.GetCacheDir());
                if (directory != null && !directory.exists()) {
                    directory.mkdirs();
                }
                File imageFile = new File(directory, imagePath.substring(imagePath.lastIndexOf("/"), imagePath.length()));
                if (imageFile.exists()) {
                    ImageResponse ir = new ImageResponse();
                    ir.setPosition(position);
                    ir.setBitmap(Files.getBitmap(imageFile.getPath()));
                    listener.onLoaded2(ir, holder);
                } else if (Connectivity.isConnected()) {
                    try {
                        HttpURLConnection conn = (HttpURLConnection) new URL(imagePath).openConnection();
                        conn.setConnectTimeout(5000);
                        conn.setReadTimeout(5000);
                        InputStream is = conn.getInputStream();
                        OutputStream output = new FileOutputStream(imageFile);
                        byte[] data = new byte[1024];
                        while (true) {
                            int count = is.read(data);
                            if (count == -1) {
                                break;
                            }
                            output.write(data, 0, count);
                        }
                        output.flush();
                        output.close();
                        is.close();
                        try {
                            Thread.sleep(100);
                        } catch (Exception e) {
                        }
                        conn.disconnect();
                        ImageResponse ir2 = new ImageResponse();
                        ir2.setPosition(position);
                        ir2.setBitmap(Files.getBitmap(imageFile.getPath()));
                        listener.onLoaded2(ir2, holder);
                    } catch (Exception e2) {
                    }
                }
            }
        }.start();
    }

    @BindingAdapter({"imageId"})
    public static void setImage(ImageView imageView, int imageId) {
        imageView.setImageResource(imageId);
    }

    @BindingAdapter({"showDuration"})
    public static void setDuration(TextView textView, int seconds) {
        if (seconds <= 0) {
            textView.setVisibility(View.GONE);
            return;
        }
        String str = "";
        try {
            textView.setText(String.format("%01dh %02dmin", new Object[]{Integer.valueOf(seconds / 3600), Integer.valueOf((seconds % 3600) / 60)}));
        } catch (Exception e) {
            textView.setText("");
        }
    }

    @BindingAdapter({"showRating"})
    public static void setRating(RatingBar ratingBar, int rating) {
        try {
            ratingBar.setRating((float) (rating / 20));
        } catch (Exception e) {
            ratingBar.setRating(0.0f);
        }
    }

    @BindingAdapter({"showHDIcon"})
    public static void bindShowHDIcon(ImageView view, boolean isHD) {
        view.setImageResource(isHD ? R.drawable.hd_icon : R.drawable.hd_icon_disabled);
    }

    @BindingAdapter({"showSeenIcon"})
    public static void bindShowSeenIcon(ImageView view, boolean seen) {
        view.setImageResource(seen ? R.drawable.seen_icon : R.drawable.seen_icon_disabled);
    }

    @BindingAdapter({"showFavoriteIcon"})
    public static void bindShowFavoriteIcon(ImageView view, boolean favorite) {
        view.setImageResource(favorite ? R.drawable.favorite_icon : R.drawable.favorite_icon_disabled);
    }

    @BindingAdapter({"setDate"})
    public static void setDate(TextView view, String date) {
        view.setText(date.substring(0, date.indexOf(" ")));
    }

    @BindingAdapter({"justifyText"})
    public static void justifyTextView(TextView view, String text) {
        view.setText(fromHtml("<html><body style=\"text-align:justify\">" + text + "</body></Html>"));
    }

    @BindingAdapter({"loadData"})
    public static void loadDataToWebView(WebView view, String text) {
        view.loadData(String.format(" %s ", new Object[]{"<html><body style=\"text-align:justify\">" + text + "</body></Html>"}), "text/html", "utf-8");
    }

    public static Spanned fromHtml(String html) {
        if (VERSION.SDK_INT >= 24) {
            return Html.fromHtml(html, 0);
        }
        return Html.fromHtml(html);
    }
}
