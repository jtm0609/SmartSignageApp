package com.jtmcompany.smartadvertisingboard.videoedit.ui;

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
import com.jtmcompany.smartadvertisingboard.videoedit.adapter.ThumnailAdapter;
import com.waynell.videorangeslider.RangeSlider;

import java.util.ArrayList;
import java.util.List;


public class ThumnailFragment extends Fragment implements  View.OnTouchListener {
    public static List<Bitmap> thumnail_list = new ArrayList<>();
    public static ThumnailAdapter recyclerAdapter;

    protected VideoView mVideoview;
    protected Uri mSelectUri;
    protected RelativeLayout trimLayout;
    protected Button videoPlayBt, videoPauseBt;
    protected SeekBar indicatorSeekbar;
    protected boolean isPlaying;
    protected boolean isTrimOK;
    protected TextView startTimeTv,endTimeTv;
    protected RangeSlider slider;
    protected static Boolean is_Running;
    protected Runnable r;
    protected Handler handler=new Handler();
    protected RecyclerView recyclerView;

    protected ThumnailFragment(VideoView videoview, Uri selectVideoUri, List list) {
        mVideoview = videoview;
        mSelectUri=selectVideoUri;
        thumnail_list=list;
        Log.d("tak3","thumnailView");

    }


    protected void copyParentMember(View view){ //부모가 초기화한 멤버변수들을 그대로 복사해서 전달해줌
        is_Running=true;
        init(view);
        recyclerViewSetting(); //리싸이클러뷰 설정


        //인디케이터 시크바설정
        indicatorSeekbar.setOnTouchListener(this);
        indicatorSeekbar.setMax(mVideoview.getDuration()/1000);

    }

    public void init(View view){
        recyclerView = view.findViewById(R.id.range_rv_recycler);
        trimLayout=view.findViewById(R.id.trim_layout);
        indicatorSeekbar = view.findViewById(R.id.trim_indicator);
        startTimeTv=view.findViewById(R.id.trim_video_currentTime);
        endTimeTv=view.findViewById(R.id.trim_video_endTime);
        slider = (RangeSlider)view.findViewById(R.id.range_slider);


    }

    public void recyclerViewSetting(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        if(recyclerView==null){
            Log.d("tak7","recycler null");
        }

        recyclerView.setLayoutManager(linearLayoutManager);

        //각 썸네일 크기정의
        final int itemCount = 8;
        int padding = getResources().getDimensionPixelOffset(R.dimen.activity_horizontal_margin);
        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        int thumbWidth = getResources().getDimensionPixelOffset(R.dimen.range_thumb_width);
        final int itemWidth = (screenWidth - (2 * (padding + thumbWidth))) / itemCount;

        recyclerAdapter = new ThumnailAdapter(thumnail_list, itemWidth);
        recyclerView.setAdapter(recyclerAdapter);
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





