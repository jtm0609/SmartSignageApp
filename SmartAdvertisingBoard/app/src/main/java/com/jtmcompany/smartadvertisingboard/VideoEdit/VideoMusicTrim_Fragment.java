package com.jtmcompany.smartadvertisingboard.VideoEdit;


import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.innovattic.rangeseekbar.RangeSeekBar;
import com.jtmcompany.smartadvertisingboard.R;

import java.text.SimpleDateFormat;


public class VideoMusicTrim_Fragment extends Fragment implements RangeSeekBar.SeekBarChangeListener, View.OnClickListener {
Uri selectMusicUri;
RangeSeekBar rangeSeekBar;
MediaPlayer mediaPlayer;
Handler handler=new Handler();
Runnable r;
Button music_play_bt;
Button music_stop_bt;
ImageView music_check_bt;
ImageView music_exit_bt;
TextView seekvar_tvLeft;
TextView seekbar_tvRight;
FragmentManager fragmentManager;
    public VideoMusicTrim_Fragment(Uri selectMusicUri) {
        this.selectMusicUri = selectMusicUri;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_video_music_trim_, container, false);
        music_check_bt=view.findViewById(R.id.music_check);
        music_exit_bt=view.findViewById(R.id.musio_exit);
        music_check_bt.setOnClickListener(this);
        music_exit_bt.setOnClickListener(this);
        seekvar_tvLeft=view.findViewById(R.id.tvLeft);
        seekbar_tvRight=view.findViewById(R.id.tvRight);
        music_play_bt=view.findViewById(R.id.music_play_bt);
        music_stop_bt=view.findViewById(R.id.music_stop_bt);
        music_play_bt.setOnClickListener(this);
        music_stop_bt.setOnClickListener(this);
        mediaPlayer=MediaPlayer.create(getContext(),selectMusicUri);
        int duration=mediaPlayer.getDuration()/1000;
        rangeSeekBar=view.findViewById(R.id.music_seekbar);
        rangeSeekBar.setSeekBarChangeListener(this);
        //rangeSeekBar.setOnRangeSeekBarChangeListener(this);

        Log.d("tak20","duration: "+duration);
        rangeSeekBar.setMaxThumbValue(duration);
        rangeSeekBar.setMinThumbValue(0);
        rangeSeekBar.setMax(duration);

        //rangeSeekBar.setSelectedMaxValue(duration);
        //rangeSeekBar.setSelectedMinValue(0);
        //rangeSeekBar.setRangeValues(0,duration);

        //mediaPlayer.getDuration()/1000;
        handler.postDelayed(r=new Runnable() {
            @Override
            public void run() {

                Log.d("tak4","music_trim_handler");
                if(mediaPlayer.getCurrentPosition()>= rangeSeekBar.getMaxThumbValue()*1000) {
                    mediaPlayer.seekTo(rangeSeekBar.getMinThumbValue() * 1000);
                    mediaPlayer.pause();
                    Log.d("tak","test");
                }
                handler.postDelayed(r, 1000);

                if(!mediaPlayer.isPlaying()){
                    music_play_bt.setVisibility(View.VISIBLE);
                    music_stop_bt.setVisibility(View.GONE);

                }else if(mediaPlayer.isPlaying()) {
                    music_stop_bt.setVisibility(View.VISIBLE);
                    music_play_bt.setVisibility(View.GONE);
                }
            }
        },1000);

        fragmentManager=getFragmentManager();

        return view;
    }

    @Override
    public void onValueChanged(int i, int i1) {
        mediaPlayer.seekTo((int)i*1000);
        Log.d("tak20","min: "+i);
        Log.d("tak20","max: "+i1);
        seekvar_tvLeft.setText(toTime(i));
        seekbar_tvRight.setText(toTime(i1));

        Log.d("tak20","getminValue: "+rangeSeekBar.getMinThumbValue());
        Log.d("tak20","getMaxValue: "+rangeSeekBar.getMaxThumbValue());
    }
    /*
    @Override
    public void onRangeSeekBarValuesChanged(RangeSeekBar bar, Object minValue, Object maxValue) {
        mediaPlayer.seekTo((int)minValue*1000);
        seekvar_tvLeft.setText(getTime((int)bar.getSelectedMinValue()));
        seekbar_tvRight.setText(getTime((int)bar.getSelectedMaxValue()));
    }

     */


    @Override
    public void onClick(View view) {
        if(view==music_play_bt)
        mediaPlayer.start();

        else if(view==music_stop_bt)
            mediaPlayer.pause();

        else if(view==music_check_bt){
            int videoDuration=VideoEditAtivity.trim_end-VideoEditAtivity.trim_start;
            int musicDuration=rangeSeekBar.getMaxThumbValue()-rangeSeekBar.getMinThumbValue();
            if(videoDuration>=musicDuration) {
                VideoEditAtivity.music_trim_start = rangeSeekBar.getMinThumbValue();
                VideoEditAtivity.music_trim_end = rangeSeekBar.getMaxThumbValue();

                VideoEditAtivity.isMusicCheck=true;
                fragmentManager.beginTransaction().remove(VideoMusicTrim_Fragment.this).commit();
            }else {
                Toast.makeText(getContext(), "음악시간이 비디오시간보다 깁니다.", Toast.LENGTH_SHORT).show();
            }

        }
        else if(view==music_exit_bt){
            fragmentManager.beginTransaction().remove(VideoMusicTrim_Fragment.this).commit();
        }
    }

    private String getTime(int seconds) {
        int hr = seconds / 3600;
        int rem = seconds % 3600;
        int mn = rem / 60;
        int sec = rem % 60;
        return String.format("%02d", hr) + ":" + String.format("%02d", mn) + ":" + String.format("%02d", sec);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        handler.removeMessages(0);
        mediaPlayer.pause();
    }


    @Override
    public void onStartedSeeking() {

    }

    @Override
    public void onStoppedSeeking() {

    }

    public String toTime(int second){
        int m=second/60;
        int s=second%60;
        String mn=String.format("%02d",m);
        String sec=String.format("%02d",s);
        return mn+":"+sec;




    }
}
