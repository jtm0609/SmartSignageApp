package com.jtmcompany.smartadvertisingboard.BottomFragment.Create_Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jtmcompany.smartadvertisingboard.R;

import java.util.ArrayList;
import java.util.List;

public class Create_Fragment_myAdvertise extends Fragment {
    List<myAdvertise_Model> list=new ArrayList<>();
    myAdvertise_RecyclerAdapter recyclerAdapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_myadvertise,container,false);
        RecyclerView recyclerView=view.findViewById(R.id.myadvertise_recycler);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);
        //DB에서 사용자가 저장한 동영상제목, 이미지, 날짜, 파일을 리싸이클러뷰를이용하여 불러와서 표시하게구현할것.

        recyclerAdapter=new myAdvertise_RecyclerAdapter(list);


        recyclerView.setAdapter(recyclerAdapter);





        return view;

    }

    @Override
    public void onPause() {
        super.onDetach();
        Log.d("tak20","onPause");
        list.clear();
        recyclerAdapter.notifyDataSetChanged();

    }
}
