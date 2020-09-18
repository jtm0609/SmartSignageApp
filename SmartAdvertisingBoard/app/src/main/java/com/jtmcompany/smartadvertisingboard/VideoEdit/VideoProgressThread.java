package com.jtmcompany.smartadvertisingboard.VideoEdit;

import android.os.AsyncTask;
import android.os.SystemClock;
import android.widget.SeekBar;

public class VideoProgressThread extends AsyncTask<Object,Integer,Void> {
    SeekBar mprogress;
    int mduration;
    public int mstart;


    public VideoProgressThread(SeekBar progress,int start ,int duration) {
        mprogress=progress;
        mduration=duration;
        mstart=start;

    }

    @Override
    protected Void doInBackground(Object[] objects) {
        for(;mstart<=mduration; mstart++){

            SystemClock.sleep(1000);
            publishProgress(mduration,mstart);

        }



        return null;
    }

    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        mprogress.setMax(values[0]);
        mprogress.setProgress(values[1]);





    }

}
