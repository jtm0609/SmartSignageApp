package com.jtmcompany.smartadvertisingboard.BottomFragment.Create_Fragment;

import android.graphics.drawable.Drawable;

public class myAdvertise_Model {
    private Drawable img;

    public myAdvertise_Model(Drawable img, String title, String date) {
        this.img = img;
        this.title = title;
        this.date = date;
    }

    private String title;
    private String date;

    public Drawable getImg() {
        return img;
    }


    public String getTitle() {
        return title;
    }


    public String getDate() {
        return date;
    }



}
