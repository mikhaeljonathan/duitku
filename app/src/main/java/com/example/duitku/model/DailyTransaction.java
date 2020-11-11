package com.example.duitku.model;

public class DailyTransaction {

    private int mDate;
    private String mDay;
    private String mIncome;
    private String mExpense;

    public DailyTransaction(int date, String day, String income, String expense){
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

    public String getIncome() {
        return mIncome;
    }

    public String getExpense() {
        return mExpense;
    }

}
