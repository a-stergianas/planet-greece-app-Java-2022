package com.example.planetgreece.adapter;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.example.planetgreece.R;
import com.example.planetgreece.RecyclerViewInterface;
import com.example.planetgreece.db.DatabaseHelper;
import com.example.planetgreece.db.model.Article;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ViewHolder> {
    DatabaseHelper db;

    private int userId;
    private ArrayList<Article> articlesList;

    private final RecyclerViewInterface recyclerViewInterface;

    public ArticleAdapter(int userId, ArrayList<Article> articlesList, RecyclerViewInterface recyclerViewInterface) {
        this.userId = userId;
        this.articlesList = articlesList;
        this.recyclerViewInterface = recyclerViewInterface;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageView;
        private TextView title, site, date;
        private ImageButton btnLike, btnSave;

        public ViewHolder(@NonNull View view, RecyclerViewInterface recyclerViewInterface) {
            super(view);

            imageView = view.findViewById(R.id.ivArticleImage);
            title = view.findViewById(R.id.tvTitle);
            site = view.findViewById(R.id.tvSite);
            date = view.findViewById(R.id.tvDate);
            btnLike = view.findViewById(R.id.btnLike);
            btnSave = view.findViewById(R.id.btnSave);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (recyclerViewInterface != null) {
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            recyclerViewInterface.onItemClick(position);
                        }
                    }
                }
            });
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        db = DatabaseHelper.getInstance(viewGroup.getContext());

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_article, viewGroup, false);
        return new ViewHolder(view, recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, @SuppressLint("RecyclerView") final int position) {
        String title = articlesList.get(position).getTitle(),
                siteName = articlesList.get(position).getSiteName(),
                link = articlesList.get(position).getLink(),
                createdAt = articlesList.get(position).getCreatedAt();
        int image = articlesList.get(position).getImage();

        int articleId = articlesList.get(position).getId();

        viewHolder.title.setText(title);
        viewHolder.imageView.setImageResource(image);
        viewHolder.site.setText(siteName);
        //να μπει το λινκ
        viewHolder.date.setText(createdAt);

        if (db.isArticleInSaved(userId, articleId))
            viewHolder.btnSave.setImageTintList(ContextCompat.getColorStateList(viewHolder.btnSave.getContext(), R.color.secondary_green));

        viewHolder.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!db.isArticleInSaved(userId, articleId)) {
                    db.addArticleToSaved(userId, articleId);
                    viewHolder.btnSave.setImageTintList(ContextCompat.getColorStateList(v.getContext(), R.color.secondary_green));
                    Toast.makeText(v.getContext(), "Saved article", Toast.LENGTH_SHORT).show();
                } else {
                    db.removeArticleFromSaved(userId, articleId);
                    viewHolder.btnSave.setImageTintList(ContextCompat.getColorStateList(v.getContext(), R.color.white));
                    Toast.makeText(v.getContext(), "Removed article", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if (articlesList != null) {
            return articlesList.size();
        } else {
            return 0;
        }
    }
}
