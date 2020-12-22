package com.example.duitku.model;

public class DailyTransaction {

    private int date;
    private String day;
    private double income;
    private double expense;

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
