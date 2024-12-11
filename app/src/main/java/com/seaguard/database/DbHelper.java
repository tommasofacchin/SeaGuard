package com.seaguard.database;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

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

    public static void uploadImage (byte[] imageBytes, BiConsumer<String, Exception> callBack) {
        StorageReference imageRef = FirebaseStorage.getInstance().getReference()
                .child("images/" + System.currentTimeMillis() + ".png");

        UploadTask uploadTask = imageRef.putBytes(imageBytes);
        uploadTask.addOnSuccessListener(
            taskSnapshot -> {
                imageRef.getDownloadUrl().addOnSuccessListener(path ->callBack.accept(path.toString(), null));
            }
        ).addOnFailureListener(e-> callBack.accept(null, e));
    }

    public static void getCategories (BiConsumer<List<CategoryModel>, Exception> callBack) {
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
                    callBack.accept(elems, null);
                })
                .addOnFailureListener(e -> callBack.accept(null, e));
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
