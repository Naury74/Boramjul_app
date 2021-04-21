package com.naury.boramjul;

import android.graphics.Bitmap;

import java.io.Serializable;

public class BookListItem implements Serializable {
    private String thumbnail;
    private String category;
    private String title;
    private String author;
    private String price;
    private String score_review;
    private int count;

    public BookListItem(String thumbnail, String category, String title, String author, String price, String score_review) {
        this.thumbnail = thumbnail;
        this.category = category;
        this.title = title;
        this.author = author;
        this.price = price;
        this.score_review = score_review;
        this.count = 1;
    }

    public BookListItem(String thumbnail, String category, String title, String author, String price, String score_review, int count) {
        this.thumbnail = thumbnail;
        this.category = category;
        this.title = title;
        this.author = author;
        this.price = price;
        this.score_review = score_review;
        this.count = count;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getScore_review() {
        return score_review;
    }

    public void setScore_review(String score_review) {
        this.score_review = score_review;
    }
}
