package com.jtmcompany.smartadvertisingboard.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.jtmcompany.smartadvertisingboard.R;
import com.jtmcompany.smartadvertisingboard.photoedit.PhotoEditActivity;
import com.jtmcompany.smartadvertisingboard.videoedit.ui.VideoEditAtivity;


public class UploadActivity extends AppCompatActivity {

    private static final int REQUEST_TAKE_GALLERY_VIDEO = 100;
    private static final int REQUEST_TAKE_GALLERY_PHOTO = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        Log.d("tak3","uploadActivity");

        Intent intent=getIntent();
        String data=intent.getStringExtra("upload");
        if(data.equals("video"))
            updoadVideo();
        else if(data.equals("photo"))
            uploadPhoto();
    }


    private void updoadVideo(){
        Log.d("tak3","uploadvideo");
        Intent intent =new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
        startActivityForResult(Intent.createChooser(intent,"Select Video"),REQUEST_TAKE_GALLERY_VIDEO);
    }

    private void uploadPhoto() {
        Log.d("tak3","uploadPhoto");
        Intent intent =new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,REQUEST_TAKE_GALLERY_PHOTO);
    }


        @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("tak3","onActivity");
        if(resultCode==RESULT_OK){
            if(requestCode==REQUEST_TAKE_GALLERY_VIDEO){   //비디오
                Log.d("tak","resultcode_ok");
                Uri selectVideoUri=data.getData();
                Log.d("tak3","uri: "+ selectVideoUri);
               Intent intent=new Intent(this, VideoEditAtivity.class);
               intent.putExtra("selectUri",selectVideoUri);
               startActivity(intent);
               finish();
            }else if(requestCode==REQUEST_TAKE_GALLERY_PHOTO){    //사진
                Uri selectedImageUri=data.getData();
                Intent intent=new Intent(this,PhotoEditActivity.class);
                intent.putExtra("selectUri",selectedImageUri);
                startActivity(intent);
                finish();


            }


        }else if(resultCode==RESULT_CANCELED){
            Log.d("tak3","result_cancel");
            finish();
        }
    }
}
