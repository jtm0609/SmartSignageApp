package com.jtmcompany.smartadvertisingboard.Naver;

import android.app.Application;


public class Naver_AuthInfo extends Application {
    private static final String OAUTH_CLIENT_ID="37a_XBCHzkCpB4PUnIlc";
    private static final String OAUTH_CLIENT_SECRET="UItxa7mb9o";
    private static final String OAUTH_CLIENT_NAME="모두의 광고";

    public static String getOauthClientId() {
        return OAUTH_CLIENT_ID;
    }

    public static String getOauthClientSecret() {
        return OAUTH_CLIENT_SECRET;
    }

    public static String getOauthClientName() {
        return OAUTH_CLIENT_NAME;
    }
}
