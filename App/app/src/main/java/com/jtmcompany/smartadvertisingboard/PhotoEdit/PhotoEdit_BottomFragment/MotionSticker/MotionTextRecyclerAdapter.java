package com.jtmcompany.smartadvertisingboard.PhotoEdit.PhotoEdit_BottomFragment.MotionSticker;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jtmcompany.smartadvertisingboard.R;

import java.util.ArrayList;
import java.util.List;

public class MotionTextRecyclerAdapter extends RecyclerView.Adapter<MotionTextRecyclerAdapter.MotionViewHolder> {
private List<String> list=new ArrayList<>();
private MotionTextSelectListener mMotionTextSelectListener=null;

    public interface MotionTextSelectListener {
           void onItemClick(View v, int position);
    }

    public void MotionSetOnClickListener(MotionTextSelectListener motionTextSelectListener){
        mMotionTextSelectListener=motionTextSelectListener;
    }

    public MotionTextRecyclerAdapter(List<String> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public MotionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(parent.getContext());
        View view=layoutInflater.inflate(R.layout.motion_recycler_item,null);
        return new MotionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MotionViewHolder holder, final int position) {
        holder.bt_View.setText(list.get(position));
        holder.bt_View.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMotionTextSelectListener.onItemClick(holder.bt_View,position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MotionViewHolder extends RecyclerView.ViewHolder {
        Button bt_View;
        public MotionViewHolder(@NonNull View itemView) {
            super(itemView);
            bt_View=itemView.findViewById(R.id.motionText);


        }
    }
}
