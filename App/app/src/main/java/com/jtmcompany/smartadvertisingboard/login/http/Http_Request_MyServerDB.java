package com.jtmcompany.smartadvertisingboard.login.http;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class Http_Request_MyServerDB  {
    private String mName,mEmail,mpw;
    private String SERVER_URL;
    private Handler mhandler;

    //SNS 회원가입할때 요청됨
    public Http_Request_MyServerDB(String name, String email, String pw) {
        mName=name;
        mEmail=email;
        mpw=pw;
    }
    //로그인할때 요청됨
    public Http_Request_MyServerDB(Handler handler,String email, String pw) {
        mhandler=handler;
        mEmail=email;
        mpw=pw;
    }
    //회원가입할때 요청됨
    public Http_Request_MyServerDB(Handler handler,String name,String email, String pw) {
        mhandler=handler;
        mName=name;
        mEmail=email;
        mpw=pw;
    }

    public String Request_Signup() {
                try{
                    SERVER_URL="http://15.164.163.214/index.php";
                    Log.d("tak5","1");
                    URL url=new URL(SERVER_URL);
                    String posData = "name=" + URLEncoder.encode(mName, "UTF-8") + "&" + "email=" + mEmail+ "&" + "pw="+mpw;
                    Log.d("tak",posData);
                    Request_MyServer(url,posData);

                }catch (Exception e){
                    Log.d("tak5","request was failed");
                    e.printStackTrace();
                }

        return null;
    }

    public String Request_Login(){
        SERVER_URL="http://15.164.163.214/login.php";
        Log.d("tak5","1");
        try {
            URL url=new URL(SERVER_URL);
            String posData = "email=" + mEmail+ "&" + "pw="+mpw;
            Request_MyServer(url, posData);

        } catch (MalformedURLException e) { e.printStackTrace(); }
       return null;
    }

    private String Request_MyServer(final URL url, final String posData){

            Thread thread=new Thread() {
                @Override
                public void run() {
                    try {
                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
                        conn.setRequestMethod("POST");
                        conn.setConnectTimeout(5000);
                        conn.setDoInput(true);
                        conn.setDoOutput(true);
                        OutputStream outputStream = conn.getOutputStream();
                        outputStream.write(posData.getBytes("utf-8"));
                        outputStream.flush();
                        outputStream.close();
                        Log.d("tak5", "STATUS CODE:" + conn.getResponseCode());
                        InputStream in = conn.getInputStream();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(in, "utf-8"));
                        String line;
                        StringBuilder stringBuffer = new StringBuilder();
                        while ((line = reader.readLine()) != null) {
                            stringBuffer.append(line);
                        }
                        Log.d("tak5", "response:" + stringBuffer.toString());
                        conn.disconnect();
                        Message message=new Message();
                        message.what=1;
                        message.obj=stringBuffer.toString();
                        //로그인or 회원가입을 요청한 핸들러에게 보냄
                        if(mhandler!=null) {
                            mhandler.sendMessage(message);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };
            thread.start();

        return null;
    }


}



