package com.seaguard.database;

import java.util.Map;

public class CategoryModel implements DbModel {
    private String id;
    private String category;

    public CategoryModel(String category) {
        this.category = category;
    }

    public CategoryModel(Map<String, Object> elem) {
        this.id = (String) elem.get("id");
        this.category = (String) elem.get("category");
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setId(String id) {
        this.id = id;
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
        return null;
    }
}
