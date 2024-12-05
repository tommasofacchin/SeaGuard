package com.seaguard.database;
import java.util.Map;

public interface DbModel {
    String getCollectionPath();
    String getId();
    Map<String, Object> toMap ();
}
