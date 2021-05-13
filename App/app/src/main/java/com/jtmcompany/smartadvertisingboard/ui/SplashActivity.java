package com.jtmcompany.smartadvertisingboard.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.jtmcompany.smartadvertisingboard.R;
import com.jtmcompany.smartadvertisingboard.login.ui.LoginActivity;

//SplashActivity -> MainActivity-> UploadAcitivity -> Photo or Video Activity
public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Handler handler=new Handler();
        handler.postDelayed(new splashHandler(),1000);
    }

    private class splashHandler implements Runnable{

        @Override
        public void run() {
            startActivity(new Intent(SplashActivity.this, LoginActivity.class));
            SplashActivity.this.finish();
        }
    }
}
