package com.example.duitku.transaction;

import java.io.Serializable;
import java.util.Date;

public class Transaction implements Serializable { // serializable biar bisa dipass ke activity

    private long id;
    private long walletId;
    private long walletDestId;
    private long categoryId;
    private String desc;
    private Date date;
    private double amount;

    public Transaction(long id, long walletId, long walletDestId, long categoryId, String desc, Date date, double amount){
        this.id = id;
        this.walletId = walletId;
        this.walletDestId = walletDestId;
        this.categoryId = categoryId;
        this.desc = desc;
        this.date = date;
        this.amount = amount;
    }

    public long getId (){
        return id;
    }

    public Date getDate() {
        return date;
    }

    public long getWalletId() {
        return walletId;
    }

    public long getWalletDestId() {
        return walletDestId;
    }

    public long getCategoryId() {
        return categoryId;
    }

    public String getDesc() {
        return desc;
    }

    public double getAmount() {
        return amount;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setWalletId(long walletId) {
        this.walletId = walletId;
    }

    public void setWalletDestId(long walletDestId) {
        this.walletDestId = walletDestId;
    }

    public void setCategoryId(long categoryId) {
        this.categoryId = categoryId;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
