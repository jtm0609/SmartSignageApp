package com.jtmcompany.smartadvertisingboard.VideoEdit;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.jtmcompany.smartadvertisingboard.R;
import com.waynell.videorangeslider.RangeSlider;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class ThumnailView extends Fragment implements  View.OnTouchListener {
    static List<Bitmap> thumnail_list = new ArrayList<>();
    int mDuration;
    VideoView mVideoview;
    Uri mSelectUri;

    MediaMetadataRetriever mediaMetadataRetriever;
    static trimRecyclerAdapter recyclerAdapter;
    RelativeLayout trim_layout;
    ImageView loading_iv;
    //ImageView trim_OK_bt;
    Button videoPlay_bt;
    Button videoPause_bt;
    SeekBar indicator_seek;
    boolean isPlaying;
    boolean isTrim_OK;
    //Progress_Thtead progress_thtead;
    TextView startTime_tv;
    TextView endTime_tv;
    RangeSlider slider;
    protected static Boolean is_Running;
    Runnable r;
    Handler handler=new Handler();


    protected ThumnailView(VideoView videoview, Uri selectVideoUri, List list) {
        mVideoview = videoview;
        mSelectUri=selectVideoUri;
        thumnail_list=list;
        Log.d("tak3","thumnailView");

    }



    protected void init(View view){


        is_Running=true;

        trim_layout=view.findViewById(R.id.trim_layout);


        indicator_seek = view.findViewById(R.id.trim_indicator);

        startTime_tv=view.findViewById(R.id.trim_video_currentTime);
        endTime_tv=view.findViewById(R.id.trim_video_endTime);
        slider = (RangeSlider)view.findViewById(R.id.range_slider);

        //trim_OK_bt.setOnClickListener(this);
        //인디케이터시크바 터치못하게 방지
        indicator_seek.setOnTouchListener(this);


        //<<리싸이클러뷰>>
        RecyclerView recyclerView = view.findViewById(R.id.range_rv_recycler);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);


        //각 썸네일 크기정의
        final int itemCount = 8;
        int padding = getResources().getDimensionPixelOffset(R.dimen.activity_horizontal_margin);
        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        int thumbWidth = getResources().getDimensionPixelOffset(R.dimen.range_thumb_width);
        final int itemWidth = (screenWidth - (2 * (padding + thumbWidth))) / itemCount;


        //썸네일이 추출될때까지 로딩
        //loading_gif();

        recyclerAdapter = new trimRecyclerAdapter(thumnail_list, itemWidth);
        recyclerView.setAdapter(recyclerAdapter);
        //<<리싸이클러뷰>>

        //인디케이터 시크바설정
        indicator_seek.setMax(mVideoview.getDuration()/1000);

        //range Slider설정
        //slider.setTickCount(mVideoview.getDuration()/1000);



       // Compare_sliderRight();



    }


    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        //true로 하면 터치가안됨
        return true;
    }



    protected String getTime(int seconds) {
        int hr = seconds / 3600;
        int rem = seconds % 3600;
        int mn = rem / 60;
        int sec = rem % 60;
        return String.format("%02d", hr) + ":" + String.format("%02d", mn) + ":" + String.format("%02d", sec);
    }


/*
    @Override
    public void onDestroyView() {
        Log.d("tak5","destroy!");
        super.onDestroyView();
        Log.d("tak12","ondes");
        //OK버튼을 안눌렀을때는 취소하느거므로 비디오를 다시초기화
        if(!isTrim_OK) {
            mVideoview.seekTo(VideoEditAtivity.trim_start);
            handler.removeMessages(0);
            if(progress_thtead!=null)
            progress_thtead.interrupt();

        }
        handler.removeMessages(0);

        is_Running=false;
    }

 */

}





