package com.example.planetgreece.fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.planetgreece.R;
import com.example.planetgreece.Article;

import java.util.ArrayList;

public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ViewHolder> {

    //Context context;
    private ArrayList<Article> articlesList;

    public ArticleAdapter(/*Context context,*/ ArrayList<Article> articlesList) {
        //this.context = context;
        this.articlesList = articlesList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageView;
        private TextView title, site, date;

        public ViewHolder(@NonNull View view) {
            super(view);

            imageView = view.findViewById(R.id.ivArticleImage);
            title = view.findViewById(R.id.tvTitle);
            site = view.findViewById(R.id.tvSite);
            date = view.findViewById(R.id.tvDate);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_article, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
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
    }

    @Override
    public int getItemCount() {
        return articlesList.size();
    }
}
