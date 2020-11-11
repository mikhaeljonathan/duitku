package com.example.duitku.model;

public class WeeklyTransaction {

    private String mIntervals;
    private String mIncome;
    private String mExpense;

    public WeeklyTransaction(String intervals, String income, String expense){
        mIntervals = intervals;
        mIncome = income;
        mExpense = expense;
    }

    public String getIntervals() {
        return mIntervals;
    }

    public String getIncome() {
        return mIncome;
    }

    public String getExpense() {
        return mExpense;
    }

}
