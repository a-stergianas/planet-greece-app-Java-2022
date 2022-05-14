package com.example.planetgreece.db.model;

import com.example.planetgreece.R;
import com.example.planetgreece.db.DatabaseHelper;

import java.io.Serializable;

public class Article implements Serializable {
    private int id;
    private String title;
    private int image;
    private String siteName;
    private String link;
    private String createdAt;

    public Article() {

    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public int getImage() {
        return image;
    }

    public String getSiteName() {
        return siteName;
    }

    public String getLink() {
        return link;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
