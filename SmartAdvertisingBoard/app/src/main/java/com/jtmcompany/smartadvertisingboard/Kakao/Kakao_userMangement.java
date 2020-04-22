package com.jtmcompany.smartadvertisingboard.Kakao;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.jtmcompany.smartadvertisingboard.Http_Request_MyServerDB;
import com.jtmcompany.smartadvertisingboard.LoginInfo;
import com.kakao.auth.ApiErrorCode;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeV2ResponseCallback;
import com.kakao.usermgmt.response.MeV2Response;

public class Kakao_userMangement {
    Context mcontext;

    public Kakao_userMangement(Context mcontext) {
        this.mcontext = mcontext;
    }

    public void requstme(){
        //사용자 정보 요청결과에 대한 Callback
        UserManagement.getInstance().me(new MeV2ResponseCallback() {

            @Override
            public void onFailure(ErrorResult errorResult) {
                //로그인에 실패했을 때. 인터넷 연결이 불안정한 경우도 여기에 해당한다.
                super.onFailure(errorResult);
                int result=errorResult.getErrorCode();
                if(result== ApiErrorCode.CLIENT_ERROR_CODE) {
                    Toast.makeText(mcontext, "네트워크 연결이 불안정합니다. 다시시도해주세요.", Toast.LENGTH_SHORT).show();
                } else{
                    Log.d("tak","로그인 도중 문제생김");
                    Toast.makeText(mcontext, "로그인 도중에 문제가 생겼습니다.", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onSessionClosed(ErrorResult errorResult) {
                //로그인 도중 세션이 비정상적인 이유로 닫혔을 때
                Toast.makeText(mcontext, "세션이 닫혔습니다. 다시 시도해주세요", Toast.LENGTH_SHORT).show();
                Log.d("tak","세션 닫힘");

            }

            @Override
            public void onSuccess(MeV2Response result) {
                //로그인에 성공했을 때
                Log.d("tak","onSuccess");
                Log.d("tak","name: "+result.getNickname());
                Log.d("tak","profile: "+result.getProfileImagePath());
                Log.d("tak","id:  "+result.getId());
                Intent intent=new Intent(mcontext, LoginInfo.class);
                intent.putExtra("name",result.getNickname());
                intent.putExtra("profile",result.getProfileImagePath());

                //자동로그인이아닌 버튼으로 로그인하는경우,
                SharedPreferences sharedPreferences= mcontext.getSharedPreferences("kakaoBtLogin",mcontext.MODE_PRIVATE);
                Boolean Bt_OK=sharedPreferences.getBoolean("Bt_OK",false);
                Log.d("tak3","Bt_OK: "+Bt_OK);
                String name=result.getNickname();
                if(Bt_OK) {
                    Http_Request_MyServerDB http_request_myServerDB=new Http_Request_MyServerDB(name,"kakao",null);
                    http_request_myServerDB.execute();
                    Log.d("tak3","kkoLogin_Bt");
                    mcontext.startActivity(intent);
                    ((Activity) mcontext).finish();
                }
            }
        });
    }
}
