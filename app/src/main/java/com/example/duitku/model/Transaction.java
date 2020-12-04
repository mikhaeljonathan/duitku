package com.example.duitku.model;

public class Transaction {

    private String mDate;
    private long mWalletId;
    private long mWalletDestId;
    private long mCategoryId;
    private String mDesc;
    private double mAmount;

    public Transaction(String date, long walletId, long walletDestId, long categoryId, double amount, String desc){
        mDate = date;
        mWalletId = walletId;
        mCategoryId = categoryId;
        mWalletDestId = walletDestId;
        mDesc = desc;
        mAmount = amount;
    }

    public String getDate() {
        return mDate;
    }

    public long getWalletId() {
        return mWalletId;
    }

    public long getWalletDestId() {
        return mWalletDestId;
    }

    public long getCategoryId() {
        return mCategoryId;
    }

    public String getDesc() {
        return mDesc;
    }

    public double getAmount() {
        return mAmount;
    }

}
