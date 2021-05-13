package com.jtmcompany.smartadvertisingboard.login.Google;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.UserInfo;
import com.jtmcompany.smartadvertisingboard.login.http.Http_Request_MyServerDB;
import com.jtmcompany.smartadvertisingboard.login.Network_Status_Check;
import com.jtmcompany.smartadvertisingboard.ui.SelectEditActivity;
import com.jtmcompany.smartadvertisingboard.R;

public class Google_FirebaseAuth {
    private Context mContext;
    private GoogleSignInClient mGoogleSignInClient;
    private int GOOGLE_RC_SIGN_IN=9001;
    private FirebaseAuth mAuth;
    private FirebaseUser user;

    //구글 로그인을 구성한다.
    public Google_FirebaseAuth(Context mContext) {
        this.mContext = mContext;
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(mContext.getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient= GoogleSignIn.getClient(mContext,gso);
        mAuth=FirebaseAuth.getInstance();
    }

    //구글 로그인 신청
    public void signIn(){
        Intent signIntent=mGoogleSignInClient.getSignInIntent();
        ((Activity)mContext).startActivityForResult(signIntent,GOOGLE_RC_SIGN_IN);
    }

    //파이어베이스 인증
    //구글 로그인에 성공했으면 전달된 GoogleSignInAccount 객체에서 idToken으로 Credential을 만들어 파이어베이스에 인증요청한다.
    public void firebaseAuthWithGoogle(GoogleSignInAccount acct){
        Log.d("tak","firebaseAuthWithGoogle: "+acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(),null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener((Activity)mContext, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            //회원가입 성공
                            Log.d("tak4","로그인성공");
                            Toast.makeText(mContext, "로그인성공", Toast.LENGTH_SHORT).show();
                            Log.d("tak4","OnComplete: ");
                            Intent intent = new Intent(mContext, SelectEditActivity.class);
                            (mContext).startActivity(intent);
                            ((Activity) mContext).finish();

                            //유저정보 가져오기
                            user=FirebaseAuth.getInstance().getCurrentUser();
                            String name="";
                            String email="";
                            if(user!=null) {
                                for (UserInfo profile : user.getProviderData()) {
                                    name = profile.getDisplayName();
                                    email = profile.getEmail();
                                    String uid = profile.getUid();
                                    Log.d("tak4", "name: " + name);
                                    Log.d("tak4", "email: " + email);
                                    Log.d("tak4", "uid: " + uid);

                                }
                                SharedPreferences sharedPreferences=mContext.getSharedPreferences("loginUser",mContext.MODE_PRIVATE);
                                SharedPreferences.Editor editor=sharedPreferences.edit();
                                editor.putString("name",name+"(구글 로그인)");
                                editor.commit();

                                Http_Request_MyServerDB http_request_myServerDB=new Http_Request_MyServerDB(name,email,null);
                                http_request_myServerDB.Request_Signup();

                            }

                        } else {
                            //실패
                            Log.d("tak", "로그인실패");
                            if (Network_Status_Check.getConnectivityStatus(mContext) == 3) {
                                Toast.makeText(mContext, "네트워크 연결을 확인해주세요!", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(mContext, "로그인 실패!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }




}
