package com.jtmcompany.smartadvertisingboard.myvideo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jtmcompany.smartadvertisingboard.R;

import java.util.ArrayList;
import java.util.List;

public class MyVideoRecyclerAdapter extends RecyclerView.Adapter<MyVideoRecyclerAdapter.myViewHolder> {
    List<MyVideoModel> list=new ArrayList<>();
    myVideoWatchListener watchListener;
    myVideoUploadListener uploadListener;
    myVideoDeleteListener deleteListener;


    interface myVideoWatchListener{
        void onClick(String path);
    }

    interface myVideoUploadListener{
        void onUpload(String path);
    }

    interface myVideoDeleteListener{
        void onDelete(String path,int position);
    }
    public void setMyVieoWatchListener(myVideoWatchListener myVideoClickListener){
        this.watchListener=myVideoClickListener;
    }

    public void setMyVideoUploadListener(myVideoUploadListener myVideoUploadListener){
        this.uploadListener=myVideoUploadListener;
    }

    public void setMyVideoDeleteListener(myVideoDeleteListener myVideoDeleteListener){
        this.deleteListener=myVideoDeleteListener;
    }
    public MyVideoRecyclerAdapter(List<MyVideoModel> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.myadvertise_item,parent,false);
        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, final int position) {
        final MyVideoModel model=list.get(position);
        holder.thumnailIv.setImageBitmap(model.getImg());
        holder.titleTv.setText(model.getTitle());

        holder.playIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                watchListener.onClick(model.getPath());
            }
        });
        holder.uploadBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadListener.onUpload(model.getPath());
            }
        });
        holder.deleteBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteListener.onDelete(model.getPath(),position);
            }
        });



    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class myViewHolder extends RecyclerView.ViewHolder{
        ImageView thumnailIv;
        ImageView playIv;
        TextView titleTv;
        Button uploadBt;
        Button deleteBt;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            thumnailIv=itemView.findViewById(R.id.advertise_img);
            titleTv=itemView.findViewById(R.id.advertise_title);
            playIv=itemView.findViewById(R.id.myVideoPlay);
            uploadBt=itemView.findViewById(R.id.web_upload_bt);
            deleteBt=itemView.findViewById(R.id.delete_bt);
        }
    }

}


