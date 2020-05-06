package com.jtmcompany.smartadvertisingboard;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class Create_Advertise_fragment extends Fragment {
    View view;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       view=inflater.inflate(R.layout.fragment_create_advertise,container,false);
       return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        ImageView create_advertise_bt = view.findViewById(R.id.create_advertise_bt);
        create_advertise_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(getActivity(),Create_Advertise_Activity.class);
                startActivity(intent);
            }
        });
    }
}
