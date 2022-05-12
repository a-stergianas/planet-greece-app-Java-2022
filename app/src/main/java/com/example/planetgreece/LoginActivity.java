package com.example.planetgreece;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.os.Bundle;

import com.example.planetgreece.db.DatabaseHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

public class LoginActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private FloatingActionButton google, fb, twitter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.view_pager);
        google = findViewById(R.id.fabGoogle);
        fb = findViewById(R.id.fabFacebook);
        twitter = findViewById(R.id.fabTwitter);

        tabLayout.setupWithViewPager(viewPager);
        LoginAdapter adapter = new LoginAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        adapter.addFragment(new LoginTabFragment(), "Login");
        adapter.addFragment(new SignupTabFragment(), "Sign Up");
        viewPager.setAdapter(adapter);

        google.setTranslationY(300);
        fb.setTranslationY(300);
        twitter.setTranslationY(300);
        tabLayout.setTranslationY(300);

        google.setAlpha(0f);
        fb.setAlpha(0f);
        twitter.setAlpha(0f);
        tabLayout.setAlpha(0f);

        google.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(400).start();
        fb.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(600).start();
        twitter.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(800).start();
        tabLayout.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(100).start();
    }
}