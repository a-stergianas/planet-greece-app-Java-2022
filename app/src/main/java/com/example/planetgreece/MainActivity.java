package com.example.planetgreece;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.planetgreece.db.DatabaseHelper;
import com.example.planetgreece.db.model.Article;
import com.example.planetgreece.db.model.User;
import com.example.planetgreece.fragment.Login.LoginTabFragment;
import com.example.planetgreece.fragment.Main.HomeFragment;
import com.example.planetgreece.fragment.Main.ProfileFragment;
import com.example.planetgreece.fragment.Main.SavedFragment;
import com.example.planetgreece.fragment.Main.SettingsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {
    DatabaseHelper db;

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = DatabaseHelper.getInstance(getApplicationContext());

        // TODO: Remove this when done testing
        // Add example articles
        Article article1 = new Article();
        Article article2 = new Article();
        Article article3 = new Article();

        article1.setLink("https://www.arcturos.gr/gr/draseis/viotopos-i-fusi-stin-auli-mas/");
        article1.setTitle("ΒΙΟΤΟΠΟΣ");
        article1.setSiteName("Αρκτούρος");
        article1.setImage(Integer.valueOf(R.drawable.article_image));

        article2.setImage(Integer.valueOf(R.drawable.article_image));
        article2.setLink("https://www.arcturos.gr/news/thomassurvivor/");
        article2.setTitle("Θωμάς ο… Survivor!");
        article2.setSiteName("Αρκτούρος");

        article3.setTitle("ΕΠΑΝΕΝΤΑΞΗ ΣΤΗ ΦΥΣΗ");
        article3.setLink("https://www.arcturos.gr/gr/draseis/epanedaxi-sti-fusi/");
        article3.setSiteName("Αρκτούρος");
        article3.setImage(Integer.valueOf(R.drawable.article_image));

        db.createArticle(article1);
        db.createArticle(article2);
        db.createArticle(article3);
        // End of example articles

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

}