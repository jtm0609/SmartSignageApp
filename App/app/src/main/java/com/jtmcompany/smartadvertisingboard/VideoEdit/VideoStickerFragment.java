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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jtmcompany.smartadvertisingboard.R;
import com.jtmcompany.smartadvertisingboard.StickerView.StickerImageView;
import com.jtmcompany.smartadvertisingboard.VideoEdit.Adapter.VideoEdit_StickerBottomsheet_RecyclerAdapter;

import java.util.ArrayList;
import java.util.List;


public class VideoStickerFragment extends Fragment implements VideoEdit_StickerBottomsheet_RecyclerAdapter.stickerClickListener, View.OnClickListener {

    private ImageView stickerInsertBt;
    private ImageView stickerExitBt;
    private FrameLayout videoView_container;
    private VideoView mVideoview;
    private Uri mVideoSelectUri;
    private List<Bitmap> mlist=new ArrayList<>();
    private boolean addFlag=false;
    private StickerImageView addStickerView;
    List<Drawable> list = new ArrayList<>();

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

        stickerInsertBt=view.findViewById(R.id.sticker_insert);
        stickerExitBt=view.findViewById(R.id.sticker_exit);

        addStickerView=new StickerImageView(getContext());

        //리싸이클러뷰 설정
        RecyclerView recyclerView=view.findViewById(R.id.sticker_item_recycler);
        GridLayoutManager gridLayout=new GridLayoutManager(getContext(),3);
        recyclerView.setLayoutManager(gridLayout);
        addListItem();
        VideoEdit_StickerBottomsheet_RecyclerAdapter recyclerAdapter=new VideoEdit_StickerBottomsheet_RecyclerAdapter(list);
        recyclerAdapter.setOnStickerListener(this);
        recyclerView.setAdapter(recyclerAdapter);

        stickerExitBt.setOnClickListener(this); //취소 버튼

        stickerInsertBt.setOnClickListener(this); // V 버튼

        return view;
    }

    public void addListItem(){
        list.add(getResources().getDrawable(R.drawable.character1));
        list.add(getResources().getDrawable(R.drawable.character2));
        list.add(getResources().getDrawable(R.drawable.character3));
        list.add(getResources().getDrawable(R.drawable.character4));
        list.add(getResources().getDrawable(R.drawable.character5));
        list.add(getResources().getDrawable(R.drawable.character6));
    }


    @Override
    public void stickerOnClick(int position, Drawable drawable) {
        addStickerView.setImageDrawable(drawable);

        //처음 아이템을 추가하였을때, 레이아웃에 아이템을추가
        //또는 사용자가 삭제버튼을 눌러서, 스티커뷰가 보여지지않을때, 레이아웃에 추가
        if(!addFlag || !addStickerView.isShown())
        videoView_container.addView(addStickerView);

        //추가되었다면, flag를 true로 설정
        addFlag=true;



    }

    @Override
    public void onClick(View view) {
        if(view==stickerExitBt){
            Log.d("tak3","취소");
            FragmentManager fragmentManager=getActivity().getSupportFragmentManager();
            fragmentManager.beginTransaction().remove(VideoStickerFragment.this).commit();

            //레이아웃에 아이템을 추가했다면
            if(addFlag) {
                videoView_container.removeView(addStickerView);
            }
        }
        else if(view==stickerInsertBt){
            FragmentManager fragmentManager=getActivity().getSupportFragmentManager();
            fragmentManager.beginTransaction().remove(VideoStickerFragment.this).commit();

            SelectLocation_Fragment selectLocation_fragment =new SelectLocation_Fragment(mVideoview,mVideoSelectUri,mlist,addStickerView,videoView_container);
            fragmentManager.beginTransaction().add(R.id.con, selectLocation_fragment).commit();
        }
    }
}
