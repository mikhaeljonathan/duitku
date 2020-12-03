package com.example.duitku.controller;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;

import com.example.duitku.database.DuitkuContract.BudgetEntry;
import com.example.duitku.model.Budget;

public class BudgetController {

    private Context mContext;

    public BudgetController(Context context){
        mContext = context;
    }

    public Uri addBudget(Budget budget){

        // taruh di contentvalues
        ContentValues values = new ContentValues();
        values.put(BudgetEntry.COLUMN_STARTDATE, budget.getStartDate());
        values.put(BudgetEntry.COLUMN_ENDDATE, budget.getEndDate());
        values.put(BudgetEntry.COLUMN_CATEGORY_ID, budget.getCategoryId());
        values.put(BudgetEntry.COLUMN_AMOUNT, budget.getAmount());
        String recurring;
        if (budget.isRecurring()){
            recurring = BudgetEntry.RECURRING_YES;
        } else {
            recurring = BudgetEntry.RECURRING_NO;
        }
        values.put(BudgetEntry.COLUMN_RECURRING, recurring);

        // panggil contentresolver yg nanti diterima sama contentprovider
        Uri uri = mContext.getContentResolver().insert(BudgetEntry.CONTENT_URI, values);
        return uri;

    }

}
