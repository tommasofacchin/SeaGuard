package com.seaguard.database;

import java.util.Collections;
import java.util.Map;
import java.util.HashMap;
import java.util.Objects;

public class ReportModel implements DbModel{

    private String idReport;
    private String idUser;
    private String idArea;
    private String idCategorie;
    private String date;
    private String description;
    private int urgency;


    public ReportModel(String idUser, String idArea, String idCategorie, String date, String description, int urgency) {
        this.idUser = idUser;
        this.idArea = idArea;
        this.idCategorie = idCategorie;
        this.date = date;
        this.description = description;
        this.urgency = urgency;
    }
    public ReportModel(Map<String, Object> report) {
        this.idReport = (String) report.get("idReport");
        this.idUser = (String) report.get("idUser");
        this.idArea = (String) report.get("idArea");
        this.idCategorie = (String) report.get("idCategorie");
        this.date = (String) report.get("date");
        this.description = (String) report.get("description");
        this.urgency = (int) report.get("urgency");
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
        map.put("idCategorie", idCategorie);
        map.put("urgency", urgency);
        map.put("date", date);
        map.put("description", description);
        return map;
    }
    public String getIdUser() {
        return idUser;
    }

    public String getIdArea() {
        return idArea;
    }

    public String getIdCategorie() {
        return idCategorie;
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
    public void setIdReport(String idReport) {
        this.idReport = idReport;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public void setIdArea(String idArea) {
        this.idArea = idArea;
    }

    public void setIdCategorie(String idCategorie) {
        this.idCategorie = idCategorie;
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
}
