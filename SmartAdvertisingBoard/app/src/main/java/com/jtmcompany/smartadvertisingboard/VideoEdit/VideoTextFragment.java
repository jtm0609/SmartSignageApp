package com.jtmcompany.smartadvertisingboard.VideoEdit;


import android.graphics.Bitmap;
import android.graphics.Color;
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
import com.jtmcompany.smartadvertisingboard.StickerView.StickerTextView;
import com.jtmcompany.smartadvertisingboard.VideoEdit.Adapter.VideoEdit_TextBottomsheet_RecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

public class VideoTextFragment extends Fragment implements VideoEdit_TextBottomsheet_RecyclerAdapter.textClickListener {
    private ImageView textInsert_bt;
    private ImageView textexit_bt;
    private FrameLayout videoView_container;
    private VideoView mVideoview;
    private Uri mVideoSelectUri;
    private List<Bitmap> mlist=new ArrayList<>();
    public VideoTextFragment(FrameLayout videoView_container, VideoView videoView, Uri uri, List list) {
        this.videoView_container = videoView_container;
        mVideoview=videoView;
        mVideoSelectUri=uri;
        mlist=list;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
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
                Boolean flag=false;
                Log.d("tak3","취소");
                for(int i=0; i<VideoEditAtivity.insertView.size(); i++) {
                    if (VideoEditAtivity.insertView.get(i).getmStickerView() == curTextView) {
                        flag = true;
                        break;
                    }
                }
                if(flag!=true)
                    videoView_container.removeView(curTextView);
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



    StickerTextView prieveTextView;
    StickerTextView curTextView;



    @Override
    public void textOnClick(int position) {
        Log.d("tak3",""+position);

        //삭제하는뷰가 삽입해져있는뷰랑 같다면 flag=true로 놓아 삭제를방지
        Boolean flag=false;
        for(int i=0; i<VideoEditAtivity.insertView.size(); i++) {
            if (VideoEditAtivity.insertView.get(i).getmStickerView() == curTextView) {
                flag = true;
                break;
            }
        }
        if(prieveTextView!=null && flag!=true)
            videoView_container.removeView(prieveTextView);

        final StickerTextView stickerTextView=new StickerTextView(getContext());
        stickerTextView.setText("Hello");
        curTextView=stickerTextView;



        if(position==0)
            stickerTextView.tv_main.setTextColor(Color.WHITE);
        else if(position==1)
            stickerTextView.tv_main.setTextColor(Color.BLACK);
        else if(position==2)
            stickerTextView.tv_main.setTextColor(Color.BLUE);

        videoView_container.addView(stickerTextView);

        // V버튼
        textInsert_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("tak3","클릭");
                FragmentManager fragmentManager=getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().remove(VideoTextFragment.this).commit();

                //fragmentManager.beginTransaction().replace(R.id.text_container,selectLocation_fragment).commit();
               // VideoTrimFragment videoTrimFragment=new VideoTrimFragment(mVideoview);

                //VideoEditAtivity.insertView.add(curTextView);

                //int duration=VideoEditAtivity.trim_end-VideoEditAtivity.trim_start;
                SelectLocation_Fragment selectLocation_fragment =new SelectLocation_Fragment(mVideoview,mVideoSelectUri,mlist,curTextView,videoView_container);
                fragmentManager.beginTransaction().add(R.id.con, selectLocation_fragment).commit();


            }
        });



        prieveTextView=curTextView;
    }


    //외부배경 눌렀을때 이벤트발생 (API17이상)
    /*
    @Override
    public void onCancel(@NonNull DialogInterface dialog) {
        super.onCancel(dialog);
        Boolean flag=false;
        Log.d("tak3","취소");
        for(int i=0; i<VideoEditAtivity.insertView.size(); i++) {
            if (VideoEditAtivity.insertView.get(i).getmStickerView() == curTextView) {
                flag = true;
                break;
            }
        }
        if(flag!=true)
            videoView_container.removeView(curTextView);
    }

     */



}
