package com.example.mangareader;

import java.io.Serializable;

public class Manga implements Serializable {
    String next_page;
    String prev_page;
    String title;
    String description;

    public Manga(String next_page, String prev_page, String title, String description, String latest_chapter, String thumbnail, String param, String detail_url) {
        this.next_page = next_page;
        this.prev_page = prev_page;
        this.title = title;
        this.description = description;
        this.latest_chapter = latest_chapter;
        this.thumbnail = thumbnail;
        this.param = param;
        this.detail_url = detail_url;
    }
    public Manga(){}
    String latest_chapter;
    String thumbnail;
    String param;

    public String getNext_page() {
        return next_page;
    }

    public void setNext_page(String next_page) {
        this.next_page = next_page;
    }

    public String getPrev_page() {
        return prev_page;
    }

    public void setPrev_page(String prev_page) {
        this.prev_page = prev_page;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLatest_chapter() {
        return latest_chapter;
    }

    public void setLatest_chapter(String latest_chapter) {
        this.latest_chapter = latest_chapter;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

    public String getDetail_url() {
        return detail_url;
    }

    public void setDetail_url(String detail_url) {
        this.detail_url = detail_url;
    }

    String detail_url;
}
