package com.example.duitku.budget;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.example.duitku.category.Category;
import com.example.duitku.category.CategoryController;
import com.example.duitku.database.DuitkuContract.BudgetEntry;
import com.example.duitku.main.Utility;
import com.example.duitku.transaction.Transaction;
import com.example.duitku.transaction.TransactionController;

import java.util.Date;
import java.util.List;

public class BudgetController {

    public static final String[] budgetPeriod = {"Monthly", "3 Month (Quarter)", "Yearly"};
    public static final String[] budgetType = {BudgetEntry.TYPE_MONTH, BudgetEntry.TYPE_3MONTH, BudgetEntry.TYPE_YEAR};

    private Context context;

    public BudgetController(Context context){
        this.context = context;
    }

    public Uri addBudget(Budget budget){
        initialUsed(budget);
        ContentValues values = convertBudgetToContentValues(budget);
        Uri uri = context.getContentResolver().insert(BudgetEntry.CONTENT_URI, values);
        return uri;
    }

    private ContentValues convertBudgetToContentValues(Budget budget){
        String startDate = null;
        String endDate = null;
        if (budget.getStartDate() != null) {
            startDate = Utility.convertDateToString(budget.getStartDate());
            endDate = Utility.convertDateToString(budget.getEndDate());
        }

        String type = budget.getType();
        if (startDate != null) type = null;

        ContentValues ret = new ContentValues();
        ret.put(BudgetEntry.COLUMN_AMOUNT, budget.getAmount());
        ret.put(BudgetEntry.COLUMN_USED, budget.getUsed());
        ret.put(BudgetEntry.COLUMN_STARTDATE, startDate);
        ret.put(BudgetEntry.COLUMN_ENDDATE, endDate);
        ret.put(BudgetEntry.COLUMN_TYPE, type);
        ret.put(BudgetEntry.COLUMN_CATEGORY_ID, budget.getCategoryId());

        return ret;
    }

    private void initialUsed(Budget budget){
        TransactionController transactionController = new TransactionController(context);
        List<Transaction> transactions = transactionController.getTransactionsByBudget(budget);

        double used = 0;
        for (Transaction transaction: transactions){
            used += transaction.getAmount();
        }

        budget.setUsed(used);
    }

    public String[] getFullProjection(){
        String[] projection = new String[]{BudgetEntry.COLUMN_ID,
                BudgetEntry.COLUMN_STARTDATE,
                BudgetEntry.COLUMN_ENDDATE,
                BudgetEntry.COLUMN_AMOUNT,
                BudgetEntry.COLUMN_USED,
                BudgetEntry.COLUMN_TYPE,
                BudgetEntry.COLUMN_CATEGORY_ID};
        return projection;
    }

    public Budget convertCursorToBudget(Cursor data){

        int idColumnIndex = data.getColumnIndex(BudgetEntry.COLUMN_ID);
        int amountColumnIndex = data.getColumnIndex(BudgetEntry.COLUMN_AMOUNT);
        int usedColumnIndex = data.getColumnIndex(BudgetEntry.COLUMN_USED);
        int startDateColumnIndex = data.getColumnIndex(BudgetEntry.COLUMN_STARTDATE);
        int endDateColumnIndex = data.getColumnIndex(BudgetEntry.COLUMN_ENDDATE);
        int typeColumnIndex = data.getColumnIndex(BudgetEntry.COLUMN_TYPE);
        int categoryIdColumnIndex = data.getColumnIndex(BudgetEntry.COLUMN_CATEGORY_ID);

        long id = data.getLong(idColumnIndex);
        double amount = data.getDouble(amountColumnIndex);
        double used = data.getDouble(usedColumnIndex);
        Date startDate = Utility.parseDate(data.getString(startDateColumnIndex));
        Date endDate = Utility.parseDate(data.getString(endDateColumnIndex));
        String type = data.getString(typeColumnIndex);
        long categoryId = data.getLong(categoryIdColumnIndex);

        Budget ret = new Budget(id, amount, used, startDate, endDate, type, categoryId);
        return ret;
    }

    public int updateBudget(Budget budget){
        ContentValues values = convertBudgetToContentValues(budget);
        String id = Long.toString(budget.getId());
        int rowsUpdated = context.getContentResolver().update(Uri.withAppendedPath(BudgetEntry.CONTENT_URI, id), values, null, null);
        return rowsUpdated;
    }

    public int updateBudgetFromTransaction(Transaction transaction){
        Budget budget = getBudgetByCategoryId(transaction.getCategoryId());

        if (budget == null) return 0;
        budget.setUsed(budget.getUsed() + transaction.getAmount());

        int rowsUpdated = updateBudget(budget);
        return rowsUpdated;
    }

    public Budget getBudgetByCategoryId(long categoryId){
        String[] projection = getFullProjection();
        String selection = BudgetEntry.COLUMN_CATEGORY_ID + " = ?";
        String[] selectionArgs = new String[]{Long.toString(categoryId)};

        Budget ret = null;
        Cursor data = context.getContentResolver().query(BudgetEntry.CONTENT_URI, projection, selection, selectionArgs, null);
        if (data.moveToFirst()){
            ret = convertCursorToBudget(data);
        }
        return ret;
    }

}
