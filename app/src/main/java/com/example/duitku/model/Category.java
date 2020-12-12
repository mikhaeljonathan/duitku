package com.example.duitku.model;

public class Category {

    private long mId;
    private String mName;
    private String mType;

    public Category(long id, String name, String type){
        mId = id;
        mName = name;
        mType = type;
    }

    public long getId(){
        return mId;
    }

    public String getName() {
        return mName;
    }

    public String getType() {
        return mType;
    }
}
