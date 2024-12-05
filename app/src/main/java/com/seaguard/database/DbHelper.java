package com.seaguard.database;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
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

}
