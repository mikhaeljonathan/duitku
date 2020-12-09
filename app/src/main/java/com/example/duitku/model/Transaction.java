package com.example.duitku.model;

import java.util.Date;

public class Transaction {

    private long mId;
    private Date mDate;
    private long mWalletId;
    private long mWalletDestId;
    private long mCategoryId;
    private String mDesc;
    private double mAmount;

    public Transaction(long id, Date date, long walletId, long walletDestId, long categoryId, double amount, String desc){
        mId = id;
        mDate = date;
        mWalletId = walletId;
        mCategoryId = categoryId;
        mWalletDestId = walletDestId;
        mDesc = desc;
        mAmount = amount;
    }

    public long getId (){
        return mId;
    }

    public Date getDate() {
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
