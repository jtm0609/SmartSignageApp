package com.jtmcompany.smartadvertisingboard.login.Kakao;

import android.content.Context;

import android.util.Log;
import android.widget.Toast;

import com.jtmcompany.smartadvertisingboard.login.Network_Status_Check;
import com.kakao.auth.ISessionCallback;
import com.kakao.util.exception.KakaoException;
//로그인 성공여부 확인
public class Kakao_sessionCallback implements ISessionCallback {
    private Context mContext;
    private Kakao_userMangement userManagement;

    public Kakao_sessionCallback(Context mContext) {
        this.mContext = mContext;
        userManagement=new Kakao_userMangement(mContext);
    }

    //세션 콜백 구현
    /**
     세션이란 일정 시간동안 같은 사용자(정확하게 브라우저를 말한다)로 부터 들어오는
    일련의 요구를 하나의 상태로 보고 그 상태를 일정하게 유지시키는 기술이라고 한다.
     **/

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
