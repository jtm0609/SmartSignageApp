package com.jtmcompany.smartadvertisingboard.VideoEdit;


import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.VideoView;

import androidx.fragment.app.FragmentManager;

import com.jtmcompany.smartadvertisingboard.R;
import com.waynell.videorangeslider.RangeSlider;

import java.util.List;

public class VideoTrimFragment extends ThumnailView implements View.OnClickListener {
ImageView trim_OK_bt;
ImageView trim_Exit_bt;
Progress_Thtead progress_thtead;

    protected VideoTrimFragment(VideoView videoview, Uri selectVideoUri, List list) {
        super(videoview, selectVideoUri, list);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        VideoEditAtivity.isFragmentClose=false;
        mVideoview.seekTo(0);

        View view=inflater.inflate(R.layout.fragment_trim_video, container, false);
        trim_OK_bt=view.findViewById(R.id.trim_check);
        trim_Exit_bt=view.findViewById(R.id.trim_exit);
        videoPlay_bt = view.findViewById(R.id.videoTrim_play);
        videoPause_bt=view.findViewById(R.id.videoTrim_pause);
        trim_OK_bt.setOnClickListener(this);
        trim_Exit_bt.setOnClickListener(this);
        videoPlay_bt.setOnClickListener(this);
        videoPause_bt.setOnClickListener(this);

        init(view);
        slider.setTickCount(mVideoview.getDuration()/1000);
        slider.setRangeChangeListener(new RangeSlider.OnRangeChangeListener() {
            @Override
            public void onRangeChange(RangeSlider view, int leftPinIndex, int rightPinIndex) {
                mVideoview.seekTo(leftPinIndex*1000);
                endTime_tv.setText(getTime(slider.getRightIndex()));
                startTime_tv.setText(getTime(mVideoview.getCurrentPosition()/1000));
                indicator_seek.setProgress(leftPinIndex);
            }
        });
        Compare_sliderRight();


        return view;

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(progress_thtead!=null)
            progress_thtead.interrupt();
        handler.removeMessages(0);

        is_Running=false;
        if(!isTrim_OK){
            mVideoview.seekTo(VideoEditAtivity.trim_start*1000);
            Log.d("tak3","trimNo");
        }

        VideoEditAtivity.isFragmentClose=true;
    }

    @Override
    public void onClick(View view) {
        if(view==videoPlay_bt){
            mVideoview.start();
            isPlaying=true;
            progress_thtead=new Progress_Thtead();
            progress_thtead.start();
            videoPlay_bt.setVisibility(View.GONE);
            videoPause_bt.setVisibility(View.VISIBLE);

        }
        else if(view==videoPause_bt){
            mVideoview.pause();
            isPlaying=false;
            progress_thtead.interrupt();
            videoPause_bt.setVisibility(View.GONE);
            videoPlay_bt.setVisibility(View.VISIBLE);

        }
        else if(view==trim_OK_bt){
                FragmentManager fragmentManager=getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().remove(VideoTrimFragment.this).commit();
                isTrim_OK=true;
                if(progress_thtead!=null)
                    progress_thtead.interrupt();
                VideoEditAtivity.trim_start=slider.getLeftIndex();
                VideoEditAtivity.trim_end=slider.getRightIndex();

                //트림을완료하면 기존에삽입했던 스티커뷰들 다초기화
            if(VideoEditAtivity.addItemList.size()!=0){
                VideoEditAtivity.addItemList.clear();
            }

            }
        else if(view==trim_Exit_bt){
            FragmentManager fragmentManager=getActivity().getSupportFragmentManager();
            fragmentManager.beginTransaction().remove(VideoTrimFragment.this).commit();
        }



    }

    //슬라이더의 오른쪽이 바뀌었을때 현재비디오 위치가 바뀐오른쪽보다 커지면 다시 비디오를재시작
    protected void Compare_sliderRight(){
        Log.d("tak5","handler!");
        handler.postDelayed(r=new Runnable() {
            @Override
            public void run() {
                //VideoEditAtivity.isActivityFocus=false;
                Log.d("tak3","trimThread");
                Log.d("tak4","current: "+mVideoview.getCurrentPosition()/1000);
                Log.d("tak4","getRightIndex: "+slider.getRightIndex());
                if(mVideoview.getCurrentPosition()/1000>=slider.getRightIndex()){

                    mVideoview.seekTo((slider.getLeftIndex())*1000);
                    mVideoview.pause();
                    videoPause_bt.setVisibility(View.GONE);
                    videoPlay_bt.setVisibility(View.VISIBLE);
                }


                handler.postDelayed(r,100);
            }
        },100);

    }

    protected class Progress_Thtead extends Thread{
        @Override
        public void run() {
            Log.d("tak5","thread!");
            super.run();
            try {
                while (isPlaying) {
                    indicator_seek.setProgress(mVideoview.getCurrentPosition() / 1000);
                    Log.d("tak4", "" + mVideoview.getCurrentPosition() / 1000);
                    sleep(100);

                }
            }catch (InterruptedException e){
                Log.d("tak12","interrupt!!");
            }
        }
    }
}
