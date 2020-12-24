package com.example.duitku.wallet;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.example.duitku.database.DuitkuContract.WalletEntry;
import com.example.duitku.wallet.Wallet;

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

    public int updateWallet(Wallet wallet){
        ContentValues values = convertWalletToContentValues(wallet);
        String id = Long.toString(wallet.getId());
        int rowsUpdated = context.getContentResolver().update(Uri.withAppendedPath(WalletEntry.CONTENT_URI, id), values, null, null);
        return rowsUpdated;
    }

    public int deleteWallet(long id){
        int rowsDeleted = context.getContentResolver().delete(Uri.withAppendedPath(WalletEntry.CONTENT_URI, Long.toString(id)), null, null);
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

    public String[] getFullProjection(){
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
