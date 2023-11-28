package com.example.mangareader;

import java.io.Serializable;

public class Detail implements Serializable {
String title;
    String thumbnail;
    String genre;

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public Detail(String title, String thumbnail, String synopsis, String genre) {
        this.title = title;
        this.thumbnail = thumbnail;
        this.synopsis = synopsis;
        this.genre = genre;
    }
    public Detail(){}
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    String synopsis;
}
