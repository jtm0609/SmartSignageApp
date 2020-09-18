package com.jtmcompany.smartadvertisingboard.PhotoEdit.PhotoEdit_BottomFragment.Effect;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.jtmcompany.smartadvertisingboard.PhotoEdit.PhotoEdit_BottomFragment.GifRecyclerAdapter;
import com.jtmcompany.smartadvertisingboard.R;
import com.jtmcompany.smartadvertisingboard.StickerView.StickerImageView;

import java.util.ArrayList;
import java.util.List;


public class EffectFragment extends Fragment implements GifRecyclerAdapter.MotionStickerListener {
    FrameLayout photoEdit_frameLayout;
    RecyclerView recyclerView;
    GifRecyclerAdapter gifRecyclerAdapter;

    public EffectFragment(FrameLayout photoEdit_frameLayout) {
        this.photoEdit_frameLayout = photoEdit_frameLayout;
    }

    List<Uri> list=new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_effect, container, false);

        recyclerView=view.findViewById(R.id.effect_recycler);

        list.add(Uri.parse("android.resource://com.jtmcompany.smartadvertisingboard/raw/effect1"));
        list.add(Uri.parse("android.resource://com.jtmcompany.smartadvertisingboard/raw/effect2"));
        list.add(Uri.parse("android.resource://com.jtmcompany.smartadvertisingboard/raw/effect3"));
        list.add(Uri.parse("android.resource://com.jtmcompany.smartadvertisingboard/raw/effect4"));
        list.add(Uri.parse("android.resource://com.jtmcompany.smartadvertisingboard/raw/effect5"));
        list.add(Uri.parse("android.resource://com.jtmcompany.smartadvertisingboard/raw/effect6"));
        list.add(Uri.parse("android.resource://com.jtmcompany.smartadvertisingboard/raw/effect7"));
        list.add(Uri.parse("android.resource://com.jtmcompany.smartadvertisingboard/raw/effect8"));
        list.add(Uri.parse("android.resource://com.jtmcompany.smartadvertisingboard/raw/effect9"));


        gifRecyclerAdapter=new GifRecyclerAdapter(list,getContext());
        recyclerView.setAdapter(gifRecyclerAdapter);
        GridLayoutManager gridLayoutManager=new GridLayoutManager(getContext(),3);
        recyclerView.setLayoutManager(gridLayoutManager);
        gifRecyclerAdapter.setMotionStickerListener(this);

        return view;
    }


    @Override
    public void onClick(View v, int position, Uri gif_uri) {
        StickerImageView stickerImageView=new StickerImageView(getContext(),gif_uri);
        Glide.with(getContext()).load(gif_uri).into((ImageView) stickerImageView.getMainView());
        photoEdit_frameLayout.addView(stickerImageView);
    }
}