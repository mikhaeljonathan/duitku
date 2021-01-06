package com.example.duitku.budget;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.example.duitku.category.Category;
import com.example.duitku.category.CategoryController;
import com.example.duitku.database.DuitkuContract;
import com.example.duitku.database.DuitkuContract.BudgetEntry;
import com.example.duitku.main.Utility;
import com.example.duitku.transaction.Transaction;
import com.example.duitku.transaction.TransactionController;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class BudgetController {

    public static final String[] budgetPeriod = {"Monthly", "3 Month (Quarter)", "Yearly"};
    public static final String[] budgetType = {BudgetEntry.TYPE_MONTH, BudgetEntry.TYPE_3MONTH, BudgetEntry.TYPE_YEAR};
    public static final HashMap<String, Integer> budgetPeriodMap = new HashMap<>();

    static {
        budgetPeriodMap.put(budgetType[0], 0);
        budgetPeriodMap.put(budgetType[1], 1);
        budgetPeriodMap.put(budgetType[2], 2);
    }

    private Context context;

    public BudgetController(Context context){
        this.context = context;
    }

    // basic operations
    public Uri addBudget(Budget budget){
        initialUsed(budget);
        ContentValues values = convertBudgetToContentValues(budget);
        Uri uri = context.getContentResolver().insert(BudgetEntry.CONTENT_URI, values);
        return uri;
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

    public int updateBudget(Budget budget){
        initialUsed(budget);
        ContentValues values = convertBudgetToContentValues(budget);
        Uri uri = ContentUris.withAppendedId(BudgetEntry.CONTENT_URI, budget.getId());
        int rowsUpdated = context.getContentResolver().update(uri, values, null, null);
        return rowsUpdated;
    }

    public int deleteBudget(Budget budget){
        int rowsDeleted = context.getContentResolver().delete(ContentUris.withAppendedId(BudgetEntry.CONTENT_URI, budget.getId()), null, null);
        return rowsDeleted;
    }

    // get budget
    public Budget getBudgetById(long id){
        if (id == -1) return null;

        Budget ret = null;
        Cursor data = context.getContentResolver().query(ContentUris.withAppendedId(BudgetEntry.CONTENT_URI, id), getFullProjection(), null, null, null);
        if (data.moveToFirst()){
            ret = convertCursorToBudget(data);
        }

        return ret;
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

    public Budget getBudgetByTransaction(Transaction transaction){
        Budget budget = getBudgetByCategoryId(transaction.getCategoryId());

        if (budget == null) return null;

        if (budget.getStartDate() == null){ // ga custom
            String type = budget.getType();

            Calendar calendarDate = Calendar.getInstance();
            calendarDate.setTime(transaction.getDate());

            Calendar calendarBudget = Calendar.getInstance();;

            if (type.equals(BudgetEntry.TYPE_MONTH)){

                if (calendarDate.get(Calendar.MONTH) == calendarBudget.get(Calendar.MONTH)){
                    return budget;
                } else {
                    return null;
                }

            } else if (type.equals(BudgetEntry.TYPE_3MONTH)){
                return null;

            } else {

                if (calendarDate.get(Calendar.YEAR) == calendarBudget.get(Calendar.YEAR)){
                    return budget;
                } else {
                    return null;
                }

            }
        } else { // custom date

            if (budget.getStartDate().compareTo(transaction.getDate()) <= 0 && budget.getEndDate().compareTo(transaction.getDate()) >= 0){
                return budget;
            } else {
                return null;
            }
        }

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

    // converting
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

    // operation from other entity's operation
    public int updateBudgetFromInitialTransaction(Transaction transaction){
        Budget budget = getBudgetByCategoryId(transaction.getCategoryId());

        if (budget == null) return 0;
        budget.setUsed(budget.getUsed() + transaction.getAmount());

        int rowsUpdated = updateBudget(budget);
        return rowsUpdated;
    }

    public void updateBudgetFromUpdatedTransaction(Transaction transactionBefore, Transaction transactionAfter){
        Budget budget = getBudgetByTransaction(transactionBefore);

        // categoryny berubah otomatis budget jg berubah
        if (transactionBefore.getCategoryId() != transactionAfter.getCategoryId()){
            if (budget != null){
                budget.setUsed(budget.getUsed() - transactionBefore.getAmount());
                updateBudget(budget);
            }

            Budget budgetAfter = getBudgetByTransaction(transactionAfter);
            if (budgetAfter != null){
                budgetAfter.setUsed(budgetAfter.getUsed() + transactionAfter.getAmount());
                updateBudget(budgetAfter);
            }

        } else { // dalam category yg sama

            // ada pergantian fase budget
            Budget budgetAfter = getBudgetByTransaction(transactionAfter);

            if (budget != null && budgetAfter == null){
                budget.setUsed(budget.getUsed() - transactionBefore.getAmount());
                updateBudget(budget);
            }

            if (budgetAfter != null && budget == null){
                budgetAfter.setUsed(budgetAfter.getUsed() + transactionAfter.getAmount());
                updateBudget(budgetAfter);
            }

            if (budget != null && budgetAfter != null){
                budget.setUsed(budget.getUsed() - transactionBefore.getAmount() + transactionAfter.getAmount());
                updateBudget(budget);
            }

        }

    }

}
