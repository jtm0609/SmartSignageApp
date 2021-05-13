package com.jtmcompany.smartadvertisingboard.photoedit.adapter;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.jtmcompany.smartadvertisingboard.R;

import java.util.ArrayList;
import java.util.List;

public class GifRecyclerAdapter extends RecyclerView.Adapter<GifRecyclerAdapter.Viewholder> {
private List<Uri> list= new ArrayList<>();
private Context context;
private MotionStickerListener mMotionStickerListener=null;
    public interface MotionStickerListener{
        void onClick(View v, int position, Uri gif_uri);
    }
    public void setMotionStickerListener(MotionStickerListener motionStickerListener){
        mMotionStickerListener=motionStickerListener;
    }

    public GifRecyclerAdapter(List<Uri> list , Context context) {
        this.list = list;
        this.context=context;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(parent.getContext());
        View view=layoutInflater.inflate(R.layout.photo_edit_recycler_item,null);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, final int position) {
        Log.d("tas",""+list.get(position));
        final ImageView imageView=holder.imageView;
        Glide.with(context).load(list.get(position)).into(imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMotionStickerListener.onClick(view,position,list.get(position));

            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        ImageView imageView;
        public Viewholder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.photo_edit_img);
        }
    }
}
