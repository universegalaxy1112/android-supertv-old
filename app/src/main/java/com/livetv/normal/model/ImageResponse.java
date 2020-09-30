package com.livetv.normal.model;

import android.graphics.Bitmap;

public class ImageResponse {
    private Bitmap bitmap;
    private int position;

    public int getPosition() {
        return this.position;
    }

    public void setPosition(int position2) {
        this.position = position2;
    }

    public Bitmap getBitmap() {
        return this.bitmap;
    }

    public void setBitmap(Bitmap bitmap2) {
        this.bitmap = bitmap2;
    }
}
