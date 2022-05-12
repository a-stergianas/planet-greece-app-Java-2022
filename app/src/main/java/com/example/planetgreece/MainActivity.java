package com.example.planetgreece;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        getSupportFragmentManager().beginTransaction().replace(R.id.mainConstaint, new HomeFragment()).commit();
        bottomNavigationView.setSelectedItemId(R.id.navHome);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;
                switch (item.getItemId()){
                    case R.id.navHome:
                        fragment = new HomeFragment();
                        break;
                    case R.id.navSaved:
                        fragment = new SavedFragment();
                        break;
                    case R.id.navProfile:
                        fragment = new ProfileFragment();
                        break;
                    case R.id.navSettings:
                        fragment = new SettingsFragment();
                        break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.mainConstaint, fragment).commit();
                return true;
            }
        });
    }
}