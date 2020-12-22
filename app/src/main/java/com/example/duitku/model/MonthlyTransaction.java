package com.example.duitku.model;

public class MonthlyTransaction {

    private int month;
    private double income;
    private double expense;

    public MonthlyTransaction(int month, double income, double expense){
        this.month = month;
        this.income = income;
        this.expense = expense;
    }

    public int getMonth() {
        return month;
    }

    public double getIncome() {
        return income;
    }

    public double getExpense() {
        return expense;
    }
}
