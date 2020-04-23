package com.jtmcompany.smartadvertisingboard;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Network_Status_Check {
    public static final int TYPE_WIFI=1;
    public static final int TYPE_MOBILE=2;
    public static final int TYPE_NOT_CONNECTED=3;

    public static int getConnectivityStatus(Context context){
        ConnectivityManager manager=(ConnectivityManager)context.getSystemService(context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo= manager.getActiveNetworkInfo();
        if(networkInfo !=null){
            int type=networkInfo.getType();
            if(type==ConnectivityManager.TYPE_MOBILE){//쓰리지나 LTE
                return TYPE_MOBILE;
            }else if(type==ConnectivityManager.TYPE_WIFI){
                return TYPE_WIFI;
            }
        }
        return TYPE_NOT_CONNECTED;
    }
}
