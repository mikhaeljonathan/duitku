package com.example.duitku.controller;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.example.duitku.database.DuitkuContract.CategoryEntry;
import com.example.duitku.database.DuitkuContract.WalletEntry;
import com.example.duitku.database.DuitkuContract.TransactionEntry;
import com.example.duitku.model.Category;
import com.example.duitku.model.Wallet;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class WalletController {

    private Context mContext;

    public WalletController(Context context){
        mContext = context;
    }

    public Uri addWallet(Wallet wallet){

        // taruh di contentvalues
        ContentValues values = new ContentValues();
        values.put(WalletEntry.COLUMN_NAME, wallet.getWalletName());
        values.put(WalletEntry.COLUMN_AMOUNT, wallet.getAmount());
        values.put(WalletEntry.COLUMN_DESC, wallet.getDescription());

        // add wallet to database
        Uri uri = mContext.getContentResolver().insert(WalletEntry.CONTENT_URI, values);

        // add transaction initial balance
        Calendar c = Calendar.getInstance();
        ContentValues cv = new ContentValues();
        cv.put(TransactionEntry.COLUMN_DATE, new SimpleDateFormat("dd/MM/yyyy").format(c.getTime()));
        cv.put(TransactionEntry.COLUMN_WALLET_ID, ContentUris.parseId(uri));
        cv.put(TransactionEntry.COLUMN_CATEGORY_ID, new CategoryController(mContext).getCategoryByNameAndType(CategoryEntry.DEFAULT_CATEGORY_NAME, CategoryEntry.TYPE_INCOME).getId());
        cv.put(TransactionEntry.COLUMN_DESC, "Initial Balance");
        cv.put(TransactionEntry.COLUMN_AMOUNT, wallet.getAmount());
        mContext.getContentResolver().insert(TransactionEntry.CONTENT_URI, cv);

        // panggil contentresolver yg nanti diterima sama contentprovider
        return uri;

    }

    public int updateWallet(Wallet wallet, Uri currentWalletUri){

        // taruh di contentvalues
        ContentValues values = new ContentValues();
        values.put(WalletEntry.COLUMN_NAME, wallet.getWalletName());
        values.put(WalletEntry.COLUMN_AMOUNT, wallet.getAmount());
        values.put(WalletEntry.COLUMN_DESC, wallet.getDescription());

        // add or subtract transaction
        double amountBefore = 0;
        double amountCurrent = wallet.getAmount();
        Cursor temp = mContext.getContentResolver().query(currentWalletUri, new String[]{WalletEntry.COLUMN_AMOUNT}, null, null, null);
        if (temp.moveToFirst()){
            amountBefore = temp.getDouble(temp.getColumnIndex(WalletEntry.COLUMN_AMOUNT));
        }

        Calendar c = Calendar.getInstance();
        ContentValues cv = new ContentValues();
        cv.put(TransactionEntry.COLUMN_DATE, new SimpleDateFormat("dd/MM/yyyy").format(c.getTime()));
        cv.put(TransactionEntry.COLUMN_WALLET_ID, ContentUris.parseId(currentWalletUri));
        cv.put(TransactionEntry.COLUMN_DESC, "Balance Adjustment");
        if (amountBefore > amountCurrent){
            cv.put(TransactionEntry.COLUMN_CATEGORY_ID, new CategoryController(mContext).getCategoryByNameAndType(CategoryEntry.DEFAULT_CATEGORY_NAME, CategoryEntry.TYPE_EXPENSE).getId());
            cv.put(TransactionEntry.COLUMN_AMOUNT, amountBefore - amountCurrent);
        } else if (amountBefore < amountCurrent){
            cv.put(TransactionEntry.COLUMN_CATEGORY_ID, new CategoryController(mContext).getCategoryByNameAndType(CategoryEntry.DEFAULT_CATEGORY_NAME, CategoryEntry.TYPE_INCOME).getId());
            cv.put(TransactionEntry.COLUMN_AMOUNT, amountCurrent - amountBefore);
        }
        mContext.getContentResolver().insert(TransactionEntry.CONTENT_URI, cv);

        int rowsUpdated = mContext.getContentResolver().update(currentWalletUri, values, null, null);

        return rowsUpdated;

    }

    public int deleteWallet(Uri currentWalletUri){

        int rowsDeleted = mContext.getContentResolver().delete(currentWalletUri, null, null);
        return rowsDeleted;

    }

    public double calculateTotalAmount(Cursor data){
        double ret = 0;
        if (!data.moveToFirst()) return ret;
        do {
            ret += data.getDouble(data.getColumnIndex(WalletEntry.COLUMN_AMOUNT));
        } while (data.moveToNext());
        return ret;
    }

    public Wallet getWalletFromId(long id){

        Wallet ret = null;
        Cursor walletQuery = mContext.getContentResolver().query(ContentUris.withAppendedId(WalletEntry.CONTENT_URI, id), new String[]{WalletEntry.COLUMN_ID, WalletEntry.COLUMN_NAME, WalletEntry.COLUMN_AMOUNT, WalletEntry.COLUMN_DESC}, null, null, null);
        if (walletQuery.moveToFirst()){
            String name = walletQuery.getString(walletQuery.getColumnIndex(WalletEntry.COLUMN_NAME));
            double amount = walletQuery.getDouble(walletQuery.getColumnIndex(WalletEntry.COLUMN_AMOUNT));
            String desc = walletQuery.getString(walletQuery.getColumnIndex(WalletEntry.COLUMN_DESC));
            ret = new Wallet(name, amount, desc);
        }
        return ret;

    }

}
