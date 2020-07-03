package com.jtmcompany.smartadvertisingboard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.jtmcompany.smartadvertisingboard.BottomFragment.Create_Fragment.Create_Fragment;
import com.jtmcompany.smartadvertisingboard.BottomFragment.Home_Fragment;
import com.jtmcompany.smartadvertisingboard.BottomFragment.Mypage_Fragment;

public class LoginInfo_Activity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_info);


        final Home_Fragment home_fragment = new Home_Fragment();
        final Create_Fragment create_fragment = new Create_Fragment();
        final Mypage_Fragment mypage_fragment = new Mypage_Fragment();

        getSupportFragmentManager().beginTransaction().replace(R.id.container, home_fragment).commit();
        BottomNavigationView bottom_navigationview = findViewById(R.id.bottom_navigation);
        bottom_navigationview.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.home:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, home_fragment).commit();
                        return true;
                    case R.id.create:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, create_fragment).commit();
                        return true;
                    case R.id.mypage:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, mypage_fragment).commit();
                        return true;
                }
                return false;
            }
        });
    }


}
