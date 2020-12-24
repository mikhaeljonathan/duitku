package com.example.duitku.budget;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;

import com.example.duitku.database.DuitkuContract.BudgetEntry;

import java.text.DateFormat;

public class BudgetController {

    public static final String[] budgetPeriod = {"Monthly", "3 Month (Quarter)", "Yearly"};
    public static final String[] budgetType = {BudgetEntry.TYPE_MONTH, BudgetEntry.TYPE_3MONTH, BudgetEntry.TYPE_YEAR};

    private Context context;

    public BudgetController(Context context){
        this.context = context;
    }

    public Uri addBudget(Budget budget){
        ContentValues values = convertBudgetToContentValues(budget);
        Uri uri = context.getContentResolver().insert(BudgetEntry.CONTENT_URI, values);
        return uri;
    }

    public ContentValues convertBudgetToContentValues(Budget budget){
        String startDate = null;
        String endDate = null;
        if (budget.getStartDate() != null) {
            startDate = DateFormat.getDateInstance(DateFormat.SHORT).format(budget.getStartDate());
            endDate = DateFormat.getDateInstance(DateFormat.SHORT).format(budget.getEndDate());
        }

        ContentValues ret = new ContentValues();
        ret.put(BudgetEntry.COLUMN_AMOUNT, budget.getAmount());
        ret.put(BudgetEntry.COLUMN_STARTDATE, startDate);
        ret.put(BudgetEntry.COLUMN_ENDDATE, endDate);
        ret.put(BudgetEntry.COLUMN_TYPE, budget.getType());
        ret.put(BudgetEntry.COLUMN_CATEGORY_ID, budget.getCategoryId());

        return ret;
    }

    public String[] getFullProjection(){
        String[] projection = new String[]{BudgetEntry.COLUMN_ID,
                BudgetEntry.COLUMN_STARTDATE,
                BudgetEntry.COLUMN_ENDDATE,
                BudgetEntry.COLUMN_AMOUNT,
                BudgetEntry.COLUMN_TYPE,
                BudgetEntry.COLUMN_CATEGORY_ID};
        return projection;
    }

}
