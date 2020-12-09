package com.example.duitku.model;

public class MonthlyTransaction {

    private int mMonth;
    private double mIncome;
    private double mExpense;

    public MonthlyTransaction(int month, double income, double expense){
        mMonth = month;
        mIncome = income;
        mExpense = expense;
    }

    public int getMonth() {
        return mMonth;
    }

    public double getIncome() {
        return mIncome;
    }

    public double getExpense() {
        return mExpense;
    }
}
