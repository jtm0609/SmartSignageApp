package com.jtmcompany.smartadvertisingboard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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

public class SignupActivity extends AppCompatActivity implements TextWatcher {
EditText name_edit;
EditText email_edit;
EditText pw_edit;
EditText pw2_edit;
Button signup_bt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        name_edit=findViewById(R.id.email_login_name);
        email_edit=findViewById(R.id.email_login_email);
        pw_edit=findViewById(R.id.email_login_pw);
        pw2_edit=findViewById(R.id.email_login_pw2);
        signup_bt=findViewById(R.id.email_signup);

        name_edit.addTextChangedListener(this);
        email_edit.addTextChangedListener(this);
        pw_edit.addTextChangedListener(this);
        pw2_edit.addTextChangedListener(this);
        
        Toolbar mtoolbar=findViewById(R.id.toolbar);
        setSupportActionBar(mtoolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        signup_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(pw_edit.getText().toString().equals(pw2_edit.getText().toString())){

                    Http_Request_MyServerDB http_request_myServerDB=new Http_Request_MyServerDB(handler,name_edit.getText().toString(),email_edit.getText().toString(),pw_edit.getText().toString());
                    http_request_myServerDB.Request_Signup();

                }else{
                    Toast.makeText(SignupActivity.this, "비밀번호와 비밀번호확인이 다릅니다.", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    Handler handler=new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if(msg.what==1){
                String response_value=String.valueOf(msg.obj);
                Log.d("tak",response_value);
                if(response_value.equals("기존사용자")){
                    Toast.makeText(SignupActivity.this, "입력하신 이메일로 가입한 사용자가있습니다.", Toast.LENGTH_SHORT).show();
                    email_edit.setText("");
                    pw_edit.setText("");
                    pw2_edit.setText("");
                    name_edit.setText("");
                }else{
                    Intent intent=new Intent(SignupActivity.this,LoginActivity.class);
                    startActivity(intent);
                    finish();
                    Toast.makeText(SignupActivity.this, "회원가입이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                }

            }
        }
    };


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            Intent intent=new Intent(this,LoginActivity_Email.class);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        if(!name_edit.getText().toString().equals("") &&
                !email_edit.getText().toString().equals("") &&
                !pw_edit.getText().toString().equals("") &&
                !pw2_edit.getText().toString().equals("")) {
            signup_bt.setEnabled(true);
        }
        else
            signup_bt.setEnabled(false);
    }
    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
    @Override
    public void afterTextChanged(Editable editable) { }
}
