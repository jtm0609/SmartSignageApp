package com.jtmcompany.smartadvertisingboard.myvideo;

import android.graphics.Bitmap;

public class MyVideoModel {
    private Bitmap img;
    private String title;
    private String path;


    public MyVideoModel(Bitmap img, String title, String path) {
        this.img = img;
        this.title = title;
        this.path=path;

    }


    public String getPath() {
        return path;
    }
    public Bitmap getImg() {
        return img;
    }
    public String getTitle() {
        return title;
    }



}
