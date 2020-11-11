package com.example.duitku.model;

public class CategoryTransaction {

    private String mCategory;
    private String mAmount;

    public CategoryTransaction(String category, String amount){
        mCategory = category;
        mAmount = amount;
    }

    public String getCategory() {
        return mCategory;
    }

    public String getAmount() {
        return mAmount;
    }
}
