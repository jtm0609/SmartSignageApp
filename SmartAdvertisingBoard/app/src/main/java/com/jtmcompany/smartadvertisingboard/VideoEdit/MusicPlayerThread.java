package com.jtmcompany.smartadvertisingboard.VideoEdit;

import android.media.MediaPlayer;
import android.util.Log;
import android.widget.VideoView;

public class MusicPlayerThread extends Thread {
    VideoView videoView;
    MediaPlayer musicPlayer;

    public MusicPlayerThread(VideoView videoView, MediaPlayer musicPlayer) {
        this.videoView = videoView;
        this.musicPlayer = musicPlayer;
    }
    @Override
    public void run() {
        super.run();
        while (videoView.isPlaying()) {
            try {
                Log.d("tak99", "Music_play_thread");
                Log.d("tak4","현재위치: "+musicPlayer.getCurrentPosition()/1000);
                Log.d("tak4","끝날위치: "+VideoEditAtivity.music_trim_end);
                if (musicPlayer.getCurrentPosition() / 1000 >= VideoEditAtivity.music_trim_end) {


                    musicPlayer.seekTo(VideoEditAtivity.trim_start * 1000);
                    musicPlayer.pause();

                }
                //sleep(1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if(!videoView.isPlaying()) {
            musicPlayer.pause();

        }
    }
}
