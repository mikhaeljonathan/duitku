package com.example.duitku.model;

public class DailyTransaction extends TransactionHeader{

    private int mDate;
    private String mDay;
    private double mIncome;
    private double mExpense;

    public DailyTransaction(int date, String day, double income, double expense){
        mDate = date;
        mDay = day;
        mIncome = income;
        mExpense = expense;
    }

    public int getDate() {
        return mDate;
    }

    public String getDay() {
        return mDay;
    }

    public double getIncome() {
        return mIncome;
    }

    public double getExpense() {
        return mExpense;
    }

}
