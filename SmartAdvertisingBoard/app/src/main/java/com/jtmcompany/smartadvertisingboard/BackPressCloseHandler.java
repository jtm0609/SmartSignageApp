package com.jtmcompany.smartadvertisingboard;

import android.app.Activity;
import android.widget.Toast;

public class BackPressCloseHandler {
    private long backClickTime=0;
    private Activity activity;

    public BackPressCloseHandler(Activity activity) {
        this.activity = activity;
    }

    public void onBackPressed(){
        if(System.currentTimeMillis()>backClickTime+2000){
            Toast.makeText(activity, "뒤로 가기 버튼을 한 번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show();
        }
        if(System.currentTimeMillis()<=backClickTime+2000){
            activity.finish();
        }
    }
}
