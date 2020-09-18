package com.jtmcompany.smartadvertisingboard.PhotoEdit.PhotoEdit_BottomFragment.Text;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.jtmcompany.smartadvertisingboard.R;
import com.jtmcompany.smartadvertisingboard.StickerView.StickerTextView;

import java.util.ArrayList;
import java.util.List;


public class TextFragment extends Fragment implements TextRecyclerAdapter.TextSelectListener {
  FrameLayout photoEdit_frame;
  RecyclerView recyclerView;
  List<Drawable> list=new ArrayList<>();

    public TextFragment(FrameLayout photoEdit_frame) {
        this.photoEdit_frame = photoEdit_frame;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_text, container, false);
        recyclerView=view.findViewById(R.id.text_recycler);
        GridLayoutManager gridLayoutManager=new GridLayoutManager(getContext(),3);
        list.add(getResources().getDrawable(R.drawable.text1));
        list.add(getResources().getDrawable(R.drawable.text2));
        list.add(getResources().getDrawable(R.drawable.text3));
        TextRecyclerAdapter text_recyclerAdapter=new TextRecyclerAdapter(list);
        text_recyclerAdapter.setOnTextSelectListener(this);
        recyclerView.setAdapter(text_recyclerAdapter);
        recyclerView.setLayoutManager(gridLayoutManager);
        return view;
    }


    @Override
    public void Onclick(int position) {
        StickerTextView stickerTextView=new StickerTextView(getContext());
        stickerTextView.setText("hello");
        if(position==0){
            stickerTextView.tv_main.setTextColor(Color.WHITE);
        }else if(position==1){
            stickerTextView.tv_main.setTextColor(Color.BLACK);
        }else if(position==2){
            stickerTextView.tv_main.setTextColor(Color.BLUE);


        }
        photoEdit_frame.addView(stickerTextView);
    }
}
