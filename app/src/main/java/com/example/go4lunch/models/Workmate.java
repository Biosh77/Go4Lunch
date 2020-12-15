package com.example.go4lunch.models;

import androidx.annotation.Nullable;

import java.io.Serializable;
import java.util.List;

public class Workmate implements Serializable {

    private String uid;
    private String username;
    @Nullable
    private String urlPicture;
    private String interestedBy;
    private List<String> likes;

    public Workmate() {
    }

    public Workmate(String uid, String username, String urlPicture) {
        this.uid = uid;
        this.username = username;
        this.urlPicture = urlPicture;
    }

    // --- GETTERS ---
    public String getUid() { return uid; }
    public String getUsername() { return username; }
    public String getUrlPicture() { return urlPicture; }
    public String getInterestedBy() { return interestedBy; }
    public List<String>getLikes(){return likes; }

    // --- SETTERS ---
    public void setUsername(String username) { this.username = username; }
    public void setUid(String uid) { this.uid = uid; }
    public void setUrlPicture(String urlPicture) { this.urlPicture = urlPicture; }
    public void setInterestedBy(String interestedBy) {this.interestedBy = interestedBy; }
    public void setLikes(List<String> likes) {this.likes = likes; }
}