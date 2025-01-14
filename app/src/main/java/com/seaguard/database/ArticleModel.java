package com.seaguard.database;

import org.osmdroid.api.IGeoPoint;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ArticleModel implements DbModel{

    private String id;
    private String title;
    private double longitude;
    private double latitude;
    private String description;
    private String content; // Testo completo dell'articolo
    private String link; // Link esterno (se necessario)

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
        map.put("latitude", latitude);
        map.put("longitude", longitude);
        map.put("content", content);
        map.put("link", description);
        return map;
    }

    public ArticleModel(String title, double longitude, double latitude, String description, String content, String link) {
        this.title = title;
        this.longitude = longitude;
        this.latitude = latitude;
        this.description = description;
        this.content = content;
        this.link = link;
    }

    public ArticleModel(String id, Map<String, Object> elem) {
        this.id = id;
        this.title = elem.get("title") instanceof String ? (String) elem.get("title ") : "";
        this.longitude = elem.get("longitude") instanceof Double ? (Double) elem.get("longitude ") : 0.0;
        this.latitude = elem.get("latitude") instanceof Double ? (Double) elem.get("latitude ") : 0.0;
        this.description = elem.get("description") instanceof String ? (String) elem.get("description ") : "";
        this.content = elem.get("content") instanceof String ? (String) elem.get("content ") : "";
        this.link = elem.get("link") instanceof String ? (String) elem.get("link ") : "";
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
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

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getTitle() {
        return title;
    }
}
