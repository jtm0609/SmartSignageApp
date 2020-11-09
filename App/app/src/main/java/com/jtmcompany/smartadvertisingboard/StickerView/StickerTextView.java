package com.jtmcompany.smartadvertisingboard.StickerView;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;



public class StickerTextView extends StickerView {

    public AutoResizeTextView tv_main;





    public StickerTextView(Context context) {
        super(context);



    }



    public StickerTextView(Context context, AttributeSet attrs) {

        super(context, attrs);

    }



    public StickerTextView(Context context, AttributeSet attrs, int defStyle) {

        super(context, attrs, defStyle);

    }



    @Override

    public View getMainView() {
        setText_TAG();

        if(tv_main != null)

            return tv_main;



        tv_main = new AutoResizeTextView(getContext());

        //tv_main.setTextSize(22);

        tv_main.setTextColor(Color.BLUE);


        tv_main.setGravity(Gravity.CENTER);

        tv_main.setTextSize(400);

        tv_main.setShadowLayer(4, 0, 0, Color.BLACK);

        tv_main.setMaxLines(1);

        LayoutParams params = new LayoutParams(

                ViewGroup.LayoutParams.MATCH_PARENT,

                ViewGroup.LayoutParams.MATCH_PARENT

        );

        params.gravity = Gravity.CENTER;

        tv_main.setLayoutParams(params);

        if(getImageViewFlip()!=null)

            getImageViewFlip().setVisibility(View.GONE);

        return tv_main;

    }



    public void setText(String text){

        if(tv_main!=null)

            tv_main.setText(text);

    }



    public String getText(){

        if(tv_main!=null)

            return tv_main.getText().toString();



        return null;

    }



    public static float pixelsToSp(Context context, float px) {

        float scaledDensity = context.getResources().getDisplayMetrics().scaledDensity;

        return px/scaledDensity;

    }



    @Override

    protected void onScaling(boolean scaleUp) {

        super.onScaling(scaleUp);

    }

    @Override
    public void onClick(DialogInterface var1, int var2) {

        setText(textEdit.getText().toString());
    }
}