package com.jtmcompany.smartadvertisingboard.Kakao;

import android.content.Context;

import android.util.Log;
import android.widget.Toast;

import com.jtmcompany.smartadvertisingboard.Network_Status_Check;
import com.kakao.auth.ISessionCallback;
import com.kakao.usermgmt.UserManagement;
import com.kakao.util.exception.KakaoException;

public class Kakao_sessionCallback implements ISessionCallback {
    Context mContext;
    Kakao_userMangement userManagement;

    public Kakao_sessionCallback(Context mContext) {
        this.mContext = mContext;
        userManagement=new Kakao_userMangement(mContext);

    }

    @Override
    public void onSessionOpened() {
        //로그인 세션이 열렸을 때.
        Toast.makeText(mContext, "로그인 성공", Toast.LENGTH_SHORT).show();
        //카카오 유저정보 요청
        Log.d("tak3","onSessionOpend");
        userManagement.requstme();
    }

    @Override
    public void onSessionOpenFailed(KakaoException exception) {
        if(Network_Status_Check.getConnectivityStatus(mContext)==3){
            Toast.makeText(mContext, "네트워크 연결을 확인해주세요!", Toast.LENGTH_SHORT).show();
        }else {
            //로그인 세션이 정상적으로 열리지 않았을 때.
            Toast.makeText(mContext, "로그인 실패", Toast.LENGTH_SHORT).show();
            Log.d("tak", "세션 열리지않음");
            Log.d("tak", String.valueOf(exception));
        }
    }


}
