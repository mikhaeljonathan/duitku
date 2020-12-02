package com.example.duitku.model;

public class Transaction {

    private String mDate;
    private String mWallet;
    private String mCategory;
    private String mDesc;
    private double mAmount;

    public Transaction(String date, String wallet, String category, double amount, String desc){
        mDate = date;
        mWallet = wallet;
        mCategory = category;
        mDesc = desc;
        mAmount = amount;
    }

    public String getDate() {
        return mDate;
    }

    public void setDate(String Date) {
        this.mDate = mDate;
    }

    public String getWallet() {
        return mWallet;
    }

    public void setWallet(String Wallet) {
        this.mWallet = mWallet;
    }

    public String getCategory() {
        return mCategory;
    }

    public void setCategory(String Category) {
        this.mCategory = mCategory;
    }

    public String getDesc() {
        return mDesc;
    }

    public void setDesc(String mDesc) {
        this.mDesc = mDesc;
    }

    public double getAmount() {
        return mAmount;
    }

    public void setAmount(double mAmount) {
        this.mAmount = mAmount;
    }
}
