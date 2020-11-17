package com.jtmcompany.smartadvertisingboard.PhotoEdit.PhotoEdit_BottomFragment.Text;

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

public class TextRecyclerAdapter extends RecyclerView.Adapter<TextRecyclerAdapter.MyViewHolder> {


    List<Drawable> list= new ArrayList<>();
    TextSelectListener textSelectListener;


    public interface TextSelectListener{
        void Onclick(int position);
    }

    public void setOnTextSelectListener(TextSelectListener textSelectListener){
        this.textSelectListener=textSelectListener;
    }

    public TextRecyclerAdapter(List<Drawable> list) {
        this.list = list;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(parent.getContext());
        View view=layoutInflater.inflate(R.layout.photo_edit_recycler_item,null);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {

        holder.imageView.setImageDrawable(list.get(position));
        holder.imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textSelectListener.Onclick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.photo_edit_img);


        }
    }
}
