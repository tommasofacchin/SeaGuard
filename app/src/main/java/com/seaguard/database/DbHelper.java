package com.seaguard.database;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
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
            taskSnapshot -> imageRef.getDownloadUrl().addOnSuccessListener(path -> onSuccess.accept(path.toString()))
        ).addOnFailureListener(onFailure::accept);
    }

    public static void getCategories(Consumer<List<CategoryModel>> onSuccess, Consumer<Exception> onFailure) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("categories")
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    List<CategoryModel> elems = new ArrayList<>();
                    querySnapshot.forEach(document -> {
                        CategoryModel elem = new CategoryModel(document.getId(), document.getData());
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

    public static ListenerRegistration getReports(String idUser, Consumer<List<ReportModel>> onSuccess, Consumer<Exception> onFailure) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        return db.collection("reports")
                .whereEqualTo("idUser", idUser)
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .addSnapshotListener((values, e) -> {
                    if (e != null) onFailure.accept(e);
                    else if (values != null) {
                        List<ReportModel> reports = new ArrayList<>();
                        values.forEach(document -> reports.add(new ReportModel(document.getId(), document.getData())));
                        onSuccess.accept(reports);
                    }
                });
    }

    public static ListenerRegistration getArticles(Consumer<List<ArticleModel>> onSuccess, Consumer<Exception> onFailure) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        return db.collection("articles")
                .addSnapshotListener((values, e) -> {
                    if (e != null) onFailure.accept(e);
                    else if (values != null) {
                        List<ArticleModel> articles = new ArrayList<>();
                        values.forEach(document -> articles.add(new ArticleModel(document.getId(), document.getData())));
                        onSuccess.accept(articles);
                    }
                });
    }

     public static ListenerRegistration getComments(String idReport, Consumer<List<CommentModel>> onSuccess, Consumer<Exception> onFailure) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        return db.collection("comments")
                .whereEqualTo("idReport", idReport)
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .addSnapshotListener((values, e) -> {
                    if (e != null) onFailure.accept(e);
                    else if (values != null) {
                        List<CommentModel> comments = new ArrayList<>();
                        values.forEach(document -> comments.add(new CommentModel(document.getId(), document.getData())));
                        onSuccess.accept(comments);
                    }
                });
    }

    public static void getEntities (Consumer<List<EntityModel>> onSuccess, Consumer<Exception> onFailure) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("entities")
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    List<EntityModel> entities = new ArrayList<>();
                    querySnapshot.forEach(document -> {
                        entities.add(new EntityModel(document.getId(), document.getData()));
                    });
                    onSuccess.accept(entities);
                })
                .addOnFailureListener(onFailure::accept);
    }

    public static void getEntity (String id, Consumer<EntityModel> onSuccess, Consumer<Exception> onFailure) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("entities")
                .document(id)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        onSuccess.accept(new EntityModel(documentSnapshot.getId(), documentSnapshot.getData()));
                    }
                })
                .addOnFailureListener(onFailure::accept);
    }

}
