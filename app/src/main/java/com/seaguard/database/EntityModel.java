package com.seaguard.database;

import com.google.firebase.firestore.GeoPoint;

import java.util.HashMap;
import java.util.Map;

public class EntityModel implements DbModel {
    private String id;
    private String description;
    private int level;
    private double latitude;
    private double longitude;
    private String name;
    private String phoneNumber;
    private String website;

    public EntityModel(String website, String phoneNumber, String name, double longitude, double latitude, int level, String description) {
        this.website = website;
        this.phoneNumber = phoneNumber;
        this.name = name;
        this.longitude = longitude;
        this.latitude = latitude;
        this.level = level;
        this.description = description;
    }

    public EntityModel(String id, Map<String, Object> report) {
        this.id = id;
        this.website = report.get("website") instanceof String ? (String) report.get("website") : "";
        this.phoneNumber = report.get("pnumber") instanceof String ? (String) report.get("pnumber") : "";
        this.name = report.get("nomeEnte") instanceof String ? (String) report.get("nomeEnte") : "";;
        this.level = report.get("level") instanceof Long ? ((Long) report.get("level")).intValue() : 0;
        this.description = report.get("description") instanceof String ? (String) report.get("description") : "";;

        if (report.get("location") instanceof GeoPoint) {
            GeoPoint location = (GeoPoint) report.get("location");
            this.latitude = location.getLatitude();
            this.longitude = location.getLongitude();
        } else {
            this.latitude = 0.0;
            this.longitude = 0.0;
        }
    }


    public String getWebsite() {
        return website;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getName() {
        return name;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public int getLevel() {
        return level;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    @Override
    public String getCollectionPath() {
        return "entities";
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("description", description);
        map.put("level", level);
        map.put("location", new GeoPoint(latitude, longitude));
        map.put("name", name);
        map.put("phoneNumber", phoneNumber);
        map.put("website", website);
        return map;
    }
}
