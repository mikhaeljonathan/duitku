package com.example.duitku.model;

public class Category {

    private String mName;
    private String mType;

    public Category(String name, String type){
        mName = name;
        mType = type;
    }

    public String getName() {
        return mName;
    }

    public String getType() {
        return mType;
    }
}
