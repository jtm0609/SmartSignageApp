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


public class Text_RecyclerAdapter extends RecyclerView.Adapter<Text_RecyclerAdapter.TextViewHolder> {

       public interface textClickListener{
           public void textOnClick(int position);
       }

       private List<Drawable> mImages= new ArrayList<>();
       private textClickListener mListener;

       public void setOnTextListener(textClickListener listener){
           mListener=listener;
       }


    public Text_RecyclerAdapter(List<Drawable> drawables) {
        mImages = drawables;
    }

    @NonNull
    @Override
    public TextViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater= LayoutInflater.from(parent.getContext());
        View view=layoutInflater.inflate(R.layout.sticker_item,null);
        return new TextViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TextViewHolder holder, int position) {
        Drawable drawable=mImages.get(position);
        holder.imageView.setImageDrawable(drawable);

    }

    @Override
    public int getItemCount() {
        return mImages.size();
    }



    public class TextViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        public TextViewHolder(@NonNull View itemView) {
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


