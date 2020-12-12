package com.example.duitku.controller;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.view.View;
import android.widget.TextView;

import com.example.duitku.database.DuitkuContract.TransactionEntry;
import com.example.duitku.database.DuitkuContract.CategoryEntry;
import com.example.duitku.database.DuitkuContract.WalletEntry;
import com.example.duitku.model.Transaction;
import com.example.duitku.model.Wallet;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TransactionController {

    private Context mContext;

    public TransactionController(Context context){
        mContext = context;
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
        values.put(TransactionEntry.COLUMN_WALLETDEST_ID, walletDestId);
        values.put(TransactionEntry.COLUMN_AMOUNT, amount);
        values.put(TransactionEntry.COLUMN_DESC, desc);

        // panggil contentresolver yg nanti diterima sama contentprovider
        Uri uri = mContext.getContentResolver().insert(TransactionEntry.CONTENT_URI, values);
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
        Cursor temp = mContext.getContentResolver().query(ContentUris.withAppendedId(CategoryEntry.CONTENT_URI, categoryId), new String[]{CategoryEntry.COLUMN_ID, CategoryEntry.COLUMN_TYPE}, null, null, null);
        if (temp.moveToFirst()){
            if (temp.getString(temp.getColumnIndex(CategoryEntry.COLUMN_TYPE)).equals(CategoryEntry.TYPE_EXPENSE)){
                ContentValues cv = new ContentValues();
                cv.put(WalletEntry.COLUMN_AMOUNT, new WalletController(mContext).getWalletFromId(walletId).getAmount() - amount);
                mContext.getContentResolver().update(ContentUris.withAppendedId(WalletEntry.CONTENT_URI, walletId), cv, null, null);
            } else {
                ContentValues cv = new ContentValues();
                cv.put(WalletEntry.COLUMN_AMOUNT, new WalletController(mContext).getWalletFromId(walletId).getAmount() + amount);
                mContext.getContentResolver().update(ContentUris.withAppendedId(WalletEntry.CONTENT_URI, walletId), cv, null, null);
            }
        }

        // panggil contentresolver yg nanti diterima sama contentprovider
        Uri uri = mContext.getContentResolver().insert(TransactionEntry.CONTENT_URI, values);
        return uri;
    }

}
