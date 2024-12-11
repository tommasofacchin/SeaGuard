package com.seaguard.ui.home;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.osmdroid.util.GeoPoint;


public class HomeViewModel extends ViewModel {
    private Runnable callBack;
    private boolean isFirstRun;
    private GeoPoint centerPoint;
    private Integer zoomLevel;

    public HomeViewModel() {
        this.isFirstRun = true;
        centerPoint = new GeoPoint(41.8902, 12.4922); // Rome
        zoomLevel = 16;
    }

    public void setCallBack (Runnable callBack) {
        this.callBack = callBack;
    }

    public void setLocation() {
        if(callBack != null) callBack.run();
        if(isFirstRun) isFirstRun = false;
    }

    public boolean isFirstRun () {
        return isFirstRun;
    }

    public GeoPoint getCenterPoint() {
        return centerPoint;
    }

    public void setCenterPoint(@NonNull GeoPoint point) {
        centerPoint.setCoords(point.getLatitude(), point.getLongitude());
    }

    public int getZoomLevel() {
        return zoomLevel;
    }

    public void setZoomLevel(int zoom) {
        zoomLevel = zoom;
    }

}