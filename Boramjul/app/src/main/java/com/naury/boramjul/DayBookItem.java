package com.naury.boramjul;

public class DayBookItem {

    private String thumbnail;
    private String book_cover;
    private String title;
    private String author;

    public DayBookItem(String thumbnail, String book_cover, String title, String author) {
        this.thumbnail = thumbnail;
        this.book_cover = book_cover;
        this.title = title;
        this.author = author;
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
