package com.jtmcompany.smartadvertisingboard.Login.Naver;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.nhn.android.naverlogin.OAuthLogin;

public class NaverOAuthLoginHandler extends com.nhn.android.naverlogin.OAuthLoginHandler {
private Context mcontext;
private OAuthLogin mOAuthLoginModule;
private Http_Request_NaverServer naverHttp;

    public NaverOAuthLoginHandler(Context context) {
        mcontext=context;
        mOAuthLoginModule=OAuthLogin.getInstance();
        naverHttp=new Http_Request_NaverServer(context);
    }
        @Override
        public void run(boolean success) {
            if(success){
                String accessToken=mOAuthLoginModule.getAccessToken(mcontext);
                String refreshToken=mOAuthLoginModule.getRefreshToken(mcontext);
                long expiresAt=mOAuthLoginModule.getExpiresAt(mcontext);
                String tokenType=mOAuthLoginModule.getTokenType(mcontext);
                Log.d("login_naver","accessToken: "+accessToken);
                Log.d("login_naver","refreshToken: "+refreshToken);
                Log.d("login_naver","expires: : "+expiresAt);
                Log.d("login_naver","TokenType: "+tokenType);
                Log.d("login_naver","OAuthState: "+mOAuthLoginModule.getState(mcontext));
                if((mOAuthLoginModule.getState(mcontext).toString().equals("OK"))){
                    Log.d("login_naver","naverhandler");
                    Log.d("login_naver","run");

                    naverHttp.naverMember_Info();
                }

            }else{
                String errorCode=mOAuthLoginModule.getLastErrorCode(mcontext).getCode();
                String errorDsec=mOAuthLoginModule.getLastErrorDesc(mcontext);
                Log.d("login_naver","error code: "+errorCode+" errorDsec: "+errorDsec);
                Toast.makeText(mcontext, "로그인에 실패하였습니다.", Toast.LENGTH_SHORT).show();
            }
        }





}
