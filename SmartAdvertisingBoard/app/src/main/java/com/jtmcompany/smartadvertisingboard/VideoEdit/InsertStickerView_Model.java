package com.jtmcompany.smartadvertisingboard.VideoEdit;

import com.jtmcompany.smartadvertisingboard.StickerView.StickerView;

public class InsertStickerView_Model {
private StickerView mStickerView;
private int insert_start_time;
private int insert_end_time;
private int width;
private int height;
private float rotatation;

    public float getRotatation() {
        return rotatation;
    }

    public void setRotatation(float rotatation) {
        this.rotatation = rotatation;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }


    public InsertStickerView_Model(StickerView mStickerView) {
        this.mStickerView = mStickerView;
    }

    public StickerView getmStickerView() {
        return mStickerView;
    }

    public void setInsert_start_time(int insert_start_time) {
        this.insert_start_time = insert_start_time;
    }

    public void setInsert_end_time(int insert_end_time) {
        this.insert_end_time = insert_end_time;
    }

    public int getInsert_start_time() {
        return insert_start_time;
    }

    public int getInsert_end_time() {
        return insert_end_time;
    }
}
