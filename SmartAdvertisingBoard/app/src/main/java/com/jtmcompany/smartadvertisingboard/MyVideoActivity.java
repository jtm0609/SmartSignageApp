package com.jtmcompany.smartadvertisingboard;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jtmcompany.smartadvertisingboard.DB.MyVideoDB;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

public class MyVideoActivity extends AppCompatActivity implements MyVideoRecyclerAdapter.myVideoClickListener {
    List<MyVideo_Model> list=new ArrayList<>();
    Realm mRealm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_video);
        //DB 데이터획득
        Realm.init(this);
        mRealm=Realm.getDefaultInstance();
        RealmResults<MyVideoDB> myVideos=mRealm.where(MyVideoDB.class).findAll();
        //DB에 있는데 이터 조회
        for(int i=0; i<myVideos.size(); i++){
            MyVideoDB myVideo=myVideos.get(i);
            Log.d("tak3",myVideo.videoPath);
            if(myVideo.videoPath!=null) {
                Bitmap bitmap = extractThumnail(myVideo.videoPath);
                //썸네일, 비디오제목, 비디오경로
                list.add(new MyVideo_Model(bitmap, myVideo.videoName, myVideo.videoPath));
            }
        }

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);
        RecyclerView recyclerView=findViewById(R.id.myadvertise_recycler);
        recyclerView.setLayoutManager(linearLayoutManager);


        MyVideoRecyclerAdapter recyclerAdapter=new MyVideoRecyclerAdapter(list);
        recyclerAdapter.setMyVieoListener(this);
        recyclerView.setAdapter(recyclerAdapter);


    }

    //리싸이클러뷰의 이미지에 비디오 0초의 썸네일을 보여줌
    public Bitmap extractThumnail(String path){
        MediaMetadataRetriever mediaMetadataRetriever=new MediaMetadataRetriever();
        mediaMetadataRetriever.setDataSource(path);
        Bitmap bitmap=mediaMetadataRetriever.getFrameAtIndex(0000000);//0초영상 추출
        return bitmap;
    }

    @Override
    public void onClick(String path) {
        Intent intent=new Intent(MyVideoActivity.this,VideoWatchActivity.class);
        intent.putExtra("videoPath",path);
        startActivity(intent);
    }
}
