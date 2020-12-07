package com.example.duitku.model;

public class MonthYearHeader extends TransactionHeader {

    String mMonth;
    String mYear;

    public MonthYearHeader(String month, String year){
        mMonth = month;
        mYear = year;
    }

    public String getMonth() {
        return mMonth;
    }

    public String getYear() {
        return mYear;
    }
}
