package com.jtmcompany.smartadvertisingboard;

public class NDK {
    static {
        //라이브러리 로드
        System.loadLibrary("sample-ffmpeg");
    }

    //네이티브 함수 선언
    public native int scanning(String filepath);
}
