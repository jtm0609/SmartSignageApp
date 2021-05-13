package com.jtmcompany.smartadvertisingboard.videoedit.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jtmcompany.smartadvertisingboard.R;
import com.jtmcompany.smartadvertisingboard.videoedit.model.EditorMenu;

import java.util.ArrayList;
import java.util.List;

public class VideoEditAdapter extends RecyclerView.Adapter<VideoEditAdapter.ViewHolder>{
    private List<EditorMenu> editor_list=new ArrayList<>();
    private OnClickEditor_ModelListener mListener;
    public interface OnClickEditor_ModelListener{
        public void OnClickedEditorItem(int position);
    }

    public void setOnClickedListener(OnClickEditor_ModelListener listener){
        mListener=listener;
    }


    public VideoEditAdapter(List<EditorMenu> editor_list) {
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
        EditorMenu editor_model=editor_list.get(position);
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
                    mListener.OnClickedEditorItem(getAdapterPosition());
                }
            });
        }

    }
}
