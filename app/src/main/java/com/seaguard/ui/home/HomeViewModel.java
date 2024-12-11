package com.seaguard.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.osmdroid.util.GeoPoint;


public class HomeViewModel extends ViewModel {
    private Runnable callBack;
    private boolean isFirstRun;
    private final MutableLiveData<GeoPoint> centerPoint;
    private final MutableLiveData<Integer> zoomLevel;

    public HomeViewModel() {
        this.isFirstRun = true;
        centerPoint = new MutableLiveData<>();
        zoomLevel = new MutableLiveData<>();
        centerPoint.setValue(new GeoPoint(41.8902, 12.4922)); // Rome
        zoomLevel.setValue(16);
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

    public LiveData<GeoPoint> getCenterPoint() {
        return centerPoint;
    }

    public void setCenterPoint(GeoPoint point) {
        centerPoint.setValue(point);
    }

    public LiveData<Integer> getZoomLevel() {
        return zoomLevel;
    }

    public void setZoomLevel(int zoom) {
        zoomLevel.setValue(zoom);
    }

}