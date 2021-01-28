package com.example.duitku.budget;

import java.util.Date;

public class Budget {

    private long _id;
    private double budget_amount;
    private double budget_used;
    private Date budget_startdate;
    private Date budget_enddate;
    private String budget_type;
    private long category_id;

    public Budget() {

    }

    public Budget(long _id, double budget_amount, double budget_used, Date budget_startdate, Date budget_enddate, String budget_type, long category_id) {
        this._id = _id;
        this.budget_amount = budget_amount;
        this.budget_used = budget_used;
        this.budget_startdate = budget_startdate;
        this.budget_enddate = budget_enddate;
        this.budget_type = budget_type;
        this.category_id = category_id;
    }

    public void set_id(long _id) {
        this._id = _id;
    }

    public long get_id() {
        return _id;
    }

    public double getBudget_amount() {
        return budget_amount;
    }

    public double getBudget_used() {
        return budget_used;
    }

    public Date getBudget_startdate() {
        return budget_startdate;
    }

    public Date getBudget_enddate() {
        return budget_enddate;
    }

    public String getBudget_type() {
        return budget_type;
    }

    public long getCategory_id() {
        return category_id;
    }

    public void setBudget_amount(double budget_amount) {
        this.budget_amount = budget_amount;
    }

    public void setBudget_used(double budget_used) {
        this.budget_used = budget_used;
    }

    public void setBudget_startdate(Date budget_startdate) {
        this.budget_startdate = budget_startdate;
    }

    public void setBudget_enddate(Date budget_enddate) {
        this.budget_enddate = budget_enddate;
    }

    public void setBudget_type(String budget_type) {
        this.budget_type = budget_type;
    }

    public void setCategory_id(long category_id) {
        this.category_id = category_id;
    }
}
