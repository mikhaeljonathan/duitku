package com.example.duitku.transaction;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.example.duitku.category.CategoryTransaction;
import com.example.duitku.database.DuitkuContract.TransactionEntry;
import com.example.duitku.database.DuitkuContract.CategoryEntry;
import com.example.duitku.database.DuitkuContract.WalletEntry;
import com.example.duitku.transaction.Transaction;
import com.example.duitku.wallet.WalletController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TransactionController {

    private Context context;

    public TransactionController(Context context){
        this.context = context;
    }

    public Uri addTransferTransaction(Transaction transaction){

        String date = new SimpleDateFormat("dd/MM/yyyy").format(transaction.getDate());
        double amount = transaction.getAmount();
        String desc = transaction.getDesc();
        long walletId = transaction.getWalletId();
        long walletDestId = transaction.getWalletDestId();

        // taruh di contentvalues
        ContentValues values = new ContentValues();
        values.put(TransactionEntry.COLUMN_DATE, date);
        values.put(TransactionEntry.COLUMN_WALLET_ID, walletId);
        values.put(TransactionEntry.COLUMN_WALLET_DEST_ID, walletDestId);
        values.put(TransactionEntry.COLUMN_AMOUNT, amount);
        values.put(TransactionEntry.COLUMN_DESC, desc);

        // panggil contentresolver yg nanti diterima sama contentprovider
        Uri uri = context.getContentResolver().insert(TransactionEntry.CONTENT_URI, values);
        return uri;

    }

    public Uri addNonTransferTransaction(Transaction transaction){

        // taruh di contentvalues
        String date = new SimpleDateFormat("dd/MM/yyyy").format(transaction.getDate());
        double amount = transaction.getAmount();
        String desc = transaction.getDesc();
        long categoryId = transaction.getCategoryId();
        long walletId = transaction.getWalletId();

        ContentValues values = new ContentValues();
        values.put(TransactionEntry.COLUMN_DATE, date);
        values.put(TransactionEntry.COLUMN_WALLET_ID, walletId);
        values.put(TransactionEntry.COLUMN_AMOUNT, amount);
        values.put(TransactionEntry.COLUMN_DESC, desc);
        values.put(TransactionEntry.COLUMN_CATEGORY_ID, categoryId);

        // tambah atau kurangin wallet
        Cursor temp = context.getContentResolver().query(ContentUris.withAppendedId(CategoryEntry.CONTENT_URI, categoryId), new String[]{CategoryEntry.COLUMN_ID, CategoryEntry.COLUMN_TYPE}, null, null, null);
        if (temp.moveToFirst()){
            if (temp.getString(temp.getColumnIndex(CategoryEntry.COLUMN_TYPE)).equals(CategoryEntry.TYPE_EXPENSE)){
                ContentValues cv = new ContentValues();
                cv.put(WalletEntry.COLUMN_AMOUNT, new WalletController(context).getWalletById(walletId).getAmount() - amount);
                context.getContentResolver().update(ContentUris.withAppendedId(WalletEntry.CONTENT_URI, walletId), cv, null, null);
            } else {
                ContentValues cv = new ContentValues();
                cv.put(WalletEntry.COLUMN_AMOUNT, new WalletController(context).getWalletById(walletId).getAmount() + amount);
                context.getContentResolver().update(ContentUris.withAppendedId(WalletEntry.CONTENT_URI, walletId), cv, null, null);
            }
        }

        // panggil contentresolver yg nanti diterima sama contentprovider
        Uri uri = context.getContentResolver().insert(TransactionEntry.CONTENT_URI, values);
        return uri;
    }

    public Uri addTransaction(Transaction transaction){
        ContentValues values = convertTransactionToContentValues(transaction);
        Uri uri = context.getContentResolver().insert(TransactionEntry.CONTENT_URI, values);
        return uri;
    }

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
        Date date = parseDate(data.getString(dateColumnIndex));
        double amount= data.getDouble(amountColumnIndex);

        Transaction ret = new Transaction(transactionId, walletId, walletDestId, categoryId, desc, date, amount);
        return ret;

    }

    public List<Transaction> convertCursorToList(Cursor data){
        List<Transaction> ret = new ArrayList<>();
        if (!data.moveToFirst()) return ret;
        do {
            ret.add(convertCursorToTransaction(data));
        } while (data.moveToNext());
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

    public int deleteAllTransactionWithWalletId(long walletId){
        String selection = TransactionEntry.COLUMN_WALLET_ID + " = ? OR " + TransactionEntry.COLUMN_WALLET_DEST_ID + " = ?";
        String[] selectionArgs = new String[]{Long.toString(walletId), Long.toString(walletId)};
        int rowsDeleted = context.getContentResolver().delete(TransactionEntry.CONTENT_URI, selection, selectionArgs);
        return rowsDeleted;
    }

    private Date parseDate(String date){
        Date ret = null;
        try {
            ret = new SimpleDateFormat("dd/MM/yyyy").parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return ret;
    }

    private ContentValues convertTransactionToContentValues(Transaction transaction){
        String date = new SimpleDateFormat("dd/MM/yyyy").format(transaction.getDate());

        ContentValues ret = new ContentValues();
        ret.put(TransactionEntry.COLUMN_WALLET_ID, transaction.getWalletId());
        ret.put(TransactionEntry.COLUMN_WALLET_DEST_ID, transaction.getWalletDestId());
        ret.put(TransactionEntry.COLUMN_CATEGORY_ID, transaction.getCategoryId());
        ret.put(TransactionEntry.COLUMN_DESC, transaction.getDesc());
        ret.put(TransactionEntry.COLUMN_DATE, date);
        ret.put(TransactionEntry.COLUMN_AMOUNT, transaction.getAmount());
        return ret;
    }

    public List<CategoryTransaction> convertHashMapToList(HashMap<Long, CategoryTransaction> hashMap){
        List<CategoryTransaction> ret = new ArrayList<>();
        for (Map.Entry mapElement: hashMap.entrySet()){
            ret.add((CategoryTransaction) mapElement.getValue());
        }
        return ret;
    }

}
