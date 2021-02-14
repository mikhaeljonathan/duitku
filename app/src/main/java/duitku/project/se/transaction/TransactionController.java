package duitku.project.se.transaction;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import duitku.project.se.budget.Budget;
import duitku.project.se.budget.BudgetController;
import duitku.project.se.category.Category;
import duitku.project.se.category.CategoryController;
import duitku.project.se.transaction.category.CategoryTransaction;
import duitku.project.se.database.DuitkuContract.BudgetEntry;
import duitku.project.se.database.DuitkuContract.TransactionEntry;
import duitku.project.se.database.DuitkuContract.CategoryEntry;
import duitku.project.se.main.Utility;
import duitku.project.se.wallet.Wallet;
import duitku.project.se.wallet.WalletController;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TransactionController {

    private final Context context;

    public TransactionController(Context context) {
        this.context = context;
    }

    // basic operations
    public Uri addTransaction(Transaction transaction) {
        ContentValues values = convertTransactionToContentValues(transaction);
        Uri uri = context.getContentResolver().insert(TransactionEntry.CONTENT_URI, values);

        Category category = new CategoryController(context).getCategoryById(transaction.getCategory_id());
        if (category != null && category.getCategory_type().equals(CategoryEntry.TYPE_EXPENSE)) { //budget pasti expense
            new BudgetController(context).updateBudgetFromInitialTransaction(transaction);
        }

        return uri;
    }

    public int updateTransaction(Transaction transactionBefore, Transaction transactionAfter) {
        ContentValues values = convertTransactionToContentValues(transactionAfter);
        Uri uri = ContentUris.withAppendedId(TransactionEntry.CONTENT_URI, transactionAfter.get_id());
        int rowsUpdated = context.getContentResolver().update(uri, values, null, null);

        new WalletController(context).updateWalletFromUpdatedTransaction(transactionBefore, transactionAfter);
        new BudgetController(context).updateBudgetFromUpdatedTransaction(transactionBefore, transactionAfter);

        return rowsUpdated;
    }

    public int deleteTransaction(Transaction transaction) {
        new WalletController(context).updateWalletFromDeletedTransaction(transaction);
        new BudgetController(context).updateBudgetFromDeletedTransaction(transaction);

        return context.getContentResolver().delete(ContentUris.withAppendedId(TransactionEntry.CONTENT_URI, transaction.get_id()), null, null);
    }

    // get transaction
    public List<Transaction> getAllTransaction() {
        Cursor data = context.getContentResolver().query(TransactionEntry.CONTENT_URI,
                getFullProjection(), null, null, null);
        return convertCursorToListOfTransaction(data);
    }

    public Transaction getTransactionById(long id) {
        if (id == -1) return null;

        Transaction ret = null;
        Cursor data = context.getContentResolver().query(ContentUris.withAppendedId(TransactionEntry.CONTENT_URI, id), getFullProjection(), null, null, null);
        if (data.moveToFirst()) {
            ret = convertCursorToTransaction(data);
        }

        return ret;
    }

    public List<Transaction> getTransactionsByBudget(Budget budget) {
        Calendar calendar = Calendar.getInstance();
        int curMonth = calendar.get(Calendar.MONTH);
        int curYear = calendar.get(Calendar.YEAR);

        // monthly
        int monthLowerBound = curMonth + 1;
        int monthUpperBound = curMonth + 1;

        if (budget.getBudget_startdate() == null) { // ga custom date
            String type = budget.getBudget_type();
            if (type.equals(BudgetEntry.TYPE_3MONTH)) {

                int quarter = Utility.getQuarter(curMonth);
                monthLowerBound = 3 * (quarter - 1) + 1;
                monthUpperBound = 3 * quarter;

            } else if (type.equals(BudgetEntry.TYPE_YEAR)) {

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
        String[] selectionArgs = new String[]{Long.toString(budget.getCategory_id()),
                Integer.toString(monthLowerBound),
                Integer.toString(monthUpperBound),
                "%/%/" + curYear};
        Cursor data = context.getContentResolver().query(TransactionEntry.CONTENT_URI, projection, selection, selectionArgs, null);

        return convertCursorToListOfTransaction(data);
    }

    private List<Transaction> getTransactionsByBudgetCustomDate(Budget budget) {
        String startDate = Utility.convertDateToString(budget.getBudget_startdate());
        String endDate = Utility.convertDateToString(budget.getBudget_enddate());
        String dateLowerBound = startDate.substring(6) + startDate.substring(3, 5) + startDate.substring(0, 2);
        String dateUpperBound = endDate.substring(6) + endDate.substring(3, 5) + endDate.substring(0, 2);

        String[] projection = getFullProjection();
        String selection = TransactionEntry.COLUMN_CATEGORY_ID + " = ? " +
                "AND SUBSTR(" + TransactionEntry.COLUMN_DATE + ", 7)||SUBSTR(" + TransactionEntry.COLUMN_DATE + ", 4, 2)||SUBSTR(" + TransactionEntry.COLUMN_DATE + ", 1, 2) " +
                "BETWEEN ? AND ?";
        String[] selectionArgs = new String[]{Long.toString(budget.getCategory_id()), dateLowerBound, dateUpperBound};
        Cursor data = context.getContentResolver().query(TransactionEntry.CONTENT_URI, projection, selection, selectionArgs, null);

        return convertCursorToListOfTransaction(data);
    }

    public String[] getFullProjection() {
        return new String[]{TransactionEntry.COLUMN_ID,
                TransactionEntry.COLUMN_WALLET_ID,
                TransactionEntry.COLUMN_WALLET_DEST_ID,
                TransactionEntry.COLUMN_CATEGORY_ID,
                TransactionEntry.COLUMN_DESC,
                TransactionEntry.COLUMN_DATE,
                TransactionEntry.COLUMN_AMOUNT};
    }

    // converting
    public Transaction convertCursorToTransaction(Cursor data) {
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
        double amount = data.getDouble(amountColumnIndex);

        if (walletDestId == 0) {
            walletDestId = -1;
        }

        if (categoryId == 0) {
            categoryId = -1;
        }

        return new Transaction(transactionId, walletId, walletDestId, categoryId, desc, date, amount);
    }

    public ContentValues convertTransactionToContentValues(Transaction transaction) {
        String date = Utility.convertDateToString(transaction.getTransaction_date());
        Long categoryId = null;
        if (transaction.getCategory_id() != -1) {
            categoryId = transaction.getCategory_id();
        }
        Long walletDestId = null;
        if (transaction.getWalletdest_id() != -1) {
            walletDestId = transaction.getWalletdest_id();
        }

        ContentValues ret = new ContentValues();
        ret.put(TransactionEntry.COLUMN_WALLET_ID, transaction.getWallet_id());
        ret.put(TransactionEntry.COLUMN_WALLET_DEST_ID, walletDestId);
        ret.put(TransactionEntry.COLUMN_CATEGORY_ID, categoryId);
        ret.put(TransactionEntry.COLUMN_DESC, transaction.getTransaction_desc());
        ret.put(TransactionEntry.COLUMN_DATE, date);
        ret.put(TransactionEntry.COLUMN_AMOUNT, transaction.getTransaction_amount());

        return ret;
    }

    public List<Transaction> convertCursorToListOfTransaction(Cursor data) {
        List<Transaction> ret = new ArrayList<>();
        if (!data.moveToFirst()) return ret;
        do {
            ret.add(convertCursorToTransaction(data));
        } while (data.moveToNext());
        return ret;
    }

    public List<CategoryTransaction> convertHashMapToListOfCategoryTransaction(HashMap<Long, CategoryTransaction> hashMap) {
        List<CategoryTransaction> ret = new ArrayList<>();
        for (Map.Entry mapElement : hashMap.entrySet()) {
            ret.add((CategoryTransaction) mapElement.getValue());
        }
        return ret;
    }

    public HashMap<String, Object> convertTransactionToHashMap(Transaction transaction) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put(TransactionEntry.COLUMN_ID, transaction.get_id());
        hashMap.put(TransactionEntry.COLUMN_WALLET_ID, transaction.getWallet_id());
        hashMap.put(TransactionEntry.COLUMN_WALLET_DEST_ID, transaction.getWalletdest_id());
        hashMap.put(TransactionEntry.COLUMN_CATEGORY_ID, transaction.getCategory_id());
        hashMap.put(TransactionEntry.COLUMN_DESC, transaction.getTransaction_desc());
        hashMap.put(TransactionEntry.COLUMN_DATE, transaction.getTransaction_date());
        hashMap.put(TransactionEntry.COLUMN_AMOUNT, transaction.getTransaction_amount());
        return hashMap;
    }

    // operations from other entity's operation
    public int deleteAllTransactionWithWalletId(long walletId) {
        String selection = TransactionEntry.COLUMN_WALLET_ID + " = ? OR " + TransactionEntry.COLUMN_WALLET_DEST_ID + " = ?";
        String[] selectionArgs = new String[]{Long.toString(walletId), Long.toString(walletId)};

        Cursor data = context.getContentResolver().query(TransactionEntry.CONTENT_URI, getFullProjection(), selection, selectionArgs, null);
        List<Transaction> transactions = convertCursorToListOfTransaction(data);

        for (Transaction transaction : transactions) {
            new BudgetController(context).updateBudgetFromDeletedTransaction(transaction);
        }

        return context.getContentResolver().delete(TransactionEntry.CONTENT_URI, selection, selectionArgs);
    }

    public int deleteAllTransactionWithCategoryId(long categoryId) {
        String selection = TransactionEntry.COLUMN_CATEGORY_ID + "= ?";
        String[] selectionArgs = new String[]{Long.toString(categoryId)};

        Cursor data = context.getContentResolver().query(TransactionEntry.CONTENT_URI, getFullProjection(), selection, selectionArgs, null);
        List<Transaction> transactions = convertCursorToListOfTransaction(data);

        for (Transaction transaction : transactions) {
            new BudgetController(context).updateBudgetFromDeletedTransaction(transaction);
        }

        return context.getContentResolver().delete(TransactionEntry.CONTENT_URI, selection, selectionArgs);
    }

    public Uri addTransactionFromInitialWallet(long walletId, Wallet wallet) {
        CategoryController categoryController = new CategoryController(context);
        Category category;

        if (wallet.getWallet_amount() < 0) {
            category = categoryController.getDefaultCategory(CategoryEntry.TYPE_EXPENSE);
        } else {
            category = categoryController.getDefaultCategory(CategoryEntry.TYPE_INCOME);
        }

        Calendar calendar = Calendar.getInstance();
        long walletDestId = -1;
        long categoryId = category.get_id();
        String desc = "Initial Balance for Wallet " + wallet.getWallet_name();
        Date date = calendar.getTime();
        double amount = Math.abs(wallet.getWallet_amount());
        Transaction transaction = new Transaction(-1, walletId, walletDestId, categoryId, desc, date, amount);

        return addTransaction(transaction);
    }

    public Uri addTransactionFromUpdatedWallet(double amountBefore, Wallet wallet) {
        CategoryController categoryController = new CategoryController(context);
        Category category;
        if (amountBefore < wallet.getWallet_amount()) {
            category = categoryController.getDefaultCategory(CategoryEntry.TYPE_INCOME);
        } else {
            category = categoryController.getDefaultCategory(CategoryEntry.TYPE_EXPENSE);
        }

        Calendar calendar = Calendar.getInstance();
        long walletId = wallet.get_id();
        long walletDestId = -1;
        long categoryId = category.get_id();
        String desc = "Balance Adjustment for Wallet " + wallet.getWallet_name();
        Date date = calendar.getTime();
        double amount = Math.abs(amountBefore - wallet.getWallet_amount());
        Transaction transaction = new Transaction(-1, walletId, walletDestId, categoryId, desc, date, amount);

        return addTransaction(transaction);
    }

    public CategoryTransaction recalculateCategoryTransaction(CategoryTransaction categoryTransaction) {
        List<Transaction> temp = new ArrayList<>(categoryTransaction.getTransactions());

        categoryTransaction.getTransactions().clear();
        categoryTransaction.setAmount(0);

        for (Transaction transaction : temp) {
            Transaction temp2 = getTransactionById(transaction.get_id());
            if (temp2 == null) continue;
            categoryTransaction.addTransaction(temp2);
        }

        return categoryTransaction;
    }

}
