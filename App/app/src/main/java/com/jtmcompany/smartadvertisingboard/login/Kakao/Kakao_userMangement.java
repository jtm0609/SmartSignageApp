package com.jtmcompany.smartadvertisingboard.login.Kakao;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.jtmcompany.smartadvertisingboard.login.http.Http_Request_MyServerDB;
import com.jtmcompany.smartadvertisingboard.ui.SelectEditActivity;
import com.kakao.auth.ApiErrorCode;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeV2ResponseCallback;
import com.kakao.usermgmt.response.MeV2Response;

public class Kakao_userMangement {
    private Context mContext;

    public Kakao_userMangement(Context mcontext) {
        this.mContext = mcontext;
    }

    public void requstme(){
        //사용자 정보 요청결과에 대한 Callback
        UserManagement.getInstance().me(new MeV2ResponseCallback() {

            //사용자 정보 요청 실패
            @Override
            public void onFailure(ErrorResult errorResult) {
                super.onFailure(errorResult);
                int result=errorResult.getErrorCode();
                if(result== ApiErrorCode.CLIENT_ERROR_CODE) {
                    Toast.makeText(mContext, "네트워크 연결이 불안정합니다. 다시시도해주세요.", Toast.LENGTH_SHORT).show();
                }

            }

            //세션 오픈 실패. 세션이 삭제된 경우
            @Override
            public void onSessionClosed(ErrorResult errorResult) {
                    Toast.makeText(mContext, "세션이 닫혔습니다. 다시 시도해주세요", Toast.LENGTH_SHORT).show();
                    Log.d("tak", "세션 닫힘");
            }

            //사용자 정보 요청에 성공한 경우
            @Override
            public void onSuccess(MeV2Response result) {
                Log.d("tak","onSuccess");
                Log.d("tak","name: "+result.getNickname());
                Log.d("tak","profile: "+result.getProfileImagePath());
                Log.d("tak","id:  "+result.getId());
                Intent intent=new Intent(mContext, SelectEditActivity.class);
                intent.putExtra("name",result.getNickname());
                intent.putExtra("profile",result.getProfileImagePath());


                //자동로그인이아닌 버튼으로 로그인하는경우,
                SharedPreferences sharedPreferences= mContext.getSharedPreferences("kakaoBtLogin",mContext.MODE_PRIVATE);
                Boolean Bt_OK=sharedPreferences.getBoolean("Bt_OK",false);
                Log.d("tak3","Bt_OK: "+Bt_OK);
                String name=result.getNickname();
                if(Bt_OK) {
                    sharedPreferences=mContext.getSharedPreferences("loginUser",mContext.MODE_PRIVATE);
                    SharedPreferences.Editor editor=sharedPreferences.edit();
                    editor.putString("name",name+ "(카카오 로그인)");
                    editor.commit();
                    Http_Request_MyServerDB http_request_myServerDB=new Http_Request_MyServerDB(name,"kakao",null);
                    http_request_myServerDB.Request_Signup();
                    Log.d("tak3","kkoLogin_Bt");
                    mContext.startActivity(intent);
                    ((Activity) mContext).finish();
                }
            }
        });
    }
}
