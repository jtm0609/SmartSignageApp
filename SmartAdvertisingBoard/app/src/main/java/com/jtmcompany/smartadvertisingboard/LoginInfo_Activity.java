package com.jtmcompany.smartadvertisingboard;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
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
import com.jtmcompany.smartadvertisingboard.BottomFragment.Create_Fragment.Create_Fragment;
import com.jtmcompany.smartadvertisingboard.BottomFragment.Create_Fragment.UploadActivity;
import com.jtmcompany.smartadvertisingboard.BottomFragment.Home_Fragment;
import com.jtmcompany.smartadvertisingboard.BottomFragment.Mypage_Fragment;

public class LoginInfo_Activity extends AppCompatActivity implements View.OnClickListener {

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle toggle;
    VideoView video_advertise;
    ImageView image_advertise;
    ImageView image_gif;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_info);

        if(ContextCompat.checkSelfPermission(this,Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
        }


        final Home_Fragment home_fragment = new Home_Fragment();
        final Create_Fragment create_fragment = new Create_Fragment();
        final Mypage_Fragment mypage_fragment = new Mypage_Fragment();

        //비디오이미지
        video_advertise=findViewById(R.id.video);
        video_advertise.setOnClickListener(this);
        video_advertise.setVideoURI(Uri.parse("android.resource://com.jtmcompany.smartadvertisingboard/raw/sample_video"));
        video_advertise.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                video_advertise.start();
            }
        });
        video_advertise.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                video_advertise.start();
            }
        });

        //모션이미지
        //이미지 모서리 둥글게
        image_advertise=findViewById(R.id.image_img);
        image_advertise.setOnClickListener(this);
        image_gif=findViewById(R.id.image_gif);
        GradientDrawable drawable= (GradientDrawable) getDrawable(R.drawable.background_rounding);
        image_advertise.setBackground(drawable);
        image_advertise.setClipToOutline(true);
        Glide.with(this).load(R.raw.effect).apply(RequestOptions.bitmapTransform(new RoundedCorners(14))).into(image_gif);

        androidx.appcompat.widget.Toolbar toolbar=findViewById(R.id.navigation_toobar);
        setSupportActionBar(toolbar);

        ActionBar actionbar=getSupportActionBar();
        actionbar.setDisplayShowTitleEnabled(false);
        actionbar.setDisplayHomeAsUpEnabled(true);

        drawerLayout=findViewById(R.id.main_drawer);
        toggle=new ActionBarDrawerToggle(this, drawerLayout, R.string.drawer_open,R.string.drawer_close);

        //네비게이션뷰 모양 색변경(흰색으로)
        DrawerArrowDrawable arrow=toggle.getDrawerArrowDrawable();
        arrow.setColor(Color.WHITE);

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView=findViewById(R.id.navigation_v);
        navigationView.setItemIconTintList(null);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id=item.getItemId();
                if(id==R.id.menuitem1){ //내 비디오함
                    Intent intent=new Intent(LoginInfo_Activity.this,MyVideoActivity.class);
                    startActivity(intent);

                }else if(id==R.id.menuitem2){ //설정
                    Toast.makeText(LoginInfo_Activity.this, "클릭2", Toast.LENGTH_SHORT).show();
                }else if(id==R.id.menuitem3){ //로그아웃
                    Toast.makeText(LoginInfo_Activity.this, "클릭3", Toast.LENGTH_SHORT).show();
                }

                return false;
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(toggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

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
}
