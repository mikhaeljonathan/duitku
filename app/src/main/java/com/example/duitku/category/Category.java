package com.example.duitku.category;

public class Category {

    private long id;
    private String name;
    private String type;

    public Category(long id, String name, String type){
        this.id = id;
        this.name = name;
        this.type = type;
    }

    public long getId(){
        return id;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(String type) {
        this.type = type;
    }
}
