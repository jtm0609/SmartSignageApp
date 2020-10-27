package com.jtmcompany.smartadvertisingboard.VideoEdit.VO;

import com.jtmcompany.smartadvertisingboard.StickerView.StickerView;

public class addItem_VO {
    private StickerView stickerView;



    private String path;
    private int start;
    private int end;
    private int width;
    private int height;
    private int X;
    private int Y;



    public addItem_VO(String path, int width, int height, int start_time, int end_time, StickerView stickerView, int X, int Y) {
        this.path = path;
        this.start = start_time;
        this.end = end_time;
        this.width = width;
        this.height = height;
        this.stickerView=stickerView;
        this.X=X;
        this.Y=Y;

    }

    public addItem_VO(StickerView stickerView, int start, int end) {
        this.stickerView = stickerView;
        this.start = start;
        this.end = end;
    }

    public int getX() {
        return X;
    }

    public int getY() {
        return Y;
    }

    public StickerView getStickerView() {
        return stickerView;
    }

    public void setStickerView(StickerView stickerView) {
        this.stickerView = stickerView;
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


    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }
}
