package com.jtmcompany.smartadvertisingboard.VideoEdit.Adapter;

import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jtmcompany.smartadvertisingboard.R;

import java.util.ArrayList;
import java.util.List;

public class Sticker_RecyclerAdapter extends RecyclerView.Adapter<Sticker_RecyclerAdapter.MyViewHolder> {
    List<Drawable> drawables=new ArrayList<>();
    stickerClickListener mListener;

    public interface stickerClickListener{
        void OnStickerClicked(int position,Drawable drawable);
    }
    public void setOnStickerListener(stickerClickListener listener){
        mListener=listener;

    }

    public Sticker_RecyclerAdapter(List<Drawable> drawables) {
        this.drawables = drawables;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater= LayoutInflater.from(parent.getContext());
        View view=inflater.inflate(R.layout.sticker_item,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Drawable drawable=drawables.get(position);
        holder.sticker_imageView.setImageDrawable(drawable);

    }

    @Override
    public int getItemCount() {
        return drawables.size();
    }




    public class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView sticker_imageView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            sticker_imageView=itemView.findViewById(R.id.item);
            sticker_imageView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    mListener.OnStickerClicked(getAdapterPosition(),drawables.get(getAdapterPosition()));
                    Log.d("tak3","id: "+ view.getId());
                    Log.d("tak3","position: "+ getAdapterPosition());

                }
            });
        }
    }

}



