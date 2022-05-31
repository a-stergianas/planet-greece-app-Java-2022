package com.example.planetgreece.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.planetgreece.R;
import com.example.planetgreece.RecyclerViewInterface;
import com.example.planetgreece.db.DatabaseHelper;
import com.example.planetgreece.db.model.Article;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

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
        private TextView title, site, date, likes;
        private ImageButton btnLike, btnSave;

        public ViewHolder(@NonNull View view, RecyclerViewInterface recyclerViewInterface) {
            super(view);

            imageView = view.findViewById(R.id.ivArticleImage);
            title = view.findViewById(R.id.tvTitle);
            site = view.findViewById(R.id.tvSite);
            date = view.findViewById(R.id.tvDate);
            btnLike = view.findViewById(R.id.btnLike);
            btnSave = view.findViewById(R.id.btnSave);
            likes = view.findViewById(R.id.tvLikes);

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

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, @SuppressLint("RecyclerView") final int position) {
        String title = articlesList.get(position).getTitle(),
                siteName = articlesList.get(position).getSiteName(),
                createdAt = articlesList.get(position).getCreatedAt(),
                image = articlesList.get(position).getImage();
        // transforming int likes to final int array so we can update it inside the onClick listener
        final int[] likes = { articlesList.get(position).getLikes() };

        int articleId = articlesList.get(position).getId();

        viewHolder.title.setText(title);
//        viewHolder.imageView.setImageResource(image);
        Picasso.get().load(image).into(viewHolder.imageView);
        viewHolder.site.setText(siteName);

        viewHolder.date.setText(createdAt.substring(0, 10)); // only 10 first characters which equals only the date without the time
        viewHolder.likes.setText(likes[0] + " likes");

        if (db.isArticleInSaved(userId, articleId))
            viewHolder.btnSave.setImageTintList(ContextCompat.getColorStateList(viewHolder.btnSave.getContext(), R.color.secondary_green));

        if (db.isArticleInLiked(userId, articleId))
            viewHolder.btnLike.setImageTintList(ContextCompat.getColorStateList(viewHolder.btnLike.getContext(), R.color.secondary_green));

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

        viewHolder.btnLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!db.isArticleInLiked(userId, articleId)) {
                    db.addArticleToLiked(userId, articleId);
                    viewHolder.btnLike.setImageTintList(ContextCompat.getColorStateList(v.getContext(), R.color.secondary_green));
                    likes[0] = likes[0] + 1;
                    // unnecessary toast message
                } else {
                    db.removeArticleFromLiked(userId, articleId);
                    viewHolder.btnLike.setImageTintList(ContextCompat.getColorStateList(v.getContext(), R.color.white));
                    likes[0] = likes[0] - 1;
                }
                viewHolder.likes.setText(likes[0] + " likes");
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
