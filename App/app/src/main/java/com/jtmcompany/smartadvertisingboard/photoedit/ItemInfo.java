package com.jtmcompany.smartadvertisingboard.photoedit;

public class ItemInfo {
    private String path;
    private int width;
    private int height;
    private double x;
    private double y;


    public ItemInfo(String path, int width, int height, double x, double y) {
        this.path = path;
        this.width = width;
        this.height = height;
        this.x = x;
        this.y = y;

    }

    public String getPath() {
        return path;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }



}
