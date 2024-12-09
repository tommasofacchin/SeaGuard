package com.seaguard.database;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.seaguard.ui.reports.ReportsViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class DbHelper {

    public static void add (DbModel elem, BiConsumer<DocumentReference, Exception> callBack) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(elem.getCollectionPath())
            .add(elem.toMap())
            .addOnSuccessListener(docRef -> callBack.accept(docRef, null))
            .addOnFailureListener(e -> callBack.accept(null, e));
    }

    public static void update (DbModel elem, Consumer<Exception> callBack) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(elem.getCollectionPath())
            .document(elem.getId())
            .set(elem.toMap())
            .addOnSuccessListener(unused -> callBack.accept(null))
            .addOnFailureListener(callBack::accept);
    }

    public static void delete (DbModel elem, Consumer<Exception> callBack) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(elem.getCollectionPath())
            .document(elem.getId())
            .delete()
            .addOnSuccessListener(unused -> callBack.accept(null))
            .addOnFailureListener(callBack::accept);
    }

    // Tommaso
    public static void getReports(Consumer<List<ReportModel>> onSuccess, Consumer<Exception> onFailure) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("reports") // Nome della collection
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    List<ReportModel> reports = new ArrayList<>();
                    querySnapshot.forEach(document -> {
                        // Converti il documento in un ReportModel
                        ReportModel report = new ReportModel(document.getData());
                        if (report != null) {
                            report.setIdReport(document.getId()); // Imposta l'ID del documento
                            reports.add(report);
                        }
                    });
                    onSuccess.accept(reports); // Chiamata al callback con i dati
                })
                .addOnFailureListener(onFailure::accept); // Callback per gestire errori
    }







}
