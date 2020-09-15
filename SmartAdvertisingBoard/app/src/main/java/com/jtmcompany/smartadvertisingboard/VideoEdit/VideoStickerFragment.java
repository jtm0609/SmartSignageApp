package com.jtmcompany.smartadvertisingboard.VideoEdit;


import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.VideoView;

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

        RecyclerView recyclerView=view.findViewById(R.id.sticker_item_recycler);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        stickerInsert_bt=view.findViewById(R.id.sticker_insert);
        stickerExit_bt=view.findViewById(R.id.sticker_exit);
        stickerExit_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager=getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().remove(VideoStickerFragment.this).commit();
                Boolean flag=false;
                Log.d("tak3","취소");
                for(int i=0; i<VideoEditAtivity.insertView.size(); i++) {
                    if (VideoEditAtivity.insertView.get(i).getmStickerView() == curStickerView) {
                        flag = true;
                        break;
                    }
                }
                if(flag!=true)
                    videoView_container.removeView(curStickerView);
            }
        });

        list.add(getResources().getDrawable(R.drawable.icon));
        list.add(getResources().getDrawable(R.drawable.advertise));
        VideoEdit_StickerBottomsheet_RecyclerAdapter recyclerAdapter=new VideoEdit_StickerBottomsheet_RecyclerAdapter(list);
        recyclerAdapter.setOnStickerListener(this);
        recyclerView.setAdapter(recyclerAdapter);


        return view;
    }

    StickerImageView prieveStickerView;
    StickerImageView curStickerView;
    @Override
    public void stickerOnClick(int position, Drawable drawable) {
        final StickerImageView stickerImageView=new StickerImageView(getContext());
        stickerImageView.setImageDrawable(drawable);


        Boolean flag=false;
        for(int i=0; i<VideoEditAtivity.insertView.size(); i++) {
            if (VideoEditAtivity.insertView.get(i).getmStickerView() == curStickerView) {
                flag = true;
                break;
            }
        }
        if(prieveStickerView!=null && flag!=true)
            videoView_container.removeView(prieveStickerView);

        curStickerView=stickerImageView;

        videoView_container.addView(curStickerView);

        prieveStickerView=curStickerView;

        // V 버튼
        stickerInsert_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager=getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().remove(VideoStickerFragment.this).commit();

                int duration=VideoEditAtivity.trim_end-VideoEditAtivity.trim_start;
                SelectLocation_Fragment selectLocation_fragment =new SelectLocation_Fragment(mVideoview,mVideoSelectUri,mlist,curStickerView,videoView_container);
                fragmentManager.beginTransaction().add(R.id.con, selectLocation_fragment).commit();

                //InsertStickerView_Model insert_model=new InsertStickerView_Model(stickerImageView);
                //VideoEditAtivity.insertView.add(insert_model);
                //VideoEditAtivity.insertView.add(stickerImageView);

            }
        });

    }

}
