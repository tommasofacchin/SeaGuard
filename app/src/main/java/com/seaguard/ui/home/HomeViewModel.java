package com.seaguard.ui.home;

import androidx.lifecycle.ViewModel;

import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.util.function.Supplier;

public class HomeViewModel extends ViewModel {
    private MyLocationNewOverlay location;
    private Supplier<MyLocationNewOverlay> callBack;
    private boolean permissionsRequested;

    public HomeViewModel() {
        this.permissionsRequested = false;
    }

    public void setCallBack (Supplier<MyLocationNewOverlay> callBack) {
        this.callBack = callBack;
    }

    public void setLocation() {
        permissionsRequested = true;
        if(callBack != null) location = callBack.get();
    }

    public MyLocationNewOverlay getLocation() {
        return location;
    }

    public boolean permissionsRequested() {
        return permissionsRequested;
    }
}