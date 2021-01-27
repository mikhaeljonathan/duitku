package com.example.duitku.budget;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.example.duitku.database.DuitkuContract.BudgetEntry;
import com.example.duitku.main.Utility;
import com.example.duitku.notification.NotificationController;
import com.example.duitku.transaction.Transaction;
import com.example.duitku.transaction.TransactionController;

import java.util.ArrayList;
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

    private final Context context;

    public BudgetController(Context context) {
        this.context = context;
    }

    // basic operations
    public Uri addBudget(Budget budget) {
        initialUsed(budget);
        ContentValues values = convertBudgetToContentValues(budget);
        Uri uri = context.getContentResolver().insert(BudgetEntry.CONTENT_URI, values);

        budget.set_id(ContentUris.parseId(uri));
        createNotification(budget);
        return uri;
    }

    public int updateBudget(Budget budget) {
        ContentValues values = convertBudgetToContentValues(budget);
        Uri uri = ContentUris.withAppendedId(BudgetEntry.CONTENT_URI, budget.get_id());
        return context.getContentResolver().update(uri, values, null, null);
    }

    public int updateAndRestartBudget(Budget budget){
        initialUsed(budget);
        createNotification(budget);

        return updateBudget(budget);
    }

    private void initialUsed(Budget budget) {
        List<Transaction> transactions = new TransactionController(context).getTransactionsByBudget(budget);

        double used = 0;
        for (Transaction transaction : transactions) {
            used += transaction.getTransaction_amount();
        }

        budget.setBudget_used(used);
    }

    private void createNotification(Budget budget){
        if (budget.getBudget_used() > budget.getBudget_amount()){
            new NotificationController(context).sendOnChannelBudgetOverflow(budget);
        }
    }

    public int deleteBudget(Budget budget) {
        return context.getContentResolver()
                .delete(ContentUris.withAppendedId(BudgetEntry.CONTENT_URI, budget.get_id()), null, null);
    }

    public int deleteBudgetWithCategoryId(long categoryId){
        Budget budget = getBudgetByCategoryId(categoryId);

        if (budget != null){
            return deleteBudget(budget);
        }

        return 0;
    }

    // get budget
    public List<Budget> getAllBudget(){
        Cursor data = context.getContentResolver().query(BudgetEntry.CONTENT_URI,
                getFullProjection(), null, null, null);
        return convertCursorToListOfBudgets(data);
    }

    public Budget getBudgetById(long id) {
        if (id == -1) return null;

        Budget ret = null;
        Cursor data = context.getContentResolver().query(ContentUris.withAppendedId(BudgetEntry.CONTENT_URI, id),
                getFullProjection(), null, null, null);
        if (data.moveToFirst()) {
            ret = convertCursorToBudget(data);
        }

        return ret;
    }

    public Budget getBudgetByCategoryId(long categoryId) {
        String[] projection = getFullProjection();
        String selection = BudgetEntry.COLUMN_CATEGORY_ID + " = ?";
        String[] selectionArgs = new String[]{Long.toString(categoryId)};

        Budget ret = null;
        Cursor data = context.getContentResolver().query(BudgetEntry.CONTENT_URI, projection, selection, selectionArgs, null);
        if (data.moveToFirst()) {
            ret = convertCursorToBudget(data);
        }
        return ret;
    }

    public Budget getBudgetByTransaction(Transaction transaction) {
        Budget budget = getBudgetByCategoryId(transaction.getCategory_id());

        if (budget == null) return null;

        if (budget.getBudget_startdate() == null) { // ga custom
            String type = budget.getBudget_type();

            Calendar calendarDate = Calendar.getInstance();
            calendarDate.setTime(transaction.getTransaction_date());

            Calendar calendarBudget = Calendar.getInstance();

            if (type.equals(BudgetEntry.TYPE_MONTH) &&
                    calendarDate.get(Calendar.MONTH) == calendarBudget.get(Calendar.MONTH)) {
                return budget;

            } else if (type.equals(BudgetEntry.TYPE_YEAR) &&
                    calendarDate.get(Calendar.YEAR) == calendarBudget.get(Calendar.YEAR)) {
                return budget;

            } else { //3 month
                int quarterDate = Utility.getQuarter(calendarDate.get(Calendar.MONTH) + 1);
                int quarterBudget = Utility.getQuarter(calendarDate.get(Calendar.MONTH) + 1);
                if (quarterDate == quarterBudget){
                    return budget;
                }

            }
        } else { // custom date
            if (budget.getBudget_startdate().compareTo(transaction.getTransaction_date()) <= 0
                    && budget.getBudget_enddate().compareTo(transaction.getTransaction_date()) >= 0) {
                return budget;
            }
        }

        return null;
    }

    public String[] getFullProjection() {
        return new String[]{BudgetEntry.COLUMN_ID,
                BudgetEntry.COLUMN_STARTDATE,
                BudgetEntry.COLUMN_ENDDATE,
                BudgetEntry.COLUMN_AMOUNT,
                BudgetEntry.COLUMN_USED,
                BudgetEntry.COLUMN_TYPE,
                BudgetEntry.COLUMN_CATEGORY_ID};
    }

    // converting
    public Budget convertCursorToBudget(Cursor data) {
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

        return new Budget(id, amount, used, startDate, endDate, type, categoryId);
    }

    public List<Budget> convertCursorToListOfBudgets(Cursor data){
        List<Budget> ret = new ArrayList<>();
        if (!data.moveToFirst()) return ret;
        do {
            ret.add(convertCursorToBudget(data));
        } while (data.moveToNext());
        return ret;
    }

    private ContentValues convertBudgetToContentValues(Budget budget) {
        String startDate = null;
        String endDate = null;
        if (budget.getBudget_startdate() != null) {
            startDate = Utility.convertDateToString(budget.getBudget_startdate());
            endDate = Utility.convertDateToString(budget.getBudget_enddate());
        }

        String type = budget.getBudget_type();
        if (startDate != null) type = null;

        ContentValues ret = new ContentValues();
        ret.put(BudgetEntry.COLUMN_AMOUNT, budget.getBudget_amount());
        ret.put(BudgetEntry.COLUMN_USED, budget.getBudget_used());
        ret.put(BudgetEntry.COLUMN_STARTDATE, startDate);
        ret.put(BudgetEntry.COLUMN_ENDDATE, endDate);
        ret.put(BudgetEntry.COLUMN_TYPE, type);
        ret.put(BudgetEntry.COLUMN_CATEGORY_ID, budget.getCategory_id());

        return ret;
    }

    public HashMap<String, Object> convertBudgetToHashMap(Budget budget){
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put(BudgetEntry.COLUMN_ID, budget.get_id());
        hashMap.put(BudgetEntry.COLUMN_AMOUNT, budget.getBudget_amount());
        hashMap.put(BudgetEntry.COLUMN_USED, budget.getBudget_used());
        hashMap.put(BudgetEntry.COLUMN_STARTDATE, budget.getBudget_startdate());
        hashMap.put(BudgetEntry.COLUMN_ENDDATE, budget.getBudget_enddate());
        hashMap.put(BudgetEntry.COLUMN_TYPE, budget.getBudget_type());
        hashMap.put(BudgetEntry.COLUMN_CATEGORY_ID, budget.getCategory_id());
        return hashMap;
    }

    // operation from other entity's operation
    public void updateBudgetFromInitialTransaction(Transaction transaction) {
        Budget budget = getBudgetByCategoryId(transaction.getCategory_id());
        if (budget == null) return;

        updateAndRestartBudget(budget);
    }

    public void updateBudgetFromUpdatedTransaction(Transaction transactionBefore, Transaction transactionAfter) {
        Budget budgetBefore = getBudgetByTransaction(transactionBefore);
        Budget budgetAfter = getBudgetByTransaction(transactionAfter);

        if (budgetBefore != null){
            updateAndRestartBudget(budgetBefore);
        }

        if (budgetAfter != null){
            updateAndRestartBudget(budgetAfter);
        }
    }

    public void updateBudgetFromDeletedTransaction(Transaction transaction){
        Budget budget = getBudgetByCategoryId(transaction.getCategory_id());
        if (budget == null) return;

        budget.setBudget_used(budget.getBudget_used() - transaction.getTransaction_amount());

        updateBudget(budget);
    }

}
