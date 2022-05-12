package com.example.planetgreece.db.model;

public class Article {
    private int id;
    private String title;
    private String content;
    private String image;
    private String siteName;
    private String link;
    private String createdAt;

    public Article() {

    }

    public Article(String title, String content, String image, String siteName, String link) {
        this.title = title;
        this.content = content;
        this.image = image;
        this.siteName = siteName;
        this.link = link;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
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

    public String getCreatedAt() {
        return createdAt;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
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

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
