package com.example.planetgreece.db.model;

import java.io.Serializable;

public class Article implements Serializable {
    private int id;
    private String title;
    private String image;
    private String siteName;
    private String link;
    private String createdAt;
    private int likes;

    public Article() {

    }

    public Article(String title, String image, String siteName, String link, String createdAt) {
        this.title = title;
        this.image = image;
        this.siteName = siteName;
        this.link = link;
        this.likes = 0;
        this.createdAt = createdAt;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getImage() {
        return image;
    }

    public String getSiteName() {
        return siteName;
    }

    public String getLink() {
        return link;
    }

    public int getLikes() {
        return likes;
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

    public void setImage(String image) {
        this.image = image;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public void incrementLikes() {
        this.likes++;
    }

    public void decrementLikes() {
        this.likes--;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
