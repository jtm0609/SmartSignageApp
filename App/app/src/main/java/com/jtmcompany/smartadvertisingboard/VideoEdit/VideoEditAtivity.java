package com.jtmcompany.smartadvertisingboard.VideoEdit;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jtmcompany.smartadvertisingboard.CustomDialog;
import com.jtmcompany.smartadvertisingboard.FFmpeg_Task;
import com.jtmcompany.smartadvertisingboard.R;
import com.jtmcompany.smartadvertisingboard.StickerView.StickerImageView;
import com.jtmcompany.smartadvertisingboard.StickerView.StickerView;
import com.jtmcompany.smartadvertisingboard.VideoEdit.Adapter.VideoEdit_RecyclerAdapter;
import com.jtmcompany.smartadvertisingboard.VideoEdit.Music.MusicList;
import com.jtmcompany.smartadvertisingboard.VideoEdit.VO.EditorMenu_VO;
import com.jtmcompany.smartadvertisingboard.VideoEdit.VO.addItem_VO;
import com.jtmcompany.smartadvertisingboard.Utils.getPathUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


public class VideoEditAtivity extends AppCompatActivity implements VideoEdit_RecyclerAdapter.OnClickEditor_ModelListener, View.OnClickListener, MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener {
    private static final int REQUEST_CODE_MUSIC =200 ;
    private static final int RESULT_OK_MUSIC = 300;
    private List<EditorMenu_VO> list=new ArrayList<>();
    private FrameLayout videoView_container;
    private ImageView complete_bt,video_play_bt,video_stop_bt, back_bt;
    private VideoView videoView;

    public static int trim_start,trim_end,music_trim_start,music_trim_end;
    static boolean isFragmentClose;
    static boolean isMusicCheck=false;
    private int mDuration;
    private int REQUEST_CODE=100;


    //썸네일 시간, 시간을10으로 분할한 시간
    float time = 0;
    double plusTime;
    private Uri select_Video_Uri,select_Music_Uri;
    private List<Bitmap> thumnail_list=new ArrayList();
    private Handler videoHandler;
    private MediaMetadataRetriever mediaMetadataRetriever;
    private VideoTrimFragment videoTrimFragment;
    private VideoTextFragment videoTextFragment;
    private FragmentManager fragmentManager;

    private Boolean VideoRestart=false;
    private Boolean isMusicSelected=false;

    private MediaPlayer musicPlayer;
    private MusicPlayerThread musicPlayerThread;
    private String videoPath;

    private CustomDialog customDialog;
    private View.OnClickListener positiveLisener,negativeLisener;
    private String musicPath;
    public static List<addItem_VO> addItemList;
    public static List<addItem_VO> updateItemList;
    private ProgressBar progressBar;
    private TextView progressBar_startTv;
    private TextView progressBar_endTv;
    private long backKeyClickTime=0;
    private VideoEditThread videoEditThread;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_edit_ativity);
        addItemList=new ArrayList<>();
        updateItemList=new ArrayList<>();
        isFragmentClose=true;

        video_play_bt=findViewById(R.id.video_play);
        video_stop_bt=findViewById(R.id.video_stop);
        complete_bt=findViewById(R.id.video_complete_bt);
        back_bt=findViewById(R.id.back_bt);
        videoView_container=findViewById(R.id.video_eidt_container);
        videoView=findViewById(R.id.video_eidt_video);
        progressBar=findViewById(R.id.video_progress);
        progressBar_startTv=findViewById(R.id.progresstime_s);
        progressBar_endTv=findViewById(R.id.progresstime_e);

        video_play_bt.setOnClickListener(this);
        video_stop_bt.setOnClickListener(this);
        complete_bt.setOnClickListener(this);
        back_bt.setOnClickListener(this);
        videoView_container.setOnClickListener(this);
        videoView.setOnPreparedListener(this);
        videoView.setOnCompletionListener(this);

        select_Video_Uri=getIntent().getParcelableExtra("selectUri");
        videoView.setVideoURI(select_Video_Uri);

        RecyclerView recyclerView=findViewById(R.id.video_edit_recycler);
        list.add(new EditorMenu_VO(getResources().getDrawable(R.drawable.trim),"잘라내기"));
        list.add(new EditorMenu_VO(getResources().getDrawable(R.drawable.text),"텍스트"));
        list.add(new EditorMenu_VO(getResources().getDrawable(R.drawable.sticker),"스티커"));
        list.add(new EditorMenu_VO(getResources().getDrawable(R.drawable.music),"음악"));
        list.add(new EditorMenu_VO(getResources().getDrawable(R.drawable.gallery),"사진"));
        VideoEdit_RecyclerAdapter recyclerAdapter=new VideoEdit_RecyclerAdapter(list);
        recyclerAdapter.setOnClickedListener(this);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(recyclerAdapter);


        //비디오가 duration이 끝났는지 실시간으로 감지
        //끝났으면 중지버튼이아닌 실행버튼이나옴
        videoEditThread=new VideoEditThread(videoView,video_play_bt,video_stop_bt,progressBar, progressBar_startTv,progressBar_endTv);
        videoHandler=new Handler();
        videoHandler.post(videoEditThread);

        videoPath = getPathUtils.getPath(this, select_Video_Uri);
        mediaMetadataRetriever = new MediaMetadataRetriever();
        mediaMetadataRetriever.setDataSource(videoPath);

        extract_Thumnail();

        fragmentManager=getSupportFragmentManager();
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        if(isMusicSelected) {
            mediaPlayer.setVolume(0, 0);
        }
        //비디오 썸네일 설정
        videoView.seekTo(trim_start*1000);
        Log.d("tak99","prepare");

        if(!VideoRestart) {
            trim_end = videoView.getDuration() / 1000;
            mDuration = videoView.getDuration() / 1000;
        }
        Log.d("tak3","trim_end: "+trim_end);
        Log.d("tak3","trim_start: "+trim_start);
        int curTime=videoView.getCurrentPosition()/1000-trim_start;
        int maxTime=VideoEditAtivity.trim_end-trim_start-1;

        progressBar_startTv.setText(getTime(curTime));
        progressBar_endTv.setText(getTime(maxTime));
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        //비디오가 끝나면 음악의현재위치를 음악을 자른부분으로 다시초기화
        if(musicPlayer!=null &&musicPlayer.isPlaying()) {
            musicPlayer.pause();
        }

    }



    @Override
    public void OnClickedEditor_Model(int position) {
        Log.d("tak4",""+position);

        //잘라내기
        if(position==0){
            videoTrimFragment =new VideoTrimFragment(videoView,select_Video_Uri,thumnail_list);
            fragmentManager.beginTransaction().add(R.id.con,videoTrimFragment).commit();

            //텍스트
        }else if(position==1){
            videoTextFragment=new VideoTextFragment(videoView_container,videoView,select_Video_Uri,thumnail_list);
            fragmentManager.beginTransaction().add(R.id.con,videoTextFragment).commit();

            //스티커
        }else if(position==2){
            VideoStickerFragment videoStickerFragment=new VideoStickerFragment(videoView_container,videoView, select_Video_Uri, thumnail_list);
            fragmentManager.beginTransaction().add(R.id.con,videoStickerFragment).commit();

            //음악
        }else if(position==3){
            Intent intent =new Intent(this, MusicList.class);
            startActivityForResult(intent,REQUEST_CODE_MUSIC);

            //사진
        }else if(position==4){
            Intent intent=new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
            startActivityForResult(intent,REQUEST_CODE);
        }
    }

    @Override
    public void onClick(View view) {
        if(view==video_play_bt){
            videoView.start();
            video_stop_bt.setVisibility(View.VISIBLE);
            video_play_bt.setVisibility(VideoView.GONE);
            Log.d("tak3","music_start: "+music_trim_start);
            Log.d("tak3","music_end: "+music_trim_end);
            if(isMusicCheck){
                //음악을 설정하고 최초로 play버튼을 누를때
                if((musicPlayerThread==null)||
                        musicPlayerThread.getState().equals(Thread.State.TERMINATED)) {
                    musicPlayer.seekTo(music_trim_start*1000);
                    musicPlayerThread = new MusicPlayerThread(videoView, musicPlayer);
                    musicPlayerThread.start();
                }
                musicPlayer.start();
            }

        }else if(view==video_stop_bt){
            videoView.pause();
            if(musicPlayer!=null) {
                musicPlayer.pause();
            }
            video_play_bt.setVisibility(View.VISIBLE);
            video_stop_bt.setVisibility(View.GONE);


        }else if(view==videoView_container){
            for(int position=0; position<addItemList.size(); position++){
                addItemList.get(position).getStickerView().hide_View();
            }

        }else if(view==complete_bt){
            if(select_Music_Uri!=null)
                musicPath= getPathUtils.getPath(VideoEditAtivity.this, select_Music_Uri);

            //비디오에 올려져있는 아이템에대한 정보(크기, 좌표, 시간등)를 리스트에담음
            int[] location=new int[2];
            videoView.getLocationOnScreen(location);
            int videoX=location[0];
            int videoY=location[1];
            //*****구현이 필요한부분
            Log.d("tak14","chidCount: "+videoView_container.getChildCount());
            Log.d("tak14","addItemSize: "+addItemList.size());
                for(int i=1; i<videoView_container.getChildCount(); i++){
                    int itemWidth=0, itemHeight=0, itemStartTime=0, itemEndTime=0;
                    int itemX=0,itemY=0;
                    float itemRotation;
                    String itemPath="";
                    StickerView addItem = null;
                    int id=videoView_container.getChildAt(i).getId();


                    for(int j=0; j<addItemList.size(); j++){
                        addItem=addItemList.get(j).getStickerView();
                        //프래그먼트에서 설정했던 아이템들뷰의 id와 비디오위에 올려져있는 뷰의 id가같다면
                        if(id==addItem.getId()){
                            if(addItem instanceof StickerImageView){
                                ImageView iv= (ImageView) addItem.getMainView();
                                itemWidth=iv.getWidth();
                                itemHeight=iv.getHeight();
                                iv.getLocationOnScreen(location);

                            }else {
                                View tv= (View) addItem.getMainView();
                                itemWidth=tv.getWidth();
                                itemHeight=tv.getHeight();
                                tv.getLocationOnScreen(location);

                            }

                            itemRotation=addItem.getRotation();
                            Matrix rotateMatrix = new Matrix();
                            rotateMatrix.postRotate(itemRotation);
                            Log.d("tak12","width: "+itemWidth);
                            Log.d("tak12","height: "+itemHeight);
                            Log.d("tak12","rotation: "+itemRotation);
                            Bitmap bitmap=convertBitmap(addItem.getMainView(),itemWidth,itemHeight,rotateMatrix);
                            itemPath=saveImg(bitmap);

                            itemX=location[0]-videoX;
                            itemY=location[1]-videoY;
                            //프래그먼트에서 설정했던 아이템의 시간정보를 가져옴
                            itemStartTime=addItemList.get(j).getStart();
                            itemEndTime=addItemList.get(j).getEnd();
                            break;
                        }

                    }
                    Log.d("tak14","itemPath: "+itemPath);
                    Log.d("tak14","itemWidth: "+itemWidth);
                    Log.d("tak14","itemHeight: "+itemHeight);
                    Log.d("tak14","itemStartTime: "+itemStartTime);
                    Log.d("tak14","itemEndTime: "+itemEndTime);
                    Log.d("tak14","addItem: "+addItem);
                    Log.d("tak14","itemX: "+itemX);
                    Log.d("tak14","itemY: "+itemY);
                    updateItemList.add(new addItem_VO(itemPath,itemWidth,itemHeight,itemStartTime,itemEndTime,addItem,itemX,itemY));

                }
                addItemList=new ArrayList<>();
                addItemList.addAll(updateItemList);
                updateItemList.clear();

            //*****구현이 필요한부분

            positiveLisener=new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String videoTitle=customDialog.getTitleET().getText().toString();

                    if(videoTitle.equals(""))
                        Toast.makeText(VideoEditAtivity.this, "빈칸을 채워주세요.", Toast.LENGTH_SHORT).show();
                    else{
                        FFmpeg_Task ffmpeg_task=new FFmpeg_Task(VideoEditAtivity.this,videoPath,musicPath,addItemList,videoTitle);
                        ffmpeg_task.loadFFMpegBinary();
                        ffmpeg_task.executeCutVideoCommand(trim_start, trim_end);
                        finish();
                    }
                }
            };

            negativeLisener=new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    customDialog.dismiss();
                }
            };
            customDialog=new CustomDialog(VideoEditAtivity.this,positiveLisener,negativeLisener);
            customDialog.show();
            customDialog.getTimeLayout().setVisibility(View.GONE);

        }else if(view==back_bt){
            finish();
        }
    }

    //썸네일추출
    //프래그먼트에서 썸네일을 추출하면, 프래그먼트를킬때마다 로딩중이므로, 엑티비티에서 미리 list를 추출해서 프래그먼트가실행될때 바로뜨게한다.
    public void extract_Thumnail(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                SystemClock.sleep(1000);
                plusTime = mDuration / 8.0;
                for (int i = 0; i < 8; i++) {
                    thumnail_list.add(mediaMetadataRetriever.getFrameAtTime((long) (time * 1000000), mediaMetadataRetriever.OPTION_CLOSEST));//?초 영상 추출)
                    time += plusTime;
                    Log.d("tak2", "time" + time);

                    //추출중에 trim아이콘을 눌렀을때, 리스트업데이트
                    if(ThumnailView.thumnail_list!=null && ThumnailView.recyclerAdapter!=null){
                        ThumnailView.thumnail_list=thumnail_list;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ThumnailView.recyclerAdapter.notifyDataSetChanged();
                            }
                        });
                    }
                }
            }
        }).start();
    }


    @Override
    protected void onStop() {
        super.onStop();
        Log.d("tak4","onStop");
        videoView.pause();
        videoView.seekTo(trim_start*1000);
        VideoRestart=true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(videoHandler!=null) {
            //videoHandler.removeMessages(0);
            //핸들러종료
            videoEditThread.getHandler().removeMessages(0);

        }
        if(addItemList!=null)
            addItemList.clear();
        Log.d("tak12","onDestroy");
    }

    //갤러리에서 이미지를골랐을때 비디오콘테이너에 추가한다.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQUEST_CODE){
            if(resultCode==RESULT_OK){
                try{
                    InputStream in=getContentResolver().openInputStream(data.getData());
                    Bitmap img= BitmapFactory.decodeStream(in);
                    in.close();
                    StickerImageView galleryImageView=new StickerImageView(this);

                    galleryImageView.setImageBitmap(img);
                    videoView_container.addView(galleryImageView);

                    SelectLocation_Fragment selectLocation_fragment =new SelectLocation_Fragment(videoView,select_Video_Uri,thumnail_list,galleryImageView,videoView_container);
                    fragmentManager.beginTransaction().add(R.id.con, selectLocation_fragment).commit();

                } catch (Exception e) { e.printStackTrace(); }
            }
        }else if(requestCode==REQUEST_CODE_MUSIC){
            if(resultCode==RESULT_OK_MUSIC){
                select_Music_Uri=data.getParcelableExtra("musicUri");
                Log.d("tak4","Uri?: "+select_Music_Uri);
                MusicTrim_Fragment musicTrim_fragment=new MusicTrim_Fragment(select_Music_Uri);
                fragmentManager.beginTransaction().add(R.id.con,musicTrim_fragment).commit();
                musicPlayer=MediaPlayer.create(this,select_Music_Uri);
            }
        }
    }

    //뒤로가기 제어
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        if (System.currentTimeMillis() > backKeyClickTime + 2000) {
            backKeyClickTime = System.currentTimeMillis();
            Toast.makeText(this, "뒤로 가기 버튼을 한 번 더 누르면 종료 됩니다.", Toast.LENGTH_SHORT).show();
        }else if(System.currentTimeMillis()<=backKeyClickTime+2000)
            finish();

    }

    private String getTime(int seconds) {
        int hr = seconds / 3600;
        int rem = seconds % 3600;
        int mn = rem / 60;
        int sec = rem % 60;
        return String.format("%02d", hr) + ":" + String.format("%02d", mn) + ":" + String.format("%02d", sec);
    }


    //뷰 -> 비트맵으로 변환
    private Bitmap convertBitmap (View v,int width,int height, Matrix rotateMatrix){

        Bitmap be= Bitmap.createBitmap(width,height,
                Bitmap.Config.ARGB_8888);
        Canvas c=new Canvas(be);
        v.draw(c);

        Bitmap b=Bitmap.createBitmap(be,0,0,width,height,rotateMatrix,false);

        return b;
    }

    //저장소에 쓰기
    private String saveImg(Bitmap bitmap){
        String path="";
        try {
            File moviesDir= Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES);
            File outputFile=new File(moviesDir,"add_1.png");
            int num=1;
            while(outputFile.exists()){
                num++;
                outputFile=new File(moviesDir,"add_"+num+".png");
            }

            FileOutputStream fos = new FileOutputStream(outputFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();

            path=outputFile.getAbsolutePath();
        }catch (Exception e){
            Toast.makeText(this, "error", Toast.LENGTH_SHORT).show();
        }
        return path;
    }


}
