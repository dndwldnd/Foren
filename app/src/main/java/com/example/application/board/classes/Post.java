package com.example.application.board.classes;

import com.google.firebase.database.Exclude;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Post {
    private String uid;
    private String title;
    private String message;
    private final String date;
    private String author;


    public Post() {
        Date now = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss a", Locale.KOREA);

        date = simpleDateFormat.format(now);
    }

    public Post(String u, String t, String m, String a) {
        uid = u;
        title = t;
        message = m;
        author = a;

        Date now = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd a hh:mm:ss", Locale.KOREA);

        date = simpleDateFormat.format(now);
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("uid", uid);
        result.put("author", author);
        result.put("title", title);
        result.put("message", message);
        result.put("date", date);

        return result;
    }
}
