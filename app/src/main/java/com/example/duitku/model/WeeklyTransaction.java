package com.example.duitku.model;

public class WeeklyTransaction {

    private int week;
    private String intervals;
    private double income;
    private double expense;

    public WeeklyTransaction(int week, String intervals, double income, double expense){
        this.week = week;
        this.intervals = intervals;
        this.income = income;
        this.expense = expense;
    }

    public int getWeek(){
        return week;
    }

    public String getIntervals() {
        return intervals;
    }

    public double getIncome() {
        return income;
    }

    public double getExpense() {
        return expense;
    }

}
