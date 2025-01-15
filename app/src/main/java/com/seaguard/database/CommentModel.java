package com.seaguard.database;

import com.google.firebase.Timestamp;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class CommentModel implements DbModel {
    private String id;
    private String idReport;
    private String idUser;
    private String username;
    private int rating;
    private String content;
    private Timestamp timestamp;

    public CommentModel(String idReport, String idUser, String username, int rating, String content, Timestamp timestamp) {
        this.idReport = idReport;
        this.idUser = idUser;
        this.username = username;
        this.rating = rating;
        this.content = content;
        this.timestamp = timestamp;
    }

    public CommentModel(String id, Map<String, Object> elem) {
        this.id = id;
        this.idReport = elem.get("idReport") instanceof String ? (String) elem.get("idReport") : "";
        this.idUser = elem.get("idUser") instanceof String ? (String) elem.get("idUser") : "";
        this.username = elem.get("username") instanceof String ? (String) elem.get("username") : "";
        this.rating = elem.get("rating") instanceof Long ? ((Long) elem.get("rating")).intValue() : 0;
        this.content = elem.get("content") instanceof String ? (String) elem.get("content") : "";
        this.timestamp = elem.get("timestamp") instanceof Timestamp ? (Timestamp) elem.get("timestamp") : null;
    }

    public String getIdReport() {
        return idReport;
    }

    public void setIdReport(String idReport) {
        this.idReport = idReport;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public String getTime() {
        return (timestamp != null) ? new SimpleDateFormat("HH:mm", Locale.getDefault()).format(timestamp.toDate()) : "";
    }

    public String getDate() {
        return (timestamp != null) ? new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(timestamp.toDate()) : "";
    }

    @Override
    public String getCollectionPath() {
        return "comments";
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("idReport", idReport);
        map.put("idUser", idUser);
        map.put("username", username);
        map.put("rating", rating);
        map.put("content", content);
        map.put("timestamp", timestamp);
        return map;
    }

}
