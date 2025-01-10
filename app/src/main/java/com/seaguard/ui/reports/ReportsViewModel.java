package com.seaguard.ui.reports;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.seaguard.database.DbHelper;
import com.seaguard.database.ReportModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ReportsViewModel extends ViewModel {


    private static final String TAG = "ReportsViewModel";
    private final MutableLiveData<String> mText;
    private MutableLiveData<List<ReportModel>> reports = new MutableLiveData<>();

    public ReportsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Segnalazioni");

        reports = new MutableLiveData<>();
        loadReports();
    }

    /*
    private final MutableLiveData<List<Map<String, Object>>> reportsLiveData = new MutableLiveData<>();

    private void fetchReports() {
        CollectionReference reportsCollection = firestore.collection("reports");

        reportsCollection.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot querySnapshot = task.getResult();
                if (querySnapshot != null) {
                    List<Map<String, Object>> reports = new ArrayList<>();
                    querySnapshot.forEach(document -> {
                        try {
                            Map<String, Object> reportData = document.getData();
                            Log.d(TAG, "Documento ricevuto: " + reportData);
                            reports.add(reportData);
                        } catch (Exception e) {
                            Log.e(TAG, "Errore durante l'elaborazione del documento: " + document.getId(), e);
                        }
                    });
                    reportsLiveData.setValue(reports);
                    Log.d(TAG, "Lista dei report aggiornata: " + reports);
                } else {
                    Log.w(TAG, "QuerySnapshot è null");
                }
            } else {
                Exception e = task.getException();
                Log.e(TAG, "Errore nel recupero dei report", e);
                reportsLiveData.setValue(null);
            }
        });
    }

    public LiveData<List<Map<String, Object>>> getReports() {
        return reportsLiveData;
    }
     */

    public LiveData<List<ReportModel>> getReports() {
       return reports;
    }

    public void loadReports () {
        DbHelper.getReports(
                FirebaseAuth.getInstance().getUid(),
                reports::setValue,
                (e) -> {}
        );
    }

}