package com.example.duitku.model;

public class MonthlyTransaction {

    private String mMonth;
    private String mIncome;
    private String mExpense;

    public MonthlyTransaction(String month, String income, String expense){
        mMonth = month;
        mIncome = income;
        mExpense = expense;
    }

    public String getMonth() {
        return mMonth;
    }

    public String getIncome() {
        return mIncome;
    }

    public String getExpense() {
        return mExpense;
    }
}
