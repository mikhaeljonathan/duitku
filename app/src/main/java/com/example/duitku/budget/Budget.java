package com.example.duitku.budget;

import java.util.Date;

public class Budget {

    private long id;
    private double amount;
    private Date startDate;
    private Date endDate;
    private String type;
    private long categoryId;

    public Budget(long id, double amount, Date startDate, Date endDate, String type, long categoryId){
        this.id = id;
        this.amount = amount;
        this.startDate = startDate;
        this.endDate = endDate;
        this.type = type;
        this.categoryId = categoryId;
    }

    public long getId() {return id;}

    public double getAmount() {
        return amount;
    }

    public Date getStartDate(){
        return startDate;
    }

    public Date getEndDate(){
        return endDate;
    }

    public String getType() {
        return type;
    }

    public long getCategoryId() {
        return categoryId;
    }

}
