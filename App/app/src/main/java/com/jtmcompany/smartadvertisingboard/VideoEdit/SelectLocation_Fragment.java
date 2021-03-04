package com.jtmcompany.smartadvertisingboard.VideoEdit;


import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.VideoView;

import androidx.fragment.app.FragmentManager;

import com.jtmcompany.smartadvertisingboard.R;
import com.jtmcompany.smartadvertisingboard.StickerView.StickerView;
import com.jtmcompany.smartadvertisingboard.VideoEdit.VO.addItemVO;
import com.waynell.videorangeslider.RangeSlider;

import java.util.List;


public class SelectLocation_Fragment extends ThumnailView implements View.OnClickListener, RangeSlider.OnRangeChangeListener {
    private ImageView trim_OK_bt;
    private ImageView trim_EXIT_bt;
    private Progress_Thtead progress_thtead;
    private StickerView addItem;
    private FragmentManager fragmentManager;
    private FrameLayout mVideo_container;

    private VideoView videoView;
    String itemPath;
    int start,end;



    protected SelectLocation_Fragment(VideoView videoview, Uri selectVideoUri, List list, StickerView tv, FrameLayout container) {
        super(videoview, selectVideoUri, list);
        this.videoView=videoview;
        addItem=tv;
        mVideo_container=container;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        VideoEditAtivity.isFragmentClose=false;
        mVideoview.seekTo(VideoEditAtivity.trim_start*1000);

        View view= inflater.inflate(R.layout.fragment_select_location_, container, false);
        initView(view);
        copyParentMember(view);
        listnerSetting();



        slider.setTickCount(VideoEditAtivity.trim_end-VideoEditAtivity.trim_start);
        indicatorSeekbar.setMax(VideoEditAtivity.trim_end-VideoEditAtivity.trim_start);

        videoHandleListener();
        fragmentManager=getActivity().getSupportFragmentManager();
        return view;
    }

    public void initView(View view){
        trim_OK_bt=view.findViewById(R.id.trim_check);
        trim_EXIT_bt=view.findViewById(R.id.trim_exit);
        videoPlayBt = view.findViewById(R.id.videoTrim_play);
        videoPauseBt=view.findViewById(R.id.videoTrim_pause);
    }

    public void listnerSetting(){
        trim_EXIT_bt.setOnClickListener(this);
        trim_OK_bt.setOnClickListener(this);
        videoPlayBt.setOnClickListener(this);
        videoPauseBt.setOnClickListener(this);
        slider.setRangeChangeListener(this); //슬라이더가변하면 시작,끝 텍스트 변경
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
        else if(view==trim_OK_bt){
            fragmentManager.beginTransaction().remove(SelectLocation_Fragment.this).commit();
            isTrimOK=true;
            if(progress_thtead!=null)
                progress_thtead.interrupt();

                start=slider.getLeftIndex()+VideoEditAtivity.trim_start;
                end=slider.getRightIndex()+VideoEditAtivity.trim_start;

                //비디오 엑티비티에서 complete버튼을 눌렀을때, 프레임아웃컨테이너의 자식들뷰와, addItemList의 뷰를 구분하기위해,
                //setId를설정(id가같은것이면 뷰가같다는말임)
                addItem.setId(VideoEditAtivity.addItemList.size()+1);

                VideoEditAtivity.addItemList.add(new addItemVO(addItem,start,end));
                 //설정했으면 다시 비디오를 처음부터 시작
                mVideoview.seekTo(VideoEditAtivity.trim_start*1000);
                mVideoview.pause();

            }


        else if(view==trim_EXIT_bt){
            fragmentManager.beginTransaction().remove(SelectLocation_Fragment.this).commit();
            mVideo_container.removeView(addItem);
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
    //추가된 아이템이 정해진 시간이 되면 나타남
    protected void videoHandleListener(){
        Log.d("tak5","handler!");
        handler.postDelayed(r=new Runnable() {
            @Override
            public void run() {
                if(mVideoview.getCurrentPosition()/1000>=slider.getRightIndex()+VideoEditAtivity.trim_start){
                    mVideoview.seekTo((slider.getLeftIndex()+VideoEditAtivity.trim_start)*1000);
                    mVideoview.pause();
                    videoPauseBt.setVisibility(View.GONE);
                    videoPlayBt.setVisibility(View.VISIBLE);

                }

                //추가한 아이템들도 정해진 시간이 되면 나타남
                for(int i=0; i<VideoEditAtivity.addItemList.size(); i++){
                    int start_time=VideoEditAtivity.addItemList.get(i).getStart();
                    int end_time=VideoEditAtivity.addItemList.get(i).getEnd();
                    addView_appear(start_time,end_time,VideoEditAtivity.addItemList.get(i).getStickerView());

                }

                handler.postDelayed(r,10);
            }
        },10);

    }


    public void addView_appear(final int start_time, final int end_time, final StickerView addStickerView){
        Log.d("tak3","start: "+start_time);
        Log.d("tak3","end: "+end_time);
        if(mVideoview.isPlaying()) {
            if (mVideoview.getCurrentPosition() / 1000 >= start_time && mVideoview.getCurrentPosition()/1000<=end_time) {
                addStickerView.setVisibility(View.VISIBLE);
                Log.d("tak3", "1");
            }
            if (mVideoview.getCurrentPosition() / 1000 > end_time || mVideoview.getCurrentPosition() / 1000 < start_time) {
                addStickerView.setVisibility(View.GONE);
                Log.d("tak3", "2");
            }
        }else{
            addStickerView.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onRangeChange(RangeSlider view, int leftPinIndex, int rightPinIndex) {
        mVideoview.seekTo((leftPinIndex+VideoEditAtivity.trim_start)*1000);
        endTimeTv.setText(getTime(slider.getRightIndex()));
        startTimeTv.setText(getTime(mVideoview.getCurrentPosition()/1000-VideoEditAtivity.trim_start));
        indicatorSeekbar.setProgress(leftPinIndex);
    }


    protected class Progress_Thtead extends Thread{
        @Override
        public void run() {
            Log.d("tak5","thread!");
            super.run();
            try {
                while (mVideoview.isPlaying()) {
                    indicatorSeekbar.setProgress(mVideoview.getCurrentPosition() / 1000-VideoEditAtivity.trim_start);
                    Log.d("tak4", "" + mVideoview.getCurrentPosition() / 1000);
                    sleep(100);

                }
            }catch (InterruptedException e){
                Log.d("tak12","interrupt!!");
            }
        }
    }


}
