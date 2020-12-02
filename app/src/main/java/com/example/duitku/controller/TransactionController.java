package com.example.duitku.controller;

import android.content.ContentValues;
import android.net.Uri;

import com.example.duitku.database.DuitkuContract.TransactionEntry;
import com.example.duitku.database.DuitkuContract;
import com.example.duitku.model.Transaction;

public class TransactionController {

    public Uri addTransaction(Transaction transaction){

        // taruh di contentvalues
        ContentValues values = new ContentValues();
        values.put(TransactionEntry.COLUMN_DATE, transaction.getDate());
        values.put(TransactionEntry.COLUMN_WALLET_ID, transaction.getWallet());
        values.put(TransactionEntry.COLUMN_CATEGORY_ID, transaction.getCategory());
        values.put(TransactionEntry.COLUMN_AMOUNT, transaction.getAmount());
        values.put(TransactionEntry.COLUMN_DESC, transaction.getDesc());

        return null;

    }

}
