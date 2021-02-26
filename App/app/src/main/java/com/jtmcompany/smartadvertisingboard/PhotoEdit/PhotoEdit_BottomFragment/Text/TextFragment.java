package com.jtmcompany.smartadvertisingboard.PhotoEdit.PhotoEdit_BottomFragment.Text;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jtmcompany.smartadvertisingboard.R;
import com.jtmcompany.smartadvertisingboard.StickerView.StickerTextView;

import java.util.ArrayList;
import java.util.List;


public class TextFragment extends Fragment implements TextRecyclerAdapter.TextSelectListener {
  private FrameLayout photoEdit_frame;
  private RecyclerView recyclerView;
  private List<Drawable> list=new ArrayList<>();

    public TextFragment(FrameLayout photoEdit_frame) {
        this.photoEdit_frame = photoEdit_frame;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_text, container, false);

        recyclerView=view.findViewById(R.id.text_recycler);

        listAddItem();

        //리싸이클러뷰 설정(그리드)
        GridLayoutManager gridLayoutManager=new GridLayoutManager(getContext(),2);
        TextRecyclerAdapter textRecyclerAdapter=new TextRecyclerAdapter(list);
        textRecyclerAdapter.setOnTextSelectListener(this);
        recyclerView.setAdapter(textRecyclerAdapter);
        recyclerView.setLayoutManager(gridLayoutManager);
        return view;
    }

    public void listAddItem(){
        list.add(getResources().getDrawable(R.drawable.text1));
        list.add(getResources().getDrawable(R.drawable.text2));
        list.add(getResources().getDrawable(R.drawable.text3));
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
