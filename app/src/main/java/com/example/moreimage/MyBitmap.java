package com.example.moreimage;

import android.graphics.Bitmap;

/**
 * Created by 丁瑞 on 2017/4/13.
 */

public class MyBitmap {
    String path;
    Bitmap bm;

    public MyBitmap(String path, Bitmap bm) {
        this.path = path;
        this.bm = bm;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Bitmap getBm() {
        return bm;
    }

    public void setBm(Bitmap bm) {
        this.bm = bm;
    }
}
