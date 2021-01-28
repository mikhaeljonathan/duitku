package com.example.duitku.database;

import com.example.duitku.database.DuitkuContract.WalletEntry;
import com.example.duitku.database.DuitkuContract.BudgetEntry;
import com.example.duitku.database.DuitkuContract.TransactionEntry;
import com.example.duitku.database.DuitkuContract.CategoryEntry;
import com.example.duitku.database.DuitkuContract.UserEntry;
import com.example.duitku.firebase.FirebaseHelper;
import com.example.duitku.firebase.FirebaseWriter;
import com.example.duitku.transaction.Transaction;
import com.example.duitku.transaction.TransactionController;


import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class DuitkuProvider extends ContentProvider {

    private static final int WALLET = 100;
    private static final int WALLET_ID = 101;
    private static final int CATEGORY = 200;
    private static final int CATEGORY_ID = 201;
    private static final int BUDGET = 300;
    private static final int BUDGET_ID = 301;
    private static final int TRANSACTION = 400;
    private static final int TRANSACTION_ID = 401;
    private static final int USER = 500;

    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    // biar bisa diexecute scr global, ga di dalem method manapun
    static{
        uriMatcher.addURI(DuitkuContract.CONTENT_AUTHORITY, DuitkuContract.PATH_WALLET, WALLET);
        uriMatcher.addURI(DuitkuContract.CONTENT_AUTHORITY, DuitkuContract.PATH_WALLET + "/#", WALLET_ID);
        uriMatcher.addURI(DuitkuContract.CONTENT_AUTHORITY, DuitkuContract.PATH_CATEGORY, CATEGORY);
        uriMatcher.addURI(DuitkuContract.CONTENT_AUTHORITY, DuitkuContract.PATH_CATEGORY + "/#", CATEGORY_ID);
        uriMatcher.addURI(DuitkuContract.CONTENT_AUTHORITY, DuitkuContract.PATH_BUDGET, BUDGET);
        uriMatcher.addURI(DuitkuContract.CONTENT_AUTHORITY, DuitkuContract.PATH_BUDGET + "/#", BUDGET_ID);
        uriMatcher.addURI(DuitkuContract.CONTENT_AUTHORITY, DuitkuContract.PATH_TRANSACTION, TRANSACTION);
        uriMatcher.addURI(DuitkuContract.CONTENT_AUTHORITY, DuitkuContract.PATH_TRANSACTION + "/#", TRANSACTION_ID);
        uriMatcher.addURI(DuitkuContract.CONTENT_AUTHORITY, DuitkuContract.PATH_USER, USER);
    }

    private DuitkuDbHelper duitkuDbHelper;

    @Override
    public boolean onCreate() {
        duitkuDbHelper = new DuitkuDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteDatabase db = duitkuDbHelper.getReadableDatabase();

        Cursor cursor;

        int match = uriMatcher.match(uri);
        switch(match){
            case WALLET:
                cursor = db.query(WalletEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case WALLET_ID:
                selection = WalletEntry.COLUMN_ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                cursor = db.query(WalletEntry.TABLE_NAME, projection, selection, selectionArgs,null, null, sortOrder);
                break;
            case CATEGORY:
                cursor = db.query(CategoryEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case CATEGORY_ID:
                selection = CategoryEntry.COLUMN_ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                cursor = db.query(CategoryEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case BUDGET:
                cursor = db.query(BudgetEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case BUDGET_ID:
                selection = BudgetEntry.COLUMN_ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                cursor = db.query(BudgetEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case TRANSACTION:
                cursor = db.query(TransactionEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case TRANSACTION_ID:
                selection = TransactionEntry.COLUMN_ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                cursor = db.query(TransactionEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case USER:
                cursor = db.query(UserEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }

        // biar bisa otomatis nambah or ngurangin list nya, ini diset dlu notificationnya
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }


    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        SQLiteDatabase db = duitkuDbHelper.getWritableDatabase();

        long id;

        int match = uriMatcher.match(uri);
        switch (match){
            case WALLET:
                id = db.insert(WalletEntry.TABLE_NAME, null, contentValues);
                break;
            case CATEGORY:
                id = db.insert(CategoryEntry.TABLE_NAME, null, contentValues);
                break;
            case BUDGET:
                id = db.insert(BudgetEntry.TABLE_NAME, null, contentValues);
                break;
            case TRANSACTION:
                id = db.insert(TransactionEntry.TABLE_NAME, null, contentValues);
                break;
            case USER:
                id = db.insert(UserEntry.TABLE_NAME, null, contentValues);
                break;
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }

        if (id == -1){
            return null;
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase db = duitkuDbHelper.getWritableDatabase();

        int rowsDeleted;

        int match = uriMatcher.match(uri);
        switch (match){
            case TRANSACTION:
                TransactionController transactionController = new TransactionController(getContext());
                Cursor data = query(uri, transactionController.getFullProjection(), selection, selectionArgs, null);
                List<Transaction> transactions = transactionController.convertCursorToListOfTransaction(data);
                for (Transaction transaction: transactions){
                    new FirebaseWriter(getContext()).deleteTransaction(transaction.get_id());
                }
                rowsDeleted = db.delete(TransactionEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case TRANSACTION_ID:
                String id = String.valueOf(ContentUris.parseId(uri));
                selection = TransactionEntry._ID + " = ?";
                selectionArgs = new String[] {id};
                rowsDeleted = db.delete(TransactionEntry.TABLE_NAME, selection, selectionArgs);
                new FirebaseWriter(getContext()).deleteTransaction(Long.parseLong(id));
                break;
            case WALLET_ID:
                selection = WalletEntry._ID + " = ?";
                id = String.valueOf(ContentUris.parseId(uri));
                selectionArgs = new String[] {id};
                rowsDeleted = db.delete(WalletEntry.TABLE_NAME, selection, selectionArgs);
                new FirebaseWriter(getContext()).deleteWallet(Long.parseLong(id));
                break;
            case BUDGET_ID:
                selection = BudgetEntry._ID + " = ?";
                id = String.valueOf(ContentUris.parseId(uri));
                selectionArgs = new String[] {id};
                rowsDeleted = db.delete(BudgetEntry.TABLE_NAME, selection, selectionArgs);
                new FirebaseWriter(getContext()).deleteBudget(Long.parseLong(id));
                break;
            case CATEGORY_ID:
                selection = CategoryEntry._ID + " = ?";
                id = String.valueOf(ContentUris.parseId(uri));
                selectionArgs = new String[] {id};
                rowsDeleted = db.delete(CategoryEntry.TABLE_NAME, selection, selectionArgs);
                new FirebaseWriter(getContext()).deleteCategory(Long.parseLong(id));
                break;
            case USER:
                rowsDeleted = db.delete(UserEntry.TABLE_NAME, null, null);
                break;
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }

        if (rowsDeleted != 0){
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase db = duitkuDbHelper.getWritableDatabase();

        int rowsUpdated;

        int match = uriMatcher.match(uri);
        switch (match){
            case TRANSACTION_ID:
                selection = TransactionEntry._ID + " = ?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri))};
                rowsUpdated = db.update(TransactionEntry.TABLE_NAME, contentValues, selection, selectionArgs);
                break;
            case WALLET_ID:
                selection = WalletEntry._ID + " = ?";
                selectionArgs = new String[] {String.valueOf(ContentUris.parseId(uri))};
                rowsUpdated = db.update(WalletEntry.TABLE_NAME, contentValues, selection, selectionArgs);
                break;
            case BUDGET_ID:
                selection = BudgetEntry._ID + " = ?";
                selectionArgs = new String[] {String.valueOf(ContentUris.parseId(uri))};
                rowsUpdated = db.update(BudgetEntry.TABLE_NAME, contentValues, selection, selectionArgs);
                break;
            case CATEGORY_ID:
                selection = CategoryEntry._ID + " = ?";
                selectionArgs = new String[] {String.valueOf(ContentUris.parseId(uri))};
                rowsUpdated = db.update(CategoryEntry.TABLE_NAME, contentValues, selection, selectionArgs);
                break;
            case USER:
                rowsUpdated = db.update(UserEntry.TABLE_NAME, contentValues, null, null);
                break;
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }

        if (rowsUpdated != 0){
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

}
