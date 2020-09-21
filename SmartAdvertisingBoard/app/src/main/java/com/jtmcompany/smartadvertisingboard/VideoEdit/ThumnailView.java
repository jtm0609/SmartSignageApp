package com.jtmcompany.smartadvertisingboard.VideoEdit;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jtmcompany.smartadvertisingboard.R;
import com.jtmcompany.smartadvertisingboard.VideoEdit.Adapter.TrimRecyclerAdapter;
import com.waynell.videorangeslider.RangeSlider;

import java.util.ArrayList;
import java.util.List;


public class ThumnailView extends Fragment implements  View.OnTouchListener {
    static List<Bitmap> thumnail_list = new ArrayList<>();
    VideoView mVideoview;
    Uri mSelectUri;

    static TrimRecyclerAdapter recyclerAdapter;
    RelativeLayout trim_layout;
    Button videoPlay_bt;
    Button videoPause_bt;
    SeekBar indicator_seek;
    boolean isPlaying;
    boolean isTrim_OK;
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

        recyclerAdapter = new TrimRecyclerAdapter(thumnail_list, itemWidth);
        recyclerView.setAdapter(recyclerAdapter);
        //<<리싸이클러뷰>>

        //인디케이터 시크바설정
        indicator_seek.setMax(mVideoview.getDuration()/1000);

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


}





