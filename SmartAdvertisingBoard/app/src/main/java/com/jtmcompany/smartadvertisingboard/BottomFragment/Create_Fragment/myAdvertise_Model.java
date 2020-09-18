package com.jtmcompany.smartadvertisingboard.BottomFragment.Create_Fragment;

import android.graphics.drawable.Drawable;

public class myAdvertise_Model {
    private Drawable img;

    public myAdvertise_Model(Drawable img, String title) {
        this.img = img;
        this.title = title;
    }

    private String title;

    public Drawable getImg() {
        return img;
    }


    public String getTitle() {
        return title;
    }





}
