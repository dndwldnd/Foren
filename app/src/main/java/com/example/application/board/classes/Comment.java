package com.example.application.board.classes;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Comment {

    private String uid;
    private String author;
    private String message;

    public Comment() { }

    public Comment(String uid, String author, String message) {
        this.uid = uid;
        this.author = author;
        this.message = message;
    }

    public String getUid() {
        return uid;
    }

    public String getAuthor() {
        return author;
    }

    public String getMessage() {
        return message;
    }
}
