package com.jtmcompany.smartadvertisingboard.VideoEdit;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
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

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


public class VideoEditAtivity extends AppCompatActivity implements VideoEdit_RecyclerAdapter.OnClickEditor_ModelListener, View.OnClickListener, MediaPlayer.OnPreparedListener {

    ImageView complete_bt;
    private static final int REQUEST_CODE_MUSIC =200 ;
    private static final int RESULT_OK_MUSIC = 300;
    List<EditorMenu_VO> list=new ArrayList<>();
    FrameLayout videoView_container;
    ImageView video_play_bt,video_stop_bt;
    VideoView videoView;

    public static int trim_start,trim_end;
    public static int music_trim_start=0;
    public static int music_trim_end=0;
    static boolean isFragmentClose;
    static boolean isMusicCheck=false;
    private int mDuration;
    private int REQUEST_CODE=100;


    //썸네일 시간, 시간을10으로 분할한 시간
    float time = 0;
    double plusTime;
    Uri select_Video_Uri;
    Uri select_Music_Uri;
    List<Bitmap> thumnail_list=new ArrayList();
    Runnable r;
    Handler videoHandler;
    MediaMetadataRetriever mediaMetadataRetriever;
    VideoTrimFragment videoTrimFragment;
    VideoTextFragment videoTextFragment;
    FragmentManager fragmentManager;

    Boolean VideoRestart=false;
    Boolean isMusicSelected=false;

    MediaPlayer musicPlayer;
    Music_Play_Thread music_play_thread;
    String videoPath;


    private CustomDialog customDialog;
    private View.OnClickListener positiveLisener;
    private View.OnClickListener negativeLisener;
    private String musicPath;
    public static List<addItem_VO> addItemList;
    private int videoWidth, videoHeight;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_edit_ativity);
        addItemList=new ArrayList<>();
        isFragmentClose=true;

        video_play_bt=findViewById(R.id.video_play);
        video_stop_bt=findViewById(R.id.video_stop);
        complete_bt=findViewById(R.id.video_complete_bt);
        videoView_container=findViewById(R.id.video_eidt_container);


        videoView=findViewById(R.id.video_eidt_video);


        video_play_bt.setOnClickListener(this);
        video_stop_bt.setOnClickListener(this);
        complete_bt.setOnClickListener(this);
        videoView_container.setOnClickListener(this);

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


        videoView.setOnPreparedListener(this);

        //비디오가 duration이 끝났는지 실시간으로 확인
        //끝났으면 중지버튼이아닌 실행버튼이나옴
        videoHandler=new Handler();
        videoHandler.postDelayed(r=new Runnable() {
            @Override
            public void run() {
                if (isFragmentClose) {
                    //Log.d("tak3","trim_end: "+trim_end);
                    Log.d("tak3", "videoHandler");
                    Log.d("tak3",""+trim_start);
                    if (videoView.getCurrentPosition() / 1000 >= trim_end) {
                        videoView.seekTo(trim_start * 1000);
                        videoView.pause();
                        Log.d("tak3","sibar");
                    }
                    if (!videoView.isPlaying()) {
                        video_stop_bt.setVisibility(View.GONE);
                        video_play_bt.setVisibility(View.VISIBLE);
                    }

                    for(int i=0; i<addItemList.size(); i++){
                        int start_time=addItemList.get(i).getStart();
                        int end_time=addItemList.get(i).getEnd();
                        addView_appear(start_time,end_time,addItemList.get(i).getStickerView());

                    }
                }
                videoHandler.postDelayed(r, 1000);
            }
        },1000);

        videoPath = getPath(this,select_Video_Uri);
        mediaMetadataRetriever = new MediaMetadataRetriever();
        mediaMetadataRetriever.setDataSource(videoPath);

        extract_Thumnail();

        fragmentManager=getSupportFragmentManager();
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {


        Log.d("tak77","videoWidth: "+videoView.getWidth());
        Log.d("tak77","videoHeight: "+videoView.getHeight());
        Log.d("tak77","videoCWidth: "+videoView_container.getWidth());
        Log.d("tak77","videoCHeight: "+videoView_container.getHeight());
        if(isMusicSelected)
            mediaPlayer.setVolume(0,0);

        Log.d("tak3","setOnPRE");
        if(!VideoRestart) {
            trim_end = videoView.getDuration() / 1000;
            mDuration = videoView.getDuration() / 1000;
        }
        Log.d("tak3","trim_end: "+trim_end);
        Log.d("tak3","trim_start: "+trim_start);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("tak3","onStop");
        videoView.pause();
        videoView.seekTo(trim_start*1000);
        VideoRestart=true;
    }



    public void addView_appear(final int start_time, final int end_time, final StickerView insert_stickerView){
        Log.d("tak3","start: "+start_time);
        Log.d("tak3","end: "+end_time);
        if(videoView.isPlaying()) {
            if (videoView.getCurrentPosition() / 1000 == start_time) {
                insert_stickerView.setVisibility(View.VISIBLE);
                Log.d("tak3", "1");
            }
            if (videoView.getCurrentPosition() / 1000 > end_time || videoView.getCurrentPosition() / 1000 < start_time) {
                insert_stickerView.setVisibility(View.GONE);
                Log.d("tak3", "2");
            }
        }else{
            insert_stickerView.setVisibility(View.GONE);
        }

    }


    @Override
    public void OnClickedEditor_Model(int position) {
        Log.d("tak4",""+position);

        //잘라내기
        if(position==0){
            videoTrimFragment =new VideoTrimFragment(videoView,select_Video_Uri,thumnail_list);
            fragmentManager.beginTransaction().add(R.id.con,videoTrimFragment).commit();

            //잘라내기아이콘을눌렀을때, 핸들러를종료함으로써, 다시 동영상처음부터 끝까지 시작하게한다.
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
    protected void onDestroy() {
        super.onDestroy();
        if(videoHandler!=null)
            videoHandler.removeMessages(0);
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

                } catch (Exception e) {
                    e.printStackTrace();
                }
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

    @Override
    public void onClick(View view) {
        if(view==video_play_bt){
            videoView.start();
            video_stop_bt.setVisibility(View.VISIBLE);
            video_play_bt.setVisibility(VideoView.GONE);
            Log.d("tak3","music_start: "+music_trim_start);
            Log.d("tak3","music_end: "+music_trim_end);
            if(isMusicCheck){
                musicPlayer.seekTo(music_trim_start*1000);
                music_play_thread=new Music_Play_Thread();
                musicPlayer.start();
                music_play_thread.start();

            }

        }else if(view==video_stop_bt){
            videoView.pause();
            if(musicPlayer!=null)
                musicPlayer.pause();
            video_play_bt.setVisibility(View.VISIBLE);
            video_stop_bt.setVisibility(View.GONE);
            if(music_play_thread!=null)
                music_play_thread.interrupt();

        }else if(view==videoView_container){
            for(int position=0; position<addItemList.size(); position++){
                addItemList.get(position).getStickerView().hide_View();

            }

        }else if(view==complete_bt){
            if(select_Music_Uri!=null)
            musicPath= getPath(VideoEditAtivity.this, select_Music_Uri);

            positiveLisener=new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String videoTitle=customDialog.getTitleET().getText().toString();

                    if(videoTitle.equals(""))
                        Toast.makeText(VideoEditAtivity.this, "빈칸을 채워주세요.", Toast.LENGTH_SHORT).show();
                    else{
                        FFmpeg_Task ffmpeg_task=new FFmpeg_Task(VideoEditAtivity.this,videoPath,musicPath,addItemList,videoWidth,videoHeight);
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
            customDialog.getTimeET().setVisibility(View.GONE);

        }
    }

    public class Music_Play_Thread extends Thread{
        @Override
        public void run() {
            super.run();
            while (videoView.isPlaying()) {
                try {

                    Log.d("tak3", "Music_play_thread");
                    if (musicPlayer.getCurrentPosition() / 1000 > music_trim_end) {
                        musicPlayer.seekTo(trim_start * 1000);
                        musicPlayer.pause();

                    }
                    sleep(1000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if(!videoView.isPlaying())
                musicPlayer.pause();
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
