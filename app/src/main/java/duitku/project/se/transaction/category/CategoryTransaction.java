package duitku.project.se.transaction.category;

import duitku.project.se.transaction.Transaction;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CategoryTransaction implements Serializable { // serializable biar bisa dipass ke activity

    private final List<Transaction> transactions;
    private final long categoryId;
    private double amount;

    public CategoryTransaction(long categoryId, double amount) {
        transactions = new ArrayList<>();
        this.categoryId = categoryId;
        this.amount = amount;
    }

    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
        addAmount(transaction.getTransaction_amount());
    }

    public void addAmount(double amount) {
        this.amount += amount;
    }

    public long getCategoryId() {
        return categoryId;
    }

    public double getAmount() {
        return amount;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
