package com.jtmcompany.smartadvertisingboard.PhotoEdit;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.jtmcompany.smartadvertisingboard.CustomDialog;
import com.jtmcompany.smartadvertisingboard.FFmpeg_Task;
import com.jtmcompany.smartadvertisingboard.PhotoEdit.PhotoEdit_BottomFragment.Effect.EffectFragment;
import com.jtmcompany.smartadvertisingboard.PhotoEdit.PhotoEdit_BottomFragment.MotionSticker.MotionFragment;
import com.jtmcompany.smartadvertisingboard.PhotoEdit.PhotoEdit_BottomFragment.Text.TextFragment;
import com.jtmcompany.smartadvertisingboard.R;
import com.jtmcompany.smartadvertisingboard.StickerView.StickerImageView;
import com.jtmcompany.smartadvertisingboard.StickerView.StickerTextView;
import com.jtmcompany.smartadvertisingboard.getPathUtils;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class PhotoEditActivity extends AppCompatActivity {
    private ImageView imageView;
    private Uri select_Photo_Uri;
    private String select_Photo_Path;
    private List<ItemInfo> selectItem_List=new ArrayList<>();
    private BottomNavigationView bottomNavigationView;
    private FrameLayout photoEdit_frameLayout;
    private ImageView complete_bt,back_bt;
    private int photoWidth;
    private int photoHeight;
    private CustomDialog customDialog;
    private View.OnClickListener positiveLisener;
    private View.OnClickListener negativeLisener;
    private long backKeyClickTime=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_edit);

        complete_bt=findViewById(R.id.photo_complete_bt);
        back_bt=findViewById(R.id.photo_back_bt);
        //프레임레이아웃
        photoEdit_frameLayout=findViewById(R.id.photo_edit_frame);

        bottomNavigationView=findViewById(R.id.photo_edit_bottom_nav);
        bottomNavigationView.setItemIconTintList(null);
        getSupportFragmentManager().beginTransaction().replace(R.id.gif_con,new MotionFragment(photoEdit_frameLayout)).commit();
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id=item.getItemId();
                //모션스티커
                if(id==R.id.motion_sticker){
                    getSupportFragmentManager().beginTransaction().replace(R.id.gif_con,new MotionFragment(photoEdit_frameLayout)).commit();
                //텍스트
                }else if(id==R.id.text){
                    getSupportFragmentManager().beginTransaction().replace(R.id.gif_con,new TextFragment(photoEdit_frameLayout)).commit();
                //효과
                } else if(id==R.id.effect){
                    getSupportFragmentManager().beginTransaction().replace(R.id.gif_con,new EffectFragment(photoEdit_frameLayout)).commit();

                }

                return true;
            }
        });

        //RecyclerView recyclerView=findViewById(R.id.motion_recycler);
        imageView=findViewById(R.id.photo_eidt_img);
        select_Photo_Uri=getIntent().getParcelableExtra("selectUri");
        imageView.setImageURI(select_Photo_Uri);
        select_Photo_Path= getPathUtils.getPath(this, select_Photo_Uri);
        Log.d("tak12,","selectPhotoUri: "+select_Photo_Uri);
        Log.d("tak12,","selectPhotoPATH: "+select_Photo_Path);

        back_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        complete_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                //레이아웃 자식의 개수
                int count=photoEdit_frameLayout.getChildCount();

                //xml에서 fitXY와 matchparent로 고정된 이미지크기를 나타나게했음 922x1084
                int imageWidth=imageView.getWidth();
                int imageHeight=imageView.getHeight();
                //922x1084는 ffmpeg라이브리에서 scale을 922, 1084로해줌(포토에디터의 좌표와 일치시키기위해)
                //고정된크기로한이유: 이미지마다 크기가다르므로, 포토에디터에서 사진의 크기가 다르게나타남
                Log.d("tak14","이미지 width: "+imageWidth); //922
                Log.d("tak14","이미지 height:" +imageHeight); //1084

                //이미지의 x,y좌표를구함(추가된 아이템의 상대좌표를 구하기위해)
                int []location=new int[2];
                imageView.getLocationOnScreen(location);
                int photoX=location[0];
                int photoY=location[1];
                Log.d("tak14","이미지 x: "+photoX);
                Log.d("tak14","이미지 y:" +photoY);

                //이미지의 해상도구하기 x,y( 해상도가큰 카메라사진은 ffmpeg를통해 동영상으로 만들면 회전이되었음, 이를 해결하기위해 해상도를 파악함)
                photoWidth=findPhotoPixel().outWidth;
                photoHeight=findPhotoPixel().outHeight;


                //현재 프레임레이아웃에 추가된 gif수 만큼 반복문 실행(이미지뷰는 제외하므로 1부터시작)
                for(int i=1; i<count; i++){
                    View v= photoEdit_frameLayout.getChildAt(i);
                    String path="";

                    //추가할 gif의 넓이, 높이, x좌표, y좌표
                    //회전이랑 flip은 못하였음
                    int itemWidth;
                    int itemHeight;
                    double itemX;
                    double itemY;

                    //gif라면
                    if(v instanceof StickerImageView) {
                        Log.d("tak13","이미지");
                        StickerImageView stickerImageView=(StickerImageView)v;
                        ImageView iv = (ImageView) stickerImageView.getMainView();
                        //추가할 gif의 uri
                        Uri uri = stickerImageView.getUri();
                        //추가할 gif의 Path
                        path = saveGif(uri);
                        iv.getLocationOnScreen(location);
                        Log.d("tak13","Uri: "+uri);

                        itemWidth=iv.getWidth();
                        itemHeight=iv.getHeight();


                    }
                    //텍스트라면
                    else{
                        Log.d("tak13","텍스트");
                        StickerTextView stickerTextView=(StickerTextView)v;
                        View tv=stickerTextView.getMainView();
                        path=saveText(tv);
                        tv.getLocationOnScreen(location);
                        itemWidth=tv.getWidth();
                        itemHeight=tv.getHeight();


                    }
                    //부모뷰안에 자식뷰의 좌표를 구하기위해 부모뷰의 x,y좌표를 빼줌
                    itemX=location[0]-photoX;
                    itemY=location[1]-photoY;

                    //리스트에 저장
                    selectItem_List.add(new ItemInfo(path,itemWidth,itemHeight,itemX,itemY));


                    Log.d("tak14","Path: "+path);
                    Log.d("tak14","width: "+itemWidth);
                    Log.d("tak14","height: "+itemHeight);
                    Log.d("tak14","ItemX: "+itemX);
                    Log.d("tak14","ItemY: "+itemY);


                }

                //저장소에 저장된 리스트의 gif파일들 모두 삭제
                //deleteAllGif();
                //리스트 초기화
                //selectItem_List.clear();

                //다이얼로그 저장버튼을 눌렀을때,
                positiveLisener=new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String videoTitle=customDialog.getTitleET().getText().toString();
                        String videoTime=customDialog.getTimeET().getText().toString();
                        if(videoTime.equals("") || videoTitle.equals("")){
                            Toast.makeText(PhotoEditActivity.this, "빈칸을 채워주세요.", Toast.LENGTH_SHORT).show();
                        }else {
                            FFmpeg_Task ffmpeg_task = new FFmpeg_Task(PhotoEditActivity.this, select_Photo_Path, selectItem_List, photoWidth, photoHeight, videoTitle, videoTime);
                            ffmpeg_task.loadFFMpegBinary();
                            ffmpeg_task.PhotoToVideoCommand();
                            finish();
                        }

                    }
                };
                //다이얼로그 취소버튼을 눌렀을때
                negativeLisener=new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        customDialog.dismiss();
                    }
                };

                if(count==1){
                    Toast.makeText(PhotoEditActivity.this, "포토에 아이템을 추가하세요.", Toast.LENGTH_SHORT).show();
                }else {
                    customDialog = new CustomDialog(PhotoEditActivity.this, positiveLisener, negativeLisener);
                    customDialog.show();
                }

            }


        });
    }

    //메인 사진의 해상도구하기
    private BitmapFactory.Options findPhotoPixel() {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(select_Photo_Path, options);
        Log.d("tak13","ImgFile: "+options.outWidth+", "+options.outHeight);
        return options;
    }

    //휴대폰에 gif저장
    private String saveGif(Uri gif_uri)  {
        String outputFilePath="";
        try {
            File file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES);
            Uri uri = gif_uri;
            InputStream is = getContentResolver().openInputStream(uri);
//            InputStream is=getResources().openRawResource(R.raw.happy1);

            BufferedInputStream bis = new BufferedInputStream(is);
            ByteArrayOutputStream baos=new ByteArrayOutputStream();
            byte[] img=new byte[1024];

            int current=0;
            while ((current=bis.read())!=-1){
                baos.write(current);
            }

            File outputFile = new File(file, "add_1.gif");
            int num=1;
            while(outputFile.exists()){
                num++;
                outputFile=new File(file,"add_"+num+".gif");
            }

            outputFilePath=outputFile.getAbsolutePath();
            FileOutputStream fos = new FileOutputStream(outputFile);
            fos.write(baos.toByteArray());
            fos.flush();
            fos.close();
            is.close();


        }catch (Exception e){
            e.printStackTrace();
        }

    return outputFilePath;
    }

    //휴대폰에 뷰를 png파일로 변환후 저장
    public String saveText(View tv){
        String outputFilePath="";
        //텍스트뷰를 이미지로 변환
        Bitmap be=Bitmap.createBitmap(tv.getWidth(),tv.getHeight(),Bitmap.Config.ARGB_8888);
        Canvas c=new Canvas(be);
        tv.draw(c);

        Bitmap b=Bitmap.createBitmap(be);
        try{
            File file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES);
            File outputFile = new File(file, "addGif_1.png");
            int num=1;
            while(outputFile.exists()){
                num++;
                outputFile=new File(file,"add_"+num+".png");
            }
            FileOutputStream fos=new FileOutputStream(outputFile);
            b.compress(Bitmap.CompressFormat.PNG,100,fos);
            fos.flush();
            fos.close();
            outputFilePath=outputFile.getAbsolutePath();

        }catch (Exception e){
            e.printStackTrace();
        }

        return outputFilePath;
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




}