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
import com.jtmcompany.smartadvertisingboard.StickerView.StickerView;
import com.waynell.videorangeslider.RangeSlider;

import java.util.List;

public class VideoTrimFragment extends ThumnailView implements View.OnClickListener, RangeSlider.OnRangeChangeListener {
private ImageView trimOKBt, trimExitBt;
private Progress_Thtead progress_thtead;
private View view;

    protected VideoTrimFragment(VideoView videoview, Uri selectVideoUri, List list) {
        super(videoview, selectVideoUri, list);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //잘라내기를하면, 화면에 추가했던 아이템들 정보들이 다사라짐(초기화)
        if(!VideoEditAtivity.addItemList.isEmpty()) {
            for(int i=0; i<VideoEditAtivity.addItemList.size(); i++){
                StickerView removeStickerView=VideoEditAtivity.addItemList.get(i).getStickerView();
                if(removeStickerView.getParent()!=null){
                    ViewGroup myCanvas = ((ViewGroup)removeStickerView.getParent());
                    myCanvas.removeView(removeStickerView);
                }
            }
            VideoEditAtivity.addItemList.clear();
        }


        VideoEditAtivity.isFragmentClose=false;
        mVideoview.seekTo(0);
        view=inflater.inflate(R.layout.fragment_trim_video, container, false);
        initView();
        setViewListener(); //뷰 리스너 설정

        copyParentMember(view); //상속받은 함수를 호출
        slider.setTickCount(mVideoview.getDuration()/1000);
        slider.setRangeChangeListener(this);
        Compare_sliderRight();

        return view;

    }


    public void initView(){
        trimOKBt=view.findViewById(R.id.trim_check);
        trimExitBt=view.findViewById(R.id.trim_exit);
        videoPlayBt = view.findViewById(R.id.videoTrim_play);
        videoPauseBt=view.findViewById(R.id.videoTrim_pause);
    }


    public void setViewListener(){
        trimOKBt.setOnClickListener(this);
        trimExitBt.setOnClickListener(this);
        videoPlayBt.setOnClickListener(this);
        videoPauseBt.setOnClickListener(this);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(progress_thtead!=null)
            progress_thtead.interrupt();
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
            progress_thtead=new Progress_Thtead();
            progress_thtead.start();
            videoPlayBt.setVisibility(View.GONE);
            videoPauseBt.setVisibility(View.VISIBLE);

        }
        else if(view==videoPauseBt){
            mVideoview.pause();
            isPlaying=false;
            progress_thtead.interrupt();
            videoPauseBt.setVisibility(View.GONE);
            videoPlayBt.setVisibility(View.VISIBLE);

        }
        else if(view==trimOKBt){
                FragmentManager fragmentManager=getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().remove(VideoTrimFragment.this).commit();
                isTrimOK=true;
                if(progress_thtead!=null)
                    progress_thtead.interrupt();
                VideoEditAtivity.trim_start=slider.getLeftIndex();
                VideoEditAtivity.trim_end=slider.getRightIndex();

                //트림을완료하면 기존에삽입했던 스티커뷰들 다초기화
            if(VideoEditAtivity.addItemList.size()!=0){
                VideoEditAtivity.addItemList.clear();
            }

            }
        else if(view==trimExitBt){
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


    protected class Progress_Thtead extends Thread{
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
