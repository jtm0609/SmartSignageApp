package com.jtmcompany.smartadvertisingboard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class LoginInfo extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_info);


        final Home_Fragment home_fragment = new Home_Fragment();
        final Create_Fragment create_fragment = new Create_Fragment();
        final Mypage_Fragment mypage_fragment = new Mypage_Fragment();

        getSupportFragmentManager().beginTransaction().replace(R.id.container, home_fragment).commit();
        BottomNavigationView bottom_navigationview = findViewById(R.id.bottom_navigation);
        bottom_navigationview.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.home:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, home_fragment).commit();
                        return true;
                    case R.id.create:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, create_fragment).commit();
                        return true;
                    case R.id.mypage:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, mypage_fragment).commit();
                        return true;
                }
                return false;
            }
        });
    }
        /*
        name_tv=findViewById(R.id.name);
        profile_tv=findViewById(R.id.profile);
        kakao_logout_bt=findViewById(R.id.kakao_logout);
        naver_logout_bt=findViewById(R.id.naver_logout);
        google_logout_bt=findViewById(R.id.google_logout);

        mOAuthLoginInstance = OAuthLogin.getInstance();
        Intent intent=getIntent();
        String name=intent.getStringExtra("name");
        String profile=intent.getStringExtra("profile");
        name_tv.setText(name);
        profile_tv.setText(profile);

        tokenMange();

        kakao_logout_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Logout();
            }
        });

        naver_logout_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            mOAuthLoginInstance.logout(LoginInfo.this);
            redirect_login();
            }
        });

        google_logout_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                redirect_login();
            }
        });





    }

    //카카오 로그아웃
    public void Logout(){
        UserManagement.getInstance()
                .requestLogout(new LogoutResponseCallback() {
                    @Override
                    public void onSuccess(Long result) {
                        Toast.makeText(LoginInfo.this, "로그아웃 됬습니다.", Toast.LENGTH_SHORT).show();
                        redirect_login();

                    }

                    @Override
                    public void onCompleteLogout() {
                        Toast.makeText(LoginInfo.this, "로그아웃 됬습니다.2", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    //카카오 회원탈퇴
    public void unLink(){
        UserManagement.getInstance().requestUnlink(new UnLinkResponseCallback() {
            @Override
            public void onFailure(ErrorResult errorResult) {
                super.onFailure(errorResult);
                Toast.makeText(LoginInfo.this, "카카오 연결해제 실패", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onSessionClosed(ErrorResult errorResult) {
                Toast.makeText(LoginInfo.this, "세션이 닫혀있음", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(Long result) {
                Toast.makeText(LoginInfo.this, "연결 끊기 성공", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void tokenMange(){
        AuthService.getInstance().requestAccessTokenInfo(new ApiResponseCallback<AccessTokenInfoResponse>() {
            @Override
            public void onSessionClosed(ErrorResult errorResult) {
                Log.d("tak","세션이 닫혀있음");
            }

            @Override
            public void onFailure(ErrorResult errorResult) {
                Log.d("tak","토큰 정보 요청 실패");
            }

            @Override
            public void onSuccess(AccessTokenInfoResponse result) {
                Log.d("tak2","만료시간: "+result.getExpiresInMillis());
                Log.d("tak2", "사용자아이디: "+result.getUserId());
            }


        });
    }

    public void redirect_login(){

        Intent intent =new Intent(LoginInfo.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

     */

}
