package com.jtmcompany.smartadvertisingboard.login.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.jtmcompany.smartadvertisingboard.R;
import com.jtmcompany.smartadvertisingboard.login.http.Http_Request_MyServerDB;
import com.jtmcompany.smartadvertisingboard.ui.SelectEditActivity;

public class EmailLoginActivity extends AppCompatActivity implements View.OnClickListener, TextWatcher {
private TextView signup_bt;
private EditText idEt, pwEt;
private Button emailLoginBt;
private Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login__email);
        initView();

        idEt.addTextChangedListener(this);
        pwEt.addTextChangedListener(this);
        emailLoginBt.setOnClickListener(this);
        signup_bt.setOnClickListener(this);

        handler=new MyHandler();
    }

    void initView(){
        idEt=findViewById(R.id.login_id);
        pwEt=findViewById(R.id.login_pw);
        emailLoginBt=findViewById(R.id.email_login_bt);
        signup_bt=findViewById(R.id.email_signup);
    }


    @Override
    public void onClick(View view) {
    if(view==signup_bt)
    {Intent intent=new Intent(this, SignupActivity.class);
        startActivity(intent);
        finish();
    }else if(view==emailLoginBt){
        Http_Request_MyServerDB http_request_myServerDB=new Http_Request_MyServerDB(handler,idEt.getText().toString(),pwEt.getText().toString());
       http_request_myServerDB.Request_Login();
        }
    }



    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    if(!idEt.getText().toString().equals("")&&
    !pwEt.getText().toString().equals("")){
        emailLoginBt.setEnabled(true);
    }else{
        emailLoginBt.setEnabled(false);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
    @Override
    public void afterTextChanged(Editable editable) { }


    public class MyHandler extends Handler{
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            super.handleMessage(msg);
            if(msg.what==1){
                String response_value=String.valueOf(msg.obj);
                Log.d("tak",response_value);
                if(response_value.equals("로그인실패")){
                    Toast.makeText(EmailLoginActivity.this, "로그인에 실패했습니다.", Toast.LENGTH_SHORT).show();
                    idEt.setText("");
                    pwEt.setText("");
                }else{
                    Toast.makeText(EmailLoginActivity.this, "로그인성공", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(EmailLoginActivity.this, SelectEditActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        }
    }
}
