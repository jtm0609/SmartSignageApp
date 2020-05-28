package com.jtmcompany.smartadvertisingboard.VideoEdit;


import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.VideoView;

import com.jtmcompany.smartadvertisingboard.R;
import com.jtmcompany.smartadvertisingboard.StickerView.StickerTextView;
import com.jtmcompany.smartadvertisingboard.StickerView.StickerView;
import com.jtmcompany.smartadvertisingboard.VideoEdit.ThumnailView;
import com.waynell.videorangeslider.RangeSlider;

import java.util.List;


public class SelectTextLocation_Fragment extends ThumnailView implements View.OnClickListener {
    ImageView trim_OK_bt;
    ImageView trim_EXIT_bt;
    Progress_Thtead progress_thtead;
    InsertStickerView_Model mInsertText_model;
    StickerView mInsertTv;
    FragmentManager fragmentManager;
    FrameLayout mVideo_container;

    protected SelectTextLocation_Fragment(VideoView videoview, Uri selectVideoUri, List list, StickerView tv, FrameLayout container) {
        super(videoview, selectVideoUri, list);
        mInsertTv=tv;
        mVideo_container=container;

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        VideoEditAtivity.isFragmentClose=false;
        mVideoview.seekTo(VideoEditAtivity.trim_start*1000);

        View view= inflater.inflate(R.layout.fragment_select_text_location_, container, false);
        trim_OK_bt=view.findViewById(R.id.trim_check);
        trim_EXIT_bt=view.findViewById(R.id.trim_exit);
        videoPlay_bt = view.findViewById(R.id.videoTrim_play);
        videoPause_bt=view.findViewById(R.id.videoTrim_pause);
        trim_EXIT_bt.setOnClickListener(this);
        trim_OK_bt.setOnClickListener(this);
        videoPlay_bt.setOnClickListener(this);
        videoPause_bt.setOnClickListener(this);
        Log.d("tak3","trim_end2: "+VideoEditAtivity.trim_end);
        Log.d("tak3","trim_start2: "+VideoEditAtivity.trim_start);
        init(view);
        Log.d("tak3","trim_end3: "+VideoEditAtivity.trim_end);
        Log.d("tak3","trim_start3: "+VideoEditAtivity.trim_start);
        slider.setTickCount(VideoEditAtivity.trim_end-VideoEditAtivity.trim_start);
        indicator_seek.setMax(VideoEditAtivity.trim_end-VideoEditAtivity.trim_start);
        slider.setRangeChangeListener(new RangeSlider.OnRangeChangeListener() {
            @Override
            public void onRangeChange(RangeSlider view, int leftPinIndex, int rightPinIndex) {
                mVideoview.seekTo((leftPinIndex+VideoEditAtivity.trim_start)*1000);
                endTime_tv.setText(getTime(slider.getRightIndex()));
                startTime_tv.setText(getTime(mVideoview.getCurrentPosition()/1000-VideoEditAtivity.trim_start));
                indicator_seek.setProgress(leftPinIndex);
            }
        });
        Compare_sliderRight();

        fragmentManager=getActivity().getSupportFragmentManager();



        return view;
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
            fragmentManager.beginTransaction().remove(SelectTextLocation_Fragment.this).commit();
            isTrim_OK=true;
            if(progress_thtead!=null)
                progress_thtead.interrupt();
            //VideoEditAtivity.trim_start=slider.getLeftIndex();
            //VideoEditAtivity.trim_end=slider.getRightIndex();
            mInsertText_model=new InsertStickerView_Model(mInsertTv);
            mInsertText_model.setInsert_start_time(slider.getLeftIndex()+VideoEditAtivity.trim_start);
            mInsertText_model.setInsert_end_time(slider.getRightIndex()+VideoEditAtivity.trim_start);

            VideoEditAtivity.insertView.add(mInsertText_model);

            //설정했으면 다시 비디오를 처음부터 시작
            mVideoview.seekTo(VideoEditAtivity.trim_start*1000);
            mVideoview.pause();
        }

        else if(view==trim_EXIT_bt){
            fragmentManager.beginTransaction().remove(SelectTextLocation_Fragment.this).commit();
            Boolean flag=false;
            Log.d("tak3","취소");
            for(int i=0; i<VideoEditAtivity.insertView.size(); i++) {
                if (VideoEditAtivity.insertView.get(i).getmStickerView() == mInsertTv) {
                    flag = true;
                    break;
                }
            }
            if(flag!=true)
                mVideo_container.removeView(mInsertTv);
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        handler.removeMessages(0);
        if(progress_thtead!=null)
            progress_thtead.interrupt();

        is_Running=false;
        VideoEditAtivity.isFragmentClose=true;


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
                Log.d("tak5","currentposition: "+slider.getRightIndex()+VideoEditAtivity.trim_start);
                if(mVideoview.getCurrentPosition()/1000>=slider.getRightIndex()+VideoEditAtivity.trim_start){

                    mVideoview.seekTo((slider.getLeftIndex()+VideoEditAtivity.trim_start)*1000);
                    mVideoview.pause();
                    videoPause_bt.setVisibility(View.GONE);
                    videoPlay_bt.setVisibility(View.VISIBLE);

                    //stop되면 스레드역시 종료
                    // if(progress_thtead!=null)
                    //progress_thtead.interrupt();

                }

                for(int i=0; i<VideoEditAtivity.insertView.size(); i++){
                    int start_time=VideoEditAtivity.insertView.get(i).getInsert_start_time();
                    int end_time=VideoEditAtivity.insertView.get(i).getInsert_end_time();
                    Insert_View_appear(start_time,end_time,VideoEditAtivity.insertView.get(i).getmStickerView());

                }

                handler.postDelayed(r,1000);
            }
        },1000);

    }


    public void Insert_View_appear(final int start_time, final int end_time, final StickerView insert_stickerView){
        Log.d("tak3","start: "+start_time);
        Log.d("tak3","end: "+end_time);
        if(mVideoview.isPlaying()) {
            if (mVideoview.getCurrentPosition() / 1000 == start_time) {
                insert_stickerView.setVisibility(View.VISIBLE);
                Log.d("tak3", "1");
            }
            if (mVideoview.getCurrentPosition() / 1000 > end_time || mVideoview.getCurrentPosition() / 1000 < start_time) {
                insert_stickerView.setVisibility(View.GONE);
                Log.d("tak3", "2");
            }
        }else{
            insert_stickerView.setVisibility(View.GONE);
        }

    }


    protected class Progress_Thtead extends Thread{
        @Override
        public void run() {
            Log.d("tak5","thread!");
            super.run();
            try {
                while (mVideoview.isPlaying()) {
                    indicator_seek.setProgress(mVideoview.getCurrentPosition() / 1000-VideoEditAtivity.trim_start);
                    Log.d("tak4", "" + mVideoview.getCurrentPosition() / 1000);
                    sleep(100);

                }
            }catch (InterruptedException e){
                Log.d("tak12","interrupt!!");
            }
        }
    }
}
