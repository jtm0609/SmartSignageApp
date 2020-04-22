package com.jtmcompany.smartadvertisingboard;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity_Email extends AppCompatActivity implements View.OnClickListener, TextWatcher {
TextView signup_bt;
EditText id_edit;
EditText pw_edit;
Button email_login_bt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login__email);
        id_edit=findViewById(R.id.login_id);
        pw_edit=findViewById(R.id.login_pw);
        id_edit.addTextChangedListener(this);
        pw_edit.addTextChangedListener(this);
        email_login_bt=findViewById(R.id.email_login_bt);
        email_login_bt.setOnClickListener(this);

        signup_bt=findViewById(R.id.email_signup);
        signup_bt.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
    if(view==signup_bt)
    {Intent intent=new Intent(this,SignupActivity.class);
        startActivity(intent);
        finish();
    }else if(view==email_login_bt){
        Http_Request_MyServerDB http_request_myServerDB=new Http_Request_MyServerDB(null,id_edit.getText().toString(),pw_edit.getText().toString());
        http_request_myServerDB.execute();
    }

    }



    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    if(!id_edit.getText().toString().equals("")&&
    !pw_edit.getText().toString().equals("")){
        email_login_bt.setEnabled(true);
    }else{
        email_login_bt.setEnabled(false);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
    @Override
    public void afterTextChanged(Editable editable) { }
}
