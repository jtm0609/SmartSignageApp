package com.jtmcompany.smartadvertisingboard.MyVideo;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import com.jtmcompany.smartadvertisingboard.R;

public class VideoWatchActivity extends AppCompatActivity {

    private VideoView videoView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_watch);
        videoView=findViewById(R.id.myVideo);
        Intent intent=getIntent();
        String videoPath=intent.getStringExtra("videoPath");
        videoView.setVideoPath(videoPath);
        //비디오의 재생, 일시정지등을 할 수있는 컨트롤바 설정
        videoView.setMediaController(new MediaController(this));

        //비디오가 준비되면 바로 비디오시작
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                videoView.start();
            }
        });

        //비디오가 끝나면 엑티비티 종료
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                finish();
            }
        });
    }

    //화면이 안보일때
    @Override
    protected void onPause() {
        super.onPause();
        if(videoView!=null && videoView.isPlaying())
            videoView.pause();
    }
}
