package com.jtmcompany.smartadvertisingboard.BottomFragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.jtmcompany.smartadvertisingboard.Login.LoginActivity;
import com.jtmcompany.smartadvertisingboard.R;
import com.kakao.auth.ApiResponseCallback;
import com.kakao.auth.AuthService;
import com.kakao.auth.network.response.AccessTokenInfoResponse;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;
import com.kakao.usermgmt.callback.UnLinkResponseCallback;
import com.nhn.android.naverlogin.OAuthLogin;

public class Home_Fragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_home,container,false);
        //name_tv=findViewById(R.id.name);
        //profile_tv=findViewById(R.id.profile);
        Button kakao_logout_bt=view.findViewById(R.id.kakao_logout);
        Button naver_logout_bt=view.findViewById(R.id.naver_logout);
        final Button google_logout_bt=view.findViewById(R.id.google_logout);

        final OAuthLogin mOAuthLoginInstance = OAuthLogin.getInstance();
        //Intent intent=getIntent();
        //String name=intent.getStringExtra("name");
        //String profile=intent.getStringExtra("profile");
        //name_tv.setText(name);
        //profile_tv.setText(profile);

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
                mOAuthLoginInstance.logout(getContext());
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



        return view;
    }

    //카카오 로그아웃
    public void Logout(){
        UserManagement.getInstance()
                .requestLogout(new LogoutResponseCallback() {
                    @Override
                    public void onSuccess(Long result) {
                        Toast.makeText(getContext(), "로그아웃 됬습니다.", Toast.LENGTH_SHORT).show();
                        redirect_login();

                    }

                    @Override
                    public void onCompleteLogout() {
                        Toast.makeText(getContext(), "로그아웃 됬습니다.2", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    //카카오 회원탈퇴
    public void unLink(){
        UserManagement.getInstance().requestUnlink(new UnLinkResponseCallback() {
            @Override
            public void onFailure(ErrorResult errorResult) {
                super.onFailure(errorResult);
                Toast.makeText(getContext(), "카카오 연결해제 실패", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onSessionClosed(ErrorResult errorResult) {
                Toast.makeText(getContext(), "세션이 닫혀있음", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(Long result) {
                Toast.makeText(getContext(), "연결 끊기 성공", Toast.LENGTH_SHORT).show();
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

        Intent intent =new Intent(getContext(), LoginActivity.class);
        startActivity(intent);
        getActivity().finish();
    }



    }

