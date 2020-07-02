package com.jtmcompany.smartadvertisingboard.VideoEdit;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.os.SystemClock;
import android.util.Log;

import com.github.hiteshsondhi88.libffmpeg.ExecuteBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.FFmpeg;
import com.github.hiteshsondhi88.libffmpeg.LoadBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegCommandAlreadyRunningException;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegNotSupportedException;
import com.jtmcompany.smartadvertisingboard.TestFFmpge;

import java.io.File;
import java.io.IOException;

public class FFmpeg_Task {
    FFmpeg fFmpeg;
    Context mContext;
    String filePath;
    String selectVideoPath;
    String selectMusicPath;
    boolean video_Trim_Ok=false;
    boolean music_Trim_Ok=false;
    boolean music_Ok=false;
    boolean img_Ok=false;
    boolean final_OK=false;
    ExecuteBinaryResponseHandler handler;
    String trimVideoPath;
    String trimMusicPath;
    String musicVideoPath;
    String imageVideoPath;
    File moviesDir;
    int insert_img_position=0;
    String imageVideoPath2;



    public FFmpeg_Task(Context mContext, String selectVideoPath, String selectMusicPath) {
        this.mContext = mContext;
        this.selectVideoPath=selectVideoPath;
        this.selectMusicPath=selectMusicPath;
    }



    //ffmpeg load
    public void loadFFMpegBinary(){
        try{
            if(fFmpeg==null){
                Log.d("tak12","ffmpeg: null");
                fFmpeg= FFmpeg.getInstance(mContext);
            }
            fFmpeg.loadBinary((new LoadBinaryResponseHandler(){
                @Override
                public void onFailure() {
                    super.onFailure();
                }
                @Override
                public void onSuccess() {
                    super.onSuccess();
                    Log.d("tak12","ffmpeg: correct Loaded");
                }
            }));
        }catch (FFmpegNotSupportedException e){
            Log.d("tak12","FFmpegNotSupportedException "+e);
        }catch (Exception e){
            Log.d("tak12","Exception not supported"+e);
        }
    }



    //비디오 trim
    public void executeCutVideoCommand(int startsMs, int endMs){
        moviesDir= Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES);
        String fileprefix="trim_video";
        String fileExtn=".mp4";
        File dest=new File(moviesDir,fileprefix+fileExtn);
        if(dest.exists()){
            dest.delete();
        }
        /*
        int fileNO=0;
        while(dest.exists()){
            fileNO++;
            dest=new File(moviesDir,fileprefix+fileNO+fileExtn);
        }
         */
        Log.d("tak15", "startTrim: src: " + selectVideoPath);
        Log.d("tak12", "startTrim: dest: " + dest.getAbsolutePath());
        Log.d("tak12", "startTrim: startMs: " + startsMs);
        Log.d("tak12", "startTrim: endMs: " + endMs);
        trimVideoPath=dest.getAbsolutePath();

        //1. 동영상 trim
            String[] complexCommand = {"-ss", "" + startsMs, "-y", "-i", selectVideoPath, "-t", "" + (endMs - startsMs), "-vcodec", "mpeg4", "-b:v", "2097152", "-b:a", "48000", "-ac", "2", "-ar", "22050", trimVideoPath};
            execFFmpegBinary(complexCommand);
            video_Trim_Ok=true;
    }



    //2. 음악trim
    public void executeCutMusicCommand(){
            File trim_music_dest = new File(moviesDir, "trimmusic.mp3");
            trimMusicPath = trim_music_dest.getAbsolutePath();
        if(trim_music_dest.exists()){
            trim_music_dest.delete();

        }
            String[] complexCommand = {"-ss", "" + 0, "-t", "" + 17, "-i", selectMusicPath, "-acodec", "copy", trimMusicPath};
            execFFmpegBinary(complexCommand);
            music_Trim_Ok=true;
    }



    //3, 음악
    public void executeMusicVideoCommand(){
        File music_dest = new File(moviesDir, "musicVideo.mp4");
        musicVideoPath = music_dest.getAbsolutePath();
        if(music_dest.exists()){
            music_dest.delete();
        }
        String[] complexCommand = new String[]{"-i", trimVideoPath, "-i", trimMusicPath, "-c:v", "copy", "-c:a", "aac", "-map", "0:v:0", "-map", "1:a:0", "-shortest", musicVideoPath};
        execFFmpegBinary(complexCommand);
        music_Ok=true;
    }



    //4. 이미지오버레이(최초)
    public void executeImageVideoCommand(){
        int insert_img_size=VideoEditAtivity.insertView.size();
        File img_dest=new File(moviesDir,"insert"+insert_img_position+".png");
        String imgPath=img_dest.getAbsolutePath();
        File img_video_dest=new File(moviesDir,"imgVideo0.mp4");
        imageVideoPath=img_video_dest.getAbsolutePath();
        if(img_video_dest.exists()){
            img_video_dest.delete();
        }
        //String[] complexCommand={"-i",musicVideoPath,"-i", imgPath, "-preset", "ultrafast", "-strict", "-2", "-filter_complex" ,"overlay=x=200:y=400:enable='between(t,0,6)",imageVideoPath};
        //execFFmpegBinary(complexCommand);
        //img_Ok=true;
        String[] complexCommand={"-i",musicVideoPath,"-i", imgPath, "-preset", "ultrafast", "-strict", "-2", "-filter_complex" ,"overlay=x=200:y=400:enable=" +
                "'between(t,"+VideoEditAtivity.insertView.get(insert_img_position).getInsert_start_time()+","+VideoEditAtivity.insertView.get(insert_img_position).getInsert_end_time()+")",imageVideoPath};
        execFFmpegBinary(complexCommand);

        insert_img_position++;
        
            img_Ok=true;
    }

    //4. 이미지오버레이
    public void executeImageVideoCommand2(){
        if(imageVideoPath2!=null)
        imageVideoPath=imageVideoPath2;

        File img_video_dest=new File(moviesDir,"imgVideo"+insert_img_position+".mp4");
        imageVideoPath2=img_video_dest.getAbsolutePath();
        if(img_video_dest.exists()){
            img_video_dest.delete();
        }

        int insert_img_size=VideoEditAtivity.insertView.size();
        File img_dest=new File(moviesDir,"insert"+insert_img_position+".png");
        String imgPath=img_dest.getAbsolutePath();

        //String[] complexCommand={"-i",musicVideoPath,"-i", imgPath, "-preset", "ultrafast", "-strict", "-2", "-filter_complex" ,"overlay=x=200:y=400:enable='between(t,0,6)",imageVideoPath};
        //execFFmpegBinary(complexCommand);
        //img_Ok=true;

        String[] complexCommand={"-i",imageVideoPath,"-i", imgPath, "-preset", "ultrafast", "-strict", "-2", "-filter_complex" ,"overlay=x=200:y=400:enable=" +
                "'between(t,"+VideoEditAtivity.insertView.get(insert_img_position).getInsert_start_time()+","+VideoEditAtivity.insertView.get(insert_img_position).getInsert_end_time()+")",imageVideoPath2};
        execFFmpegBinary(complexCommand);



        Log.d("tak12","test: "+insert_img_position);
        insert_img_position++;
        if(insert_img_position>=insert_img_size)
            final_OK=true;
    }




    //ffmpeg command를 파라미터로 사용할 메소드
    // 이를 전달하여 FFMpeg 클래스의 메소드실행
    private void execFFmpegBinary(final String[] command){
        try{
            fFmpeg.execute(command,handler=new ExecuteBinaryResponseHandler()
            {
                @Override
                public void onSuccess(String message) {
                    super.onSuccess(message);
                    Log.d("tak12","Success with output: "+message);
                }
                @Override
                public void onProgress(String message) {
                    super.onProgress(message);
                    Log.d("tak12","Progress with output: "+message);
                }
                @Override
                public void onFailure(String message) {
                    super.onFailure(message);
                    Log.d("tak12","Failure with output: "+message);
                }
                @Override
                public void onStart() {
                    super.onStart();
                    Log.d("tak12","Started command: ffmpeg:  "+command);
                }
                @Override
                public void onFinish() {
                    super.onFinish();
                    Log.d("tak12","Finish command: ffmpeg:  "+command);
                    SystemClock.sleep(3000);
                    if(final_OK)
                        System.exit(0);

                    else if(img_Ok&&video_Trim_Ok&& music_Trim_Ok && music_Ok)
                        executeImageVideoCommand2();

                    else if(video_Trim_Ok&& music_Trim_Ok && music_Ok)
                        executeImageVideoCommand();

                    else if(video_Trim_Ok && music_Trim_Ok)
                        executeMusicVideoCommand();

                    else if(video_Trim_Ok)
                        executeCutMusicCommand();
                }
            });
        }catch (FFmpegCommandAlreadyRunningException e){
            Log.d("tak12","catch");

        }

    }

}


