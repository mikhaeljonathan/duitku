package com.example.duitku.model;

public class Transaction {

    private String mWallet;
    private String mCategory;
    private String mDesc;
    private String mAmount;

    public Transaction(String wallet, String category, String desc, String amount){
        mWallet = wallet;
        mCategory = category;
        mDesc = desc;
        mAmount = amount;
    }

    public String getWallet() {
        return mWallet;
    }

    public String getCategory() {
        return mCategory;
    }

    public String getDesc() {
        return mDesc;
    }

    public String getAmount() {
        return mAmount;
    }

}
