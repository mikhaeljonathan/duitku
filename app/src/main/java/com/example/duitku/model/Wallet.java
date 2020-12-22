package com.example.duitku.model;

public class Wallet {

    private long id;
    private String name;
    private double amount;
    private String description;

    public Wallet(long id, String name, double amount, String description){
        this.id = id;
        this.name = name;
        this.amount = amount;
        this.description = description;
    }

    public long getId(){
        return id;
    }

    public String getName() {
        return name;
    }

    public double getAmount() {
        return amount;
    }

    public String getDescription() {
        return description;
    }
}
