package com.jtmcompany.smartadvertisingboard.VideoEdit;


import android.graphics.Bitmap;
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
import com.jtmcompany.smartadvertisingboard.StickerView.StickerImageView;
import com.jtmcompany.smartadvertisingboard.VideoEdit.Adapter.VideoEdit_StickerBottomsheet_RecyclerAdapter;

import java.util.ArrayList;
import java.util.List;


public class VideoStickerFragment extends Fragment implements VideoEdit_StickerBottomsheet_RecyclerAdapter.stickerClickListener {

    private ImageView stickerInsert_bt;
    private ImageView stickerExit_bt;
    FrameLayout videoView_container;
    VideoView mVideoview;
    Uri mVideoSelectUri;
    List<Bitmap> mlist=new ArrayList<>();

    private boolean addFlag=false;
    private StickerImageView addStickerView;

    public VideoStickerFragment(FrameLayout videoView_container, VideoView videoView, Uri uri, List list) {
        this.videoView_container = videoView_container;
        mVideoview=videoView;
        mVideoSelectUri=uri;
        mlist=list;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_video_sticker, container, false);

        List<Drawable> list = new ArrayList<>();

        addStickerView=new StickerImageView(getContext());

        RecyclerView recyclerView=view.findViewById(R.id.sticker_item_recycler);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);


        list.add(getResources().getDrawable(R.drawable.icon));
        list.add(getResources().getDrawable(R.drawable.advertise));
        VideoEdit_StickerBottomsheet_RecyclerAdapter recyclerAdapter=new VideoEdit_StickerBottomsheet_RecyclerAdapter(list);
        recyclerAdapter.setOnStickerListener(this);
        recyclerView.setAdapter(recyclerAdapter);

        stickerInsert_bt=view.findViewById(R.id.sticker_insert);
        stickerExit_bt=view.findViewById(R.id.sticker_exit);
        //취소버튼
        stickerExit_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager=getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().remove(VideoStickerFragment.this).commit();

                Log.d("tak3","취소");

                //레이아웃에 아이템을 추가했다면
                if(addFlag) {
                    videoView_container.removeView(addStickerView);
                }
            }
        });


        return view;
    }


    @Override
    public void stickerOnClick(int position, Drawable drawable) {
        addStickerView.setImageDrawable(drawable);

        //처음 아이템을 추가하였을때, 레이아웃에 아이템을추가
        if(!addFlag)
        videoView_container.addView(addStickerView);

        //추가되었다면, flag를 true로 설정
        addFlag=true;

        // V 버튼
        stickerInsert_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager=getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().remove(VideoStickerFragment.this).commit();

                SelectLocation_Fragment selectLocation_fragment =new SelectLocation_Fragment(mVideoview,mVideoSelectUri,mlist,addStickerView,videoView_container);
                fragmentManager.beginTransaction().add(R.id.con, selectLocation_fragment).commit();


            }
        });

    }

}
