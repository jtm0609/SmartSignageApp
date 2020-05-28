package com.jtmcompany.smartadvertisingboard;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.jtmcompany.smartadvertisingboard.VideoEdit.VideoEditAtivity;

import org.florescu.android.rangeseekbar.RangeSeekBar;

public class UploadActivity extends AppCompatActivity {

    private static final int REQUEST_TAKE_GALLERY_VIDEO = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        Log.d("tak3","oncreate");
        updoadVideo();
    }

    private void updoadVideo(){
        Log.d("tak3","uploadvideo");
        Intent intent =new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
        startActivityForResult(Intent.createChooser(intent,"Select Video"),REQUEST_TAKE_GALLERY_VIDEO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("tak3","onActivity");
        if(resultCode==RESULT_OK){
            Log.d("tak3","result_ok");
            Log.d("tak","result_ok");
            if(requestCode==REQUEST_TAKE_GALLERY_VIDEO){
                Log.d("tak","resultcode_ok");
                Uri selectVideoUri=data.getData();
                Log.d("tak3","uri: "+ selectVideoUri);
               Intent intent=new Intent(this, VideoEditAtivity.class);
               intent.putExtra("selectUri",selectVideoUri);
               startActivity(intent);
               finish();


            }
        }else if(resultCode==RESULT_CANCELED){
            Log.d("tak3","result_cancel");
            finish();
        }
    }
}
