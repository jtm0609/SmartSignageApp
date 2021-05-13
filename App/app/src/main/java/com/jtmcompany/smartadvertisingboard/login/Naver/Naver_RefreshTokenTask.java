package com.jtmcompany.smartadvertisingboard.login.Naver;

import android.content.Context;
import android.os.AsyncTask;

import com.nhn.android.naverlogin.OAuthLogin;

public class Naver_RefreshTokenTask extends AsyncTask<Void, Void, String> {

        private OAuthLogin mOAuthLoginModule;
        private Context mContext;

    public Naver_RefreshTokenTask(Context context) {
        mOAuthLoginModule=OAuthLogin.getInstance();
        mContext=context;
    }

    @Override
        protected String doInBackground(Void... params) {
            return mOAuthLoginModule.refreshAccessToken(mContext);

        }


    }

