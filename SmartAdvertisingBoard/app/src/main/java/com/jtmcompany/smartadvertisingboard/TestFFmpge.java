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
        //loadFFMpegBinary();







        cutBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selectVideoUri!=null){
//                    executeCutVideoCommand(rangeSeekBar.getSelectedMinValue().intValue() *1000,rangeSeekBar.getSelectedMaxValue().intValue()*1000);
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







}
