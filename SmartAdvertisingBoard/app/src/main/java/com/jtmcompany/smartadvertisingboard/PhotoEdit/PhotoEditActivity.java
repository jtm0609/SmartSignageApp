package com.jtmcompany.smartadvertisingboard.PhotoEdit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Display;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.jtmcompany.smartadvertisingboard.PhotoEdit.PhotoEdit_BottomFragment.Effect.EffectFragment;
import com.jtmcompany.smartadvertisingboard.PhotoEdit.PhotoEdit_BottomFragment.MotionSticker.MotionFragment;
import com.jtmcompany.smartadvertisingboard.PhotoEdit.PhotoEdit_BottomFragment.Text.TextFragment;
import com.jtmcompany.smartadvertisingboard.R;
import com.jtmcompany.smartadvertisingboard.StickerView.StickerImageView;
import com.jtmcompany.smartadvertisingboard.StickerView.StickerTextView;
import com.jtmcompany.smartadvertisingboard.StickerView.StickerView;
import com.jtmcompany.smartadvertisingboard.VideoEdit.FFmpeg_Task;


import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class PhotoEditActivity extends AppCompatActivity {
    private ImageView imageView;
    private Uri select_Photo_Uri;
    private String select_Photo_Path;
    private List<String> selectItem_PathList=new ArrayList<>();
    private BottomNavigationView bottomNavigationView;
    private FrameLayout photoEdit_frameLayout;
    private ImageView complete_bt;
    private int photo_x;
    private int photo_y;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_edit);

        complete_bt=findViewById(R.id.photo_complete_bt);
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
        select_Photo_Path=getPath(this,select_Photo_Uri);
        Log.d("tak12,","selectPhotoUri: "+select_Photo_Uri);
        Log.d("tak12,","selectPhotoPATH: "+select_Photo_Path);



        complete_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //레이아웃 자식의 개수
                int count=photoEdit_frameLayout.getChildCount();

                //메인 사진의 해상도구하기 x,y
                photo_x=findPhotoPixel().outWidth;
                photo_y=findPhotoPixel().outHeight;


                //현재 프레임레이아웃에 추가된 gif수 만큼 반복문 실행(이미지뷰는 제외하므로 1부터시작)
                for(int i=1; i<count; i++){
                    View v= photoEdit_frameLayout.getChildAt(i);
                    String path="";

                    //추가할 gif의 넓이, 높이, x좌표, y좌표
                    int width;
                    int height;
                    int rotation;
                    double x;
                    double y;
                    int []location=new int[2];


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

                        width=iv.getWidth();
                        height=iv.getHeight();

                    }
                    //텍스트라면
                    else{
                        Log.d("tak13","텍스트");
                        StickerTextView stickerTextView=(StickerTextView)v;
                        View tv=stickerTextView.getMainView();
                        path=saveText(tv);
                        tv.getLocationOnScreen(location);
                        width=tv.getWidth();
                        height=tv.getHeight();

                    }

                    x=location[0];
                    y=location[1];

                    //리스트에 저장
                    selectItem_PathList.add(path);


                    Log.d("tak13","Path: "+path);
                    Log.d("tak12","width: "+width);
                    Log.d("tak12","height: "+height);
                    Log.d("tak12","X: "+x);
                    Log.d("tak12","Y: "+y);

                }

                FFmpeg_Task ffmpeg_task=new FFmpeg_Task(PhotoEditActivity.this,select_Photo_Path, selectItem_PathList,photo_x,photo_y);
                ffmpeg_task.loadFFMpegBinary();
                ffmpeg_task.PhotoToVideoCommand();
                Display display= getWindowManager().getDefaultDisplay();
                Point size=new Point();
                display.getRealSize(size);
                int width=size.x;
                int height=size.y;
                Log.d("tak13","스마트폰 x: "+width);
                Log.d("tak13","스마트폰 y:" +height);

                //저장소에 저장된 리스트의 gif파일들 모두 삭제
                //deleteAllGif();
                //리스트 초기화
                selectItem_PathList.clear();


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

    private void deleteAllGif(){
        for(int i=0; i<selectItem_PathList.size(); i++){
            File file= new File(selectItem_PathList.get(i));
            file.delete();
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
