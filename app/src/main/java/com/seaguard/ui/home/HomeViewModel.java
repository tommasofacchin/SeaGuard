package com.seaguard.ui.home;

import androidx.lifecycle.ViewModel;

import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.util.function.Supplier;

public class HomeViewModel extends ViewModel {
    private Runnable callBack;
    private boolean permissionsRequested;

    public HomeViewModel() {
        this.permissionsRequested = false;
    }

    public void setCallBack (Runnable callBack) {
        this.callBack = callBack;
    }

    public void setLocation() {
        permissionsRequested = true;
        if(callBack != null) callBack.run();
    }

    public boolean permissionsRequested() {
        return permissionsRequested;
    }

}