package com.jtmcompany.smartadvertisingboard.videoedit.ui;


import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.innovattic.rangeseekbar.RangeSeekBar;
import com.jtmcompany.smartadvertisingboard.R;


public class MusicTrimFragment extends Fragment implements RangeSeekBar.SeekBarChangeListener, View.OnClickListener {
private Uri selectMusicUri;
private RangeSeekBar rangeSeekBar;
private MediaPlayer mediaPlayer;
private Handler handler=new Handler();
private Runnable r;
private Button musicPlayBt,musicStopBt;
private ImageView musicCheckBt,musicExitBt;
private TextView seekvarLeftTv,seekbarRightTv;
private FragmentManager fragmentManager;
    public MusicTrimFragment(Uri selectMusicUri) {
        this.selectMusicUri = selectMusicUri;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_video_music_trim_, container, false);


        initView(view);


        musicCheckBt.setOnClickListener(this);
        musicExitBt.setOnClickListener(this);
        musicPlayBt.setOnClickListener(this);
        musicStopBt.setOnClickListener(this);
        rangeSeekBar.setSeekBarChangeListener(this);


        mediaPlayer=MediaPlayer.create(getContext(),selectMusicUri);
        int duration=mediaPlayer.getDuration()/1000;

        rangeSeekBar.setMaxThumbValue(duration);
        rangeSeekBar.setMinThumbValue(0);
        rangeSeekBar.setMax(duration);


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
                    musicPlayBt.setVisibility(View.VISIBLE);
                    musicStopBt.setVisibility(View.GONE);

                }else if(mediaPlayer.isPlaying()) {
                    musicStopBt.setVisibility(View.VISIBLE);
                    musicPlayBt.setVisibility(View.GONE);
                }
            }
        },1000);
        fragmentManager=getFragmentManager();

        return view;
    }
    public void initView(View view){
        musicCheckBt=view.findViewById(R.id.music_check);
        musicExitBt=view.findViewById(R.id.musio_exit);
        seekvarLeftTv=view.findViewById(R.id.tvLeft);
        seekbarRightTv=view.findViewById(R.id.tvRight);
        musicPlayBt=view.findViewById(R.id.music_play_bt);
        musicStopBt=view.findViewById(R.id.music_stop_bt);
        rangeSeekBar=view.findViewById(R.id.music_seekbar);
    }

    @Override
    public void onValueChanged(int i, int i1) {
        mediaPlayer.seekTo((int)i*1000);

        seekvarLeftTv.setText(toTime(i));
        seekbarRightTv.setText(toTime(i1));

    }

    @Override
    public void onClick(View view) {
        if(view==musicPlayBt)
        mediaPlayer.start();

        else if(view==musicStopBt)
            mediaPlayer.pause();

        else if(view==musicCheckBt){
            int videoDuration= VideoEditAtivity.trim_end-VideoEditAtivity.trim_start;
            int musicDuration=rangeSeekBar.getMaxThumbValue()-rangeSeekBar.getMinThumbValue();
            if(videoDuration>=musicDuration) {
                Log.d("tak88","min: "+rangeSeekBar.getMinThumbValue());
                Log.d("tak88","max: "+rangeSeekBar.getMaxThumbValue());
                VideoEditAtivity.music_trim_start = rangeSeekBar.getMinThumbValue();
                VideoEditAtivity.music_trim_end = rangeSeekBar.getMaxThumbValue();

                VideoEditAtivity.isMusicCheck=true;
                fragmentManager.beginTransaction().remove(MusicTrimFragment.this).commit();
            }else {
                Toast.makeText(getContext(), "음악시간이 비디오시간보다 깁니다.", Toast.LENGTH_SHORT).show();
            }

        }
        else if(view==musicExitBt){
            fragmentManager.beginTransaction().remove(MusicTrimFragment.this).commit();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        handler.removeMessages(0);
        mediaPlayer.pause();
    }

    public String toTime(int second){
        int m=second/60;
        int s=second%60;
        String mn=String.format("%02d",m);
        String sec=String.format("%02d",s);
        return mn+":"+sec;
    }

    @Override
    public void onStartedSeeking() { }
    @Override
    public void onStoppedSeeking() { }

}
