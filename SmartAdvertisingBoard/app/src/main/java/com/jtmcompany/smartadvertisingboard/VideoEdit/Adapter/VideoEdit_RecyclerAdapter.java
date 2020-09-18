package com.jtmcompany.smartadvertisingboard.VideoEdit.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jtmcompany.smartadvertisingboard.R;
import com.jtmcompany.smartadvertisingboard.VideoEdit.Editor_Model;

import java.util.ArrayList;
import java.util.List;

public class VideoEdit_RecyclerAdapter extends RecyclerView.Adapter<VideoEdit_RecyclerAdapter.ViewHolder>{
    List<Editor_Model> editor_list=new ArrayList<>();
    OnClickEditor_ModelListener mListener;
    public interface OnClickEditor_ModelListener{
        public void OnClickedEditor_Model(int position);
    }

    public void setOnClickedListener(OnClickEditor_ModelListener listener){
        mListener=listener;
    }


    public VideoEdit_RecyclerAdapter(List<Editor_Model> editor_list) {
        this.editor_list = editor_list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View view=inflater.inflate(R.layout.video_edit_recycler_item,null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Editor_Model editor_model=editor_list.get(position);
        holder.imageView.setImageDrawable(editor_model.getImage());
        holder.textView.setText(editor_model.getText());
    }

    @Override
    public int getItemCount() {
        return editor_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView textView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.video_edit_item_img);
            textView=itemView.findViewById(R.id.video_edit_item_text);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.OnClickedEditor_Model(getAdapterPosition());
                }
            });
        }

    }
}
