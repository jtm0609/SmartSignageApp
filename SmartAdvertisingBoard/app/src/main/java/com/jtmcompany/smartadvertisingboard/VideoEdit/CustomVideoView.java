package com.jtmcompany.smartadvertisingboard.VideoEdit;

import android.content.Context;
import android.widget.VideoView;

public class CustomVideoView extends VideoView {
    private int mWidth;
    private int mHeight;

    public CustomVideoView(Context context) {
        super(context);
    }

    public void setWidth(int width) {
        this.mWidth = width;
    }

    public void setHeight(int height) {
        this.mHeight = height;
    }
    public void resize(int width, int height){
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = getDefaultSize(mWidth, widthMeasureSpec);
        int height = getDefaultSize(mHeight, heightMeasureSpec);
        if (mWidth > 0 && mHeight > 0) {
            if (mWidth * height > width * mHeight) {
                // Log.i("@@@", "image too tall, correcting");
                height = width * mHeight / mWidth;
            } else if (mWidth * height < width * mHeight) {
                // Log.i("@@@", "image too wide, correcting");
                width = height * mWidth / mHeight;
            } else {
                // Log.i("@@@", "aspect ratio is correct: " +
            }
        }

    }
}
