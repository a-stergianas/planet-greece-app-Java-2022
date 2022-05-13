package com.example.planetgreece;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import com.example.planetgreece.db.model.User;
import com.example.planetgreece.fragment.ArticleAdapter;
import com.example.planetgreece.fragment.Login.LoginTabFragment;
import com.example.planetgreece.fragment.Main.HomeFragment;
import com.example.planetgreece.fragment.Main.ProfileFragment;
import com.example.planetgreece.fragment.Main.SavedFragment;
import com.example.planetgreece.fragment.Main.SettingsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<Article> articleList;
    private RecyclerView recyclerView;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_home);

        articleList = new ArrayList<>();
        recyclerView = findViewById(R.id.rvArticles);
        setArticlesInfo();
        setArticleAdapter();

        Intent intent = getIntent();
        User user = (User) intent.getSerializableExtra(LoginTabFragment.USER_OBJECT);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        getSupportFragmentManager().beginTransaction().replace(R.id.mainConstaint, HomeFragment.newInstance(user)).commit();
        bottomNavigationView.setSelectedItemId(R.id.navHome);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;

                switch (item.getItemId()){
//                    case R.id.navHome:
//                        fragment = HomeFragment.newInstance(user);
//                        break;
                    case R.id.navSaved:
                        fragment = SavedFragment.newInstance(user);
                        break;
                    case R.id.navProfile:
                        fragment = ProfileFragment.newInstance(user);
                        break;
                    case R.id.navSettings:
                        fragment = SettingsFragment.newInstance(user);
                        break;
                    default:
                        fragment = HomeFragment.newInstance(user);
                        break;
                }

                getSupportFragmentManager()
                        .beginTransaction()
                        .setReorderingAllowed(true)
                        .replace(R.id.mainConstaint, fragment)
                        .commit();

                return true;
            }
        });
    }

    private void setArticlesInfo() {
        articleList.add(new Article());
        articleList.add(new Article());
        articleList.add(new Article());
        articleList.add(new Article());
        articleList.add(new Article());
    }

    private void setArticleAdapter() {
        ArticleAdapter adapter = new ArticleAdapter(articleList);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

}