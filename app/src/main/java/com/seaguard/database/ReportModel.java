package com.seaguard.database;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.firebase.Timestamp;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Map;
import java.util.HashMap;

public class ReportModel implements DbModel, Parcelable {

    private String idReport;
    private String idUser;
    private String area;
    private double latitude;
    private double longitude;
    private String category;
    private Timestamp timestamp;
    private String description;
    private int urgency;
    private String image;

    public ReportModel(String idUser, String area, double latitude, double longitude, String category, Timestamp timestamp, String description, int urgency, String image) {
        this.idUser = idUser;
        this.area = area;
        this.latitude = latitude;
        this.longitude = longitude;
        this.category = category;
        this.timestamp = timestamp;
        this.description = description;
        this.urgency = urgency;
        this.image = image;
    }

    public ReportModel(String id, Map<String, Object> report) {
        this.idReport = id;
        this.idUser = report.get("idUser") instanceof String ? (String) report.get("idUser") : "";
        this.area = report.get("area") instanceof String ? (String) report.get("area") : "";
        this.latitude = report.get("latitude") instanceof Double? (double) report.get("latitude") : 0.0;
        this.longitude = report.get("longitude") instanceof Double? (double) report.get("longitude") : 0.0;
        this.category = report.get("category") instanceof String ? (String) report.get("category") : "";
        this.timestamp = report.get("timestamp") instanceof Timestamp ? (Timestamp) report.get("timestamp") : null;
        this.description = report.get("description") instanceof String ? (String) report.get("description") : "";
        this.urgency = report.get("urgency") instanceof Long ? ((Long) report.get("urgency")).intValue() : 0;
        this.image = report.get("image") instanceof String ? (String) report.get("image") : "";
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
        Map<String, Object> map = new HashMap<>();
        map.put("idUser", idUser);
        map.put("area", area);
        map.put("latitude", latitude);
        map.put("longitude", longitude);
        map.put("category", category);
        map.put("urgency", urgency);
        map.put("timestamp", timestamp);
        map.put("description", description);
        map.put("image", image);
        return map;
    }
    public String getIdUser() {
        return idUser;
    }

    public String getArea() {
        return area;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getCategory() {
        return category;
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

    public void setArea(String area) {
        this.area = area;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(idReport);
        dest.writeString(idUser);
        dest.writeString(area);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
        dest.writeString(category);
        dest.writeParcelable(timestamp, flags);
        dest.writeString(description);
        dest.writeInt(urgency);
        dest.writeString(image);
    }

    protected ReportModel(Parcel in) {
        idReport = in.readString();
        idUser = in.readString();
        area = in.readString();
        latitude = in.readDouble();
        longitude = in.readDouble();
        category = in.readString();
        timestamp = in.readParcelable(Timestamp.class.getClassLoader());
        description = in.readString();
        urgency = in.readInt();
        image = in.readString();
    }

    public static final Creator<ReportModel> CREATOR = new Creator<>() {
        @Override
        public ReportModel createFromParcel(Parcel in) {
            return new ReportModel(in);
        }

        @Override
        public ReportModel[] newArray(int size) {
            return new ReportModel[size];
        }
    };

}
