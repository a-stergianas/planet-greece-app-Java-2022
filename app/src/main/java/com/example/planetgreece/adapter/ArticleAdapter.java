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
import androidx.recyclerview.widget.RecyclerView;
import com.example.planetgreece.R;
import com.example.planetgreece.RecyclerViewInterface;
import com.example.planetgreece.db.DatabaseHelper;
import com.example.planetgreece.db.model.Article;

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

        viewHolder.title.setText(title);
        viewHolder.imageView.setImageResource(image);
        viewHolder.site.setText(siteName);
        //να μπει το λινκ
        viewHolder.date.setText(createdAt);

        viewHolder.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int articleId = articlesList.get(position).getId();

                if (!db.isArticleInSaved(userId, articleId)) {
                    db.addArticleToSaved(userId, articleId);
                    Toast.makeText(v.getContext(), "Saved article", Toast.LENGTH_SHORT).show();
                } else {
                    db.removeArticleFromSaved(userId, articleId);
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
