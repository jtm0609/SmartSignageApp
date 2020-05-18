package com.jtmcompany.smartadvertisingboard.Login;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.jtmcompany.smartadvertisingboard.Login.Google.Google_FirebaseAuth;
import com.jtmcompany.smartadvertisingboard.Login.Kakao.Kakao_sessionCallback;
import com.jtmcompany.smartadvertisingboard.LoginInfo;
import com.jtmcompany.smartadvertisingboard.Login.Naver.NaverOAuthLoginHandler;
import com.jtmcompany.smartadvertisingboard.Login.Naver.Naver_AuthInfo;
import com.jtmcompany.smartadvertisingboard.Login.Naver.Naver_RefreshTokenTask;
import com.jtmcompany.smartadvertisingboard.R;
import com.kakao.auth.Session;
import com.kakao.usermgmt.LoginButton;
import com.nhn.android.naverlogin.OAuthLogin;
import com.nhn.android.naverlogin.data.OAuthLoginState;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

OAuthLogin mOAuthLoginModule;
NaverOAuthLoginHandler mNaverHandler;
Naver_RefreshTokenTask naver_refreshTokenTask;
Kakao_sessionCallback kakao_sessionCallback;
LoginButton kakao_login_bt;
Google_FirebaseAuth google_firebaseAuth;

ImageView custom_naver_login_bt;
ImageView custom_kakao_login_bt;
ImageView custom_google_login_bt;
Button email_login_bt;


private FirebaseUser mAuthUser;

private int GOOGLE_RC_SIGN_IN=9001;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        GradientDrawable gradientDrawable=(GradientDrawable)getDrawable(R.drawable.background_rounding);

        email_login_bt=findViewById(R.id.email_login);
        email_login_bt.setOnClickListener(this);


        //구글 로그인
        custom_google_login_bt=findViewById(R.id.custom_google_login);
        custom_google_login_bt.setBackground(gradientDrawable);
        custom_google_login_bt.setClipToOutline(true);
        custom_google_login_bt.setOnClickListener(this);
        mAuthUser=FirebaseAuth.getInstance().getCurrentUser();
        google_firebaseAuth=new Google_FirebaseAuth(this);

        if(mAuthUser!=null){
            Log.d("tak4","userOK");
            Intent intent=new Intent(LoginActivity.this, LoginInfo.class);
            startActivity(intent);
            finish();
        }else{
            Log.d("tak4","userNO");
        }


        //네이버 로그인버튼 작업 & 버튼리스너등록
        custom_naver_login_bt=findViewById(R.id.custom_naver_login);
        custom_naver_login_bt.setBackground(gradientDrawable);
        custom_naver_login_bt.setClipToOutline(true);
        custom_naver_login_bt.setOnClickListener(this);
        //naver_login_bt.setOAuthLoginHandler(mNaverHandler);

        //네이버 핸들러, refresh토큰작업, 네이버로그인모듈 초기화작업
        mNaverHandler=new NaverOAuthLoginHandler(this);
        naver_refreshTokenTask=new Naver_RefreshTokenTask(this);
        mOAuthLoginModule=OAuthLogin.getInstance();
        mOAuthLoginModule.init(this,Naver_AuthInfo.getOauthClientId(),
                Naver_AuthInfo.getOauthClientSecret(),
                Naver_AuthInfo.getOauthClientName());

        //네이버 로그인으로 자동로그인
        //네이버 access토큰이 만료되면 갱신해줌
        if(OAuthLoginState.NEED_REFRESH_TOKEN.equals(mOAuthLoginModule.getState(this))){
            naver_refreshTokenTask.execute();
            Log.d("tak","token: "+mOAuthLoginModule.getAccessToken(this));
        }

        //네이버 access토큰이있는상태임
        if(OAuthLoginState.OK.equals(mOAuthLoginModule.getState(this))){
            Log.d("tak3","naverLogin");
            Log.d("tak2","state: "+mOAuthLoginModule.getState(this));
            Intent intent=new Intent(LoginActivity.this,LoginInfo.class);
            startActivity(intent);
            finish();

        }

        //KakaoSDK.init(new GlobalApplication.KakaoSDKAdapter());
        //카카오 세션콜백 초기화 및 세션에 콜백등록
        kakao_sessionCallback=new Kakao_sessionCallback(this);
        Session session=Session.getCurrentSession();
        session.addCallback(kakao_sessionCallback);
        custom_kakao_login_bt=findViewById(R.id.custom_kakao_login);
        kakao_login_bt=findViewById(R.id.kakao_login);
        custom_kakao_login_bt.setBackground(gradientDrawable);
        custom_kakao_login_bt.setClipToOutline(true);
        custom_kakao_login_bt.setOnClickListener(this);

        //카카오버튼으로 로그인했을때를 위해 설정
        SharedPreferences sharedPreferences= getSharedPreferences("kakaoBtLogin",MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();

        //버튼이아닌, 자동으로 로그인했을때,
        // 액세스토큰 유효하거나 리프레시 토큰으로 액세스 토큰 갱신을 시도할 수 있는 경우
        //checkAndImplicitOpen()는 현재 앱에 유효한 카카오 로그인 토큰이 있다면 바로 로그인을 시켜주는 함수
        if (Session.getCurrentSession().checkAndImplicitOpen()) {
            Log.d("tak3","tak:false");
            editor.putBoolean("Bt_OK",false);
            editor.commit();
            Log.d("tak3","kkoLogin");
            Intent intent=new Intent(LoginActivity.this,LoginInfo.class);
            startActivity(intent);
            finish();

            // 무조건 재로그인을 시켜야 하는 경우
            //로그아웃이 된상태
        } else {
            Log.d("tak3","tak:true");
            editor.putBoolean("Bt_OK",true);
            editor.commit();

        }

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //새션 콜백 삭제
        Session.getCurrentSession().removeCallback(kakao_sessionCallback);
    }

    // 카카오톡|스토리 간편로그인 실행 결과를 받아서 SDK로 전달
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(Session.getCurrentSession().handleActivityResult(requestCode,resultCode,data)){
            Log.d("tak","reaquestCode: "+requestCode);
            Log.d("tak","resultCode: "+resultCode);
            Log.d("tak","data: "+data);
            return;
        }else if(requestCode==GOOGLE_RC_SIGN_IN){
            Task<GoogleSignInAccount> task=GoogleSignIn.getSignedInAccountFromIntent(data);
            try{
                GoogleSignInAccount account=task.getResult(ApiException.class);
                google_firebaseAuth.firebaseAuthWithGoogle(account);
            }catch (Exception e){
                e.printStackTrace();
            }

        }
    }




    @Override
    public void onClick(View v) {
        if(v==custom_naver_login_bt){
            mOAuthLoginModule.startOauthLoginActivity(LoginActivity.this,mNaverHandler);
        }else if(v==custom_kakao_login_bt){
            kakao_login_bt.performClick();
        }else if(v==custom_google_login_bt){
            google_firebaseAuth.signIn();
        }else if(v==email_login_bt){
            if(Network_Status_Check.getConnectivityStatus(LoginActivity.this)==3){
                Toast.makeText(LoginActivity.this, "네트워크 연결을 확인해주세요!", Toast.LENGTH_SHORT).show();
            }else {
                Intent intent = new Intent(LoginActivity.this, LoginActivity_Email.class);
                startActivity(intent);
            }


        }



        }

    }

