package com.example.duitku.transaction;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.example.duitku.budget.Budget;
import com.example.duitku.budget.BudgetController;
import com.example.duitku.category.Category;
import com.example.duitku.category.CategoryController;
import com.example.duitku.transaction.category.CategoryTransaction;
import com.example.duitku.database.DuitkuContract;
import com.example.duitku.database.DuitkuContract.BudgetEntry;
import com.example.duitku.database.DuitkuContract.TransactionEntry;
import com.example.duitku.database.DuitkuContract.CategoryEntry;
import com.example.duitku.main.Utility;
import com.example.duitku.wallet.Wallet;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TransactionController {

    private Context context;

    public TransactionController(Context context){
        this.context = context;
    }

    // basic operations
    public Uri addTransaction(Transaction transaction){
        ContentValues values = convertTransactionToContentValues(transaction);
        Uri uri = context.getContentResolver().insert(TransactionEntry.CONTENT_URI, values);

        Category category = new CategoryController(context).getCategoryById(transaction.getCategoryId());
        if (category != null && category.getType().equals(CategoryEntry.TYPE_EXPENSE)){ //budget pasti expense
            new BudgetController(context).updateBudgetFromTransaction(transaction);
        }

        return uri;
    }

    public int updateTransaction(Transaction transaction){
        ContentValues values = convertTransactionToContentValues(transaction);
        Uri uri = ContentUris.withAppendedId(TransactionEntry.CONTENT_URI, transaction.getId());
        int rowsUpdated = context.getContentResolver().update(uri, values, null, null);
        return rowsUpdated;
    }

    public int deleteTransaction(long id){
        int rowsDeleted = context.getContentResolver().delete(ContentUris.withAppendedId(TransactionEntry.CONTENT_URI, id), null, null);
        return rowsDeleted;
    }

    // get transaction
    public Transaction getTransactionById(long id){
        if (id == -1) return null;

        Transaction ret = null;
        Cursor data = context.getContentResolver().query(ContentUris.withAppendedId(TransactionEntry.CONTENT_URI, id), getFullProjection(), null, null, null);
        if (data.moveToFirst()){
            ret = convertCursorToTransaction(data);
        }

        return ret;
    }

    public List<Transaction> getTransactionsByBudget(Budget budget){
        Calendar calendar = Calendar.getInstance();
        int curMonth = calendar.get(Calendar.MONTH);
        int curYear = calendar.get(Calendar.YEAR);

        // monthly
        int monthLowerBound = curMonth + 1;
        int monthUpperBound = curMonth + 1;

        if (budget.getStartDate() == null){ // ga custom date
            String type = budget.getType();
            if (type.equals(BudgetEntry.TYPE_3MONTH)){

                int quarter = curMonth / 4 + 2; // value dari 1 sampe 4
                monthLowerBound = 3 * (quarter - 1) + 1;
                monthUpperBound = 3 * quarter;

            } else if (type.equals(BudgetEntry.TYPE_YEAR)){

                monthLowerBound = 1;
                monthUpperBound = 12;
            }
        } else { // custom date
            return getTransactionsByBudgetCustomDate(budget);
        }

        String[] projection = getFullProjection();
        String selection = TransactionEntry.COLUMN_CATEGORY_ID + " = ? " +
                "AND CAST(SUBSTR(" + TransactionEntry.COLUMN_DATE + ", 4, 2) AS INTEGER) >= ? " +
                "AND CAST(SUBSTR(" + TransactionEntry.COLUMN_DATE + ", 4, 2) AS INTEGER) <= ? " +
                "AND " + TransactionEntry.COLUMN_DATE + " LIKE ?";
        String[] selectionArgs = new String[]{Long.toString(budget.getCategoryId()),
                Integer.toString(monthLowerBound),
                Integer.toString(monthUpperBound),
                "%/%/" + curYear};
        Cursor data = context.getContentResolver().query(TransactionEntry.CONTENT_URI, projection, selection, selectionArgs, null);

        List<Transaction> ret = convertCursorToListOfTransaction(data);
        return ret;
    }

    private List<Transaction> getTransactionsByBudgetCustomDate(Budget budget){
        String startDate = Utility.convertDateToString(budget.getStartDate());
        String endDate = Utility.convertDateToString(budget.getEndDate());
        String dateLowerBound = startDate.substring(6) + startDate.substring(3, 5) + startDate.substring(0, 2);
        String dateUpperBound = endDate.substring(6) + endDate.substring(3, 5) + endDate.substring(0, 2);

        String[] projection = getFullProjection();
        String selection = TransactionEntry.COLUMN_CATEGORY_ID + " = ? " +
                "AND SUBSTR(" + TransactionEntry.COLUMN_DATE + ", 7)||SUBSTR(" + TransactionEntry.COLUMN_DATE + ", 4, 2)||SUBSTR(" + TransactionEntry.COLUMN_DATE + ", 1, 2) " +
                "BETWEEN ? AND ?";
        String[] selectionArgs = new String[]{Long.toString(budget.getCategoryId()), dateLowerBound, dateUpperBound};
        Cursor data = context.getContentResolver().query(TransactionEntry.CONTENT_URI, projection, selection, selectionArgs, null);

        List<Transaction> ret = convertCursorToListOfTransaction(data);
        return ret;
    }

    public String[] getFullProjection(){
        String[] projection = new String[]{TransactionEntry.COLUMN_ID,
                TransactionEntry.COLUMN_WALLET_ID,
                TransactionEntry.COLUMN_WALLET_DEST_ID,
                TransactionEntry.COLUMN_CATEGORY_ID,
                TransactionEntry.COLUMN_DESC,
                TransactionEntry.COLUMN_DATE,
                TransactionEntry.COLUMN_AMOUNT};
        return projection;
    }

    // converting
    public Transaction convertCursorToTransaction(Cursor data){
        int transactionIdColumnIndex = data.getColumnIndex(TransactionEntry.COLUMN_ID);
        int walletIdColumnIndex = data.getColumnIndex(TransactionEntry.COLUMN_WALLET_ID);
        int walletDestIdColumnIndex = data.getColumnIndex(TransactionEntry.COLUMN_WALLET_DEST_ID);
        int categoryIdColumnIndex = data.getColumnIndex(TransactionEntry.COLUMN_CATEGORY_ID);
        int descColumnIndex = data.getColumnIndex(TransactionEntry.COLUMN_DESC);
        int dateColumnIndex = data.getColumnIndex(TransactionEntry.COLUMN_DATE);
        int amountColumnIndex = data.getColumnIndex(TransactionEntry.COLUMN_AMOUNT);

        long transactionId = data.getLong(transactionIdColumnIndex);
        long walletId = data.getLong(walletIdColumnIndex);
        long walletDestId = data.getLong(walletDestIdColumnIndex);
        long categoryId = data.getLong(categoryIdColumnIndex);
        String desc = data.getString(descColumnIndex);
        Date date = Utility.parseDate(data.getString(dateColumnIndex));
        double amount= data.getDouble(amountColumnIndex);

        Transaction ret = new Transaction(transactionId, walletId, walletDestId, categoryId, desc, date, amount);
        return ret;
    }

    private ContentValues convertTransactionToContentValues(Transaction transaction){
        String date = Utility.convertDateToString(transaction.getDate());
        Long categoryId = null;
        if (transaction.getCategoryId() != -1){
            categoryId = transaction.getCategoryId();
        }
        Long walletDestId = null;
        if (transaction.getWalletDestId() != -1){
            walletDestId = transaction.getWalletDestId();
        }

        ContentValues ret = new ContentValues();
        ret.put(TransactionEntry.COLUMN_WALLET_ID, transaction.getWalletId());
        ret.put(TransactionEntry.COLUMN_WALLET_DEST_ID, walletDestId);
        ret.put(TransactionEntry.COLUMN_CATEGORY_ID, categoryId);
        ret.put(TransactionEntry.COLUMN_DESC, transaction.getDesc());
        ret.put(TransactionEntry.COLUMN_DATE, date);
        ret.put(TransactionEntry.COLUMN_AMOUNT, transaction.getAmount());

        return ret;
    }

    public List<Transaction> convertCursorToListOfTransaction(Cursor data){
        List<Transaction> ret = new ArrayList<>();
        if (!data.moveToFirst()) return ret;
        do {
            ret.add(convertCursorToTransaction(data));
        } while (data.moveToNext());
        return ret;
    }

    public List<CategoryTransaction> convertHashMapToListOfCategoryTransaction(HashMap<Long, CategoryTransaction> hashMap){
        List<CategoryTransaction> ret = new ArrayList<>();
        for (Map.Entry mapElement: hashMap.entrySet()){
            ret.add((CategoryTransaction) mapElement.getValue());
        }
        return ret;
    }

    // operations from other entity's operation
    public int deleteAllTransactionWithWalletId(long walletId){
        String selection = TransactionEntry.COLUMN_WALLET_ID + " = ? OR " + TransactionEntry.COLUMN_WALLET_DEST_ID + " = ?";
        String[] selectionArgs = new String[]{Long.toString(walletId), Long.toString(walletId)};
        int rowsDeleted = context.getContentResolver().delete(TransactionEntry.CONTENT_URI, selection, selectionArgs);
        return rowsDeleted;
    }

    public Uri addTransactionFromInitialWallet(long walletId, Wallet wallet){
        Calendar calendar = Calendar.getInstance();
        Category category = new CategoryController(context).getCategoryByNameAndType(CategoryEntry.DEFAULT_CATEGORY_NAME, CategoryEntry.TYPE_INCOME);
        if (wallet.getAmount() < 0){
            category = new CategoryController(context).getCategoryByNameAndType(CategoryEntry.DEFAULT_CATEGORY_NAME, CategoryEntry.TYPE_EXPENSE);
        }

        long walletDestId = -1;
        long categoryId = category.getId();
        String desc = "Initial Balance for Wallet " + wallet.getName();
        Date date = calendar.getTime();
        double amount = Math.abs(wallet.getAmount());
        Transaction transaction = new Transaction(-1, walletId, walletDestId, categoryId, desc, date, amount);

        Uri uri = addTransaction(transaction);
        return uri;
    }

    public Uri addTransactionFromUpdatedWallet(double amountBefore, Wallet wallet){
        Calendar calendar = Calendar.getInstance();
        Category category;
        if (amountBefore < wallet.getAmount()){
            category = new CategoryController(context).getCategoryByNameAndType(CategoryEntry.DEFAULT_CATEGORY_NAME, CategoryEntry.TYPE_INCOME);
        } else {
            category = new CategoryController(context).getCategoryByNameAndType(CategoryEntry.DEFAULT_CATEGORY_NAME, CategoryEntry.TYPE_EXPENSE);
        }

        long walletId = wallet.getId();
        long walletDestId = -1;
        long categoryId = category.getId();
        String desc = "Balance Adjustment for Wallet " + wallet.getName();
        Date date = calendar.getTime();
        double amount = Math.abs(amountBefore - wallet.getAmount());
        Transaction transaction = new Transaction(-1, walletId, walletDestId, categoryId, desc, date, amount);

        Uri uri = addTransaction(transaction);
        return uri;
    }

}
