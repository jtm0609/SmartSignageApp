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


public class VideoTrimFragment extends BottomSheetDialogFragment implements View.OnClickListener, View.OnTouchListener {
    List<Bitmap> thumnail_list = new ArrayList<>();
    int mDuration;
    VideoView mVideoview;
    Uri mSelectUri;
    //썸네일 시간, 시간을10으로 분할한 시간
    float time = 0;
    float plusTime;

    MediaMetadataRetriever mediaMetadataRetriever;
    trimRecyclerAdapter recyclerAdapter;
    RelativeLayout trim_layout;
    ImageView loading_iv;
    ImageView trim_OK_bt;
    Button videoPlay_bt;
    Button videoPause_bt;
    SeekBar indicator_seek;
    boolean isPlaying;
    boolean isTrim_OK;
    Progress_Thtead progress_thtead;
    TextView startTime_tv;
    TextView endTime_tv;
    RangeSlider slider;


    public VideoTrimFragment(int duration, VideoView videoview, Uri selectVideoUri) {
        mDuration = duration;
        mVideoview = videoview;
        mSelectUri=selectVideoUri;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_video_trim, container, false);
        trim_layout=view.findViewById(R.id.trim_layout);
        trim_OK_bt = view.findViewById(R.id.trim_check);
        videoPlay_bt = view.findViewById(R.id.videoTrim_play);
        videoPause_bt=view.findViewById(R.id.videoTrim_pause);
        loading_iv=view.findViewById(R.id.loading_img);
        indicator_seek = view.findViewById(R.id.trim_indicator);

        startTime_tv=view.findViewById(R.id.trim_video_currentTime);
        endTime_tv=view.findViewById(R.id.trim_video_endTime);
        slider = (RangeSlider)view.findViewById(R.id.range_slider);

        trim_OK_bt.setOnClickListener(this);
        videoPlay_bt.setOnClickListener(this);
        videoPause_bt.setOnClickListener(this);
        //인디케이터시크바 터치못하게 방지
        indicator_seek.setOnTouchListener(this);


        //<<리싸이클러뷰>>
        RecyclerView recyclerView = view.findViewById(R.id.range_rv_recycler);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        //썸네일 이미지추출
        plusTime = (float) (mDuration / 10.0);
        Log.d("tak10","uri: "+mSelectUri);
        Log.d("tak10","path: "+getPath(getContext(),mSelectUri));
        String videoPath = getPath(getContext(),mSelectUri);
        mediaMetadataRetriever = new MediaMetadataRetriever();
        mediaMetadataRetriever.setDataSource(videoPath);
        //각 썸네일 크기정의
        final int itemCount = 10;
        int padding = getResources().getDimensionPixelOffset(R.dimen.activity_horizontal_margin);
        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        int thumbWidth = getResources().getDimensionPixelOffset(R.dimen.range_thumb_width);
        final int itemWidth = (screenWidth - (2 * (padding + thumbWidth))) / itemCount;
        extract_Thumnail();

        //썸네일이 추출될때까지 로딩
        loading_gif();

        recyclerAdapter = new trimRecyclerAdapter(thumnail_list, itemWidth);
        recyclerView.setAdapter(recyclerAdapter);
        //<<리싸이클러뷰>>

        //인디케이터 시크바설정
        indicator_seek.setMax(mDuration);

        //range Slider설정
        slider.setTickCount(mDuration);
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


    //썸네일추출
    public void extract_Thumnail(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 10; i++) {
                    Log.d("tak7", "" + mVideoview.getDuration() / 1000);
                    Log.d("tak7", "test: " + (mVideoview.getDuration() / 1000) / 10.0);

                    thumnail_list.add(mediaMetadataRetriever.getFrameAtTime((long) (time * 1000000), mediaMetadataRetriever.OPTION_CLOSEST));//?초 영상 추출)
                    time += plusTime;
                    Log.d("tak7", "" + time);
                }

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        loading_iv.setVisibility(View.GONE);
                        trim_layout.setVisibility(View.VISIBLE);
                        recyclerAdapter.notifyDataSetChanged();
                    }
                });


            }
        }).start();
    }
    public void loading_gif(){
        //로딩중
        trim_layout.setVisibility(View.GONE);
        Glide.with(getActivity())
                .load(R.raw.loading).asGif().diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .error(R.drawable.music)
                .into(loading_iv);
    }

    @Override
    public void onClick(View view) {
            if(view==videoPlay_bt){
                Log.d("tak8","111");
                mVideoview.start();
                isPlaying=true;
                progress_thtead=new Progress_Thtead();
                progress_thtead.start();
                videoPlay_bt.setVisibility(View.GONE);
                videoPause_bt.setVisibility(View.VISIBLE);

        }
             else if(view==videoPause_bt){
                Log.d("tak8","dsfdsf");
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
                VideoEditAtivity.trim_start=slider.getLeftIndex();
                VideoEditAtivity.trim_end=slider.getRightIndex();

            }

    }


    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        //true로 하면 터치가안됨
        return true;
    }

    public class Progress_Thtead extends Thread{
        @Override
        public void run() {
            super.run();
            try {
                while (isPlaying) {
                    indicator_seek.setProgress(mVideoview.getCurrentPosition() / 1000);
                    Log.d("tak12", "" + mVideoview.getCurrentPosition() / 1000);
                    sleep(100);

                }
            }catch (InterruptedException e){
                Log.d("tak12","interrupt!!");
            }
        }
    }

    private String getTime(int seconds) {
        int hr = seconds / 3600;
        int rem = seconds % 3600;
        int mn = rem / 60;
        int sec = rem % 60;
        return String.format("%02d", hr) + ":" + String.format("%02d", mn) + ":" + String.format("%02d", sec);
    }

    Runnable r;
    Handler handler=new Handler();
    //슬라이더의 오른쪽이 바뀌었을때 현재비디오 위치가 바뀐오른쪽보다 커지면 다시 비디오를재시작
    public void Compare_sliderRight(){
        handler.postDelayed(r=new Runnable() {
            @Override
            public void run() {
                if(mVideoview.getCurrentPosition()>=slider.getRightIndex()*1000){
                    mVideoview.seekTo(slider.getLeftIndex()*1000);
                    mVideoview.pause();
                    videoPause_bt.setVisibility(View.GONE);
                    videoPlay_bt.setVisibility(View.VISIBLE);

                    //stop되면 스레드역시 종료
                    if(progress_thtead!=null)
                    progress_thtead.interrupt();

                }


                handler.postDelayed(r,1000);
            }
        },1000);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d("tak12","ondes");
        //OK버튼을 안눌렀을때는 취소하느거므로 비디오를 다시초기화
        if(!isTrim_OK) {
            mVideoview.seekTo(0);
            handler.removeMessages(0);
            if(progress_thtead!=null)
            progress_thtead.interrupt();

        }

    }

    //content:// 형식으로 되있는 uri로부터 파일의 실제 경로 구하기
    private String getPath(final Context context, final Uri uri) {
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        // DocumentProvider

        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {

            // ExternalStorageProvider

            if (isExternalStorageDocument(uri)) {

                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];


                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];

                }
                // TODO handle non-primary volumes
            }


            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(

                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);

            }

            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;

                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

                }
                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };
                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }

        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;

    }

    private boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());

    }



    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    private boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    private boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    private String getDataColumn(Context context, Uri uri, String selection,

                                 String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }

        return null;

    }
}





