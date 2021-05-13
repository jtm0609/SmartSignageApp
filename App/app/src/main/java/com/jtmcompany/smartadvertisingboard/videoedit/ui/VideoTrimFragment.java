package com.jtmcompany.smartadvertisingboard.videoedit.ui;


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
import com.jtmcompany.smartadvertisingboard.stickerview.StickerView;
import com.waynell.videorangeslider.RangeSlider;

import java.util.List;

public class VideoTrimFragment extends ThumnailFragment implements View.OnClickListener, RangeSlider.OnRangeChangeListener {
private ImageView trimOkBt, trimExitBt;
private trimedVideoProgressThread progress_thread;
private View view;

    public VideoTrimFragment(VideoView videoview, Uri selectVideoUri, List list) {
        super(videoview, selectVideoUri, list);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //잘라내기를하면, 화면에 추가했던 아이템들 정보들이 다사라짐(초기화)
        if(!VideoEditAtivity.itemList.isEmpty()) {
            for(int i = 0; i<VideoEditAtivity.itemList.size(); i++){
                StickerView removeStickerView=VideoEditAtivity.itemList.get(i).getStickerView();
                if(removeStickerView.getParent()!=null){
                    ViewGroup myCanvas = ((ViewGroup)removeStickerView.getParent());
                    myCanvas.removeView(removeStickerView);
                }
            }
            VideoEditAtivity.itemList.clear();
        }


        VideoEditAtivity.isFragmentClose=false;
        mVideoview.seekTo(0);
        view=inflater.inflate(R.layout.fragment_trim_video, container, false);
        initView();
        setViewListener(); //뷰 리스너 설정

        copyParentMember(view); //상속받은 함수를 호출
        slider.setTickCount(mVideoview.getDuration()/1000);
        slider.setRangeChangeListener(this);
        trimedPlay();

        return view;

    }


    public void initView(){
        trimOkBt=view.findViewById(R.id.trim_check);
        trimExitBt=view.findViewById(R.id.trim_exit);
        videoPlayBt = view.findViewById(R.id.videoTrim_play);
        videoPauseBt=view.findViewById(R.id.videoTrim_pause);
    }


    public void setViewListener(){
        trimOkBt.setOnClickListener(this);
        trimExitBt.setOnClickListener(this);
        videoPlayBt.setOnClickListener(this);
        videoPauseBt.setOnClickListener(this);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(progress_thread!=null)
            progress_thread.interrupt();
        handler.removeMessages(0);

        is_Running=false;
        if(!isTrimOK){
            mVideoview.seekTo(VideoEditAtivity.trim_start*1000);
            Log.d("tak3","trimNo");
        }

        VideoEditAtivity.isFragmentClose=true;
    }


    @Override
    public void onClick(View view) {
        if(view==videoPlayBt){
            mVideoview.start();
            isPlaying=true;
            progress_thread=new trimedVideoProgressThread();
            progress_thread.start();
            videoPlayBt.setVisibility(View.GONE);
            videoPauseBt.setVisibility(View.VISIBLE);

        }
        else if(view==videoPauseBt){
            mVideoview.pause();
            isPlaying=false;
            progress_thread.interrupt();
            videoPauseBt.setVisibility(View.GONE);
            videoPlayBt.setVisibility(View.VISIBLE);

        }
        else if(view==trimOkBt){
                FragmentManager fragmentManager=getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().remove(VideoTrimFragment.this).commit();
                isTrimOK=true;
                if(progress_thread!=null)
                    progress_thread.interrupt();
                VideoEditAtivity.trim_start=slider.getLeftIndex();
                VideoEditAtivity.trim_end=slider.getRightIndex();

            //잘라내기 완료하면 기존에삽입했던 스티커뷰들 다초기화
            if(VideoEditAtivity.itemList.size()!=0){
                VideoEditAtivity.itemList.clear();
            }

            }
        else if(view==trimExitBt){
            FragmentManager fragmentManager=getActivity().getSupportFragmentManager();
            fragmentManager.beginTransaction().remove(VideoTrimFragment.this).commit();
        }
    }


    //비디오가 자른 범위 내에서 재생
    protected void trimedPlay(){
        Log.d("tak5","handler!");
        handler.postDelayed(r=new Runnable() {
            @Override
            public void run() {
                //자른 end 길이를 넘어서면
                if(mVideoview.getCurrentPosition()/1000>=slider.getRightIndex()){
                    //자른 start길이부터 다시 시작
                    mVideoview.seekTo((slider.getLeftIndex())*1000);
                    mVideoview.pause();
                    videoPauseBt.setVisibility(View.GONE);
                    videoPlayBt.setVisibility(View.VISIBLE);
                }

                handler.postDelayed(r,100);
            }
        },100);

    }


    //rangebar 변경 콜백
    @Override
    public void onRangeChange(RangeSlider view, int leftPinIndex, int rightPinIndex) {
        mVideoview.seekTo(leftPinIndex*1000);
        endTimeTv.setText(getTime(slider.getRightIndex()));
        startTimeTv.setText(getTime(mVideoview.getCurrentPosition()/1000));
        indicatorSeekbar.setProgress(leftPinIndex);
    }


    //시크바 프로그래스바 스레드
    protected class trimedVideoProgressThread extends Thread{
        @Override
        public void run() {
            Log.d("tak5","thread!");
            super.run();
            try {
                while (isPlaying) {
                    indicatorSeekbar.setProgress(mVideoview.getCurrentPosition() / 1000);
                    Log.d("tak4", "" + mVideoview.getCurrentPosition() / 1000);
                    sleep(100);

                }
            }catch (InterruptedException e){
                Log.d("tak12","interrupt!!");
            }
        }
    }
}
