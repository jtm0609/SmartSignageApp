package com.jtmcompany.smartadvertisingboard.BottomFragment.Create_Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jtmcompany.smartadvertisingboard.R;

import java.util.ArrayList;
import java.util.List;

public class myAdvertise_RecyclerAdapter extends RecyclerView.Adapter<myAdvertise_RecyclerAdapter.myViewHolder> {
    List<myAdvertise_Model> list=new ArrayList<>();

    public myAdvertise_RecyclerAdapter(List<myAdvertise_Model> list) {
        this.list = list;
    }


    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.myadvertise_item,parent,false);
        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        myAdvertise_Model model=list.get(position);
        holder.image.setImageDrawable(model.getImg());
        holder.title.setText(model.getTitle());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class myViewHolder extends RecyclerView.ViewHolder{
        ImageView image;
        TextView title;
        TextView date;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            image=itemView.findViewById(R.id.advertise_img);
            title=itemView.findViewById(R.id.advertise_title);
        }
    }

}


