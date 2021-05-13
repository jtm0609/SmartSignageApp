package com.jtmcompany.smartadvertisingboard.login.Kakao;

import android.content.Context;

import androidx.annotation.Nullable;

import com.kakao.auth.ApprovalType;
import com.kakao.auth.AuthType;
import com.kakao.auth.IApplicationConfig;
import com.kakao.auth.ISessionConfig;
import com.kakao.auth.KakaoAdapter;

public class KakaoSDKAdapter extends KakaoAdapter{
    // Application이 가지고 있는 정보(Manifest에 Appkey 등)를 얻기 위한 인터페이스 입니다.
    @Override
    public IApplicationConfig getApplicationConfig() {
        return new IApplicationConfig() {
            @Override
            public Context getApplicationContext() {
                return GlobalApplication.getGlobalApplicationContext();
            }
        };
    }

    //로그인시 사용될 Session의 옵션이다.
    //ISessionConfig는 카카오 로그인과 관련된 설정을 저장하는 객체입니다.
    // 모든 로그인방식 사용(카카오톡, 카카오스토리, 웹뷰등)
    @Override
    public ISessionConfig getSessionConfig() {
        return new ISessionConfig() {
            @Override
            public AuthType[] getAuthTypes() {
                //로그인을 하는 방식을 지정하는부분
                //KAKAO_LOGIN_ALL: 모든 로그인방식 사용 가능. 정확히는, 카카오톡이나 카카오스토리가 있으면 그 쪽으로 로그인 기능을 제공하고, 둘 다 없으면 웹뷰를 통한 로그인을 제공한다.
                return new AuthType[]{AuthType.KAKAO_LOGIN_ALL};
            }

            @Override
            public boolean isUsingWebviewTimer() {
                // SDK 로그인시 사용되는 WebView에서 pause와 resume시에 Timer를 설정하여 CPU소모를 절약한다.
                return false;
            }

            @Override
            public boolean isSecureMode() {
                // 로그인시 access token과 refresh token을 저장할 때의 암호화 여부를 결정한다.
                return false;
            }

            @Nullable
            @Override
            public ApprovalType getApprovalType() {
                return ApprovalType.INDIVIDUAL;
            }

            @Override
            public boolean isSaveFormData() {
                // Kakao SDK 에서 사용되는 WebView에서 email 입력폼의 데이터를 저장할지 여부를 결정한다.
                // true일 경우, 다음번에 다시 로그인 시 email 폼을 누르면 이전에 입력했던 이메일이 나타난다.
                return true;
            }
        };
    }
}
