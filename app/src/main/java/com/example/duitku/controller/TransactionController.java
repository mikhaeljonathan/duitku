package com.example.duitku.controller;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.widget.TextView;

import com.example.duitku.database.DuitkuContract.TransactionEntry;
import com.example.duitku.database.DuitkuContract;
import com.example.duitku.model.Transaction;

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
        values.put(TransactionEntry.COLUMN_WALLET_ID, transaction.getWalletId());
        values.put(TransactionEntry.COLUMN_WALLETDEST_ID, walletDestId);
        values.put(TransactionEntry.COLUMN_AMOUNT, transaction.getAmount());
        values.put(TransactionEntry.COLUMN_DESC, transaction.getDesc());

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

        // panggil contentresolver yg nanti diterima sama contentprovider
        Uri uri = mContext.getContentResolver().insert(TransactionEntry.CONTENT_URI, values);
        return uri;
    }

}
