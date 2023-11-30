package com.example.mangareader;
import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.PropertyName;
public class UserFav {
    public UserFav(){}
    public UserFav(String email, String mangaUrl,String thumbnail) {
        this.email = email;
        this.mangaUrl = mangaUrl;
        this.thumbnail = thumbnail;
    }
    @PropertyName("EMAIL")
    String email;
    @PropertyName("EMAIL")
    public String getEmail() {
        return email;
    }
    @PropertyName("EMAIL")
    public void setEmail(String email) {
        this.email = email;
    }
    @PropertyName("THUMBNAIL")
    String thumbnail;
    @PropertyName("THUMBNAIL")
    public String getThumbnail() {
        return thumbnail;
    }
    @PropertyName("THUMBNAIL")
    public void setThumbnail(String thumbnail) {this.thumbnail = thumbnail;}
    @PropertyName("MANGAURL")
    public String getMangaUrl() {
        return mangaUrl;
    }
    @PropertyName("MANGAURL")
    public void setMangaUrl(String mangaUrl) {
        this.mangaUrl = mangaUrl;
    }
    @PropertyName("MANGAURL")
    String mangaUrl;
}
