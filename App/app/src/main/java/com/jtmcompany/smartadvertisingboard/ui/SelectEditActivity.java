package com.jtmcompany.smartadvertisingboard.ui;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.graphics.drawable.DrawerArrowDrawable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.jtmcompany.smartadvertisingboard.R;
import com.jtmcompany.smartadvertisingboard.login.ui.LoginActivity;
import com.jtmcompany.smartadvertisingboard.myvideo.MyVideoActivity;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;
import com.nhn.android.naverlogin.OAuthLogin;
import com.nhn.android.naverlogin.data.OAuthLoginState;

public class SelectEditActivity extends AppCompatActivity implements View.OnClickListener, MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener, NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private VideoView video_advertise;
    private ImageView image_advertise,image_gif;
    private long backKeyClickTime=0;
    public static ProgressDialog pd;
    private final OAuthLogin mOAuthLoginInstance = OAuthLogin.getInstance(); //네이버
    private androidx.appcompat.widget.Toolbar toolbar;
    private NavigationView navigationView;
    private  String userName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_info);

        //유저 이름을 가져온다.
        SharedPreferences sharedPreferences=getSharedPreferences("loginUser",MODE_PRIVATE);
        userName=sharedPreferences.getString("name","");

        permissionCheck(); //퍼미션체크
        initView(); //뷰 초기화
        mainViewSetting(); //메인화면(비디오뷰, 이미지) 설정
        toolbarSetting(); //툴바 설정
        navigationViewSetting(); //네비게이션뷰 설정

    }

    void initView(){
        video_advertise=findViewById(R.id.video); //비디오광고 비디오뷰
        image_advertise=findViewById(R.id.image_img); //이미지광고 이미지뷰
        image_gif=findViewById(R.id.image_gif); //이미지 광고 GIF
        drawerLayout=findViewById(R.id.main_drawer); //전체 레이아웃
        navigationView=findViewById(R.id.navigation_v);  //네비게이션 뷰 레이아웃
        toolbar=findViewById(R.id.navigation_toobar); //툴바

    }
    public void permissionCheck(){
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
        }
    }

    void mainViewSetting(){
        //비디오뷰 설정
        video_advertise.setOnClickListener(this);
        video_advertise.setVideoURI(Uri.parse("android.resource://com.jtmcompany.smartadvertisingboard/raw/sample_video"));
        video_advertise.setOnPreparedListener(this);
        video_advertise.setOnCompletionListener(this);

        //모션이미지
        //이미지 모서리 둥글게
        image_advertise.setOnClickListener(this);
        GradientDrawable drawable= (GradientDrawable) getDrawable(R.drawable.background_rounding);
        image_advertise.setBackground(drawable);
        image_advertise.setClipToOutline(true);
        Glide.with(this).load(R.raw.effect).apply(RequestOptions.bitmapTransform(new RoundedCorners(14))).into(image_gif);

    }

    void toolbarSetting(){
        //툴바설정
        setSupportActionBar(toolbar);
        ActionBar actionbar=getSupportActionBar();
        actionbar.setDisplayShowTitleEnabled(false);
        actionbar.setDisplayHomeAsUpEnabled(true);
    }

    void navigationViewSetting(){
        //네비게이션뷰 설정
        toggle=new ActionBarDrawerToggle(this, drawerLayout, R.string.drawer_open,R.string.drawer_close);

        //네비게이션뷰 모양 색변경(흰색)
        DrawerArrowDrawable arrow=toggle.getDrawerArrowDrawable();
        arrow.setColor(Color.WHITE);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        //헤더레이아웃에 접근
        //쉐어드 프리퍼런스에 저장한 유저이름과 이미지로 변경
        View header=navigationView.getHeaderView(0);
        TextView name_tv=header.findViewById(R.id.header_name);
        name_tv.setText(userName);
        navigationView.setItemIconTintList(null);

        //콜백 리스너설정
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(toggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //뷰 버튼 콜백
    @Override
    public void onClick(View view) {
        //비디오 광고
        if(view==video_advertise){
            Intent intent =new Intent(this, UploadActivity.class);
            intent.putExtra("upload","video");
            startActivity(intent);

        }
        //이미지 광고
        else if(view==image_advertise){
            Intent intent =new Intent(this, UploadActivity.class);
            intent.putExtra("upload","photo");
            startActivity(intent);
        }
    }

    //카카오 로그아웃
    public void kakaoLogout(){
        UserManagement.getInstance()
                .requestLogout(new LogoutResponseCallback() {
                    @Override
                    public void onSuccess(Long result) {
                        //Toast.makeText(MainActivity.this, "로그아웃 됬습니다.", Toast.LENGTH_SHORT).show();
                        redirect_login();
                    }
                    @Override
                    public void onCompleteLogout() {
                        //Toast.makeText(MainActivity.this, "로그아웃 됬습니다.2", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void redirect_login(){
        Intent intent =new Intent(SelectEditActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    //비디오뷰 콜백
    @Override
    public void onPrepared(MediaPlayer mp) {
        video_advertise.start();
    }
    @Override
    public void onCompletion(MediaPlayer mp) {
        video_advertise.start();
    }

    //네비게이션 아이템 클릭 콜백
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            int id=item.getItemId();
            if(id==R.id.menuitem1){ //내 비디오함
                pd=ProgressDialog.show(SelectEditActivity.this,"로딩중","광고를 가져오는중입니다...");
                Intent intent=new Intent(SelectEditActivity.this, MyVideoActivity.class);
                startActivity(intent);
            }else if(id==R.id.menuitem2){ //설정
            }else if(id==R.id.menuitem5){ //로그아웃
                //로그아웃시 access 토큰, refresh 토큰 삭제됨
                if(OAuthLoginState.OK.equals(mOAuthLoginInstance.getState(SelectEditActivity.this))){
                    mOAuthLoginInstance.logout(SelectEditActivity.this);
                    Toast.makeText(SelectEditActivity.this, "네이버 로그아웃", Toast.LENGTH_SHORT).show();
                    redirect_login();
                }
                else if(FirebaseAuth.getInstance().getCurrentUser()!=null) {
                    FirebaseAuth.getInstance().signOut();
                    Toast.makeText(SelectEditActivity.this, "구글 로그아웃", Toast.LENGTH_SHORT).show();
                    redirect_login();
                }
                else {
                    kakaoLogout();
                    Toast.makeText(SelectEditActivity.this, "카카오 로그아웃", Toast.LENGTH_SHORT).show();
                }

            }
            return false;

    }

    //뒤로가기 제어
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        if (System.currentTimeMillis() > backKeyClickTime + 2000) {
            backKeyClickTime = System.currentTimeMillis();
            Toast.makeText(this, "뒤로 가기 버튼을 한 번 더 누르면 종료 됩니다.", Toast.LENGTH_SHORT).show();
        }else if(System.currentTimeMillis()<=backKeyClickTime+2000)
            finish();


    }
}
