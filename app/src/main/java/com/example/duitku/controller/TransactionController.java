package com.example.duitku.controller;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;

import com.example.duitku.database.DuitkuContract.TransactionEntry;
import com.example.duitku.database.DuitkuContract;
import com.example.duitku.model.Transaction;

public class TransactionController {

    private Context mContext;

    public TransactionController(Context context){
        mContext = context;
    }

    public Uri addTransaction(Transaction transaction){

        // taruh di contentvalues
        ContentValues values = new ContentValues();
        values.put(TransactionEntry.COLUMN_DATE, transaction.getDate());
        values.put(TransactionEntry.COLUMN_WALLET_ID, transaction.getWalletId());
        values.put(TransactionEntry.COLUMN_CATEGORY_ID, transaction.getCategoryId());
        values.put(TransactionEntry.COLUMN_AMOUNT, transaction.getAmount());
        values.put(TransactionEntry.COLUMN_DESC, transaction.getDesc());

        // panggil contentresolver yg nanti diterima sama contentprovider
        Uri uri = mContext.getContentResolver().insert(TransactionEntry.CONTENT_URI, values);
        return uri;

    }

}
