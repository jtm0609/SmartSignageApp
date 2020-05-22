package com.jtmcompany.smartadvertisingboard.VideoEdit;

import android.graphics.Bitmap;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jtmcompany.smartadvertisingboard.R;
import com.jtmcompany.smartadvertisingboard.VideoEdit.Adapter.VideoEdit_RecyclerAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

public class trimRecyclerAdapter extends RecyclerView.Adapter<trimRecyclerAdapter.Viewholder> {
    List<Bitmap> mbitmaps=new ArrayList<>();
    int mItemWidth;

    public trimRecyclerAdapter(List<Bitmap> bitmaps, int itemwidth) {
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
