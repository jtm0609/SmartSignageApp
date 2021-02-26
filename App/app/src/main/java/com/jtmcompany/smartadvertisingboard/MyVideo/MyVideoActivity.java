package com.jtmcompany.smartadvertisingboard.MyVideo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jtmcompany.smartadvertisingboard.RealmDB.MyVideoDB;
import com.jtmcompany.smartadvertisingboard.MainActivity;
import com.jtmcompany.smartadvertisingboard.Utils.FileUploadUtils;
import com.jtmcompany.smartadvertisingboard.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

public class MyVideoActivity extends AppCompatActivity implements MyVideoRecyclerAdapter.myVideoWatchListener, MyVideoRecyclerAdapter.myVideoUploadListener, MyVideoRecyclerAdapter.myVideoDeleteListener {
    List<MyVideoModel> list=new ArrayList<>();
    Realm mRealm;
    MyVideoRecyclerAdapter recyclerAdapter;
    Toolbar toolbar;

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId()==android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_video);
        toolbar=  findViewById(R.id.myVideoToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Log.d("tag","test");

        //DB 데이터획득
        Realm.init(this);
        mRealm=Realm.getDefaultInstance();
        RealmResults<MyVideoDB> myVideos=mRealm.where(MyVideoDB.class).findAll();
        //DB에 있는데 이터 조회
        for(int i=0; i<myVideos.size(); i++){

            MyVideoDB myVideo=myVideos.get(i);
            Log.d("tak3",myVideo.videoPath);
            if(myVideo.videoPath!=null ) {
                File file=new File(myVideo.videoPath);
                if(file.exists()) {
                    Bitmap bitmap = extractThumnail(myVideo.videoPath);
                    //썸네일, 비디오제목, 비디오경로
                    list.add(new MyVideoModel(bitmap, myVideo.videoName, myVideo.videoPath));
                }
            }
        }

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);
        RecyclerView recyclerView=findViewById(R.id.myadvertise_recycler);
        recyclerView.setLayoutManager(linearLayoutManager);


        recyclerAdapter=new MyVideoRecyclerAdapter(list);
        recyclerAdapter.setMyVieoWatchListener(this);
        recyclerAdapter.setMyVideoUploadListener(this);
        recyclerAdapter.setMyVideoDeleteListener(this);
        recyclerView.setAdapter(recyclerAdapter);

        MainActivity.pd.dismiss();

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

    //광고업로드
    @Override
    public void onUpload(String path) {
        File file=new File(path);
        FileUploadUtils.sendServer(file);
        Toast.makeText(this, "전송했습니다.", Toast.LENGTH_SHORT).show();
    }

    //광고삭제
    @Override
    public void onDelete(final String path, final int position) {
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                MyVideoDB vo=mRealm.where(MyVideoDB.class).equalTo("videoPath",path).findFirst();
                if(vo!=null){
                    vo.deleteFromRealm();
                    Toast.makeText(MyVideoActivity.this, "삭제 되었습니다.", Toast.LENGTH_SHORT).show();

                    list.remove(position);
                    recyclerAdapter.notifyDataSetChanged();
                }
            }
        });

        }
    }
