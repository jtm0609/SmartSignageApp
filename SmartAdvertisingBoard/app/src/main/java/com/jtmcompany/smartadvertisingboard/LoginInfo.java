package com.jtmcompany.smartadvertisingboard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.jtmcompany.smartadvertisingboard.Login.LoginActivity;
import com.kakao.auth.ApiResponseCallback;
import com.kakao.auth.AuthService;
import com.kakao.auth.network.response.AccessTokenInfoResponse;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;
import com.kakao.usermgmt.callback.UnLinkResponseCallback;
import com.nhn.android.naverlogin.OAuthLogin;

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


}
