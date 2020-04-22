package com.jtmcompany.smartadvertisingboard;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class Http_Request_MyServerDB extends AsyncTask<Void,Void,Void> {
    private String mName;
    private String mEmail;
    private String mpw;
    private String SERVER_URL;

    public Http_Request_MyServerDB(String name, String email, String pw) {
        //URL설정
        if(pw==null)
            SERVER_URL="http://52.79.242.45/index.php";
        //로그인을 할경우
        else if(pw!=null){
            SERVER_URL="http://52.79.242.45/login.php";
        }
        mName=name;
        mEmail=email;
        mpw=pw;
    }



    @Override
    protected Void doInBackground(Void... param) {
        try{
            Log.d("tak5","1");
            URL url=new URL(SERVER_URL);
            String posData;
            if(mpw==null) {
                posData = "name=" + URLEncoder.encode(mName, "UTF-8") + "&" + "email=" + mEmail;
            }else{
                posData="email="+mEmail+"&"+"pw="+mpw;

            }
            Log.d("tak",posData);
            HttpURLConnection conn=(HttpURLConnection)url.openConnection();
            conn.setRequestProperty("Content-Type","application/x-www-form-urlencoded;charset=UTF-8");
            conn.setRequestMethod("POST");
            conn.setConnectTimeout(5000);
            conn.setDoInput(true);

            conn.setDoOutput(true);
            OutputStream outputStream=conn.getOutputStream();
            outputStream.write(posData.getBytes("utf-8"));
            outputStream.flush();
            outputStream.close();

            Log.d("tak5","STATUS CODE:" + conn.getResponseCode());

            InputStream in = conn.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in, "utf-8"));
            StringBuilder stringBuffer = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuffer.append(line);
            }

            Log.d("tak5","response:" + stringBuffer.toString());

            conn.disconnect();

        }catch (Exception e){
            Log.d("tak5","request was failed");
            e.printStackTrace();
        }
        return null;
    }
}



