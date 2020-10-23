package com.jtmcompany.smartadvertisingboard.VideoEdit;

import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.VideoView;

import com.jtmcompany.smartadvertisingboard.StickerView.StickerView;

public class VideoEditThread implements Runnable {
    VideoView video;
    ImageView playBt, stopBt;
    Handler handler = new Handler();
    ProgressBar progressBar;
    TextView startTv;
    TextView endTv;

    public Handler getHandler() {
        return handler;
    }

    public VideoEditThread(VideoView video, ImageView playBt, ImageView stopBt, ProgressBar progressBar, TextView startTv, TextView endTv) {
        this.video = video;
        this.playBt = playBt;
        this.stopBt = stopBt;
        this.progressBar=progressBar;
        this.startTv=startTv;
        this.endTv=endTv;
    }

    @Override
    public void run() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (VideoEditAtivity.isFragmentClose) {
                    Log.d("tak3","videoHandler");
                    if (video.getCurrentPosition() / 1000 >= VideoEditAtivity.trim_end) {
                        video.seekTo(VideoEditAtivity.trim_start * 1000);
                        video.pause();
                    }
                    //비디오가 실행중이라면
                    if(video.isPlaying()){
                        int maxTime=VideoEditAtivity.trim_end-VideoEditAtivity.trim_start-1;
                        progressBar.setMax(maxTime);
                        int curTime=video.getCurrentPosition()/1000-VideoEditAtivity.trim_start;
                        progressBar.setProgress(curTime);
                        startTv.setText(getTime(curTime));
                        endTv.setText(getTime(maxTime));
                    }

                    //비디오가 멈추면 정지버튼 활성화
                    if (!video.isPlaying()) {
                        stopBt.setVisibility(View.GONE);
                        playBt.setVisibility(View.VISIBLE);

                    }

                    //추가한 아이템이 각자 설정한 시간이되면 화면에 표시됨
                    for (int i = 0; i < VideoEditAtivity.addItemList.size(); i++) {
                        int start_time = VideoEditAtivity.addItemList.get(i).getStart();
                        int end_time = VideoEditAtivity.addItemList.get(i).getEnd();
                        addView_appear(start_time, end_time, VideoEditAtivity.addItemList.get(i).getStickerView());
                    }
                }
                handler.postDelayed(VideoEditThread.this, 100);
            }
        }, 100);
    }





    public void addView_appear(final int start_time, final int end_time, final StickerView insert_stickerView) {
        Log.d("tak3", "start: " + start_time);
        Log.d("tak3", "end: " + end_time);
        Log.d("tak3","cur: "+video.getCurrentPosition()/1000);
        if (video.isPlaying()) {
            if (video.getCurrentPosition() / 1000 >= start_time && video.getCurrentPosition()/1000<=end_time) {
                insert_stickerView.setVisibility(View.VISIBLE);
                Log.d("tak3", "1");
            }
            if (video.getCurrentPosition() / 1000 > end_time || video.getCurrentPosition() / 1000 < start_time) {
                insert_stickerView.setVisibility(View.GONE);
                Log.d("tak3", "2");
            }
        } else {
            insert_stickerView.setVisibility(View.VISIBLE);
        }


    }

    private String getTime(int seconds) {
        int hr = seconds / 3600;
        int rem = seconds % 3600;
        int mn = rem / 60;
        int sec = rem % 60;
        return String.format("%02d", hr) + ":" + String.format("%02d", mn) + ":" + String.format("%02d", sec);
    }

}
