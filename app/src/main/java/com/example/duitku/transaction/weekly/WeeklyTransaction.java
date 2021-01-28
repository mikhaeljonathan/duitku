package com.example.duitku.transaction.weekly;

public class WeeklyTransaction {

    private final int week;
    private final String intervals;
    private final double income;
    private final double expense;

    public WeeklyTransaction(int week, String intervals, double income, double expense) {
        this.week = week;
        this.intervals = intervals;
        this.income = income;
        this.expense = expense;
    }

    public int getWeek() {
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
