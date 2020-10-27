package com.jtmcompany.smartadvertisingboard.BottomFragment.Create_Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.jtmcompany.smartadvertisingboard.R;

import java.util.ArrayList;
import java.util.List;

public class Create_Fragment extends Fragment {
    View view;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d("tak","test1");
        view= inflater.inflate(R.layout.fragment_create,container,false);
        return view;

    }

    @Override
    public void onStart() {
        Log.d("tak","test2");
        super.onStart();
        ViewPager viewPager=view.findViewById(R.id.create_viewpager);
        TabLayout tabLayout=view.findViewById(R.id.create_tablayout);
        viewPager.setAdapter(new MypagerAdapter(getChildFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);

    }

    class MypagerAdapter extends FragmentPagerAdapter{
        List<Fragment> fragments=new ArrayList<>();
        private String titles[]=new String[]{"광고만들기", "나의광고"};
        public MypagerAdapter(@NonNull FragmentManager fm) {
            super(fm);
            Log.d("tak","생성자");
            fragments.add(new Create_Fragment_createAdvertise());
            fragments.add(new Create_Fragment_myAdvertise());

        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            Log.d("tak","getItem");
            return this.fragments.get(position);
        }

        @Override
        public int getCount() {
            Log.d("tak","getCount");
            return this.fragments.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("tak","test3");
    }
}
