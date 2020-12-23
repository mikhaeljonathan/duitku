package com.example.duitku.database;

import com.example.duitku.database.DuitkuContract.WalletEntry;
import com.example.duitku.database.DuitkuContract.BudgetEntry;
import com.example.duitku.database.DuitkuContract.TransactionEntry;
import com.example.duitku.database.DuitkuContract.CategoryEntry;
import com.example.duitku.model.Category;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class DuitkuProvider extends ContentProvider {

    // URI yang beda2 ini nanti dimatch sama integer pake UriMatcher
    private static final int WALLET = 100;
    private static final int WALLET_ID = 101;
    private static final int CATEGORY = 200;
    private static final int CATEGORY_ID = 201;
    private static final int BUDGET = 300;
    private static final int BUDGET_ID = 301;
    private static final int TRANSACTION = 400;
    private static final int TRANSACTION_ID = 401;

    private static final UriMatcher mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    // biar bisa diexecute scr global, ga di dalem method manapun
    static{
        mUriMatcher.addURI(DuitkuContract.CONTENT_AUTHORITY, DuitkuContract.PATH_WALLET, WALLET);
        mUriMatcher.addURI(DuitkuContract.CONTENT_AUTHORITY, DuitkuContract.PATH_WALLET + "/#", WALLET_ID);
        mUriMatcher.addURI(DuitkuContract.CONTENT_AUTHORITY, DuitkuContract.PATH_CATEGORY, CATEGORY);
        mUriMatcher.addURI(DuitkuContract.CONTENT_AUTHORITY, DuitkuContract.PATH_CATEGORY + "/#", CATEGORY_ID);
        mUriMatcher.addURI(DuitkuContract.CONTENT_AUTHORITY, DuitkuContract.PATH_BUDGET, BUDGET);
        mUriMatcher.addURI(DuitkuContract.CONTENT_AUTHORITY, DuitkuContract.PATH_BUDGET + "/#", BUDGET_ID);
        mUriMatcher.addURI(DuitkuContract.CONTENT_AUTHORITY, DuitkuContract.PATH_TRANSACTION, TRANSACTION);
        mUriMatcher.addURI(DuitkuContract.CONTENT_AUTHORITY, DuitkuContract.PATH_TRANSACTION + "/#", TRANSACTION_ID);
    }

    private DuitkuDbHelper duitkuDbHelper;

    @Override
    public boolean onCreate() {
        // Bikin object SQLiteOpenHelper nya
        duitkuDbHelper = new DuitkuDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        // dapetin database nya beneran;
        SQLiteDatabase db = duitkuDbHelper.getReadableDatabase();

        // cursor buat dapetin hasil querynya
        Cursor cursor = null;

        // query ini akan ngequery hasil yang berbeda sesuai dengan
        // URI yang dipass sama ContentResolsver

        // yang ada tambahan _ID nya itu buat query 1 cursor doang ya
        int match = mUriMatcher.match(uri);
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
            case TRANSACTION:
                cursor = db.query(TransactionEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case TRANSACTION_ID:
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }

        // biar bisa otomatis nambah or ngurangin list nya, ini diset dlu notificationnya
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }



    // yang ini buat insert ke db
    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {

        SQLiteDatabase db = duitkuDbHelper.getWritableDatabase();
        long id = -1;

        int match = mUriMatcher.match(uri);
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
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }

        // kalau gagal id nya bakal -1
        if (id == -1){
            return null;
        }

        // dinotif biar trs keupdate
        getContext().getContentResolver().notifyChange(uri, null);

        // return uri nya dari gabungan uri sebelumnya ditambah id yang baru diinsert
        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {

        SQLiteDatabase db = duitkuDbHelper.getWritableDatabase();
        int rowsDeleted = 0;

        int match = mUriMatcher.match(uri);
        switch (match){
            case TRANSACTION:
                rowsDeleted = db.delete(TransactionEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case WALLET_ID:
                selection = WalletEntry._ID + "=?";
                selectionArgs = new String[] {String.valueOf(ContentUris.parseId(uri))};
                rowsDeleted = db.delete(WalletEntry.TABLE_NAME, selection, selectionArgs);
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
        int rowsUpdated = 0;

        int match = mUriMatcher.match(uri);
        switch (match){
            case WALLET_ID:
                selection = WalletEntry._ID + "=?";
                selectionArgs = new String[] {String.valueOf(ContentUris.parseId(uri))};
                rowsUpdated = db.update(WalletEntry.TABLE_NAME, contentValues, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }

        // kalau misal gaaada yg diupdate gausa dinotify
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
