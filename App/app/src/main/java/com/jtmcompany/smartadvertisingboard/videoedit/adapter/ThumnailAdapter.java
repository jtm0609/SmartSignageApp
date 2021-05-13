package com.jtmcompany.smartadvertisingboard.videoedit.adapter;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jtmcompany.smartadvertisingboard.R;

import java.util.ArrayList;
import java.util.List;

public class ThumnailAdapter extends RecyclerView.Adapter<ThumnailAdapter.Viewholder> {
    private List<Bitmap> mbitmaps=new ArrayList<>();
    private int mItemWidth;

    public ThumnailAdapter(List<Bitmap> bitmaps, int itemwidth) {
        this.mbitmaps = bitmaps;
        mItemWidth=itemwidth;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View view=inflater.inflate(R.layout.trim_thumnail_item,null);
        ViewGroup.LayoutParams pp= new ViewGroup.LayoutParams(mItemWidth,ViewGroup.LayoutParams.MATCH_PARENT);
        view.setLayoutParams(pp);
        return new Viewholder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
        Bitmap bitmap=mbitmaps.get(position);
        holder.imageView.setImageBitmap(bitmap);
    }

    @Override
    public int getItemCount() {
        return mbitmaps.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        ImageView imageView;
        public Viewholder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.thumnail_img);
        }
    }
}
