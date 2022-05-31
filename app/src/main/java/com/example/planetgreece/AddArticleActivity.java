package com.example.planetgreece;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.planetgreece.common.Helper;
import com.example.planetgreece.db.DatabaseHelper;
import com.example.planetgreece.db.model.Article;
import com.example.planetgreece.db.model.User;
import com.example.planetgreece.fragment.Login.LoginTabFragment;

public class AddArticleActivity extends AppCompatActivity {
    private DatabaseHelper db;

    private EditText etTitle;
    private EditText etImageLink;
    private EditText etSiteName;
    private EditText etLink;
    private EditText etDate;
    private Button btnAddArticle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_article);

        db = DatabaseHelper.getInstance(getApplicationContext());

        Intent intent = getIntent();
        User user = db.getUser(((User) intent.getSerializableExtra(LoginTabFragment.USER_OBJECT)).getId());

        if (!user.getIsAdmin()) {
            Toast.makeText(AddArticleActivity.this, "You are not an admin.", Toast.LENGTH_SHORT).show();
            finish();
        }

        etTitle = findViewById(R.id.etTitle);
        etImageLink = findViewById(R.id.etImageLink);
        etSiteName = findViewById(R.id.etSiteName);
        etLink = findViewById(R.id.etLink);
        etDate = findViewById(R.id.editTextDate2);
        btnAddArticle = findViewById(R.id.btnAddArticle);

        btnAddArticle.setOnClickListener(v -> {
            String title = etTitle.getText().toString();
            String imageLink = etImageLink.getText().toString();
            String siteName = etSiteName.getText().toString();
            String link = etLink.getText().toString();
            String date = etDate.getText().toString();

            if(title.isEmpty() || imageLink.isEmpty() || siteName.isEmpty() || link.isEmpty() || date.isEmpty()) {
                Toast.makeText(AddArticleActivity.this, "All fields are required.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!Helper.isValidDate(date)) {
                Toast.makeText(AddArticleActivity.this, "Invalid date format. Please use dd-MM-yyyy", Toast.LENGTH_SHORT).show();
                return;
            }

//            Results.addArticle(db, user, title, imageLink, siteName, link, date);

            Article article = new Article(title, imageLink, siteName, link, date);
            db.createArticle(article);

            Toast.makeText(AddArticleActivity.this, "Article added.", Toast.LENGTH_SHORT).show();
            finish();
        });

    }


}