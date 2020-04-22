package com.jtmcompany.smartadvertisingboard.Naver;

import android.content.Context;
import android.util.Log;

import com.jtmcompany.smartadvertisingboard.Http_Request_MyServerDB;
import com.nhn.android.naverlogin.OAuthLogin;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class Http_Request_NaverServer {
    Context mcontext;
    OAuthLogin mOAuthLoginModule;

    public Http_Request_NaverServer(Context mcontext) {
        this.mcontext = mcontext;
        mOAuthLoginModule=OAuthLogin.getInstance();
    }

    public void naverMember_Info(){

        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d("tak","NaverMemberProfile");
                String token=mOAuthLoginModule.getAccessToken(mcontext);
                String header="Bearer "+token;

                String apiURL= "https://openapi.naver.com/v1/nid/me";

                Map<String,String> requestHeaders=new HashMap<>();
                requestHeaders.put("Authorization",header);
                String responseBody=get(apiURL,requestHeaders);
                Log.d("tak","responseBody: "+responseBody);
                try {
                    JSONObject root=new JSONObject(responseBody);
                    JSONObject response=root.getJSONObject("response");
                    String name=response.getString("name");
                    Log.d("tak5","name: "+name);

                    Http_Request_MyServerDB myServerDB=new Http_Request_MyServerDB(name,"email",null);
                    myServerDB.execute();

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
            Log.d("tak","con: "+con);
            con.setRequestMethod("GET");
            for(Map.Entry<String,String> header: requestHeaders.entrySet()){
                con.setRequestProperty(header.getKey(),header.getValue());
            }
            int responseCode=con.getResponseCode();
            Log.d("tak","responseCode: "+responseCode);
            if(responseCode==HttpURLConnection.HTTP_OK){
                Log.d("tak","HTTP_OK");
                BufferedReader in= new BufferedReader(new InputStreamReader(con.getInputStream()));
                StringBuffer responseBody=new StringBuffer();
                String inputLine;
                while ((inputLine=in.readLine())!=null){
                    responseBody.append(inputLine);
                }
                return responseBody.toString();

            }else{
                Log.d("tak","HTTP_NO");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            con.disconnect();
        }
        return null;
    }


}
