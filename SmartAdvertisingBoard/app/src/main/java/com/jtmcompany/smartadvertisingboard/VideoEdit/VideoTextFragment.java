package com.jtmcompany.smartadvertisingboard.VideoEdit;


import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.VideoView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jtmcompany.smartadvertisingboard.R;
import com.jtmcompany.smartadvertisingboard.StickerView.StickerTextView;
import com.jtmcompany.smartadvertisingboard.VideoEdit.Adapter.VideoEdit_TextBottomsheet_RecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

public class VideoTextFragment extends Fragment implements VideoEdit_TextBottomsheet_RecyclerAdapter.textClickListener {
    private ImageView textInsert_bt,textexit_bt;
    private FrameLayout videoView_container;
    private VideoView mVideoview;
    private Uri mVideoSelectUri;
    private List<Bitmap> mlist=new ArrayList<>();

    private StickerTextView addStickerView;
    private boolean addFlag=false;
    public VideoTextFragment(FrameLayout videoView_container, VideoView videoView, Uri uri, List list) {
        this.videoView_container = videoView_container;
        mVideoview=videoView;
        mVideoSelectUri=uri;
        mlist=list;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        addStickerView=new StickerTextView(getContext());

        View view= inflater.inflate(R.layout.fragment_video_text, container, false);
        List<Drawable> list=new ArrayList<>();
        RecyclerView recyclerView=view.findViewById(R.id.text_item_recycler);
        textInsert_bt=view.findViewById(R.id.text_check);
        textexit_bt=view.findViewById(R.id.text_exit);

        //취소
        textexit_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager=getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().remove(VideoTextFragment.this).commit();

                //레이아웃에 아이템을 추가했다면
                if(addFlag)
                videoView_container.removeView(addStickerView);
            }


        });
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        list.add(getResources().getDrawable(R.drawable.text1));
        list.add(getResources().getDrawable(R.drawable.text2));
        list.add(getResources().getDrawable(R.drawable.text3));
        VideoEdit_TextBottomsheet_RecyclerAdapter recyclerAdapter=new VideoEdit_TextBottomsheet_RecyclerAdapter(list);
        recyclerAdapter.setOnTextListener(this);
        recyclerView.setAdapter(recyclerAdapter);
        return view;
    }


    @Override
    public void textOnClick(int position) {
        addStickerView.setText("Hello");

        if(position==0)
            addStickerView.tv_main.setTextColor(Color.WHITE);
        else if(position==1)
            addStickerView.tv_main.setTextColor(Color.BLACK);
        else if(position==2)
            addStickerView.tv_main.setTextColor(Color.BLUE);

        //처음 아이템을 추가하였을때, 레이아웃에 아이템을추가
        //또는 사용자가 삭제버튼을 눌러서, 스티커뷰가 보여지지않을때, 레이아웃에 추가
        if(!addFlag || !addStickerView.isShown())
        videoView_container.addView(addStickerView);

        addFlag=true;

        // V버튼
        textInsert_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("tak3","클릭");
                FragmentManager fragmentManager=getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().remove(VideoTextFragment.this).commit();

                SelectLocation_Fragment selectLocation_fragment =new SelectLocation_Fragment(mVideoview,mVideoSelectUri,mlist,addStickerView,videoView_container);
                fragmentManager.beginTransaction().add(R.id.con, selectLocation_fragment).commit();
            }
        });

    }
}
