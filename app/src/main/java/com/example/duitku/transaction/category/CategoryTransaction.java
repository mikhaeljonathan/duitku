package com.example.duitku.transaction.category;

import com.example.duitku.transaction.Transaction;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CategoryTransaction implements Serializable { // serializable biar bisa dipass ke activity

    private List<Transaction> transactions;
    private long categoryId;
    private double amount;

    public CategoryTransaction(long categoryId, double amount){
        transactions = new ArrayList<>();
        this.categoryId = categoryId;
        this.amount = amount;
    }

    public void addTransaction(Transaction transaction){
        transactions.add(transaction);
        addAmount(transaction.getAmount());
    }

    public void addAmount(double amount){
        this.amount += amount;
    }

    public long getCategoryId() {
        return categoryId;
    }

    public double getAmount() {
        return amount;
    }

    public List<Transaction> getTransactions (){
        return transactions;
    }
}