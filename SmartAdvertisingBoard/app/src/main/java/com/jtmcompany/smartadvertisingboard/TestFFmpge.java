package com.jtmcompany.smartadvertisingboard;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.github.hiteshsondhi88.libffmpeg.ExecuteBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.FFmpeg;
import com.github.hiteshsondhi88.libffmpeg.LoadBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegCommandAlreadyRunningException;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegNotSupportedException;
import com.jtmcompany.smartadvertisingboard.VideoEdit.Music.MusicList;
import com.jtmcompany.smartadvertisingboard.VideoEdit.VideoEditAtivity;

import org.florescu.android.rangeseekbar.RangeSeekBar;

import java.io.File;

public class TestFFmpge extends AppCompatActivity {
    private static final int REQUEST_TAKE_GALLERY_VIDEO = 100;
    FFmpeg fFmpeg;
    Uri selectVideoUri;
    VideoView videoView;
    RangeSeekBar rangeSeekBar;
    TextView seekvar_tvLeft, seekbar_tvRight;
    int duration;
    Runnable r;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_ffmpge);

        Button uploadBt=findViewById(R.id.uproad_bt);
        Button cutBt=findViewById(R.id.cut_bt);
        Button musicBt=findViewById(R.id.music_bt);
        Button testBt=findViewById(R.id.test_bt);
        videoView=findViewById(R.id.videoView);
        rangeSeekBar=findViewById(R.id.video_seekbar);
        seekvar_tvLeft=findViewById(R.id.tvLeft);
        seekbar_tvRight=findViewById(R.id.tvRight);
        loadFFMpegBinary();







        cutBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selectVideoUri!=null){
                    executeCutVideoCommand(rangeSeekBar.getSelectedMinValue().intValue() *1000,rangeSeekBar.getSelectedMaxValue().intValue()*1000);
                }else
                    Toast.makeText(TestFFmpge.this, "please upload a video", Toast.LENGTH_SHORT).show();
            }
        });

        musicBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(TestFFmpge.this, MusicList.class);
                startActivity(intent);
            }
        });

        //동영상+오디오 합성
        testBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(TestFFmpge.this, VideoEditAtivity.class);
                startActivity(intent);
                /*String path=Environment.getExternalStorageDirectory()+"/Music";
                File dir=new File(path);
                if(dir.isDirectory()){
                    Log.d("tak4","디렉터리 있음");
                }else{
                    Log.d("tak4","디렉터리 없음");
                }
                Log.d("tak4",path);
                File file=new File(path,"darius.mp3");
                if(!file.isFile()){
                    Log.d("tak4","file이없음");
                }
                try {
                    String filePath=file.getCanonicalPath();
                    Log.d("tak4","filepath: "+filePath);
                    //비디오 오디오합성
                    String yourRealPath=getPath(TestFFmpge.this,selectVideoUri);
                    Log.d("tak4","yourRealPath: "+yourRealPath);
                    File moviesDir=Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES);
                    String fileprefix="cut_videoo";
                    String fileExtn=".mp4";
                    File dest=new File(moviesDir,fileprefix+fileExtn);
                    String filePath2=dest.getAbsolutePath();
                    Log.d("tak4","filepath2: "+filePath2);
                    //String[] complexCommand={"-i",yourRealPath,  "-i" ,filePath, "-c:v", "copy", "-c:a", "aac", "-map", "0:v:0", "-map", "1:a:0", "-shortest",filePath2};
                    //텍스트추가
                    String path3=Environment.getExternalStorageDirectory().getCanonicalPath();
                    File file3=new File(path3,"text.ttf");
                    if(!file3.isFile()){
                        Log.d("tak6","file이 없음");
                    }
                    Log.d("tak6",path3);
                    String[] complexCommand = new String[] {
                            "-y", "-i", yourRealPath, "-vf", "drawtext=text='Title of this Video':fontfile=/storage/emulated/0/text.ttf: fontcolor=white: fontsize=24: box=1: boxcolor=black@0.5: boxborderw=5: x=20: y=20:enable='between(t,0,2)'", filePath2
                    };
                    execFFmpegBinary(complexCommand);





                } catch (IOException e) {
                    e.printStackTrace();
                }
  */


            }

        });




    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            Log.d("tak","result_ok");
            if(requestCode==REQUEST_TAKE_GALLERY_VIDEO){
                Log.d("tak","resultcode_ok");
                selectVideoUri=data.getData();
                videoView.setVideoURI(selectVideoUri);
                videoView.start();

                videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mediaPlayer) {
                        duration=mediaPlayer.getDuration()/1000;
                        Log.d("tak","duration: "+duration);
                        seekvar_tvLeft.setText("00:00:00");
                        seekbar_tvRight.setText(getTime(mediaPlayer.getDuration()/1000));
                        mediaPlayer.setLooping(true); //미디어반복
                        rangeSeekBar.setRangeValues(0,duration);
                        rangeSeekBar.setSelectedMinValue(0);
                        rangeSeekBar.setSelectedMaxValue(duration);
                        rangeSeekBar.setEnabled(true);

                        rangeSeekBar.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener() {
                            @Override
                            public void onRangeSeekBarValuesChanged(RangeSeekBar bar, Object minValue, Object maxValue) {

                                videoView.seekTo((int) minValue * 1000);
                                seekvar_tvLeft.setText(getTime((int)bar.getSelectedMinValue()));
                                seekbar_tvRight.setText(getTime((int)bar.getSelectedMaxValue()));


                            }
                        });
                        final Handler handler = new Handler();
                        handler.postDelayed(r=new Runnable() {
                            @Override
                            public void run() {

                                //Log.d("tak","position: "+videoView.getCurrentPosition());
                                if(videoView.getCurrentPosition()>= rangeSeekBar.getSelectedMaxValue().intValue()*1000) {
                                    videoView.seekTo(rangeSeekBar.getSelectedMinValue().intValue() * 1000);
                                    Log.d("tak","test");
                                }
                                handler.postDelayed(r, 1000);
                            }
                        },1000);
                    }
                });
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

    //ffmpeg load
    private void loadFFMpegBinary(){
        try{
            if(fFmpeg==null){
                Log.d("tak","ffmpeg: null");
                fFmpeg=FFmpeg.getInstance(this);
            }
            fFmpeg.loadBinary((new LoadBinaryResponseHandler(){
                @Override
                public void onFailure() {
                    super.onFailure();

                }

                @Override
                public void onSuccess() {
                    super.onSuccess();
                    Log.d("tak","ffmpeg: correct Loaded");
                }
            }));
        }catch (FFmpegNotSupportedException e){
            Log.d("tak","FFmpegNotSupportedException "+e);

        }catch (Exception e){
            Log.d("tak","Exception not supported"+e);
        }
    }

    //ffmpeg command를 파라미터로 사용할 메소드
    // 이를 전달하여 FFMpeg 클래스의 메소드실행
    private void execFFmpegBinary(final String[] command){
        try{
            fFmpeg.execute(command,new ExecuteBinaryResponseHandler()
            {
                @Override
                public void onSuccess(String message) {
                    super.onSuccess(message);
                    Log.d("tak","Success with output: "+message);
                }

                @Override
                public void onProgress(String message) {
                    super.onProgress(message);
                    Log.d("tak","Progress with output: "+message);
                }

                @Override
                public void onFailure(String message) {
                    super.onFailure(message);
                    Log.d("tak","Failure with output: "+message);
                }

                @Override
                public void onStart() {
                    super.onStart();
                    Log.d("tak","Started command: ffmpeg:  "+command);
                }

                @Override
                public void onFinish() {
                    super.onFinish();
                    Log.d("tak","Finish command: ffmpeg:  "+command);
                }
            });
        }catch (FFmpegCommandAlreadyRunningException e){
            Log.d("tak","catch");

        }

    }

    private void executeCutVideoCommand(int startsMs, int endMs){
        File moviesDir=Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES);
        String fileprefix="cut_video";
        String fileExtn=".mp4";
        String yourRealPath=getPath(TestFFmpge.this,selectVideoUri);
        Log.d("tak2","tak2: "+selectVideoUri);
        File dest=new File(moviesDir,fileprefix+fileExtn);
        int fileNO=0;
        while(dest.exists()){
            fileNO++;
            dest=new File(moviesDir,fileprefix+fileNO+fileExtn);
        }


        Log.d("tak5", "startTrim: src: " + yourRealPath);
        Log.d("tak2", "startTrim: dest: " + dest.getAbsolutePath());
        Log.d("tak", "startTrim: startMs: " + startsMs);
        Log.d("tak", "startTrim: endMs: " + endMs);
        String filePath=dest.getAbsolutePath();
        String[] complexCommand = {"-ss", "" + startsMs / 1000, "-y", "-i", yourRealPath, "-t", "" + (endMs - startsMs) / 1000,"-vcodec", "mpeg4", "-b:v", "2097152", "-b:a", "48000", "-ac", "2", "-ar", "22050", filePath};
        execFFmpegBinary(complexCommand);

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

    /**
     * Get the value of the data column for this Uri.
     */
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



    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
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
}
