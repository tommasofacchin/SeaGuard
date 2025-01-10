package com.seaguard.database;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class DbHelper {

    public static void add (DbModel elem, Consumer<DocumentReference> onSuccess, Consumer<Exception> onFailure) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(elem.getCollectionPath())
            .add(elem.toMap())
            .addOnSuccessListener(onSuccess::accept)
            .addOnFailureListener(onFailure::accept);
    }

    public static void update (DbModel elem, Consumer<Exception> onFailure) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(elem.getCollectionPath())
            .document(elem.getId())
            .set(elem.toMap())
            .addOnFailureListener(onFailure::accept);
    }

    public static void delete (DbModel elem, Consumer<Exception> onFailure) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(elem.getCollectionPath())
            .document(elem.getId())
            .delete()
            .addOnFailureListener(onFailure::accept);
    }

    public static void uploadImage (byte[] imageBytes, Consumer<String> onSuccess, Consumer<Exception> onFailure) {
        StorageReference imageRef = FirebaseStorage.getInstance().getReference()
                .child("images/" + System.currentTimeMillis() + ".png");

        UploadTask uploadTask = imageRef.putBytes(imageBytes);
        uploadTask.addOnSuccessListener(
            taskSnapshot -> {
                imageRef.getDownloadUrl().addOnSuccessListener(path -> onSuccess.accept(path.toString()));
            }
        ).addOnFailureListener(onFailure::accept);
    }

    public static void getCategories (Consumer<List<CategoryModel>> onSuccess, Consumer<Exception> onFailure) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("categories")
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    List<CategoryModel> elems = new ArrayList<>();
                    querySnapshot.forEach(document -> {
                        CategoryModel elem = new CategoryModel(document.getData());
                        elem.setId(document.getId());
                        elems.add(elem);
                    });
                    onSuccess.accept(elems);
                })
                .addOnFailureListener(onFailure::accept);
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
                        ReportModel report = new ReportModel(document.getId(), document.getData());
                        report.setIdReport(document.getId()); // Imposta l'ID del documento
                        reports.add(report);
                    });
                    onSuccess.accept(reports); // Chiamata al callback con i dati
                })
                .addOnFailureListener(onFailure::accept); // Callback per gestire errori
    }

    public static void getReports(String idUser, Consumer<List<ReportModel>> onSuccess, Consumer<Exception> onFailure) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("reports") // Nome della collection
                .whereEqualTo("idUser", idUser)
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    List<ReportModel> reports = new ArrayList<>();
                    querySnapshot.forEach(document -> {
                        // Converti il documento in un ReportModel
                        ReportModel report = new ReportModel(document.getId(), document.getData());
                        report.setIdReport(document.getId()); // Imposta l'ID del documento
                        reports.add(report);
                    });
                    onSuccess.accept(reports); // Chiamata al callback con i dati
                })
                .addOnFailureListener(onFailure::accept); // Callback per gestire errori
    }

}
