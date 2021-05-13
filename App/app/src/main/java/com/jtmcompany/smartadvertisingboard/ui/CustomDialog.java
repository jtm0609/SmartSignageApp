package com.jtmcompany.smartadvertisingboard.ui;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

import com.jtmcompany.smartadvertisingboard.R;

public class CustomDialog extends Dialog {
    private Button mPositiveButton, mNegativeButton;
    private View.OnClickListener mPositiveListener,mNegativeListener;
    private EditText titleET,timeET;
    private LinearLayout timeLayout;


    public CustomDialog(@NonNull Context context, View.OnClickListener mPositiveListener, View.OnClickListener mNegativeListener) {
        super(context);
        this.mPositiveListener = mPositiveListener;
        this.mNegativeListener = mNegativeListener;
    }


    public EditText getTitleET() {
        return titleET;
    }
    public EditText getTimeET() {
        return timeET;
    }
    public LinearLayout getTimeLayout() {
        return timeLayout;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_dialog);

        overCatstOutside(); //다이얼로그 밖의 화면은 흐리게 만들기
        initView();

        //클릭 리스너 세팅
        mPositiveButton.setOnClickListener(mPositiveListener);
        mNegativeButton.setOnClickListener(mNegativeListener);

    }


    void overCatstOutside(){
        WindowManager.LayoutParams layoutParams=new WindowManager.LayoutParams();
        layoutParams.flags=WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        layoutParams.dimAmount=0.8f;
        getWindow().setAttributes(layoutParams);
    }

    void initView(){
        titleET=findViewById(R.id.video_title);
        timeET=findViewById(R.id.video_time);
        timeLayout=findViewById(R.id.timeSetting_layout);
        mPositiveButton=findViewById(R.id.pbutton);
        mNegativeButton=findViewById(R.id.nbutton);
    }
}
