package com.jtmcompany.smartadvertisingboard;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;

public class CustomDialog extends Dialog {
    private Button mPositiveButton;
    private Button mNegativeButton;
    private View.OnClickListener mPositiveListener;
    private View.OnClickListener mNegativeListener;

    private EditText titleET;
    private EditText timeET;





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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //다이얼로그 밖의 화면은 흐리게 만들어줌
        WindowManager.LayoutParams layoutParams=new WindowManager.LayoutParams();
        layoutParams.flags=WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        layoutParams.dimAmount=0.8f;
        getWindow().setAttributes(layoutParams);

        setContentView(R.layout.custom_dialog);
        titleET=findViewById(R.id.video_title);
        timeET=findViewById(R.id.video_time);



        //세팅
        mPositiveButton=findViewById(R.id.pbutton);
        mNegativeButton=findViewById(R.id.nbutton);

        //클릭 리스너 세팅
        mPositiveButton.setOnClickListener(mPositiveListener);
        mNegativeButton.setOnClickListener(mNegativeListener);

    }
}
