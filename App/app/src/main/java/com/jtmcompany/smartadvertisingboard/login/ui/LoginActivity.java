package com.jtmcompany.smartadvertisingboard.login.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.jtmcompany.smartadvertisingboard.login.Google.Google_FirebaseAuth;
import com.jtmcompany.smartadvertisingboard.login.Kakao.Kakao_sessionCallback;
import com.jtmcompany.smartadvertisingboard.login.Naver.NaverOAuthLoginHandler;
import com.jtmcompany.smartadvertisingboard.login.Naver.Naver_AuthInfo;
import com.jtmcompany.smartadvertisingboard.login.Naver.Naver_RefreshTokenTask;
import com.jtmcompany.smartadvertisingboard.login.Network_Status_Check;
import com.jtmcompany.smartadvertisingboard.ui.SelectEditActivity;
import com.jtmcompany.smartadvertisingboard.R;
import com.kakao.auth.Session;
import com.kakao.usermgmt.LoginButton;
import com.nhn.android.naverlogin.OAuthLogin;
import com.nhn.android.naverlogin.data.OAuthLoginState;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private OAuthLogin mOAuthLoginModule;
    private NaverOAuthLoginHandler mNaverHandler;
    private Naver_RefreshTokenTask naver_refreshTokenTask;
    private Kakao_sessionCallback kakao_sessionCallback;
    private LoginButton hiden_KakaoLoginBt;
    private Google_FirebaseAuth google_firebaseAuth;

    private ImageView naverLoginBt;
    private ImageView kakaoLoginBt;
    private ImageView googleLoginBt;
    private Button emailLoginBt;
    private GradientDrawable gradientDrawable;

    private FirebaseUser mAuthUser;

    private int GOOGLE_RC_SIGN_IN=9001;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        gradientDrawable=(GradientDrawable)getDrawable(R.drawable.background_rounding);

        initView();

        emailLoginBt.setOnClickListener(this);

        googleLoginSetting(); //구글 로그인 설정
        naverLoginSetting(); //네이버 로그인 설정
        kakaoLoginSetting(); //카카오 로그인 설정


    }

    protected void initView(){
        emailLoginBt=findViewById(R.id.email_login); //이메일 로그인
        googleLoginBt=findViewById(R.id.custom_google_login); //구글 로그인
        naverLoginBt=findViewById(R.id.custom_naver_login); //네이버 로그인
        kakaoLoginBt=findViewById(R.id.custom_kakao_login); //카카오 로그인
        hiden_KakaoLoginBt=findViewById(R.id.kakao_login);
    }

    //구글 로그인
    protected void googleLoginSetting(){
        //구글 로그인
        googleLoginBt.setBackground(gradientDrawable);
        googleLoginBt.setClipToOutline(true);
        googleLoginBt.setOnClickListener(this);
        mAuthUser=FirebaseAuth.getInstance().getCurrentUser();
        google_firebaseAuth=new Google_FirebaseAuth(this);

        //구글로그인이 되어있다면 -> 자동로그인
        if(mAuthUser!=null){
            Log.d("tak4","userOK");
            Intent intent=new Intent(LoginActivity.this, SelectEditActivity.class);
            startActivity(intent);
            finish();
        }else{
            Log.d("tak4","userNO");
        }
    }

    //네이버 로그인
    protected void naverLoginSetting(){
        naverLoginBt.setBackground(gradientDrawable);
        naverLoginBt.setClipToOutline(true);
        naverLoginBt.setOnClickListener(this);

        //naver_login_bt.setOAuthLoginHandler(mNaverHandler);
        //네이버 핸들러, refresh토큰작업, 네이버로그인모듈 초기화작업
        mNaverHandler=new NaverOAuthLoginHandler(this);
        naver_refreshTokenTask=new Naver_RefreshTokenTask(this);
        mOAuthLoginModule=OAuthLogin.getInstance();
        mOAuthLoginModule.init(this,Naver_AuthInfo.getOauthClientId(),
                Naver_AuthInfo.getOauthClientSecret(),
                Naver_AuthInfo.getOauthClientName());

        //네이버 로그인으로 자동로그인
        //네이버 access 토큰이 만료되면 refresh 토큰으로갱신해줌
        if(mOAuthLoginModule.getState(this).equals(OAuthLoginState.NEED_REFRESH_TOKEN)){
            naver_refreshTokenTask.execute();
            Log.d("login_naver","token: "+mOAuthLoginModule.getAccessToken(this));
        }

        //네이버 access토큰이있는상태라면->자동 로그인
        if(mOAuthLoginModule.getState(this).equals(OAuthLoginState.OK)){
            Log.d("login_naver","naverLogin");
            Log.d("login_naver","state: "+mOAuthLoginModule.getState(this));

            Intent intent=new Intent(LoginActivity.this, SelectEditActivity.class);
            startActivity(intent);
            finish();

        }
    }

    //카카오 로그인
    protected void kakaoLoginSetting(){
        //카카오 세션콜백 초기화 및 세션에 콜백등록
        kakao_sessionCallback=new Kakao_sessionCallback(this);
        Session session=Session.getCurrentSession();
        session.addCallback(kakao_sessionCallback);

        kakaoLoginBt.setBackground(gradientDrawable);
        kakaoLoginBt.setClipToOutline(true);
        kakaoLoginBt.setOnClickListener(this);

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
            Intent intent=new Intent(LoginActivity.this, SelectEditActivity.class);
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 카카오톡|스토리 간편로그인 실행 결과를 받아서 SDK로 전달
        if(Session.getCurrentSession().handleActivityResult(requestCode,resultCode,data)){
            Log.d("tak3","reaquestCode: "+requestCode);
            Log.d("tak3","resultCode: "+resultCode);
            Log.d("tak3","data: "+data);
            return;

            //구글 로그인 결과
        }else if(requestCode==GOOGLE_RC_SIGN_IN){
            Task<GoogleSignInAccount> result=GoogleSignIn.getSignedInAccountFromIntent(data);
            try{
                //구글 로그인 성공해서 파베에 인증 요청
                if(result.isSuccessful()) {
                    //로그인 시도한 구글계정
                    GoogleSignInAccount account = result.getResult(ApiException.class);
                    //로그인을 시도한 구글계정을 파이어베이스에 인증한다.
                    google_firebaseAuth.firebaseAuthWithGoogle(account);
                }
                //구글 로그인 실패
                else {

                }
            }catch (Exception e){
                e.printStackTrace();
            }

        }
    }




    @Override
    public void onClick(View v) {
        if(v==naverLoginBt){
            mOAuthLoginModule.startOauthLoginActivity(LoginActivity.this,mNaverHandler);
        }else if(v==kakaoLoginBt){
            hiden_KakaoLoginBt.performClick();
        }else if(v==googleLoginBt){
            google_firebaseAuth.signIn();
        }else if(v==emailLoginBt){
            if(Network_Status_Check.getConnectivityStatus(LoginActivity.this)==3){
                Toast.makeText(LoginActivity.this, "네트워크 연결을 확인해주세요!", Toast.LENGTH_SHORT).show();
            }else {
                Intent intent = new Intent(LoginActivity.this, EmailLoginActivity.class);
                startActivity(intent);
            }
        }
    }

}

