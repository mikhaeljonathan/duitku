package com.example.duitku.model;

public class Budget {

    String mStartDate;
    String mEndDate;
    long mCategoryId;
    double mAmount;
    String mUsed;
    boolean mRecurring;

    public Budget(String startDate, String endDate, long category, double amount, boolean recurring){
        mStartDate = startDate;
        mEndDate = endDate;
        mCategoryId = category;
        mAmount = amount;
        mRecurring = recurring;
    }

    public String getStartDate(){
        return mStartDate;
    }

    public String getEndDate(){
        return mEndDate;
    }

    public long getCategoryId() {
        return mCategoryId;
    }

    public double getAmount() {
        return mAmount;
    }

    public String getUsed() {
        return mUsed;
    }

    public boolean isRecurring(){
        return mRecurring;
    }

}
