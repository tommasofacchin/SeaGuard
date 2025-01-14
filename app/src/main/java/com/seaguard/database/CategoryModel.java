package com.seaguard.database;

import java.util.HashMap;
import java.util.Map;

public class CategoryModel implements DbModel {
    private String id;
    private String category;

    public CategoryModel(String category) {
        this.category = category;
    }

    public CategoryModel(String id, Map<String, Object> elem) {
        this.id = id;
        this.category = elem.get("category") instanceof String ? (String) elem.get("category") : "";
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public String getCollectionPath() {
        return "categories";
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("category", category);
        return map;
    }

}
