package com.jtmcompany.smartadvertisingboard.Login.Naver;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.jtmcompany.smartadvertisingboard.LoginInfo_Activity;
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
                Log.d("tak","accessToken: "+accessToken);
                Log.d("tak","refreshToken: "+refreshToken);
                Log.d("tak","expires: : "+expiresAt);
                Log.d("tak","TokenType: "+tokenType);
                Log.d("tak","OAuthState: "+mOAuthLoginModule.getState(mcontext));
                if((mOAuthLoginModule.getState(mcontext).toString().equals("OK"))){
                    Log.d("tak3","naverhandler");
                    Intent intent=new Intent(mcontext, LoginInfo_Activity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mcontext.startActivity(intent);

                    ((Activity)mcontext).finish();
                    Log.d("tak","run");

                    naverHttp.naverMember_Info();

                }

            }else{
                String errorCode=mOAuthLoginModule.getLastErrorCode(mcontext).getCode();
                String errorDsec=mOAuthLoginModule.getLastErrorDesc(mcontext);
                Log.d("tak","error code: "+errorCode+" errorDsec: "+errorDsec);
                Toast.makeText(mcontext, "로그인에 실패하였습니다.", Toast.LENGTH_SHORT).show();
            }
        }





}
