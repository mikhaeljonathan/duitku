package com.example.duitku.model;

public class Budget {

    String mCategory;
    String mAmount;
    String mUsed;
    String mLeft;

    public Budget(String category, String amount, String used, String left){
        mCategory = category;
        mAmount = amount;
        mUsed = used;
        mLeft = left;
    }

    public String getCategory() {
        return mCategory;
    }

    public String getAmount() {
        return mAmount;
    }

    public String getUsed() {
        return mUsed;
    }

    public String getLeft() {
        return mLeft;
    }
}
