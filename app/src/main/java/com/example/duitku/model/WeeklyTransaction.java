package com.example.duitku.model;

public class WeeklyTransaction {

    private int mWeek;
    private String mIntervals;
    private double mIncome;
    private double mExpense;

    public WeeklyTransaction(int week, String intervals, double income, double expense){
        mWeek = week;
        mIntervals = intervals;
        mIncome = income;
        mExpense = expense;
    }

    public int getWeek(){
        return mWeek;
    }

    public String getIntervals() {
        return mIntervals;
    }

    public double getIncome() {
        return mIncome;
    }

    public double getExpense() {
        return mExpense;
    }

}
