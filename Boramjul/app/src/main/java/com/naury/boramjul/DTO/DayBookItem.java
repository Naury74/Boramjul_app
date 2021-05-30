package com.naury.boramjul.DTO;

public class DayBookItem {

    private String thumbnail;
    private String book_cover;
    private String title;
    private String author;
    private String price;
    private String score;
    private String prodnum;

    public DayBookItem(String thumbnail, String book_cover, String title, String author, String price, String score, String prodnum) {
        this.thumbnail = thumbnail;
        this.book_cover = book_cover;
        this.title = title;
        this.author = author;
        this.price = price;
        this.score = score;
        this.prodnum = prodnum;
    }

    public String getProdnum() {
        return prodnum;
    }

    public void setProdnum(String prodnum) {
        this.prodnum = prodnum;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getBook_cover() {
        return book_cover;
    }

    public void setBook_cover(String book_cover) {
        this.book_cover = book_cover;
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
}
