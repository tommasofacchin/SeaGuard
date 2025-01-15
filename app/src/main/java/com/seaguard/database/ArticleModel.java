package com.seaguard.database;

import com.google.firebase.Timestamp;

import org.osmdroid.api.IGeoPoint;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ArticleModel implements DbModel{

    private String id;
    private String title;
    private String description;
    private String content; // Testo completo dell'articolo
    private String link; // Link esterno (se necessario)
    private Timestamp timestamp;
    @Override
    public String getCollectionPath() {
        return "articles";
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("title", title);
        map.put("description", description);
        map.put("content", content);
        map.put("link", link);
        map.put("timestamp", timestamp);
        return map;
    }

    public ArticleModel(String title, double longitude, double latitude, String description, String content, String link, Timestamp timestamp) {
        this.title = title;
        this.description = description;
        this.content = content;
        this.link = link;
        this.timestamp = timestamp;
    }

    public ArticleModel(String id, Map<String, Object> elem) {
        this.id = id;
        this.title = elem.get("title") instanceof String ? (String) elem.get("title") : "";
        this.description = elem.get("description") instanceof String ? (String) elem.get("description") : "";
        this.content = elem.get("content") instanceof String ? (String) elem.get("content") : "";
        this.link = elem.get("link") instanceof String ? (String) elem.get("link") : "";
        this.timestamp = elem.get("timestamp") instanceof Timestamp ? (Timestamp) elem.get("timestamp") : null;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public String getTime() {
        return (timestamp != null) ? new SimpleDateFormat("HH:mm", Locale.getDefault()).format(timestamp.toDate()) : "";
    }

    public String getDate() {
        return (timestamp != null) ? new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(timestamp.toDate()) : "";
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getLink() {
        return link;
    }

    public String getContent() {
        return content;
    }

    public String getDescription() {
        return description;
    }

    public String getTitle() {
        return title;
    }
}
