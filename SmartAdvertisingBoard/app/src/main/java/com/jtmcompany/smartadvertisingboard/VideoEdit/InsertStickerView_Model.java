package com.jtmcompany.smartadvertisingboard.VideoEdit;

import com.jtmcompany.smartadvertisingboard.StickerView.StickerView;

public class InsertStickerView_Model {
private StickerView mStickerView;
private int insert_start_time;
private int insert_end_time;

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
