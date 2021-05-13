package com.jtmcompany.smartadvertisingboard.login.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.jtmcompany.smartadvertisingboard.R;
import com.jtmcompany.smartadvertisingboard.login.http.Http_Request_MyServerDB;

public class SignupActivity extends AppCompatActivity implements TextWatcher, View.OnClickListener {
private EditText nameEt,emailEt,pwEt,pw2Et;
private Button signupBt;
private Handler handler;
private Toolbar mtoolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        initView();
        handler=new MyHandler();

        //리스너설정
        nameEt.addTextChangedListener(this);
        emailEt.addTextChangedListener(this);
        pwEt.addTextChangedListener(this);
        pw2Et.addTextChangedListener(this);
        signupBt.setOnClickListener(this);

        //툴바설정
        setSupportActionBar(mtoolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }




    public void initView(){
        nameEt=findViewById(R.id.email_login_name);
        emailEt=findViewById(R.id.email_login_email);
        pwEt=findViewById(R.id.email_login_pw);
        pw2Et=findViewById(R.id.email_login_pw2);
        signupBt=findViewById(R.id.email_signup);
        mtoolbar=findViewById(R.id.toolbar);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            Intent intent=new Intent(this, EmailLoginActivity.class);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        if(!nameEt.getText().toString().equals("") &&
                !emailEt.getText().toString().equals("") &&
                !pwEt.getText().toString().equals("") &&
                !pw2Et.getText().toString().equals("")) {
            signupBt.setEnabled(true);
        }
        else
            signupBt.setEnabled(false);
    }
    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
    @Override
    public void afterTextChanged(Editable editable) { }

    @Override
    public void onClick(View v) {
        if (v == signupBt) {
            if (pwEt.getText().toString().equals(pw2Et.getText().toString())) {

                Http_Request_MyServerDB http_request_myServerDB = new Http_Request_MyServerDB(handler, nameEt.getText().toString(), emailEt.getText().toString(), pwEt.getText().toString());
                http_request_myServerDB.Request_Signup();

            } else {
                Toast.makeText(SignupActivity.this, "비밀번호와 비밀번호확인이 다릅니다.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public class MyHandler extends Handler{
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if(msg.what==1){
                String response_value=String.valueOf(msg.obj);
                Log.d("tak",response_value);
                if(response_value.equals("기존사용자")){
                    Toast.makeText(SignupActivity.this, "입력하신 이메일로 가입한 사용자가있습니다.", Toast.LENGTH_SHORT).show();
                    emailEt.setText("");
                    pwEt.setText("");
                    pw2Et.setText("");
                    nameEt.setText("");
                }else{
                    Intent intent=new Intent(SignupActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                    Toast.makeText(SignupActivity.this, "회원가입이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                }

            }
        }
    }
}
