package com.example.duitku.budget;

import java.util.Date;

public class Budget {

    private final long id;
    private double amount;
    private double used;
    private Date startDate;
    private Date endDate;
    private String type;
    private long categoryId;

    public Budget(long id, double amount, double used, Date startDate, Date endDate, String type, long categoryId){
        this.id = id;
        this.amount = amount;
        this.used = used;
        this.startDate = startDate;
        this.endDate = endDate;
        this.type = type;
        this.categoryId = categoryId;
    }

    public long getId() {return id;}

    public double getAmount() {
        return amount;
    }

    public double getUsed() {
        return used;
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

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setUsed(double used) {
        this.used = used;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setCategoryId(long categoryId) {
        this.categoryId = categoryId;
    }
}
