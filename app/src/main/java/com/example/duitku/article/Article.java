package com.example.duitku.article;

public class Article {
    private final long id;
    private String title;
    private String address;

    public Article(long id, String title, String address){
        this.id = id;
        this.title = title;
        this.address = address;
    }

    public long getId() {
        return id;
    }

    public String getAddress() {
        return address;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
