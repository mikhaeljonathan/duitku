package com.example.duitku.controller;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.example.duitku.database.DuitkuContract.WalletEntry;
import com.example.duitku.database.DuitkuContract;
import com.example.duitku.model.Wallet;

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

        // panggil contentresolver yg nanti diterima sama contentprovider
        Uri uri = mContext.getContentResolver().insert(WalletEntry.CONTENT_URI, values);
        return uri;

    }

}
