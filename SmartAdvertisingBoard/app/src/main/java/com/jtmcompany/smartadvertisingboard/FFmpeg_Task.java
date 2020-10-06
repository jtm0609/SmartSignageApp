package com.jtmcompany.smartadvertisingboard;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.github.hiteshsondhi88.libffmpeg.ExecuteBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.FFmpeg;
import com.github.hiteshsondhi88.libffmpeg.LoadBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegCommandAlreadyRunningException;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegNotSupportedException;
import com.jtmcompany.smartadvertisingboard.DB.MyVideoDB;
import com.jtmcompany.smartadvertisingboard.PhotoEdit.ItemInfo;
import com.jtmcompany.smartadvertisingboard.VideoEdit.VO.addItem_VO;
import com.jtmcompany.smartadvertisingboard.VideoEdit.VideoEditAtivity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;

public class FFmpeg_Task {
    private FFmpeg fFmpeg;
    private Context mContext;
    private String selectVideoPath;
    private String selectMusicPath;
    private ExecuteBinaryResponseHandler handler;
    private String trimVideoPath,trimMusicPath,musicVideoPath,imageVideoPath;
    File moviesDir;
    private List<addItem_VO> addItemList;


    //포토에디터 매개변수
    private String selectPhotoPath;
    private String finalVideoPath;
    private List<ItemInfo> selectItemList;
    private int photo_x;
    private int photo_y;
    private  boolean flag=false;
    private boolean photoWorking=false;
    private boolean videoWorking=false;
    private String videoTitle;
    private String videoTime;




    //비디오
    public FFmpeg_Task(Context mContext, String selectVideoPath, String selectMusicPath, List<addItem_VO> list ,String videoTitle) {
        this.mContext = mContext;
        this.selectVideoPath=selectVideoPath;
        this.selectMusicPath=selectMusicPath;
        this.addItemList=list;
        this.videoTitle=videoTitle;
    }

    //포토
    public FFmpeg_Task(Context mContext,String selectPhotoPath, List<ItemInfo> selectItemhList, int x, int y, String videoTitle, String videoTime) {
        this.mContext=mContext;
        this.selectPhotoPath=selectPhotoPath;
        this.selectItemList=selectItemhList;
        this.photo_x=x;
        this.photo_y=y;
        this.videoTitle=videoTitle;
        this.videoTime=videoTime;


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



    //1. 비디오에디터: trim
    public void executeCutVideoCommand(int startsMs, int endMs){
        Log.d("tak77","1");
        moviesDir= Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES);
        videoWorking=true;

        File dest=new File(moviesDir,"trimVideo.mp4");


        Log.d("tak15", "startTrim: src: " + selectVideoPath);
        Log.d("tak12", "startTrim: dest: " + dest.getAbsolutePath());
        Log.d("tak12", "startTrim: startMs: " + startsMs);
        Log.d("tak12", "startTrim: endMs: " + endMs);
        trimVideoPath=dest.getAbsolutePath();

        //1. 동영상 trim
        String[] complexCommand = {"-ss", "" + startsMs, "-y", "-i", selectVideoPath, "-t", "" + (endMs - startsMs), "-vcodec", "mpeg4", "-b:v", "2097152", "-b:a", "48000", "-ac", "2", "-ar", "22050","-vf","scale=720:1280" ,trimVideoPath};
        execFFmpegBinary(complexCommand);


        executeCutMusicCommand(trimVideoPath);
    }



    //2. 비디오에디터: 음악trim
    public void executeCutMusicCommand(String inputVideoPath){
        Log.d("tak77","2");
        if(selectMusicPath!=null) {
            File trim_music_dest = new File(moviesDir, "trimmusic.mp3");
            trimMusicPath = trim_music_dest.getAbsolutePath();

            Log.d("tak12","sssss"+VideoEditAtivity.trim_start);
            Log.d("tak12","eeeeee"+ VideoEditAtivity.trim_end);
            String[] complexCommand = {"-ss", "" + VideoEditAtivity.music_trim_start, "-t", "" + VideoEditAtivity.music_trim_end, "-i", selectMusicPath, "-acodec", "copy", trimMusicPath};
            execFFmpegBinary(complexCommand);
            executeMusicVideoCommand(inputVideoPath, trimMusicPath);
        }else if(!addItemList.isEmpty())
            executeImageVideoCommand(inputVideoPath);

        //trim한 비디오가 최종비디오가됨
        else
        finalVideoPath=inputVideoPath;

    }



    //3, 비디오에디터: trim한 음악넣기
    public void executeMusicVideoCommand(String inputVideoPath, String inputMusicPath){
        Log.d("tak77","3");
        File music_dest = new File(moviesDir, "musicVideo.mp4");


        musicVideoPath = music_dest.getAbsolutePath();

        String[] complexCommand = new String[]{"-i", inputVideoPath, "-i", inputMusicPath, "-c:v", "copy", "-c:a", "aac", "-map", "0:v:0", "-map", "1:a:0", "-shortest", musicVideoPath};
        execFFmpegBinary(complexCommand);

        if(!addItemList.isEmpty())
        executeImageVideoCommand(musicVideoPath);

        //trim+음악이들어간 비디오가 최종비디오가됨
        else
        finalVideoPath=musicVideoPath;
    }



    //4. 비디오에디터: 동영상에 이미지오버레이
    public void executeImageVideoCommand(String inputVideoPath) {
        Log.d("tak77","4");
        File img_video_dest = new File(moviesDir, "imgVideo1.mp4");



        imageVideoPath = img_video_dest.getAbsolutePath();

        //String[] complexCommand={"-i",musicVideoPath,"-i", imgPath, "-preset", "ultrafast", "-strict", "-2", "-filter_complex" ,"overlay=x=200:y=400:enable='between(t,0,6)",imageVideoPath};
        //execFFmpegBinary(complexCommand);
        //img_Ok=true;

        //String[] complexCommand = {"-i", inputVideoPath, "-i", imgPath, "-preset", "ultrafast", "-strict", "-2", "-filter_complex", "overlay=x=200:y=400:enable=" +
          //      "'between(t," + (VideoEditAtivity.addItemList.get(insert_img_position).getStart()-VideoEditAtivity.trim_start) + "," + (VideoEditAtivity.addItemList.get(insert_img_position).getEnd()-VideoEditAtivity.trim_start) + ")", imageVideoPath};


        //비디오, gif입력경로등을 설정하는 명령어
        List<String> inputList=new ArrayList<>();
        inputList.add("-i");
        inputList.add(inputVideoPath);

        //압룩률, 크기, 오버레이 위치등 설정을 하는 명령어
        List<String> settingList=new ArrayList<>();
        settingList.add("-preset");
        settingList.add("ultrafast");
        settingList.add("-filter_complex");

        //scale과 overlay 명령어 설정
        //video의 해상도설정
        //String scale_str="[0:v]scale=922:1084[p1]";
        String scale_str="[0:v]scale=720:1280[p1]";

        String overlay_str="";
        String filterComplex_info="";

        //리스트에 추가된 gif파일이나 png파일을 꺼내서 각 명령어에맞게 더한다.
        for(int i=0; i<addItemList.size(); i++){
            //gif파일인지 png파일인지 확인하기 위해 마지막 3글자를 추출한다.
            String itemPath=addItemList.get(i).getPath();

            //<입력부분>

            inputList.add("-i");
            inputList.add(itemPath);
            Log.d("tak115","X:" +addItemList.get(i).getX());
            Log.d("tak115","Y:" +addItemList.get(i).getY());

            //<삽입할 아이템의 크기 조정 부분>
            scale_str+=";["+(i+1)+":v]scale="+addItemList.get(i).getWidth()+":"+addItemList.get(i).getHeight()+"[i"+(i+1)+"]";
            //<삽입할 아이템의 위치 조정 부분>
            overlay_str+=";[p"+(i+1)+"][i"+(i+1)+"]overlay="+addItemList.get(i).getX()+":"+addItemList.get(i).getY();
            overlay_str+=":enable='between(t,"+(addItemList.get(i).getStart()-VideoEditAtivity.trim_start)+","+(addItemList.get(i).getEnd()-VideoEditAtivity.trim_start)+")'";


            //마지막부분이면 마지막에 변수를넣어줄필요강벗음
            if(i!=addItemList.size()-1)
                overlay_str+="[p"+(i+2)+"]";
        }
        //명령어(string부분)을 합치고 setting 리스트에넣어준다.
        filterComplex_info=scale_str+overlay_str;
        settingList.add(filterComplex_info);
        //출력할 비디오 경로추가
        settingList.add(imageVideoPath);

        //input 리스트와 setting 리스트를 합친다
        List<String> complexCommandList=new ArrayList<>();
        complexCommandList.addAll(inputList);
        complexCommandList.addAll(settingList);

        //명령어 리스트를 String 배열로 변환
        String[] complexCommand=complexCommandList.toArray(new String[complexCommandList.size()]);
        execFFmpegBinary(complexCommand);

        finalVideoPath=imageVideoPath;
    }







    //포토에디터 1. 이미지->동영상
    public void PhotoToVideoCommand(){
        photoWorking=true;
        Log.d("tak15","1: "+flag);
        Log.d("tak12","-----시작 -----");
        moviesDir= Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES);
        File photo_Video_dest=new File(moviesDir,"photoVideo"+".mp4");
        String Photo_VideoPath=photo_Video_dest.getAbsolutePath();

        //파일삭제(input비디오파일)+ //추후 gif까지 된파일은 db저장후 삭제할예정
        if(photo_Video_dest.exists()){
            photo_Video_dest.delete();
        }

        //scale= 화질 , t= 초
        //transpose 회전
        //카메라 사진은 회전해서 비디오로 만들어지는 문제가있음... 따라서 해상도가큰사진은 회전을 시켜주는 방향으로 구현함
        String[] complexCommand;
        //카메라사진
        if(photo_x>1080 && photo_y>2280)
            complexCommand= new String[]{"-loop", "1", "-i", selectPhotoPath, "-vcodec", "mpeg4", "-vf", "scale=922:1084,transpose=1", "-t", videoTime, Photo_VideoPath};
            //스크린샷이나 해상도가낮은파일
        else
            complexCommand= new String[]{"-loop", "1", "-i", selectPhotoPath, "-vcodec", "mpeg4", "-vf", "scale=922:1084", "-t", videoTime, Photo_VideoPath};


        Log.d("tak15","2: "+flag);
        execFFmpegBinary(complexCommand);
        Log.d("tak15","3: "+flag);

        //추가한 아이템이 없다면
        if(selectItemList.size()!=0)
            gifOverlayCommand(Photo_VideoPath);


    }

    //포토에디터 2. gif, png(텍스트)오버레이
    public void gifOverlayCommand(String input_VideoPath){

        Log.d("tak15","4: "+flag);
        int count = 1;
        File photo_VideoPath_dest = new File(moviesDir, "Video_Result1" + ".mp4");
        while (photo_VideoPath_dest.exists()) {
            count++;
            photo_VideoPath_dest = new File(moviesDir, "Video_Result" + count + ".mp4");
        }
        finalVideoPath = photo_VideoPath_dest.getAbsolutePath();

        //gif는 ignore_loop를해줘야 gif형식으로 오버레이가되고, shortest=1를 해야 무한루프에 빠지지않음
        //[0]은 첫번째 인풋값, [1]은 두번째 인풋값을 말함
        //-preset ultrrafast : 압축률설정
        //p1,p2,, i1, i2 변수임
        //String[] complexCommand = {"-i", input_VideoPath, "-ignore_loop", "0", "-i", gifPath,"-ignore_loop", "0", "-i", gifPath2,"-preset", "ultrafast",  "-filter_complex", "[0:v]scale=640:360[p1];[1:v]scale=100:100[i1];[2:v]scale=100:100[i2];[p1][i1]overlay=0:0:shortest=1[p2];[p2][i2]overlay=0:100:shortest=1", photo_VideoPath};

        //비디오, gif입력경로등을 설정하는 명령어
        List<String> inputList=new ArrayList<>();
        inputList.add("-i");
        inputList.add(input_VideoPath);

        //압룩률, 크기, 오버레이 위치등 설정을 하는 명령어
        List<String> settingList=new ArrayList<>();
        settingList.add("-preset");
        settingList.add("ultrafast");
        settingList.add("-filter_complex");

        //scale과 overlay 명령어 설정
        //video의 해상도설정
        String scale_str="[0:v]scale=922:1084[p1]";
        String overlay_str="";
        String filterComplex_info="";

        //리스트에 추가된 gif파일이나 png파일을 꺼내서 각 명령어에맞게 더한다.
        for(int i=0; i<selectItemList.size(); i++){
            //gif파일인지 png파일인지 확인하기 위해 마지막 3글자를 추출한다.
            String itemPath=selectItemList.get(i).getPath();
            String file_validation=itemPath.substring(itemPath.length()-3,itemPath.length());

            //<입력부분>
            //gif파일이면 ignore명령어를 넣어준다.
            if(file_validation.equals("gif")) {
                inputList.add("-ignore_loop");
                inputList.add("0");
            }

            inputList.add("-i");
            inputList.add(itemPath);
            Log.d("tak115","X:" +selectItemList.get(i).getX());
            Log.d("tak115","Y:" +selectItemList.get(i).getY());

            //<삽입할 아이템의 크기 조정 부분>
            scale_str+=";["+(i+1)+":v]scale="+selectItemList.get(i).getWidth()+":"+selectItemList.get(i).getHeight()+"[i"+(i+1)+"]";
            //<삽입할 아이템의 위치 조정 부분>
            overlay_str+=";[p"+(i+1)+"][i"+(i+1)+"]overlay="+selectItemList.get(i).getX()+":"+selectItemList.get(i).getY();
            //gif파일이면 ignore_loop랑 매치가되기위해 shortest를 넣어줘야함
            if(file_validation.equals("gif"))
                overlay_str+=":shortest=1";

            //마지막부분이면 마지막에 변수를넣어줄필요가 없음
            if(i!=selectItemList.size()-1)
                overlay_str+="[p"+(i+2)+"]";
        }
        //명령어(string부분)을 합치고 setting 리스트에넣어준다.
        filterComplex_info=scale_str+overlay_str;
        settingList.add(filterComplex_info);
        //출력할 비디오 경로추가
        settingList.add(finalVideoPath);

        //input 리스트와 setting 리스트를 합친다
        List<String> complexCommandList=new ArrayList<>();
        complexCommandList.addAll(inputList);
        complexCommandList.addAll(settingList);

        //명령어 리스트를 String 배열로 변환
        String[] complexCommand=complexCommandList.toArray(new String[complexCommandList.size()]);

        Log.d("tak15","5: "+flag);
        execFFmpegBinary(complexCommand);
        Log.d("tak15","6: "+flag);
        //명령어 전체부분을 출력(확인용)
        for(int i=0; i<complexCommand.length; i++)
            Log.d("tak13",complexCommand[i]);


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
                    Log.d("tak12", "Finish command: ffmpeg:  " + command);
                    //SystemClock.sleep(3000);

                    if (photoWorking) {
                        Log.d("tak12", "리스트사이즈: " + selectItemList.size());
                        Log.d("tak12", "testPath: " + finalVideoPath);

                        Log.d("tak12", "flag: " + flag);
                        //포토광고 제작
                        //비동기로인해 started-> started -> progress -> success ->finish-> progress-> success-> finish 로 진행됨
                        //따라서  flag를 멤버변수로 false로해두고, 첫번째의 execcommand가끝나면 falg를 true로해주고 다음 exec실행되면
                        //최종비디오가 생성된것이므로, 아이템을 지우고, 서버에 파일업로드를 함

                        if (flag) {
                            deleteAllGif();
                            //db저장
                            Realm.init(mContext);
                            Realm mRealm = Realm.getDefaultInstance();
                            mRealm.executeTransaction(new Realm.Transaction() {
                                @Override
                                public void execute(Realm realm) {
                                    MyVideoDB myVideoDB = realm.createObject(MyVideoDB.class);
                                    myVideoDB.videoName = videoTitle;
                                    myVideoDB.videoPath = finalVideoPath;
                                }
                            });
                        }
                        flag = true;
                    } else if (videoWorking) {
                        File file = new File(finalVideoPath);
                        //파일 이름변경
                        if (file.exists()){
                            File renameFile=new File(moviesDir,"Video_Result1" + ".mp4");
                            int count=1;
                            while (renameFile.exists()) {
                                count++;
                                renameFile = new File(moviesDir, "Video_Result" + count + ".mp4");
                            }
                            file.renameTo(renameFile);
                            finalVideoPath=renameFile.getAbsolutePath();
                            //db저장
                            Realm.init(mContext);
                        Realm mRealm = Realm.getDefaultInstance();
                        mRealm.executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                MyVideoDB myVideoDB = realm.createObject(MyVideoDB.class);
                                myVideoDB.videoName = videoTitle;
                                myVideoDB.videoPath = finalVideoPath;
                            }
                        });

                        //작업했던 파일 삭제작업
                           File deleteFile;
                        if(trimVideoPath!=null) {
                            deleteFile = new File(trimVideoPath);
                            if (deleteFile.exists())
                                deleteFile.delete();
                        }

                        if(trimMusicPath!=null) {
                            deleteFile = new File(trimMusicPath);
                            if (deleteFile.exists())
                                deleteFile.delete();
                        }

                        if(musicVideoPath!=null) {
                            deleteFile = new File(musicVideoPath);
                            if (deleteFile.exists())
                                deleteFile.delete();
                        }

                        if(imageVideoPath!=null) {
                            deleteFile = new File(imageVideoPath);
                            if (deleteFile.exists())
                                deleteFile.delete();
                        }

                        deleteAllPNG();


                    }
                }


                }
            });
        }catch (FFmpegCommandAlreadyRunningException e){
            Log.d("tak12","catch");

        }

    }

    private void deleteAllGif(){
        for(int i=0; i<selectItemList.size(); i++){
            File file= new File(selectItemList.get(i).getPath());
            file.delete();
        }
    }

    //deleteAll gif처럼 리스트의 사이즈를 받아서 삭제하려고했지만, 사이즈가 자꾸 0이찍혀, 파일명을보고 삭제함
    private void deleteAllPNG(){
        Log.d("tak13","size: "+addItemList.size());
            File file=new File(moviesDir,"add_1.png");
            int count=1;
            while(file.exists()){
                file.delete();
                count++;
                file=new File(moviesDir,"add_"+count+".png");
            }
    }

}


