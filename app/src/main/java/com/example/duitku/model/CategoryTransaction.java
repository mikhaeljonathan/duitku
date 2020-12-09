package com.example.duitku.model;

import java.util.ArrayList;
import java.util.List;

public class CategoryTransaction {

    private List<Long> transactions;
    private long mCategoryId;
    private double mAmount;

    public CategoryTransaction(long categoryId, double amount){
        transactions = new ArrayList<>();
        mCategoryId = categoryId;
        mAmount = amount;
    }

    public void addTransaction(long transactionId){
        transactions.add(transactionId);
    }

    public void addAmount(double amount){
        mAmount += amount;
    }

    public long getCategoryId() {
        return mCategoryId;
    }

    public double getAmount() {
        return mAmount;
    }

    public List<Long> getTransactions (){
        return transactions;
    }
}
