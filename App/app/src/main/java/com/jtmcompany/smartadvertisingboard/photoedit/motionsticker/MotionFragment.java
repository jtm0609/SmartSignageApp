package com.jtmcompany.smartadvertisingboard.photoedit.motionsticker;


import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.jtmcompany.smartadvertisingboard.photoedit.adapter.GifRecyclerAdapter;
import com.jtmcompany.smartadvertisingboard.R;
import com.jtmcompany.smartadvertisingboard.photoedit.adapter.MotionMenuRecyclerAdapter;
import com.jtmcompany.smartadvertisingboard.stickerview.StickerImageView;

import java.util.ArrayList;
import java.util.List;


public class MotionFragment extends Fragment implements GifRecyclerAdapter.MotionStickerListener {
    RecyclerView categoryRecyclerView;
    RecyclerView itemRecyclerView;

    List<Uri> Uri_list=new ArrayList<>();
    List<String> text_list=new ArrayList<>();

    FrameLayout photoEdit_frameLayout;
    GifRecyclerAdapter gifRecyclerAdapter;
    public MotionFragment(FrameLayout photoEdit_frameLayout) {
        this.photoEdit_frameLayout = photoEdit_frameLayout;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.fragment_motion, container, false);
        categoryRecyclerView=view.findViewById(R.id.motion_text_recycler);
        itemRecyclerView=view.findViewById(R.id.motion_recycler);

        text_list.add("전체");
        text_list.add("행복한");
        text_list.add("슬픈");
        text_list.add("사랑");
        text_list.add("귀여운");

        MotionMenuRecyclerAdapter textRecyclerAdapter=new MotionMenuRecyclerAdapter(text_list);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        categoryRecyclerView.setLayoutManager(linearLayoutManager);
        categoryRecyclerView.setAdapter(textRecyclerAdapter);

        //text 클릭 리싸이클러
        textRecyclerAdapter.MotionSetOnClickListener(new MotionMenuRecyclerAdapter.MotionTextSelectListener() {
            @Override
            public void onItemClick(View v, int position) {
                //Toast.makeText(getContext(), ""+position, Toast.LENGTH_SHORT).show();
                Uri_list.clear();
                if(position==0){
                   addListItem();
                }
                else if(position==1){
                    addListHappyItem();
                } else if(position==2){
                    addListSadItem();
                } else if(position==3){
                    addListLoveItem();
                } else if(position==4){
                    addListCuteItem();
                }
                    //갱신
                gifRecyclerAdapter.notifyDataSetChanged();
            }
        });

        //처음 보일때 전체가 보여야함
        addListItem();

        gifRecyclerAdapter =new GifRecyclerAdapter(Uri_list,getActivity());
        GridLayoutManager gridLayoutManager=new GridLayoutManager(getContext(),3);
        itemRecyclerView.setLayoutManager(gridLayoutManager);
        itemRecyclerView.setAdapter(gifRecyclerAdapter);

        //gif 클릭  리싸이클러
        gifRecyclerAdapter.setMotionStickerListener(this);

        return view;
    }
    void addListItem(){
        addListHappyItem();
        addListSadItem();
        addListLoveItem();
        addListCuteItem();
    }
    void addListHappyItem(){
        Uri_list.add(Uri.parse("android.resource://com.jtmcompany.smartadvertisingboard/raw/happy1"));
        Uri_list.add(Uri.parse("android.resource://com.jtmcompany.smartadvertisingboard/raw/happy2"));
        Uri_list.add(Uri.parse("android.resource://com.jtmcompany.smartadvertisingboard/raw/happy3"));
        Uri_list.add(Uri.parse("android.resource://com.jtmcompany.smartadvertisingboard/raw/happy4"));
        Uri_list.add(Uri.parse("android.resource://com.jtmcompany.smartadvertisingboard/raw/happy5"));
        Uri_list.add(Uri.parse("android.resource://com.jtmcompany.smartadvertisingboard/raw/happy6"));
    }
    void addListLoveItem(){
        Uri_list.add(Uri.parse("android.resource://com.jtmcompany.smartadvertisingboard/raw/love1"));
        Uri_list.add(Uri.parse("android.resource://com.jtmcompany.smartadvertisingboard/raw/love2"));
        Uri_list.add(Uri.parse("android.resource://com.jtmcompany.smartadvertisingboard/raw/love3"));
        Uri_list.add(Uri.parse("android.resource://com.jtmcompany.smartadvertisingboard/raw/love4"));
        Uri_list.add(Uri.parse("android.resource://com.jtmcompany.smartadvertisingboard/raw/love5"));
        Uri_list.add(Uri.parse("android.resource://com.jtmcompany.smartadvertisingboard/raw/love6"));
    }
    void addListCuteItem(){
        Uri_list.add(Uri.parse("android.resource://com.jtmcompany.smartadvertisingboard/raw/cute1"));
        Uri_list.add(Uri.parse("android.resource://com.jtmcompany.smartadvertisingboard/raw/cute2"));
        Uri_list.add(Uri.parse("android.resource://com.jtmcompany.smartadvertisingboard/raw/cute3"));
        Uri_list.add(Uri.parse("android.resource://com.jtmcompany.smartadvertisingboard/raw/cute4"));
        Uri_list.add(Uri.parse("android.resource://com.jtmcompany.smartadvertisingboard/raw/cute5"));
        Uri_list.add(Uri.parse("android.resource://com.jtmcompany.smartadvertisingboard/raw/cute6"));
    }
    void addListSadItem(){
        Uri_list.add(Uri.parse("android.resource://com.jtmcompany.smartadvertisingboard/raw/sad1"));
        Uri_list.add(Uri.parse("android.resource://com.jtmcompany.smartadvertisingboard/raw/sad2"));
        Uri_list.add(Uri.parse("android.resource://com.jtmcompany.smartadvertisingboard/raw/sad3"));
        Uri_list.add(Uri.parse("android.resource://com.jtmcompany.smartadvertisingboard/raw/sad4"));
        Uri_list.add(Uri.parse("android.resource://com.jtmcompany.smartadvertisingboard/raw/sad5"));
        Uri_list.add(Uri.parse("android.resource://com.jtmcompany.smartadvertisingboard/raw/sad6"));
    }

    @Override
    public void onClick(View v, int position, Uri gif_uri) {
        StickerImageView stickerImageView=new StickerImageView(getContext(),gif_uri);
        Glide.with(getContext()).load(gif_uri).into((ImageView) stickerImageView.getMainView());
        photoEdit_frameLayout.addView(stickerImageView);
    }
}
