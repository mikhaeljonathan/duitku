package com.example.duitku.report;

public class ExpenseReport {

    private String mCategoryId;
    private String mAmount;
    private String mPercentage;

    public ExpenseReport(String mCategoryId, String mAmount, String mPercentage) {
        this.mCategoryId = mCategoryId;
        this.mAmount = mAmount;
        this.mPercentage = mPercentage;
    }

    public String getmCategoryId() {
        return mCategoryId;
    }

    public String getmAmount() {
        return mAmount;
    }

    public String getmPercentage() {
        return mPercentage;
    }
}
