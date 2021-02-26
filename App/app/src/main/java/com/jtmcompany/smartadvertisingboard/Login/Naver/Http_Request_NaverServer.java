package com.jtmcompany.smartadvertisingboard.Login.Naver;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.jtmcompany.smartadvertisingboard.Login.Http_Request_MyServerDB;
import com.jtmcompany.smartadvertisingboard.MainActivity;
import com.nhn.android.naverlogin.OAuthLogin;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class Http_Request_NaverServer {
    private Context mcontext;
    private OAuthLogin mOAuthLoginModule;

    public Http_Request_NaverServer(Context mcontext) {
        this.mcontext = mcontext;
        mOAuthLoginModule=OAuthLogin.getInstance();
    }

    public void naverMember_Info(){

        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d("login_naver","NaverMemberProfile");
                String token=mOAuthLoginModule.getAccessToken(mcontext);
                String header="Bearer "+token;

                String apiURL= "https://openapi.naver.com/v1/nid/me";

                Map<String,String> requestHeaders=new HashMap<>();
                requestHeaders.put("Authorization",header);
                String responseBody=get(apiURL,requestHeaders);
                Log.d("login_naver","responseBody: "+responseBody);
                try {
                    JSONObject root=new JSONObject(responseBody);
                    JSONObject response=root.getJSONObject("response");
                    String name=response.getString("name");
                    Log.d("login_naver","name: "+name);

                    //로그인 API로부터 받은 JSON의 이름과 이미지 URI을 쉐어드프리퍼런스에 STRING값으로 저장함
                    SharedPreferences sharedPreferences=mcontext.getSharedPreferences("loginUser",mcontext.MODE_PRIVATE);
                    SharedPreferences.Editor editor=sharedPreferences.edit();
                    editor.putString("name",name+"(네이버 로그인)");
                    editor.commit();


                    Http_Request_MyServerDB myServerDB=new Http_Request_MyServerDB(name,"email",null);
                    myServerDB.Request_Signup();

                    ((Activity)mcontext).finish();
                    Intent intent=new Intent(mcontext, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mcontext.startActivity(intent);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }


    private String get(String apiUrl,Map<String,String>requestHeaders){

        HttpURLConnection con=null;
        try {
            URL url=new URL(apiUrl);
            con= (HttpURLConnection) url.openConnection();
            Log.d("login_naver","con: "+con);
            con.setRequestMethod("GET");
            for(Map.Entry<String,String> header: requestHeaders.entrySet()){
                con.setRequestProperty(header.getKey(),header.getValue());
            }
            int responseCode=con.getResponseCode();
            Log.d("login_naver","responseCode: "+responseCode);
            if(responseCode==HttpURLConnection.HTTP_OK){
                BufferedReader in= new BufferedReader(new InputStreamReader(con.getInputStream()));
                StringBuffer responseBody=new StringBuffer();
                String inputLine;
                while ((inputLine=in.readLine())!=null){
                    responseBody.append(inputLine);
                }
                return responseBody.toString();

            }else{
                Log.d("login_naver","HTTP_NO");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            con.disconnect();
        }
        return null;
    }


}
