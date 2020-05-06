package com.jtmcompany.smartadvertisingboard;

import android.graphics.drawable.Drawable;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class Bottomsheet_RecyclerAdapter extends RecyclerView.Adapter<Bottomsheet_RecyclerAdapter.ItemHoder> {
private List<DataV0> list;
    public Bottomsheet_RecyclerAdapter(List<DataV0> list) {
     this.list=list;
    }

    @NonNull
    @Override
    public ItemHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View root= LayoutInflater.from(parent.getContext()).inflate(R.layout.bottom_sheet_item,parent,false);
        return new ItemHoder(root);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHoder holder, int position) {
            DataV0 v0=list.get(position);
            holder.textView.setText(v0.text);
            holder.imageView.setImageDrawable(v0.image);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ItemHoder extends RecyclerView.ViewHolder{
            ImageView imageView;
            TextView textView;

        public ItemHoder(@NonNull View root) {
            super(root);
            imageView=root.findViewById(R.id.sheet_img);
            textView=root.findViewById(R.id.sheet_text);

        }
    }

}

class DataV0{
    String text;
    Drawable image;
}

