package com.jtmcompany.smartadvertisingboard.login.Kakao;

import android.app.Application;

import com.kakao.auth.KakaoSDK;

public class GlobalApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        instance=this;
        //Kakao Sdk 초기화
        KakaoSDK.init(new KakaoSDKAdapter());
    }


    @Override
    public void onTerminate() {
        super.onTerminate();
        instance=null;
    }

    private static GlobalApplication instance;

    public static GlobalApplication getGlobalApplicationContext(){
        if(instance==null){
            throw new IllegalStateException("This Application does not inherit com.kakao.GlobalApplication");
        }
        return instance;
    }



}
