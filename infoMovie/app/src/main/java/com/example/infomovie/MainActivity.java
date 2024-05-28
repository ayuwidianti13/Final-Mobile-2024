package com.example.infomovie;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.infomovie.fragment.FavoriteFragment;
import com.example.infomovie.fragment.HomeFragment;
import com.example.infomovie.fragment.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // Pastikan untuk mengatur layout activity_main

        FragmentManager fragmentManager = getSupportFragmentManager();
        HomeFragment movieFragment = new HomeFragment();
        Fragment fragment = fragmentManager.findFragmentByTag(HomeFragment.class.getSimpleName());
        if (!(fragment instanceof HomeFragment)){
            fragmentManager
                    .beginTransaction()
                    .add(R.id.frame_container, movieFragment, HomeFragment.class.getSimpleName())
                    .commit();
        }

        BottomNavigationView bottomNav = findViewById(R.id.navmenu);
        bottomNav.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;

            if (item.getItemId() == R.id.homebtn) {
                selectedFragment = new HomeFragment();
            } else if (item.getItemId() == R.id.favhbtn) {
                 selectedFragment = new FavoriteFragment();
            } else if (item.getItemId() == R.id.profilebtn) {
                 selectedFragment = new ProfileFragment();
            }

            if (selectedFragment != null) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frame_container, selectedFragment)
                        .commit();

                return true;
            } else {
                return false;
            }
        });
    }
}
