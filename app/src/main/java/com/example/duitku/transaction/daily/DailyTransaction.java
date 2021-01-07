package com.example.duitku.transaction.daily;

public class DailyTransaction {

    private final int date;
    private final String day;
    private final double income;
    private final double expense;

    public DailyTransaction(int date, String day, double income, double expense){
        this.date = date;
        this.day = day;
        this.income = income;
        this.expense = expense;
    }

    public int getDate() {
        return date;
    }

    public String getDay() {
        return day;
    }

    public double getIncome() {
        return income;
    }

    public double getExpense() {
        return expense;
    }

}
