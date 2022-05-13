package com.example.planetgreece;

import com.example.planetgreece.R;

import java.io.Serializable;

public class Article implements Serializable {
    private int id;
    private String title;
    private String content; //νομίζω δεν μας χρειάζεται (αχιλλ)
    private int image;
    private String siteName;
    private String link;
    private String createdAt;

    public Article() {
        title = "Θωμάς ο… Survivor!";
        image = Integer.valueOf(R.drawable.article_image);
        siteName = "Αρκτούρος";
        link = "https://www.arcturos.gr/news/thomassurvivor/";
        createdAt = "10.05.2022";
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

    public void setContent(String content) {
        this.content = content;
    }

    public void setImage(String image) {
        //this.image = image;
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
