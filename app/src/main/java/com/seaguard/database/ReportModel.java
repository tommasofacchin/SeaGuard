package com.seaguard.database;

import java.util.Map;
import java.util.HashMap;
import android.graphics.Bitmap;

public class ReportModel implements DbModel{

    private String idReport;
    private String idUser;
    private String idArea;
    private double latitude;
    private double longitude;
    private String idCategorie;
    private String time;
    private String date;
    private String description;
    private int urgency;
    private String image;

    public ReportModel(String idUser, String idArea, double latitude, double longitude, String idCategorie, String time, String date, String description, int urgency, String image) {
        this.idUser = idUser;
        this.idArea = idArea;
        this.latitude = latitude;
        this.longitude = longitude;
        this.idCategorie = idCategorie;
        this.time = time;
        this.date = date;
        this.description = description;
        this.urgency = urgency;
        this.image = image;
    }
    public ReportModel(Map<String, Object> report) {
        this.idReport = (String) report.get("idReport");
        this.idUser = (String) report.get("idUser");
        this.idArea = (String) report.get("idArea");
        this.latitude = (double) report.get("latitude");
        this.longitude = (double) report.get("longitude");
        this.idCategorie = (String) report.get("idCategorie");
        this.time = (String) report.get("time");
        this.date = (String) report.get("date");
        this.description = (String) report.get("description");
        this.urgency = (int) report.get("urgency");
        this.image = (String) report.get("image");
    }

    @Override
    public String getCollectionPath() {
        return "reports";
    }

    @Override
    public String getId() {
        return idReport;
    }

    @Override
    public Map<String, Object> toMap() {

        //return Collections.emptyMap();

        Map<String, Object> map = new HashMap<>();
        map.put("idReport", idReport);
        map.put("idUser", idUser);
        map.put("idArea", idArea);
        map.put("latitude", latitude);
        map.put("longitude", longitude);
        map.put("idCategorie", idCategorie);
        map.put("urgency", urgency);
        map.put("time", time);
        map.put("date", date);
        map.put("description", description);
        map.put("image", image);
        return map;
    }
    public String getIdUser() {
        return idUser;
    }

    public String getIdArea() {
        return idArea;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getIdCategorie() {
        return idCategorie;
    }

    public String getTime() {
        return time;
    }

    public String getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }

    public int getUrgency() {
        return urgency;
    }

    public String getImage() {
        return image;
    }

    public void setIdReport(String idReport) {
        this.idReport = idReport;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public void setIdArea(String idArea) {
        this.idArea = idArea;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setIdCategorie(String idCategorie) {
        this.idCategorie = idCategorie;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setUrgency(int urgency) {
        this.urgency = urgency;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
