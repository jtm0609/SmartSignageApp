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

public class VideoEdit_StickerBottomsheet_RecyclerAdapter extends RecyclerView.Adapter<VideoEdit_StickerBottomsheet_RecyclerAdapter.ViewHolder> {

    List<Drawable> mImages=new ArrayList<>();
    stickerClickListener mListener;

    public interface stickerClickListener{
        void stickerOnClick(int position,Drawable drawable);
    }
    public void setOnStickerListener(stickerClickListener listener){
        mListener=listener;

    }

    public VideoEdit_StickerBottomsheet_RecyclerAdapter(List<Drawable> mImages) {
        this.mImages = mImages;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View view=inflater.inflate(R.layout.sticker_item,parent,false);
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
                    mListener.stickerOnClick(getAdapterPosition(),mImages.get(getAdapterPosition()));
                }
            });
        }
    }
}
