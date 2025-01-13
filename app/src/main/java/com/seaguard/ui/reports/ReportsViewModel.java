package com.seaguard.ui.reports;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.seaguard.database.DbHelper;
import com.seaguard.database.ReportModel;

import java.util.List;

public class ReportsViewModel extends ViewModel {
    private final MutableLiveData<List<ReportModel>> reports;

    public ReportsViewModel() {
        reports = new MutableLiveData<>();
        loadReports();
    }

    public LiveData<List<ReportModel>> getReports() {
       return reports;
    }

    private void loadReports () {
        DbHelper.getReports(
                FirebaseAuth.getInstance().getUid(),
                reports::setValue,
                e -> {}
        );
    }

}