package com.jtmcompany.smartadvertisingboard.VideoEdit.Adapter;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jtmcompany.smartadvertisingboard.R;

import java.util.ArrayList;
import java.util.List;


public class VideoEdit_TextBottomsheet_RecyclerAdapter  extends RecyclerView.Adapter<VideoEdit_TextBottomsheet_RecyclerAdapter.ViewHolder>{



    private List<Drawable> mImages=new ArrayList<>();
    public interface textClickListener{
        public void textOnClick(int position);
    }

    textClickListener mListener;

    public void setOnTextListener(textClickListener listener){
        mListener=listener;
    }


    public VideoEdit_TextBottomsheet_RecyclerAdapter(List<Drawable> images) {
        mImages = images;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater= LayoutInflater.from(parent.getContext());
        View view=layoutInflater.inflate(R.layout.sticker_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Drawable drawable=mImages.get(position);
        holder.imageView.setImageDrawable(drawable);

    }

    @Override
    public int getItemCount() {
        return mImages.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.item);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.textOnClick(getAdapterPosition());
                }
            });
        }
    }
}
