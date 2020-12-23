package com.example.duitku.controller;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.example.duitku.database.DuitkuContract.CategoryEntry;
import com.example.duitku.database.DuitkuContract.WalletEntry;
import com.example.duitku.database.DuitkuContract.TransactionEntry;
import com.example.duitku.model.Transaction;
import com.example.duitku.model.Wallet;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class WalletController {

    private Context context;

    public WalletController(Context context){
        this.context = context;
    }

    public Uri addWallet(Wallet wallet){
        ContentValues values = convertWalletToContentValues(wallet);
        Uri uri = context.getContentResolver().insert(WalletEntry.CONTENT_URI, values);
        return uri;
    }

    public int updateWallet(Wallet wallet, Uri currentWalletUri){

        // taruh di contentvalues
        ContentValues values = new ContentValues();
        values.put(WalletEntry.COLUMN_NAME, wallet.getName());
        values.put(WalletEntry.COLUMN_AMOUNT, wallet.getAmount());
        values.put(WalletEntry.COLUMN_DESC, wallet.getDescription());

        // add or subtract transaction
        double amountBefore = 0;
        double amountCurrent = wallet.getAmount();
        Cursor temp = context.getContentResolver().query(currentWalletUri, new String[]{WalletEntry.COLUMN_AMOUNT}, null, null, null);
        if (temp.moveToFirst()){
            amountBefore = temp.getDouble(temp.getColumnIndex(WalletEntry.COLUMN_AMOUNT));
        }

        Calendar c = Calendar.getInstance();
        ContentValues cv = new ContentValues();
        cv.put(TransactionEntry.COLUMN_DATE, new SimpleDateFormat("dd/MM/yyyy").format(c.getTime()));
        cv.put(TransactionEntry.COLUMN_WALLET_ID, ContentUris.parseId(currentWalletUri));
        cv.put(TransactionEntry.COLUMN_DESC, "Balance Adjustment");
        if (amountBefore > amountCurrent){
            cv.put(TransactionEntry.COLUMN_CATEGORY_ID, new CategoryController(context).getCategoryByNameAndType(CategoryEntry.DEFAULT_CATEGORY_NAME, CategoryEntry.TYPE_EXPENSE).getId());
            cv.put(TransactionEntry.COLUMN_AMOUNT, amountBefore - amountCurrent);
        } else if (amountBefore < amountCurrent){
            cv.put(TransactionEntry.COLUMN_CATEGORY_ID, new CategoryController(context).getCategoryByNameAndType(CategoryEntry.DEFAULT_CATEGORY_NAME, CategoryEntry.TYPE_INCOME).getId());
            cv.put(TransactionEntry.COLUMN_AMOUNT, amountCurrent - amountBefore);
        }
        context.getContentResolver().insert(TransactionEntry.CONTENT_URI, cv);

        int rowsUpdated = context.getContentResolver().update(currentWalletUri, values, null, null);

        return rowsUpdated;

    }

    public int deleteWallet(Uri currentWalletUri){

        int rowsDeleted = context.getContentResolver().delete(currentWalletUri, null, null);
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

    public Wallet getWalletById(long id){
        Wallet ret = null;
        Cursor data = context.getContentResolver().query(ContentUris.withAppendedId(WalletEntry.CONTENT_URI, id), new String[]{WalletEntry.COLUMN_ID, WalletEntry.COLUMN_NAME, WalletEntry.COLUMN_AMOUNT, WalletEntry.COLUMN_DESC}, null, null, null);
        if (data.moveToFirst()){
            ret = convertCursorToWallet(data);
        }
        return ret;
    }

    public Wallet convertCursorToWallet(Cursor data){
        int idColumnIndex = data.getColumnIndex(WalletEntry.COLUMN_ID);
        int nameColumnIndex = data.getColumnIndex(WalletEntry.COLUMN_NAME);
        int amountColumnIndex = data.getColumnIndex(WalletEntry.COLUMN_AMOUNT);
        int descColumnIndex = data.getColumnIndex(WalletEntry.COLUMN_DESC);

        long id = data.getLong(idColumnIndex);
        String name = data.getString(nameColumnIndex);
        double amount = data.getDouble(amountColumnIndex);
        String desc = data.getString(descColumnIndex);

        Wallet ret = new Wallet(id, name, amount, desc);
        return ret;
    }

    public String[] getProjection(){
        String[] projection = new String[]{WalletEntry.COLUMN_ID,
                WalletEntry.COLUMN_NAME,
                WalletEntry.COLUMN_AMOUNT,
                WalletEntry.COLUMN_DESC};
        return projection;
    }

    private ContentValues convertWalletToContentValues(Wallet wallet){
        ContentValues ret = new ContentValues();
        ret.put(WalletEntry.COLUMN_NAME, wallet.getName());
        ret.put(WalletEntry.COLUMN_AMOUNT, wallet.getAmount());
        ret.put(WalletEntry.COLUMN_DESC, wallet.getDescription());
        return ret;
    }

}
