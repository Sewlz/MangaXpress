package com.example.mangareader;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class ActivityHome extends AppCompatActivity {
    BottomNavigationView navHome;
    FrameLayout frameHome;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        frameHome = (FrameLayout) findViewById(R.id.frameHome);
        navHome = (BottomNavigationView) findViewById(R.id.navHome);
        loadFragment(new Fragment_Home());
        navHome.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if(id == R.id.search){
                    loadFragment(new Fragment_Search());
                }
                if(id == R.id.home){
                    loadFragment(new Fragment_Home());
                }
                if(id == R.id.profile){
                    loadFragment(new Fragment_Profile());
                }
                return true;
            }
        });
    }
    public void loadFragment(Fragment fragment)
    {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.frameHome,fragment);
        ft.commit();
    }
}